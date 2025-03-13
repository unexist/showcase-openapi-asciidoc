/**
 * @package Showcase-Swagger-Asciidoc
 *
 * @file Todo Cucumber steps
 * @copyright 2023-present Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the Apache License v2.0.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.domain

import cucumber.api.PendingException
import cucumber.api.java8.En

lateinit var todo: Todo

class TodoSteps : En {
    init {
        Given("^I create a todo$") {
            todo = Todo()
        }

        When("^its title is {word}$") { title: String ->
            todo.title = title
        }

        And("its description is {word}") { description: String ->
            todo.description = description
        }

        Then("^its id should be {int}$") { _: Int ->
            throw PendingException("Not implemented yet")
        }
    }
}
