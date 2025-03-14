/**
 * @package Showcase-Swagger-Asciidoc
 *
 * @file Todo module
 * @copyright 2023-present Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the Apache License v2.0.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.adapter

import dev.unexist.showcase.todo.domain.todo.Todo
import dev.unexist.showcase.todo.domain.todo.TodoBase
import dev.unexist.showcase.todo.domain.todo.TodoService
import dev.unexist.showcase.todo.infrastructure.persistence.TodoListRepository
import io.github.smiley4.ktoropenapi.delete
import io.github.smiley4.ktoropenapi.get
import io.github.smiley4.ktoropenapi.put
import io.github.smiley4.ktoropenapi.openApi
import io.github.smiley4.ktoropenapi.post
import io.github.smiley4.ktorredoc.redoc
import io.github.smiley4.ktorswaggerui.swaggerUI
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
                body<TodoBase> {
                    description = "Todo to add to the store"
                    required = true
                    example("New Todo") {
                        value = TodoBase(
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
            val todoBase: TodoBase = call.receive()
            val todo: Optional<Todo> = todoService.create(todoBase)

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
                    }
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

                if (null != id)
                    call.respond(message = TodoListRepository.getAll()
                        .first { it.id == id.toInt() })
            }

            delete({
                operationId = "deletePet"
                description = "deletes a single pet based on the supplied ID"
                request {
                    pathParameter<Long>("id") {
                        description = "Id of pet to delete"
                        required = true
                        example("default") {
                            value = 123L
                        }
                    }
                }
                response {
                    code(HttpStatusCode.NoContent) {
                        description = "the pet was successfully deleted"
                    }
                    code(HttpStatusCode.NotFound) {
                        description = "the pet with the given id was not found"
                    }
                }
            }) {
                call.respond(HttpStatusCode.NotImplemented, Unit)
            }
        }
    }

    route("api.json") {
        openApi()
    }

    route("swagger") {
        swaggerUI("/api.json")
    }

    route("redoc") {
        redoc("/api.json") {
            // Add configuration for this ReDoc "instance" here.
        }
    }
}

    /*
        @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Create new todo")
    @Tag(name = "Todo")
    @APIResponses(
        APIResponse(responseCode = "201", description = "Todo created"),
        APIResponse(responseCode = "406", description = "Bad data"),
        APIResponse(responseCode = "500", description = "Server error")
    )
    fun create(todoBase: TodoBase, @Context uriInfo: UriInfo): Response {
        val response: Response.ResponseBuilder

        val todo: Optional<Todo> = this.todoService.create(todoBase)

        if (todo.isPresent) {
            val uri: URI = uriInfo.absolutePathBuilder
                    .path(todo.get().id.toString())
                    .build();

            response = Response.created(uri).entity(todo.get())
        } else {
            response = Response.status(Response.Status.NOT_ACCEPTABLE);
        }

        return response.build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get all todos")
    @Tag(name = "Todo")
    @APIResponses(
        APIResponse(responseCode = "200", description = "List of todo", content = [
            Content(schema = Schema(type = SchemaType.ARRAY, implementation = Todo::class))]),
        APIResponse(responseCode = "204", description = "Nothing found"),
        APIResponse(responseCode = "500", description = "Server error")
    )
    fun getAll(): Response {
        val todoList: List<Todo> = this.todoService.getAll()

        val response: Response.ResponseBuilder

        if (todoList.isEmpty()) {
            response = Response.noContent();
        } else {
            response = Response.ok(Entity.json(todoList));
        }

        return response.build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get todo by id")
    @Tag(name = "Todo")
    @APIResponses(
        APIResponse(responseCode = "200", description = "Todo found", content = [
            (Content(schema = Schema(implementation = Todo::class)))]),
        APIResponse(responseCode = "404", description = "Todo not found"),
        APIResponse(responseCode = "500", description = "Server error")
    )
    fun findById(@PathParam("id") id: Int): Response {
        val result: Optional<Todo> = this.todoService.findById(id)

        val response: Response.ResponseBuilder

        if (result.isPresent) {
            response = Response.ok(Entity.json(result.get()));
        } else {
            response = Response.status(Response.Status.NOT_FOUND);
        }

        return response.build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Update todo by id")
    @Tag(name = "Todo")
    @APIResponses(
        APIResponse(responseCode = "204", description = "Todo updated"),
        APIResponse(responseCode = "404", description = "Todo not found"),
        APIResponse(responseCode = "500", description = "Server error")
    )
    fun update(@PathParam("id") id: Int, base: TodoBase): Response {
        val response: Response.ResponseBuilder

        if (this.todoService.update(id, base)) {
            response = Response.noContent();
        } else {
            response = Response.status(Response.Status.NOT_FOUND);
        }

        return response.build();
    }

    @DELETE
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Delete todo by id")
    @Tag(name = "Todo")
    fun delete(@PathParam("id") id: Int, base: TodoBase): Response {
        val response: Response.ResponseBuilder

        if (this.todoService.delete(id)) {
            response = Response.noContent();
        } else {
            response = Response.status(Response.Status.NOT_FOUND);
        }

        return response.build();
    }
     */
