/**
 * @package Showcase-Microservices-Kotlin
 *
 * @file DueDate class
 * @copyright 2023-present Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the Apache License v2.0.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.domain.todo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import dev.unexist.showcase.todo.infrastructure.NoArg
import dev.unexist.showcase.todo.infrastructure.serde.DateDeserializer
import dev.unexist.showcase.todo.infrastructure.serde.DateSerializer
import java.time.LocalDate

@NoArg
data class DueDate(
    @field:JsonSerialize(using = DateSerializer::class)
    @field:JsonDeserialize(using = DateDeserializer::class)
    val start: LocalDate,

    @field:JsonSerialize(using = DateSerializer::class)
    @field:JsonDeserialize(using = DateDeserializer::class)
    val due: LocalDate) {

    companion object {
        const val DATE_PATTERN = "yyyy-MM-dd"
    }
}
