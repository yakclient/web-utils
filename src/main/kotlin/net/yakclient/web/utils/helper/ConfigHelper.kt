package net.yakclient.web.utils.helper

import net.yakclient.web.utils.ConfigReader
import org.springframework.core.io.Resource
import java.io.File

object ConfigHelper {
    private val configs: MutableMap<String, ConfigReader> = HashMap()

    fun getConfig(file: File): ConfigReader = configs.getOrPut(file.absolutePath) { ConfigReader(file) }

    fun getConfig(file: String) = getConfig(File(file))

    fun getConfig(resource: Resource) = getConfig(resource.file)
}


