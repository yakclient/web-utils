package net.yakclient.web.utils.config

import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.util.*

class ConfigReader (
    file: File,
) {
    private val props: Properties = Properties().also { p -> FileInputStream(file).use { p.load(it) } }
    private val fileName: String = file.absolutePath

    fun readString(name: String): String? = try {
        props.getProperty(name)
    } catch (ex: IOException) {
        LoggerFactory.getLogger(this::class.java).error("Failed to load properties file: $fileName")
        null
    }

    fun <T> readObject(parser: ConfigParser<T>, property: String): T? = readString(property)?.parse(parser)

    fun isTesting(property: String = "testing"): Boolean = readObject(BooleanParser, property) ?: false
}

