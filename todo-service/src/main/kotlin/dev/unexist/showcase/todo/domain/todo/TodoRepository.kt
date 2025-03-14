/**
 * @package Showcase-Swagger-Asciidoc
 *
 * @file Todo repository interface
 * @copyright 2025-present Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the Apache License v2.0.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.domain.todo

import java.util.Optional;

interface TodoRepository {

    /**
     * Add {@link Todo} entry to list
     *
     * @param  todo  {@link Todo} entry to add
     *
     * @return Either {@code true} on success; otherwise {@code false}
     **/

    fun add(todo: Todo): Boolean

    /**
     * Update {@link Todo} with given id
     *
     * @param  todo  A {@link Todo} to update
     *
     * @return Either {@code true} on success; otherwise {@code false}
     **/

    fun update(todo: Todo): Boolean

    /**
     * Delete {@link Todo} with given id
     *
     * @param  id  Id to delete
     *
     * @return Either {@code true} on success; otherwise {@code false}
     **/

    fun deleteById(id: Int): Boolean

    /**
     * Get all {@link Todo} entries
     *
     * @return List of all stored {@link Todo}
     **/

    fun getAll(): MutableList<Todo>

    /**
     * Find {@link Todo} by given id
     *
     * @param  id  Id to find
     *
     * @return A {@link Optional} with the result of the lookup
     **/

    fun findById(id: Int): Optional<Todo>
}
