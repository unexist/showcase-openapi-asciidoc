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
import dev.unexist.showcase.todo.infrastructure.persistence.TodoListRepository
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.metrics.micrometer.MicrometerMetrics
import io.ktor.server.plugins.callid.CallId
import io.ktor.server.plugins.callid.callIdMdc
import io.ktor.server.plugins.callid.generate
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics
import io.micrometer.core.instrument.binder.system.FileDescriptorMetrics
import io.micrometer.core.instrument.binder.system.ProcessorMetrics
import io.micrometer.core.instrument.simple.SimpleMeterRegistry

fun Application.main() {
    install(ContentNegotiation) {
        jackson {
        }
    }

    install(MicrometerMetrics) {
        registry = SimpleMeterRegistry()
        meterBinders = listOf(
                ClassLoaderMetrics(),
                JvmMemoryMetrics(),
                JvmGcMetrics(),
                ProcessorMetrics(),
                JvmThreadMetrics(),
                FileDescriptorMetrics()
        )
    }

    install(CallLogging) {
        level = org.slf4j.event.Level.TRACE
        callIdMdc("X-Request-ID")
    }

    install(CallId) {
        generate(10)
    }

    routing {
        get("/todo") {
            call.respond(message = TodoListRepository.getAll())
        }

        get("/todo/{id}") {
            val id: String? = call.parameters["id"]

            if (null != id)
                call.respond(message = TodoListRepository.getAll()
                    .first { it.id == id.toInt() })
        }

        post("/todo") {
            var todo: Todo = call.receive()

            todo.id = TodoListRepository.getAll().size + 1

            TodoListRepository.add(todo)
            call.respond(message = todo, status = HttpStatusCode.Created)
        }
    }
}
