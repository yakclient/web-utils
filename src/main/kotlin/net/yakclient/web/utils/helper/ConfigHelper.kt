package net.yakclient.web.utils.helper

import net.yakclient.web.utils.config.ConfigReader
import org.springframework.core.io.Resource
import java.io.File

/**
 * Caches Config readers and prevents reloading of files.
 */
object ConfigHelper {
    /**
     * Current loaded configs.
     */
    private val configs: MutableMap<String, ConfigReader> = HashMap()

    /**
     * Returns a config unique to the file given.
     */
    fun getConfig(file: File): ConfigReader = configs.getOrPut(file.absolutePath) { ConfigReader(file) }

    /**
     * Returns a config based on the location of the given string.
     */
    fun getConfig(file: String) = getConfig(File(file))

    fun getConfig(resource: Resource) = getConfig(resource.file)
}


