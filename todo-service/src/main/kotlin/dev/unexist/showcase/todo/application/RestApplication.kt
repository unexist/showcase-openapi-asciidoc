/**
 * @package Showcase-Swagger-Asciidoc
 *
 * @file Todo application
 * @copyright 2025-present Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the Apache License v2.0.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.application

import com.orbitz.consul.Consul
import com.orbitz.consul.ConsulException
import com.orbitz.consul.model.agent.ImmutableRegistration
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.server.application.Application
import io.ktor.server.engine.CommandLineConfig
import io.ktor.server.engine.embeddedServer
import io.ktor.server.engine.loadCommonConfiguration
import io.ktor.server.netty.Netty
import java.net.ConnectException

fun main(args: Array<String>) {
    val logger = KotlinLogging.logger {}
    val server = embeddedServer(
        factory = Netty,
        module = Application::appModule,
        configure = {
            val cliConfig = CommandLineConfig(args)
            takeFrom(cliConfig.engineConfig)
            loadCommonConfiguration(cliConfig.rootConfig.environment.config)
        })

    /* Consul */
    val consulService = ImmutableRegistration.builder()
            .id("todo-${server.engineConfig.connectors[0].port}")
            .name("todo-service")
            .address("localhost")
            .port(server.engineConfig.connectors[0].port)
            .build()

    try {
        val consulClient = Consul.builder().withUrl("http://localhost:8500").build()

        consulClient.agentClient().register(consulService)
    } catch (ex: Exception) {
        when (ex) {
            is ConnectException, is ConsulException -> {
                logger.error(ex) { "Connect to Consul failed: $ex" }
            }
        }
    }

    server.start(wait = true)
}
