/**
 * @package Showcase-Swagger-Asciidoc
 *
 * @file Todo application config
 * @copyright 2025-present Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the Apache License v2.0.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.application

import dev.unexist.showcase.todo.adapter.todo
import dev.unexist.showcase.todo.domain.todo.TodoRepository
import dev.unexist.showcase.todo.domain.todo.TodoService
import dev.unexist.showcase.todo.infrastructure.persistence.TodoListRepository
import io.github.smiley4.ktoropenapi.OpenApi
import io.github.smiley4.ktoropenapi.config.OutputFormat
import io.github.smiley4.ktoropenapi.config.SchemaGenerator
import io.github.smiley4.ktoropenapi.openApi
import io.github.smiley4.ktorredoc.redoc
import io.github.smiley4.ktorswaggerui.swaggerUI
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.metrics.micrometer.MicrometerMetrics
import io.ktor.server.plugins.callid.CallId
import io.ktor.server.plugins.callid.callIdMdc
import io.ktor.server.plugins.callid.generate
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics
import io.micrometer.core.instrument.binder.system.FileDescriptorMetrics
import io.micrometer.core.instrument.binder.system.ProcessorMetrics
import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

val appModule = module {
    /* Define both as singleton scope */
    single { TodoListRepository() } bind TodoRepository::class
    single { TodoService(get()) }
}

fun Application.appModule() {
    /* Install plugins */
    install(Koin) {
        slf4jLogger()
        modules(listOf(
            appModule,
        ))
    }

    install(ContentNegotiation) {
        json()
    }

    install(CORS) {
        anyHost()
        allowCredentials = true
        allowNonSimpleContentTypes = true
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowMethod(HttpMethod.Options)
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.AccessControlAllowHeaders)
        allowHeader(HttpHeaders.AccessControlAllowOrigin)
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

    install(OpenApi) {
        outputFormat = OutputFormat.JSON

        info {
            title = "OpenAPI for todo-service"
            version = "0.1"
            contact {
                name = "Christoph Kappel"
                url = "https://unexist.dev"
                email = "christoph@unexist.dev"
            }
            license {
                name = "Apache License v2.0"
                url = "https://www.apache.org/licenses/LICENSE-2.0"
            }
        }
        server {
            url = "http://localhost:8080"
            description = "Development Server"
        }

        schemas {
            generator = SchemaGenerator.reflection {}
        }
    }

    routing {
        /* Swagger and OpenApi routes */
        route("api.json") {
            openApi()
        }

        route("swagger") {
            swaggerUI("/api.json")
        }

        route("redoc") {
            redoc("/api.json") {
                hideLoading = true
            }
        }

        /* Register app routes */
        todo()
    }
}
