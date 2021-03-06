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

import com.ufoscout.properlty.reader.Properties
import com.ufoscout.properlty.reader.PropertyValue
import org.junit.Assert.*
import org.junit.Test
import java.lang.RuntimeException
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*
import java.util.stream.Collectors

class ProperltyTest : ProperltyBaseTest() {

    @Test
    fun shouldReturnEmptyOptionalString() {
        val properties = HashMap<String, String>()

        properties.put("key.one", "value.one")
        properties.put("key.two", "value.two")

        val prop = buildProperlty(properties)

        assertNull(prop["key.three"])
    }

    @Test
    fun shouldReturnOptionalString() {
        val properties = HashMap<String, String>()

        properties.put("key.one", "value.one")
        properties.put("key.two", "value.two")

        val prop = buildProperlty(properties)

        assertEquals("value.one", prop.get("key.one"))
        assertEquals("value.two", prop["key.two"])
        assertNull(prop["key.ONE"])
    }

    @Test
    fun shouldReturnCaseInsensitive() {
        val properties = HashMap<String, String>()

        properties["key.one"] = "value.one"
        properties["KEy.tWo"] = "value.two"

        val caseSensitive = false
        val prop = buildProperlty(properties, caseSensitive)

        assertEquals("value.one", prop["key.one"])
        assertEquals("value.two", prop["key.two"])
        assertEquals("value.one", prop["key.ONE"])
        assertEquals("value.two", prop["KEY.TWo"])
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

        assertNull(prop.getInt("key.three"))
    }


    @Test
    fun shouldReturnOptionalInteger() {
        val properties = HashMap<String, String>()

        properties.put("key.one", "123")
        properties.put("key.two", "1000000")

        val prop = buildProperlty(properties)

        assertEquals(123, prop.getInt("key.one"))
        assertEquals(1000000, prop.getInt("key.two"))
    }

    @Test
    fun shouldReturnDefaultInteger() {
        val properties = HashMap<String, String>()

        properties.put("key.one", "1")

        val prop = buildProperlty(properties)

        assertEquals(1, prop.getInt("key.one", 10))
        assertEquals(10, prop.getInt("key.two", 10))

    }

    @Test(expected = NumberFormatException::class)
    fun shouldThrowExceptionParsingWrongInteger() {
        val properties = HashMap<String, String>()

        properties.put("key.one", "not a number")

        val prop = buildProperlty(properties)

        prop.getInt("key.one")

    }

    @Test
    fun shouldReturnEmptyOptionalBoolean() {
        val properties = HashMap<String, String>()

        properties.put("key.one", "value.one")
        properties.put("key.two", "value.two")

        val prop = buildProperlty(properties)

        assertNull(prop.getBoolean("key.three"))
    }


    @Test
    fun shouldReturnOptionalBoolean() {
        val properties = HashMap<String, String>()

        properties.put("key.one", "true")
        properties.put("key.two", "false")

        val prop = buildProperlty(properties)

        assertTrue(prop.getBoolean("key.one")!!)
        assertFalse(prop.getBoolean("key.two")!!)
    }

    @Test
    fun shouldReturnDefaultBoolean() {
        val properties = HashMap<String, String>()

        properties.put("key.one", "True")

        val prop = buildProperlty(properties)

        assertTrue(prop.getBoolean("key.one", false))
        assertFalse(prop.getBoolean("key.two", false))
        assertTrue(prop.getBoolean("key.two", true))

    }

    @Test(expected = RuntimeException::class)
    fun shouldThrowExceptionParsingWrongBoolean() {
        val properties = HashMap<String, String>()

        properties.put("key.one", "not a boolean")

        val prop = buildProperlty(properties)

        prop.getDouble("key.one")

    }

    @Test
    fun shouldReturnEmptyOptionalDouble() {
        val properties = HashMap<String, String>()

        properties.put("key.one", "value.one")
        properties.put("key.two", "value.two")

        val prop = buildProperlty(properties)

        assertNull(prop.getDouble("key.three"))
    }


    @Test
    fun shouldReturnOptionalDouble() {
        val properties = HashMap<String, String>()

        properties.put("key.one", "123")
        properties.put("key.two", "1000000")

        val prop = buildProperlty(properties)

        assertEquals(123.0, prop.getDouble("key.one")!!, 0.1)
        assertEquals(1000000.0, prop.getDouble("key.two")!!, 0.1)
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

        assertNull(prop.getFloat("key.three"))
    }


    @Test
    fun shouldReturnOptionalFloat() {
        val properties = HashMap<String, String>()

        properties.put("key.one", "123")
        properties.put("key.two", "1000000")

        val prop = buildProperlty(properties)

        assertEquals(123f, prop.getFloat("key.one")!!, 0.1f)
        assertEquals(1000000f, prop.getFloat("key.two")!!, 0.1f)
    }

    @Test
    fun shouldReturnDefaultFloat() {
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

        assertNull(prop.getLong("key.three"))
    }


    @Test
    fun shouldReturnOptionalLong() {
        val properties = HashMap<String, String>()

        properties.put("key.one", "123")
        properties.put("key.two", "1000000")

        val prop = buildProperlty(properties)

        assertEquals(123L, prop.getLong("key.one"))
        assertEquals(1000000L, prop.getLong("key.two"))
    }

    @Test
    fun shouldReturnDefaultLong() {
        val properties = HashMap<String, String>()

        properties.put("key.one", "1")

        val prop = buildProperlty(properties)

        assertEquals(1L, prop.getLong("key.one", 10L))
        assertEquals(10L, prop.getLong("key.two", 10L))

    }

