/**
 * @package Showcase-Swagger-Asciidoc
 *
 * @file Todo module
 * @copyright 2025-present Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the Apache License v2.0.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.adapter

import dev.unexist.showcase.todo.domain.todo.Todo
import dev.unexist.showcase.todo.domain.todo.TodoService
import io.github.smiley4.ktoropenapi.delete
import io.github.smiley4.ktoropenapi.get
import io.github.smiley4.ktoropenapi.post
import io.github.smiley4.ktoropenapi.put
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject
import java.util.Optional

fun Routing.todo() {
    val todoService by inject<TodoService>()

    route("/todo") {
        get({
            operationId = "getAll"
            description = "Get all todos"
            response {
                code(HttpStatusCode.OK) {
                    body<List<Todo>> {
                        description = "List of todo"
                        example("Todo List") {
                            value = listOf(
                                Todo(
                                    id = 1,
                                    title = "Title",
                                    description = "Description",
                                )
                            )
                        }
                    }
                }
                code(HttpStatusCode.Created) {
                    description = "Nothing found"
                }
                code(HttpStatusCode.InternalServerError) {
                    description = "Internal server error"
                }
            }
        }) {
            call.respond(message = todoService.getAll())
        }

        post({
            operationId = "addTodo"
            description = "Create new todo"
            request {
                body<Todo> {
                    description = "Todo to add to the store"
                    required = true
                    example("New Todo") {
                        value = Todo(
                            title = "Title",
                            description = "Description",
                            done = false,
                            dueDate = null
                        )
                    }
                }
            }
            response {
                code(HttpStatusCode.Created) {
                    body<Todo> {
                        description = "The created todo"
                        example("New Todo") {
                            value = Todo(
                                id = 1,
                                title = "Title",
                                description = "Description",
                                done = false,
                                dueDate = null
                            )
                        }
                    }
                }
                code(HttpStatusCode.NotAcceptable) {
                    description = "Bad data"
                }
                code(HttpStatusCode.InternalServerError) {
                    description = "Internal server error"
                }
            }
        }) {
            val base: Todo = call.receive()
            val todo: Optional<Todo> = todoService.create(base)

            if (todo.isPresent) {
                call.respond(message = todo.get(), status = HttpStatusCode.Created)
            } else {
                call.respond(HttpStatusCode.NotAcceptable)
            }
        }

        route("{id}") {
            get({
                operationId = "getTodoById"
                description = "Get todo by id"
                request {
                    pathParameter<Long>("id") {
                        description = "Id of todo to fetch"
                        required = true
                        example("default") {
                            value = 1L
                        }
                    }
                }
                response {
                    code(HttpStatusCode.OK) {
                        body<Todo>{
                            description = "The found todo with the given id"
                            example("Todo") {
                                value = Todo(
                                    title = "Title",
                                    description = "Description",
                                    done = false,
                                    dueDate = null
                                )
                            }
                        }
                    }
                    code(HttpStatusCode.NotFound) {
                        description = "Todo not found"
                    }
                    code(HttpStatusCode.InternalServerError) {
                        description = "Internal server error"
                    }
                }
            }) {
                val id: String? = call.parameters["id"]

                if (null != id) {
                    val todo: Optional<Todo> = todoService.findById(id.toInt());

                    if (todo.isPresent) {
                        call.respond(message = todo.get(), status = HttpStatusCode.OK)
                    } else {
                        call.respond(HttpStatusCode.NotAcceptable)
                    }
                }
            }

            put({
                operationId = "updateTodoById"
                description = "Update todo by a single ID"
                request {
                    pathParameter<Long>("id") {
                        description = "Id of todo to fetch"
                        required = true
                        example("default") {
                            value = 1L
                        }
                    }
                }
                response {
                    code(HttpStatusCode.OK) {
                        body<Todo>{
                            description = "The found todo with the given id"
                            example("Todo") {
                                value = Todo(
                                    title = "Title",
                                    description = "Description",
                                    done = false,
                                    dueDate = null
                                )
                            }
                        }
                    }
                    code(HttpStatusCode.NoContent) {
                        description = "Todo updated"
                    }
                    code(HttpStatusCode.NotFound) {
                        description = "Todo not found"
                    }
                    code(HttpStatusCode.InternalServerError) {
                        description = "Internal server error"
                    }
                }
            }) {
                val id: String? = call.parameters["id"]
                val base: Todo = call.receive()

                if (null != id) {
                    if (todoService.update(id.toInt(), base)) {
                        call.respond(HttpStatusCode.NoContent)
                    } else {
                        call.respond(HttpStatusCode.NotFound)
                    }
                }
            }

            delete({
                operationId = "deleteTodoById"
                description = "Delete todo by a single ID"
                request {
                    pathParameter<Long>("id") {
                        description = "Id of todo to fetch"
                        required = true
                        example("default") {
                            value = 1L
                        }
                    }
                }
                response {
                    code(HttpStatusCode.NoContent) {
                        description = "Todo deleted"
                    }
                    code(HttpStatusCode.NotFound) {
                        description = "Todo not found"
                    }
                    code(HttpStatusCode.InternalServerError) {
                        description = "Internal server error"
                    }
                }
            }) {
                val id: String? = call.parameters["id"]

                if (null != id) {
                    if (todoService.delete(id.toInt())) {
                        call.respond(HttpStatusCode.NoContent)
                    } else {
                        call.respond(HttpStatusCode.NotFound)
                    }
                }
                call.respond(HttpStatusCode.NotImplemented, Unit)
            }
        }
    }
}
