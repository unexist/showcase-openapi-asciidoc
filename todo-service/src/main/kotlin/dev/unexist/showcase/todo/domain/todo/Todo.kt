/**
 * @package Showcase-Swagger-Asciidoc
 *
 * @file Todo class
 * @copyright 2025-present Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the Apache License v2.0.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.domain.todo

import kotlinx.serialization.Serializable

@Serializable
data class Todo(
    var id: Int = 0,
    var title: String? = "",
    var description: String? = "",
    var done: Boolean? = false,
    var dueDate: DueDate? = null) {

    /**
     * Update values from base
     *
     * @param  base  Todo base class
     **/

    fun update(base: Todo) {
        this.title = base.title
        this.description = base.description
        this.done = base.done

        if (null != base.dueDate) {
            this.dueDate = base.dueDate
        }
    }
}
