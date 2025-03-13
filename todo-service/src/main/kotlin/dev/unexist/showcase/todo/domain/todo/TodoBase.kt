/**
 * @package Showcase-Swagger-Asciidoc
 *
 * @file Todo base class
 * @copyright 2023-present Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the Apache License v2.0.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.domain.todo

import com.fasterxml.jackson.annotation.JsonInclude
import dev.unexist.showcase.todo.infrastructure.NoArg

@NoArg
@JsonInclude(JsonInclude.Include.NON_NULL)
open class TodoBase(open var title: String, open var description: String,
                    open var done: Boolean, open var dueDate: DueDate?)
