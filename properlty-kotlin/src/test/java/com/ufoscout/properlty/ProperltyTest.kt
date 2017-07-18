/*******************************************************************************
 * Copyright 2017 Francesco Cina'

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ufoscout.properlty

import com.ufoscout.properlty.reader.PropertyValue
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import java.util.*
import java.util.stream.Collectors

class ProperltyTest : ProperltyBaseTest() {

    @Test
    fun shouldReturnEmptyOptionalString() {
        val properties = HashMap<String, String>()

        properties.put("key.one", "value.one")
        properties.put("key.two", "value.two")

        val prop = buildProperlty(properties)

        assertFalse(prop["key.three"].isPresent)
    }

    @Test
    fun shouldReturnOptionalString() {
        val properties = HashMap<String, String>()

        properties.put("key.one", "value.one")
        properties.put("key.two", "value.two")

        val prop = buildProperlty(properties)

        assertEquals("value.one", prop["key.one"].get())
        assertEquals("value.two", prop["key.two"].get())
    }

    @Test
    fun shouldReturnDefaultString() {
        val properties = HashMap<String, String>()

        properties.put("key.one", "value.one")

        val prop = buildProperlty(properties)

        assertEquals("value.one", prop["key.one", "default"])
        assertEquals("default", prop["key.two", "default"])
    }

    @Test
    fun shouldReturnEmptyOptionalInteger() {
        val properties = HashMap<String, String>()

        properties.put("key.one", "value.one")
        properties.put("key.two", "value.two")

        val prop = buildProperlty(properties)

        assertFalse(prop.getInt("key.three").isPresent)
    }


    @Test
    fun shouldReturnOptionalInteger() {
        val properties = HashMap<String, String>()

        properties.put("key.one", "123")
        properties.put("key.two", "1000000")

        val prop = buildProperlty(properties)

        assertEquals(123, prop.getInt("key.one").get().toInt().toLong())
        assertEquals(1000000, prop.getInt("key.two").get().toInt().toLong())
    }

    @Test
    fun shouldReturnDefaultInteger() {
        val properties = HashMap<String, String>()

        properties.put("key.one", "1")

        val prop = buildProperlty(properties)

        assertEquals(1, prop.getInt("key.one", 10).toLong())
        assertEquals(10, prop.getInt("key.two", 10).toLong())

    }

    @Test(expected = NumberFormatException::class)
    fun shouldThrowExceptionParsingWrongInteger() {
        val properties = HashMap<String, String>()

        properties.put("key.one", "not a number")

        val prop = buildProperlty(properties)

        prop.getInt("key.one")

    }

    @Test
    fun shouldReturnEmptyOptionalDouble() {
        val properties = HashMap<String, String>()

        properties.put("key.one", "value.one")
        properties.put("key.two", "value.two")

        val prop = buildProperlty(properties)

        assertFalse(prop.getDouble("key.three").isPresent)
    }


    @Test
    fun shouldReturnOptionalDouble() {
        val properties = HashMap<String, String>()

        properties.put("key.one", "123")
        properties.put("key.two", "1000000")

        val prop = buildProperlty(properties)

        assertEquals(123.0, prop.getDouble("key.one").get().toDouble(), 0.1)
        assertEquals(1000000.0, prop.getDouble("key.two").get().toDouble(), 0.1)
    }

    @Test
    fun shouldReturnDefaultDouble() {
        val properties = HashMap<String, String>()

        properties.put("key.one", "1")

        val prop = buildProperlty(properties)

        assertEquals(1.0, prop.getDouble("key.one", 1.1111), 0.1)
        assertEquals(10.0, prop.getDouble("key.two", 10.0), 0.1)

    }

    @Test(expected = NumberFormatException::class)
    fun shouldThrowExceptionParsingWrongDouble() {
        val properties = HashMap<String, String>()

        properties.put("key.one", "not a number")

        val prop = buildProperlty(properties)

        prop.getDouble("key.one")

    }

    @Test
    fun shouldReturnEmptyOptionalFloat() {
        val properties = HashMap<String, String>()

        properties.put("key.one", "value.one")
        properties.put("key.two", "value.two")

        val prop = buildProperlty(properties)

        assertFalse(prop.getFloat("key.three").isPresent)
    }


    @Test
    fun shouldReturnOptionalFloat() {
        val properties = HashMap<String, String>()

        properties.put("key.one", "123")
        properties.put("key.two", "1000000")

        val prop = buildProperlty(properties)

        assertEquals(123f, prop.getFloat("key.one").get().toFloat(), 0.1f)
        assertEquals(1000000f, prop.getFloat("key.two").get().toFloat(), 0.1f)
    }

    @Test
    fun shouldReturnDefaultfloat() {
        val properties = HashMap<String, String>()

        properties.put("key.one", "1")

        val prop = buildProperlty(properties)

        assertEquals(1f, prop.getFloat("key.one", 10f), 0.1f)
        assertEquals(10f, prop.getFloat("key.two", 10f), 0.1f)

    }

    @Test(expected = NumberFormatException::class)
    fun shouldThrowExceptionParsingWrongFloat() {
        val properties = HashMap<String, String>()

        properties.put("key.one", "not a number")

        val prop = buildProperlty(properties)

        prop.getFloat("key.one")

    }

    @Test
    fun shouldReturnEmptyOptionalLong() {
        val properties = HashMap<String, String>()

        properties.put("key.one", "value.one")
        properties.put("key.two", "value.two")

        val prop = buildProperlty(properties)

        assertFalse(prop.getLong("key.three").isPresent)
    }


    @Test
    fun shouldReturnOptionalLong() {
        val properties = HashMap<String, String>()

        properties.put("key.one", "123")
        properties.put("key.two", "1000000")

        val prop = buildProperlty(properties)

        assertEquals(123L, prop.getLong("key.one").get().toLong())
        assertEquals(1000000L, prop.getLong("key.two").get().toLong())
    }

    @Test
    fun shouldReturnDefaultLong() {
        val properties = HashMap<String, String>()

        properties.put("key.one", "1")

        val prop = buildProperlty(properties)

        assertEquals(1L, prop.getLong("key.one", 10L))
        assertEquals(10L, prop.getLong("key.two", 10L))

    }

    @Test(expected = NumberFormatException::class)
    fun shouldThrowExceptionParsingWrongLong() {
        val properties = HashMap<String, String>()

        properties.put("key.one", "not a number")

        val prop = buildProperlty(properties)

        prop.getLong("key.one")

    }

    @Test
    fun shouldReturnEmptyOptionalEnum() {
        val properties = HashMap<String, String>()

        properties.put("key.one", "value.one")
        properties.put("key.two", "value.two")

        val prop = buildProperlty(properties)

        assertFalse(prop.getEnum("key.three", NeedSomebodyToLove::class.java).isPresent)
    }


    @Test
    fun shouldReturnOptionalEnum() {
        val properties = HashMap<String, String>()

        properties.put("key.one", "ME")
        properties.put("key.two", "THEM")

        val prop = buildProperlty(properties)

        assertEquals(NeedSomebodyToLove.ME, prop.getEnum("key.one", NeedSomebodyToLove::class.java).get())
        assertEquals(NeedSomebodyToLove.THEM, prop.getEnum("key.two", NeedSomebodyToLove::class.java).get())
    }

    @Test
    fun shouldReturnDefaultEnum() {
        val properties = HashMap<String, String>()

        properties.put("key.one", "ME")

        val prop = buildProperlty(properties)

        assertEquals(NeedSomebodyToLove.ME, prop.getEnum("key.one", NeedSomebodyToLove.THEM))
        assertEquals(NeedSomebodyToLove.THEM, prop.getEnum("key.two", NeedSomebodyToLove.THEM))

    }

    @Test(expected = IllegalArgumentException::class)
    fun shouldThrowExceptionParsingWrongEnumLong() {
        val properties = HashMap<String, String>()

        properties.put("key.one", "not an enum")

        val prop = buildProperlty(properties)

        prop.getEnum("key.one", NeedSomebodyToLove::class.java)

    }

    @Test
    fun shouldReturntheKeyApplyingTheMapFunction() {
        val properties = HashMap<String, String>()

        properties.put("key.one", "111")

        val prop = buildProperlty(properties)

        assertEquals(111, prop.get<Int>("key.one", { Integer.valueOf(it) }).get().toInt().toLong())
        assertEquals(222, prop["key.two", 222, { Integer.valueOf(it) }].toInt().toLong())
        assertFalse(prop.get<Int>("key.three", { Integer.valueOf(it) }).isPresent)
    }

    @Test
    fun shouldReturnValueToArray() {
        val properties = HashMap<String, String>()

        properties.put("key.one", "111,AAAAA,BBB")

        val prop = buildProperlty(properties)

        val values = prop.getArray("key.one")
        assertEquals(3, values.size.toLong())
        assertEquals("111", values[0])
        assertEquals("AAAAA", values[1])
        assertEquals("BBB", values[2])
        assertEquals(0, prop.getArray("key.three").size.toLong())
    }

    @Test
    fun shouldReturnValueToList() {
        val properties = HashMap<String, String>()

        properties.put("key.one", "111,AAAAA,BBB")

        val prop = buildProperlty(properties)

        val values = prop.getList("key.one")
        assertEquals(3, values.size.toLong())
        assertEquals("111", values[0])
        assertEquals("AAAAA", values[1])
        assertEquals("BBB", values[2])
        assertEquals(0, prop.getList("key.three").size.toLong())
    }

    @Test
    fun shouldReturnValueToListOfCustomObjects() {
        val properties = HashMap<String, String>()

        properties.put("key.one", "111,222,333")

        val prop = buildProperlty(properties)

        val values = prop.getList<Int>("key.one", { Integer.valueOf(it) })
        assertEquals(3, values.size.toLong())
        assertEquals(111, values[0].toInt().toLong())
        assertEquals(222, values[1].toInt().toLong())
        assertEquals(333, values[2].toInt().toLong())
        assertEquals(0, prop.getList<Int>("key.three", { Integer.valueOf(it) }).size.toLong())
    }

    private fun buildProperlty(properties: Map<String, String>): Properlty {
        return Properlty(properties.entries.stream()
                .collect(Collectors.toMap({ it.key }, { PropertyValue.of(it.value) })))
    }

}