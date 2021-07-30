package net.yakclient.web.utils.helper

import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.jvm.jvmName

inline fun <reified T> asType(type: Any, message: () -> String): T {
    if (type::class.isSubclassOf(T::class)) return type as T
    throw IllegalStateException(message())
}

inline fun <reified T> asType(type: Any): T = asType(type) { "Give type(${type::class.jvmName}) does not match(${T::class.jvmName})!" }

inline fun <reified T: Annotation> KType.isAnnotationPresent() : Boolean = jvmErasure.java.isAnnotationPresent(T::class.java)

inline fun <reified T: Annotation> KClass<*>.isAnnotationPresent() : Boolean = java.isAnnotationPresent(T::class.java)