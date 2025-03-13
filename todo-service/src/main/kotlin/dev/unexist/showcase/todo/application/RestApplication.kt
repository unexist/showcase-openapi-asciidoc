/**
 * @package Showcase-Swagger-Asciidoc
 *
 * @file Todo application
 * @copyright 2023-present Christoph Kappel <christoph@unexist.dev>
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
import io.ktor.server.engine.commandLineEnvironment
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import java.net.ConnectException

fun main(args: Array<String>) {
    val logger = KotlinLogging.logger {}

    val server = embeddedServer(Netty, commandLineEnvironment(args))

    /* Consul */
    val consulService = ImmutableRegistration.builder()
            .id("todo-${server.environment.connectors[0].port}")
            .name("todo-service")
            .address("localhost")
            .port(server.environment.connectors[0].port)
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
