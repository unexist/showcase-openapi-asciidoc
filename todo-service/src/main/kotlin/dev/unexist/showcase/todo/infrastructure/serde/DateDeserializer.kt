/**
 * @package Showcase-Swagger-Asciidoc
 *
 * @file Todo deserializer
 * @copyright 2023-present Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the Apache License v2.0.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.infrastructure.serde;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import dev.unexist.showcase.todo.domain.todo.DueDate;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

class DateDeserializer : JsonDeserializer<LocalDate>() {

    /**
     * Deserialize {@link LocalDate} from string
     *
     * @param  parser
     * @param  context
     * @return
     * @throws IOException
     */

    override fun deserialize(parser: JsonParser?, context: DeserializationContext?): LocalDate? {
        var retVal: LocalDate? = null;

        if (null != parser) {
            retVal = LocalDate.from(DateTimeFormatter.ofPattern(DueDate.DATE_PATTERN)
                .parse(parser.text))
        }

        return retVal;
    }
}
