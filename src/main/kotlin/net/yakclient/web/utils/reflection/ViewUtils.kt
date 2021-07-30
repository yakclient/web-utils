package net.yakclient.web.utils.reflection

import net.yakclient.web.utils.annotation.ViewProperty
import net.yakclient.web.utils.annotation.Viewable
import net.yakclient.web.utils.helper.asType
import net.yakclient.web.utils.helper.isAnnotationPresent
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.KTypeProjection
import kotlin.reflect.full.*
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.jvmErasure

const val UNCHECKED = "unchecked_cast"

object ViewUtils {
    fun <T : Any> view(value: T): Map<String, Any?> =
        configure(value::class).also { recursivelyConfigure(it.viewNode) }.view(value)

    @Suppress(UNCHECKED)
    private fun <T : Any> recursivelyConfigure(node: ConfigurableViewNode<T, *>) {
        for (member in node.kClass.memberProperties) {
            member as KProperty1<T, *>
            val annotation: ViewProperty? = member.findAnnotation()

            if (annotation?.ignore == true) {
                node.exclude(member)
                continue
            }

            (annotation?.name ?: "").also { if (it.isNotBlank()) node.rename(member, it) }

            if (member.returnType.isAnnotationPresent<Viewable>()) recursivelyConfigure(node.get(member))
            else if (member.returnType.isSubtypeOf(Collection::class.createType(listOf(KTypeProjection.STAR)))) {
                member as KProperty1<T, Collection<*>>
                recursivelyConfigure(node.getC(member))
            }
        }
    }

    fun <T : Any> configure(kClass: KClass<out T>): ConfigurableView<T> = ConfigurableView(kClass)

    fun <T : Any> configureAndView(value: T, block: ConfigurableViewNode<T, Nothing>.() -> Unit): Map<String, Any?> =
        configure(value::class).node(block).view(value)

    fun <T : Any> plainView(value: T): Map<String, Any?> = configure(value::class).view(value)

}

inline fun <reified T : Any> ViewUtils.configure() = configure(T::class)


class ConfigurableViewNode<R : Any, P : Any>(
    val kClass: KClass<out R>,
    private val parent: ConfigurableViewNode<P, *>?,
) {
    internal val toExclude: MutableSet<String> = HashSet()
    internal val toRename: MutableMap<String, String> = HashMap()
    private val children: MutableMap<String, ConfigurableViewNode<*, R>> = HashMap()

    init {
        require(kClass.isAnnotationPresent<Viewable>()) { "Class must be annotated with @Viewable!" }
    }

    fun exclude(vararg property: KProperty1<in R, *>): ConfigurableViewNode<R, P> =
        also { toExclude.addAll(property.map { it.name }) }

    fun only(vararg property: KProperty1<in R, *>): ConfigurableViewNode<R, P> =
        also {
            toExclude.addAll(kClass.memberProperties.filterNot { member -> property.any { prop -> member.name == prop.name } }
                .map { it.name })
        }

    fun rename(property: KProperty1<R, *>, name: String): ConfigurableViewNode<R, P> =
        also { toRename[property.name] = name }

    fun <T : Any, C : Collection<T?>> getC(property: KProperty1<R, C>): ConfigurableViewNode<T, R> {
        val type = property.javaField?.genericType as ParameterizedType
        val nodeType = asType<Class<T>>(type.actualTypeArguments[0]).kotlin

        return get(property.name, nodeType)
    }

    fun <V : Any> get(property: KProperty1<R, V?>): ConfigurableViewNode<V, R> =
        get(property.name, asType(property.returnType.jvmErasure))


    private fun <V : Any> get(
        name: String,
        kClass1: KClass<V>,
    ): ConfigurableViewNode<V, R> =
        asType(children.getOrPut(name) { ConfigurableViewNode(kClass1, this) })

    fun up(): ConfigurableViewNode<P, *> = checkNotNull(parent) { "Failed to find a parent ViewNode" }
}


private fun <T : Any> ConfigurableViewNode<T, *>.shouldExclude(property: KProperty1<T, *>): Boolean =
    toExclude.any { it == property.name }

private fun <T : Any> ConfigurableViewNode<T, *>.getName(property: KProperty1<T, *>): String =
    toRename.getOrElse(property.name) { property.name }

class ConfigurableView<T : Any> internal constructor(
    kClass: KClass<out T>,
) {
    internal val viewNode: ConfigurableViewNode<T, Nothing> = ConfigurableViewNode(kClass, null)

    fun node(block: ConfigurableViewNode<T, Nothing>.() -> Unit): ConfigurableView<T> = also { block(viewNode) }

    fun view(value: T): Map<String, Any?> {
        return recursivelyMap(value, viewNode)
    }

    @Suppress(UNCHECKED)
    private fun <T : Any> recursivelyMap(pojoIn: T, configuration: ConfigurableViewNode<T, *>): Map<String, Any?> {
        require(pojoIn::class.isAnnotationPresent<Viewable>()) { "Viewable pojo must have the Viewable annotation!" }
        val map: MutableMap<String, Any?> = LinkedHashMap()
        for (member in pojoIn::class.memberProperties) {
            member as KProperty1<T, *>

            val name = configuration.getName(member)
            if (configuration.shouldExclude(member)) continue

            val child: Any? = member.get(pojoIn)
            map[name] = when {
                child == null -> null
                member.returnType.isAnnotationPresent<Viewable>() ->
                    recursivelyMap(child, configuration.get(member))
                member.returnType.isSubtypeOf(Collection::class.createType(listOf(KTypeProjection.STAR))) ->
                    (child as Collection<*>)
                        .filterNotNull()
                        .map {
                            recursivelyMap(it, configuration.get(member))
                        }
                else -> child
            }
        }

        return map
    }
}
