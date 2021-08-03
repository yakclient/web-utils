package net.yakclient.web.utils.extension

import net.yakclient.web.utils.config.ConfigReader
import org.springframework.boot.jdbc.DataSourceBuilder
import javax.sql.DataSource

@Suppress("unchecked_cast")
fun ConfigReader.createDataSource(): DataSource {
    val builder = DataSourceBuilder.create()

    builder.url(checkNotNull(readString("db-url")) { "The connection URL could not be found." })
    builder.username(checkNotNull(readString("db-user")) { "The connection username could not be found." })
    builder.password(checkNotNull(readString("db-password")) { "The connection password could not be found." })
    builder.driverClassName(checkNotNull(readString("db-driver")) { "The connection Driver could not be found(in the config)." })

    val dataSourceModule =
        checkNotNull(readString("db-ds-module")) { "Data source module cannot be null." }

    val dsClassName =
        checkNotNull(readString("db-ds-classname")) { "DataStore classname could not be found(in the config)." }

    val dsType = Class.forName(dsClassName) ?: {
        val typeModule = ModuleLayer.boot().findModule(dataSourceModule)
            .orElseThrow { IllegalStateException("Failed to find module: '$dataSourceModule'.") }
            .also { this::class.java.module.addReads(it) }

        checkNotNull(Class.forName(typeModule,
            dsClassName)) { "Class: $dsClassName Could not be found in module: '${typeModule.name}'. Make sure that the class exists and is exported by its parent module." }
    }

    builder.type(dsType as Class<out DataSource>)
    return builder.build()
}