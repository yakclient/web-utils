package net.yakclient.web.utils.helper

inline fun <reified E : Enum<E>> valueOf(name: String): E? =
    valueOf<E>(name) { lookFor, enum -> enum.name.equals(lookFor, ignoreCase = true) }

inline fun <reified E : Enum<E>> valueOf(name: String, mapper: (String, E) -> Boolean): E? {
    val values = E::class.java.enumConstants
    for (value in values) {
        if (mapper(name, value)) return value
    }
    return null
}