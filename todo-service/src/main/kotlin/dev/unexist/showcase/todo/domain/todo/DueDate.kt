/**
 * @package Showcase-Microservices-Kotlin
 *
 * @file DueDate class
 * @copyright 2025-present Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the Apache License v2.0.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.domain.todo;

import dev.unexist.showcase.todo.infrastructure.NoArg
import dev.unexist.showcase.todo.infrastructure.DateSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate

@NoArg
@Serializable(with = DateSerializer::class)
data class DueDate(
    val start: LocalDate,
    val due: LocalDate)
