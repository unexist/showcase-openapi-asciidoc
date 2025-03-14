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

data class Todo(
    var id: Int = 0,
    override var title: String = "",
    override var description: String = "",
    override var done: Boolean = false,
    override var dueDate: DueDate? = null) : TodoBase(title, description, done, dueDate) {

    /**
     * Update values from base
     *
     * @param  base  Todo base class
     **/

    fun update(base: TodoBase) {
        this.title = base.title
        this.description = base.description
        this.done = base.done

        if (null != base.dueDate) {
            this.dueDate = base.dueDate
        }
    }
}
