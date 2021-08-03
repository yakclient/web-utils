package net.yakclient.web.utils.annotation

import java.lang.annotation.Inherited

/**
 * Marks a Field or Property with the provided metadata to be used when
 * serializing to a viewable map.
 */
@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@Retention
@Inherited
annotation class ViewProperty(
    /**
     * Marks the field to ignore
     */
    val ignore: Boolean = false,

    /**
     * Defines a name for the field. Defaults to the fields name.
     */
    val name: String = ""
)

