/*******************************************************************************
 * Copyright 2017 Francesco Cina'
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.ufoscout.properlty;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.ufoscout.properlty.reader.ProgrammaticPropertiesReader;
import com.ufoscout.properlty.reader.Properties;
import com.ufoscout.properlty.reader.Reader;
import com.ufoscout.properlty.reader.decorator.ReplacerDecoratorReader;
import org.junit.Test;

import com.ufoscout.properlty.reader.PropertyValue;

import static org.junit.Assert.*;

public class ProperltyTest extends ProperltyBaseTest {

	@Test
	public void shouldReturnEmptyOptionalString() {
		final Map<String, String> properties = new HashMap<>();

		properties.put("key.one", "value.one");
		properties.put("key.two", "value.two");

		final Properlty prop = buildProperlty(properties);

		assertFalse(prop.get("key.three").isPresent());
	}

	@Test
	public void shouldReturnOptionalString() {
		final Map<String, String> properties = new HashMap<>();

		properties.put("key.one", "value.one");
		properties.put("key.two", "value.two");

		final Properlty prop = buildProperlty(properties);

		assertEquals("value.one", prop.get("key.one").get());
		assertEquals("value.two", prop.get("key.two").get());
		assertFalse(prop.get("key.ONE").isPresent());
	}

	@Test
	public void shouldReturnCaseInsensitive() {
		final Map<String, String> properties = new HashMap<>();

		properties.put("key.one", "value.one");
		properties.put("KEy.tWo", "value.two");

		final boolean caseSensitive = false;
		final Properlty prop = buildProperlty(properties, caseSensitive);

		assertEquals("value.one", prop.get("key.one").get());
		assertEquals("value.two", prop.get("key.two").get());
		assertEquals("value.one", prop.get("key.ONE").get());
		assertEquals("value.two", prop.get("KEY.TWo").get());
	}

	@Test
	public void shouldReturnDefaultString() {
		final Map<String, String> properties = new HashMap<>();

		properties.put("key.one", "value.one");

		final Properlty prop = buildProperlty(properties);

		assertEquals("value.one", prop.get("key.one", "default"));
		assertEquals("default", prop.get("key.two", "default"));
	}

	@Test
	public void shouldReturnEmptyOptionalInteger() {
		final Map<String, String> properties = new HashMap<>();

		properties.put("key.one", "value.one");
		properties.put("key.two", "value.two");

		final Properlty prop = buildProperlty(properties);

		assertFalse(prop.getInt("key.three").isPresent());
	}


	@Test
	public void shouldReturnOptionalInteger() {
		final Map<String, String> properties = new HashMap<>();

		properties.put("key.one", "123");
		properties.put("key.two", "1000000");

		final Properlty prop = buildProperlty(properties);

		assertEquals(123, prop.getInt("key.one").get().intValue());
		assertEquals(1000000, prop.getInt("key.two").get().intValue());
	}

	@Test
	public void shouldReturnDefaultInteger() {
		final Map<String, String> properties = new HashMap<>();

		properties.put("key.one", "1");

		final Properlty prop = buildProperlty(properties);

		assertEquals(1, prop.getInt("key.one", 10));
		assertEquals(10, prop.getInt("key.two", 10));

	}

	@Test(expected=NumberFormatException.class)
	public void shouldThrowExceptionParsingWrongInteger() {
		final Map<String, String> properties = new HashMap<>();

		properties.put("key.one", "not a number");

		final Properlty prop = buildProperlty(properties);

		prop.getInt("key.one");

	}

	@Test
	public void shouldReturnEmptyOptionalBoolean() {
		final Map<String, String> properties = new HashMap<>();

		properties.put("key.one", "value.one");
		properties.put("key.two", "value.two");

		final Properlty prop = buildProperlty(properties);

		assertFalse(prop.getBoolean("key.three").isPresent());
	}


	@Test
	public void shouldReturnOptionalBoolean() {
		final Map<String, String> properties = new HashMap<>();

		properties.put("key.one", "True");
		properties.put("key.two", "false");

		final Properlty prop = buildProperlty(properties);

		assertTrue(prop.getBoolean("key.one").get());
		assertFalse(prop.getBoolean("key.two").get());
	}

	@Test
	public void shouldReturnDefaultBoolean() {
		final Map<String, String> properties = new HashMap<>();

		properties.put("key.one", "true");

		final Properlty prop = buildProperlty(properties);

		assertTrue(prop.getBoolean("key.one", false));
		assertFalse(prop.getBoolean("key.two", false));
		assertTrue(prop.getBoolean("key.two", true));

	}

	@Test(expected=RuntimeException.class)
	public void shouldThrowExceptionParsingWrongBoolean() {
		final Map<String, String> properties = new HashMap<>();

		properties.put("key.one", "not a bool");

		final Properlty prop = buildProperlty(properties);

		prop.getBoolean("key.one");

	}

	@Test
	public void shouldReturnEmptyOptionalDouble() {
		final Map<String, String> properties = new HashMap<>();

		properties.put("key.one", "value.one");
		properties.put("key.two", "value.two");

		final Properlty prop = buildProperlty(properties);

		assertFalse(prop.getDouble("key.three").isPresent());
	}


	@Test
	public void shouldReturnOptionalDouble() {
		final Map<String, String> properties = new HashMap<>();

		properties.put("key.one", "123");
		properties.put("key.two", "1000000");

		final Properlty prop = buildProperlty(properties);

		assertEquals(123d, prop.getDouble("key.one").get().doubleValue(), 0.1d);
		assertEquals(1000000d, prop.getDouble("key.two").get().doubleValue(), 0.1d);
	}

	@Test
	public void shouldReturnDefaultDouble() {
		final Map<String, String> properties = new HashMap<>();

		properties.put("key.one", "1");

		final Properlty prop = buildProperlty(properties);

		assertEquals(1d, prop.getDouble("key.one", 1.1111d), 0.1d);
		assertEquals(10d, prop.getDouble("key.two", 10d), 0.1d);

	}

	@Test(expected=NumberFormatException.class)
	public void shouldThrowExceptionParsingWrongDouble() {
		final Map<String, String> properties = new HashMap<>();

		properties.put("key.one", "not a number");

		final Properlty prop = buildProperlty(properties);

		prop.getDouble("key.one");

	}

	@Test
	public void shouldReturnEmptyOptionalFloat() {
		final Map<String, String> properties = new HashMap<>();

		properties.put("key.one", "value.one");
		properties.put("key.two", "value.two");

		final Properlty prop = buildProperlty(properties);

		assertFalse(prop.getFloat("key.three").isPresent());
	}


	@Test
	public void shouldReturnOptionalFloat() {
		final Map<String, String> properties = new HashMap<>();

		properties.put("key.one", "123");
		properties.put("key.two", "1000000");

		final Properlty prop = buildProperlty(properties);

		assertEquals(123f, prop.getFloat("key.one").get().floatValue(), 0.1f);
		assertEquals(1000000f, prop.getFloat("key.two").get().floatValue(), 0.1f);
	}

	@Test
	public void shouldReturnDefaultFloat() {
		final Map<String, String> properties = new HashMap<>();

		properties.put("key.one", "1");

		final Properlty prop = buildProperlty(properties);

		assertEquals(1f, prop.getFloat("key.one", 10), 0.1f);
		assertEquals(10f, prop.getFloat("key.two", 10), 0.1f);

	}

	@Test(expected=NumberFormatException.class)
	public void shouldThrowExceptionParsingWrongFloat() {
		final Map<String, String> properties = new HashMap<>();

		properties.put("key.one", "not a number");

		final Properlty prop = buildProperlty(properties);

		prop.getFloat("key.one");

	}

	@Test
	public void shouldReturnEmptyOptionalLong() {
		final Map<String, String> properties = new HashMap<>();

		properties.put("key.one", "value.one");
		properties.put("key.two", "value.two");

		final Properlty prop = buildProperlty(properties);

		assertFalse(prop.getLong("key.three").isPresent());
	}


	@Test
	public void shouldReturnOptionalLong() {
		final Map<String, String> properties = new HashMap<>();

		properties.put("key.one", "123");
		properties.put("key.two", "1000000");

		final Properlty prop = buildProperlty(properties);

		assertEquals(123l, prop.getLong("key.one").get().longValue());
		assertEquals(1000000l, prop.getLong("key.two").get().longValue());
	}

	@Test
	public void shouldReturnDefaultLong() {
		final Map<String, String> properties = new HashMap<>();

		properties.put("key.one", "1");

		final Properlty prop = buildProperlty(properties);

		assertEquals(1l, prop.getLong("key.one", 10l));
		assertEquals(10l, prop.getLong("key.two", 10l));

	}

	@Test
	public void shouldReturnOptionalBigDecimal() {
		final Map<String, String> properties = new HashMap<>();

		properties.put("key.one", "123");
		properties.put("key.two", "1000000");

		final Properlty prop = buildProperlty(properties);

		assertEquals(123, prop.getBigDecimal("key.one").get().intValue());
		assertEquals(1000000, prop.getBigDecimal("key.two").get().intValue());
	}

	@Test
	public void shouldReturnDefaultBigDecimal() {
		final Map<String, String> properties = new HashMap<>();

		properties.put("key.one", "1");

		final Properlty prop = buildProperlty(properties);

		assertEquals(1, prop.getBigDecimal("key.one", new BigDecimal(10)).intValue());
		assertEquals(10, prop.getBigDecimal("key.two", new BigDecimal(10)).intValue());

	}

	@Test
	public void shouldReturnOptionalBigInteger() {
		final Map<String, String> properties = new HashMap<>();

		properties.put("key.one", "123");
		properties.put("key.two", "1000000");

		final Properlty prop = buildProperlty(properties);

		assertEquals(123, prop.getBigInteger("key.one").get().intValue());
		assertEquals(1000000, prop.getBigInteger("key.two").get().intValue());
	}

	@Test
	public void shouldReturnDefaultBigInteger() {
		final Map<String, String> properties = new HashMap<>();

		properties.put("key.one", "1");

		final Properlty prop = buildProperlty(properties);

		assertEquals(1, prop.getBigInteger("key.one", new BigInteger("10")).intValue());
		assertEquals(10, prop.getBigInteger("key.two", new BigInteger("10")).intValue());

	}

	@Test(expected=NumberFormatException.class)
	public void shouldThrowExceptionParsingWrongLong() {
		final Map<String, String> properties = new HashMap<>();

		properties.put("key.one", "not a number");

		final Properlty prop = buildProperlty(properties);

		prop.getLong("key.one");

	}

	@Test
	public void shouldReturnEmptyOptionalEnum() {
		final Map<String, String> properties = new HashMap<>();

		properties.put("key.one", "value.one");
		properties.put("key.two", "value.two");

		final Properlty prop = buildProperlty(properties);

		assertFalse(prop.getEnum("key.three", NeedSomebodyToLove.class).isPresent());
	}


	@Test
	public void shouldReturnOptionalEnum() {
		final Map<String, String> properties = new HashMap<>();

		properties.put("key.one", "ME");
		properties.put("key.two", "THEM");

		final Properlty prop = buildProperlty(properties);

		assertEquals(NeedSomebodyToLove.ME, prop.getEnum("key.one", NeedSomebodyToLove.class).get());
		assertEquals(NeedSomebodyToLove.THEM, prop.getEnum("key.two", NeedSomebodyToLove.class).get());
	}

	@Test
	public void shouldReturnDefaultEnum() {
		final Map<String, String> properties = new HashMap<>();

		properties.put("key.one", "ME");

		final Properlty prop = buildProperlty(properties);

		assertEquals(NeedSomebodyToLove.ME, prop.getEnum("key.one", NeedSomebodyToLove.THEM));
		assertEquals(NeedSomebodyToLove.THEM, prop.getEnum("key.two", NeedSomebodyToLove.THEM));

	}

	@Test(expected=IllegalArgumentException.class)
	public void shouldThrowExceptionParsingWrongEnumLong() {
		final Map<String, String> properties = new HashMap<>();

		properties.put("key.one", "not an enum");

		final Properlty prop = buildProperlty(properties);

		prop.getEnum("key.one", NeedSomebodyToLove.class);

	}

	@Test
	public void shouldReturntheKeyApplyingTheMapFunction() {
		final Map<String, String> properties = new HashMap<>();

		properties.put("key.one", "111");

		final Properlty prop = buildProperlty(properties);

		assertEquals(111, prop.get("key.one", Integer::valueOf).get().intValue());
		assertEquals(222, prop.get("key.two", 222, Integer::valueOf).intValue());
		assertFalse(prop.get("key.three", Integer::valueOf).isPresent());
	}

	@Test
	public void shouldReturnValueToArray() {
		final Map<String, String> properties = new HashMap<>();

		properties.put("key.one", "111,AAAAA,BBB");

		final Properlty prop = buildProperlty(properties);

		final String[] values = prop.getArray("key.one");
		assertEquals(3, values.length);
		assertEquals("111", values[0]);
		assertEquals("AAAAA", values[1]);
		assertEquals("BBB", values[2]);
		assertEquals(0, prop.getArray("key.three").length);
	}

	@Test
	public void shouldReturnValueToList() {
		final Map<String, String> properties = new HashMap<>();

		properties.put("key.one", "111,AAAAA,BBB");

		final Properlty prop = buildProperlty(properties);

		final List<String> values = prop.getList("key.one");
		assertEquals(3, values.size());
		assertEquals("111", values.get(0));
		assertEquals("AAAAA", values.get(1));
		assertEquals("BBB", values.get(2));
		assertEquals(0, prop.getList("key.three").size());
	}

	@Test
	public void shouldReturnValueToListOfCustomObjects() {
		final Map<String, String> properties = new HashMap<>();

		properties.put("key.one", "111,222,333");

		final Properlty prop = buildProperlty(properties);

		final List<Integer> values = prop.getList("key.one", Integer::valueOf);
		assertEquals(3, values.size());
		assertEquals(111, values.get(0).intValue());
		assertEquals(222, values.get(1).intValue());
		assertEquals(333, values.get(2).intValue());
		assertEquals(0, prop.getList("key.three", Integer::valueOf).size());
	}

	@Test
	public void shouldMatchPlaceholdersNotSensitiveCase() {
		final Map<String, String> properties = new HashMap<>();
		properties.put("key.ONE", "${value1:defaultValue1}");
		properties.put("keY.TWO", "${KEY.one:defaultValue2}");
		properties.put("key.three", "${KEY.one:defaultValue3}");

		final boolean caseSensitive = false;
		final Properlty prop = buildProperlty(properties, caseSensitive);

		assertEquals("defaultValue1", prop.get("key.one").get());
		assertEquals("defaultValue1", prop.get("key.two").get());
		assertEquals("defaultValue1", prop.get("key.THREE").get());

	}

	@Test
	public void shouldMatchPlaceholdersSensitiveCase() {
		final Map<String, String> properties = new HashMap<>();
		properties.put("key.ONE", "${value1:defaultValue1}");
		properties.put("keY.TWO", "${KEY.one:defaultValue2}");
		properties.put("key.three", "${KEY.one:defaultValue3}");

		final boolean caseSensitive = true;
		final Properlty prop = buildProperlty(properties, caseSensitive);

		assertEquals("defaultValue1", prop.get("key.ONE").get());
		assertEquals("defaultValue2", prop.get("keY.TWO").get());
		assertEquals("defaultValue3", prop.get("key.three").get());

	}

	private Properlty buildProperlty(Map<String, String> properties) {
		return buildProperlty(properties, true);
	}

	private Properlty buildProperlty(Map<String, String> properties, boolean caseSensitive) {
		ProperltyBuilder builder = Properlty.builder();
		properties.forEach((key, value) -> {
			builder.add(Properties.add(key, value));
		});
		return builder.caseSensitive(caseSensitive).build();
	}

}
