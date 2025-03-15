/**
 * @package Showcase-Swagger-Asciidoc
 *
 * @file Todo list repository
 * @copyright 2025-present Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the Apache License v2.0.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.infrastructure.persistence

import dev.unexist.showcase.todo.domain.todo.Todo
import dev.unexist.showcase.todo.domain.todo.TodoRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import java.util.Collections
import java.util.Optional

class TodoListRepository(val list: MutableList<Todo> = ArrayList()) : TodoRepository {
    val logger = KotlinLogging.logger {}

    override fun add(todo: Todo): Boolean {
        todo.id = this.list.size + 1

        return this.list.add(todo)
    }

    override fun update(todo: Todo): Boolean {
        var ret = false

        try {
            list[todo.id - 1] = todo

            ret = true
        } catch (e: IndexOutOfBoundsException) {
            logger.warn { "update: id=${todo.id} not found" }
        }

        return ret
    }

    override fun deleteById(id: Int): Boolean {
        var ret = false

        try {
            list.removeAt(id - 1)

            ret = true
        } catch (e: IndexOutOfBoundsException) {
            logger.warn { "deleteById: id=${id} not found" }
        }

        return ret
    }

    override fun getAll(): MutableList<Todo> {
        return Collections.unmodifiableList(list)
    }

    override fun findById(id: Int): Optional<Todo> {
        return list.stream()
            .filter { (id1): Todo -> id1 == id }
            .findFirst()
    }
}
