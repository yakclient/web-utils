package net.yakclient.web.utils.config

import java.net.URI

/**
 * Specifies the definition of a ConfigParser.
 */
typealias ConfigParser<T> = (String) -> T

internal fun <T> String.parse(parser: ConfigParser<T>): T = parser.invoke(this)

/**
 * Parses a string to a int. Allowing exceptions to be thrown
 */
val IntParser: ConfigParser<Int> = { it: String -> it.toInt() }

/**
 * Parses a String to a String. This is purely for consistency and
 * only should be used when using Parses that take a subtype.
 */
val StringParser: ConfigParser<String> = { it }

/**
 * Parses a Boolean to a String.
 */
val BooleanParser: ConfigParser<Boolean> = { it.toBoolean() }

val URIParser: ConfigParser<URI> = { URI(it) }

/**
 * Parses a list of <T> from a String. The memberParser parses
 * each member of the list(string, int, etc.)
 */
class ListParser<T>(
    private val memberParser: ConfigParser<T>,
) : ConfigParser<List<T>> {
    override operator fun invoke(value: String): List<T> = value.split(",").map { memberParser.invoke(it) }
}

/**
 * Delimits each pair of mappings in a map.
 */
const val DELIMITER: String = ","

/**
 * Separates key and values in maps.
 */
const val SEPARATOR: String = ":"

/**
 * Parses a map from a string. the keyParser parses keys and the valueParser
 * pareses values.
 *
 * Example property:
 *
 * your_prop=bar:1,foo:2
 */
class MapParser<K, V>(
    private val keyParser: ConfigParser<K>,
    private val valueParser: ConfigParser<V>,
) : ConfigParser<Map<K, V>> {
    override operator fun invoke(value: String): Map<K, V> =
        value.split(SEPARATOR).map { it.split(DELIMITER) }
            .associate { keyParser.invoke(it.first()) to valueParser.invoke(it.last()) }
}

/**
 * Defines the type that will be mapped to null(shouldnt be changed)
 */
const val NULL_TYPE = "null"

/**
 * Parses values to null if they equal the NULL_TYPE otherwise uses
 * the given config parser
 */
class NullParser<T>(
    private val nonNulls: ConfigParser<T>,
) : ConfigParser<T?> {
    override fun invoke(value: String): T? = if (value == NULL_TYPE) null else nonNulls.invoke(value)
}

fun <T> ConfigReader.readNull(name: String, parser: ConfigParser<T>): T? = readObject(NullParser(parser), name)

fun ConfigReader.readNull(name: String): String? = NullParser(StringParser).invoke(name)