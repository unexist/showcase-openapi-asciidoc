/**
 * @package Showcase-Swagger-Asciidoc
 *
 * @file Todo serializer
 * @copyright 2025-present Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the Apache License v2.0.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.infrastructure;

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object DateSerializer : KSerializer<LocalDate> {
    private const val DATE_PATTERN = "yyyy-MM-dd"

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LocalDate", PrimitiveKind.STRING)

    /**
     * Serialize {@link LocalDate} to format
     *
     * @param  encoder      A {@link Encoder} to use
     * @param  value        A value to use
     * @throws IOException
     **/

    override fun serialize(encoder: Encoder, value: LocalDate) {
        val dateString = value.format(DateTimeFormatter.ofPattern(DATE_PATTERN))

        encoder.encodeString(dateString)
    }

    /**
     * Serialize {@link LocalDate} to format
     *
     * @param  encoder      A {@link Encoder} to use
     * @param  value        A value to use
     * @throws IOException
     **/

    override fun deserialize(decoder: Decoder) : LocalDate {
        val dateString = decoder.decodeString();

        return LocalDate.from(DateTimeFormatter.ofPattern(DATE_PATTERN)
            .parse(dateString))
    }
}
