package net.yakclient.web.utils.annotation

import java.lang.annotation.Inherited

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@Retention
@Inherited
annotation class ViewProperty(
    val ignore: Boolean = false,

    val name: String = ""
)