    @Test
    fun shouldReturnOptionalBigDecimal() {
        val properties = HashMap<String, String>()

        properties.put("key.one", "123")
        properties.put("key.two", "1000000")

        val prop = buildProperlty(properties)

        assertEquals(123L, prop.getBigDecimal("key.one")?.toLong())
        assertEquals(1000000L, prop.getBigDecimal("key.two")?.toLong())
    }

    @Test
    fun shouldReturnDefaultBigDecimal() {
        val properties = HashMap<String, String>()

        properties.put("key.one", "1")

        val prop = buildProperlty(properties)

        assertEquals(1L, prop.getBigDecimal("key.one", BigDecimal("10")).toLong())
        assertEquals(10L, prop.getBigDecimal("key.two", BigDecimal(10L)).toLong())

    }

    @Test
    fun shouldReturnOptionalBigInteger() {
        val properties = HashMap<String, String>()

        properties.put("key.one", "123")
        properties.put("key.two", "1000000")

        val prop = buildProperlty(properties)

        assertEquals(123L, prop.getBigInteger("key.one")?.toLong())
        assertEquals(1000000L, prop.getBigInteger("key.two")?.toLong())
    }

    @Test
    fun shouldReturnDefaultBigInteger() {
        val properties = HashMap<String, String>()

        properties.put("key.one", "1")

        val prop = buildProperlty(properties)

        assertEquals(1L, prop.getBigInteger("key.one", BigInteger.valueOf(10L)).toLong())
        assertEquals(10L, prop.getBigInteger("key.two", BigInteger.valueOf(10L)).toLong())

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

        assertNull(prop.getEnum<NeedSomebodyToLove>("key.three"))
    }


    @Test
    fun shouldReturnOptionalEnum() {
        val properties = HashMap<String, String>()

        properties.put("key.one", "ME")
        properties.put("key.two", "THEM")

        val prop = buildProperlty(properties)

        assertEquals(NeedSomebodyToLove.ME, prop.getEnum<NeedSomebodyToLove>("key.one"))
        assertEquals(NeedSomebodyToLove.THEM, prop.getEnum<NeedSomebodyToLove>("key.two"))
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

        prop.getEnum<NeedSomebodyToLove>("key.one")

    }

    @Test
    fun shouldReturntheKeyApplyingTheMapFunction() {
        val properties = HashMap<String, String>()

        properties.put("key.one", "111")

        val prop = buildProperlty(properties)

        assertEquals(111, prop.get<Int>("key.one", { Integer.valueOf(it) }))
        assertEquals(222, prop["key.two", 222, { Integer.valueOf(it) }])
        assertNull(prop.get<Int>("key.three", { Integer.valueOf(it) }))
    }

    @Test
    fun shouldReturnValueToArray() {
        val properties = HashMap<String, String>()

        properties.put("key.one", "111,AAAAA,BBB")

        val prop = buildProperlty(properties)

        val values = prop.getArray("key.one")
        assertEquals(3, values.size)
        assertEquals("111", values[0])
        assertEquals("AAAAA", values[1])
        assertEquals("BBB", values[2])
        assertEquals(0, prop.getArray("key.three").size)
    }

    @Test
    fun shouldReturnValueToList() {
        val properties = HashMap<String, String>()

        properties.put("key.one", "111,AAAAA,BBB")

        val prop = buildProperlty(properties)

        val values = prop.getList("key.one")
        assertEquals(3, values.size)
        assertEquals("111", values[0])
        assertEquals("AAAAA", values[1])
        assertEquals("BBB", values[2])
        assertEquals(0, prop.getList("key.three").size)
    }

    @Test
    fun shouldReturnValueToListOfCustomObjects() {
        val properties = HashMap<String, String>()

        properties.put("key.one", "111,222,333")

        val prop = buildProperlty(properties)

        val values = prop.getList<Int>(key = "key.one"){Integer.valueOf(it)}
        assertEquals(3, values.size)
        assertEquals(111, values[0])
        assertEquals(222, values[1])
        assertEquals(333, values[2])
        assertEquals(0, prop.getList<Int>("key.three", { Integer.valueOf(it) }).size)
    }

    @Test
    fun shouldMatchPlaceholdersNotSensitiveCase() {
        val properties = HashMap<String, String>()
        properties["key.ONE"] = "\${value1:defaultValue1}"
        properties["keY.TWO"] = "\${KEY.one:defaultValue2}"
        properties["key.three"] = "\${KEY.one:defaultValue3}"

        val caseSensitive = false
        val prop = buildProperlty(properties, caseSensitive)

        assertEquals("defaultValue1", prop["key.one"])
        assertEquals("defaultValue1", prop["key.two"])
        assertEquals("defaultValue1", prop["key.THREE"])

    }

    @Test
    fun shouldMatchPlaceholdersSensitiveCase() {
        val properties = HashMap<String, String>()
        properties["key.ONE"] = "\${value1:defaultValue1}"
        properties["keY.TWO"] = "\${KEY.one:defaultValue2}"
        properties["key.three"] = "\${KEY.one:defaultValue3}"

        val caseSensitive = true
        val prop = buildProperlty(properties, caseSensitive)

        assertEquals("defaultValue1", prop["key.ONE"])
        assertEquals("defaultValue2", prop["keY.TWO"])
        assertEquals("defaultValue3", prop["key.three"])

    }

    private fun buildProperlty(properties: Map<String, String>, caseSensitive: Boolean = true): Properlty {
        val builder = Properlty.builder().caseSensitive(caseSensitive)
        properties.forEach { key, value -> builder.add(Properties.add(key, value)) }
        return builder.build()
    }

}
