/**
 * @package Showcase-Swagger-Asciidoc
 *
 * @file Stupid integration test
 * @copyright 2023-present Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the Apache License v2.0.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.application

import com.fasterxml.jackson.databind.ObjectMapper
import dev.unexist.showcase.todo.domain.Todo
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import kotlin.test.DefaultAsserter.assertNotNull
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class TodoModuleInfrastructureTest {
    private val mapper: ObjectMapper = ObjectMapper()

    @Test
    fun testAdd() = testApplication {
        val response = client.post("/") {
            header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(mapper.writeValueAsString(Todo(1, "Todo string", "Todo string")))
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertNotNull("Empty response", response.bodyAsText())
    }

    @Test
    fun testGetAll() = testApplication {
        val response = client.get("/todo")

        assertEquals(HttpStatusCode.OK, response.status)
        assertNotNull("Empty response", response.bodyAsText())
    }

    @Test
    fun testFindById() = testApplication {
        val response = client.get("/todo/1")

        assertEquals(HttpStatusCode.OK, response.status)
        assertNotNull("Empty response", response.bodyAsText())

        val todo = mapper.readValue(response.bodyAsText(), Todo::class.java)

        assertNotNull(todo.id)
        assertEquals(1, todo.id)
    }
}
