package net.yakclient.web.utils.annotation

import java.lang.annotation.Inherited

const val JSON_FILTER = "VIEWABLE_EXCLUSION"

@Target(AnnotationTarget.CLASS)
@Retention
@Inherited
annotation class Viewable
