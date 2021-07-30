package net.yakclient.web.utils.helper

import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.*
import kotlin.reflect.KProperty1

typealias ResModel = ResponseEntity<*>

typealias DataMap = Map<String, *>

typealias DataModel = EntityModel<DataMap>

typealias FailureModel = ResourceNotFoundException

fun <T> Optional<T>.or404(message : String = "Failed to find resource") : T = this.orElseThrow { ResourceNotFoundException(message) }

fun WebMvcLinkBuilder.withRel(property: KProperty1<*, *>) = withRel(property.name)

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class ResourceNotFoundException(message: String) : RuntimeException(message)
