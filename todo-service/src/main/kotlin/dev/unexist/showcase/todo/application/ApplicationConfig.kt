package dev.unexist.showcase.todo.application

import dev.unexist.showcase.todo.domain.todo.TodoRepository
import dev.unexist.showcase.todo.domain.todo.TodoService
import dev.unexist.showcase.todo.infrastructure.persistence.TodoListRepository
import io.github.smiley4.ktoropenapi.OpenApi
import io.ktor.serialization.Configuration
import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.metrics.micrometer.MicrometerMetrics
import io.ktor.server.plugins.callid.CallId
import io.ktor.server.plugins.callid.callIdMdc
import io.ktor.server.plugins.callid.generate
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics
import io.micrometer.core.instrument.binder.system.FileDescriptorMetrics
import io.micrometer.core.instrument.binder.system.ProcessorMetrics
import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

val appModule = module {
    single<TodoService>()
    single<TodoRepository, TodoListRepository>()
}

fun Application.module() {
    install(ContentNegotiation) {
        jackson {}
    }

    install(CORS) {
        anyHost()
        allowCredentials = true
        allowNonSimpleContentTypes = true
        method(HttpMethod.Get)
        method(HttpMethod.Post)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        method(HttpMethod.Options)
        header(HttpHeaders.ContentType)
        header(HttpHeaders.Authorization)
        header(HttpHeaders.AccessControlAllowHeaders)
        header(HttpHeaders.AccessControlAllowOrigin)
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
    }

    install(Koin) {
        slf4jLogger()
        modules(listOf(
            appModule,
        ))
    }

    routing {
        todo()
    }
}