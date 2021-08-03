package net.yakclient.web.utils.annotation

import java.lang.annotation.Inherited

const val JSON_FILTER = "VIEWABLE_EXCLUSION"

/**
 * Marks a class as serializable to a viewable map. MUST be on any
 * class that should be serialized.
 */
@Target(AnnotationTarget.CLASS)
@Retention
@Inherited
annotation class Viewable
