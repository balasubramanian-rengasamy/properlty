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
package com.properlty;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.properlty.exception.UnresolvablePlaceholdersException;
import com.properlty.reader.DoNothingReader;
import com.properlty.reader.PropertiesResourceReader;

public class ProperltyBuilderTest extends ProperltyBaseTest {

	@Test
	public void environmentVariablesKeysShouldBeIncludedAndNormalized() {

		final Map<String, String> envVar = System.getenv();
		assertFalse(envVar.isEmpty());

		final Properlty prop = Properlty.builder().build();

		for (final Entry<String, String> envEntry : envVar.entrySet()) {

			final String key = envEntry.getKey();
			final String value = envEntry.getValue();
			assertEquals(value, prop.get(key).get());

			final String normalizedKey = key.toLowerCase().replace("_", ".");
			assertEquals(value, prop.get(normalizedKey).get());
		}

	}

	@Test
	public void systemPropertiesShouldHaveHigherPriorityThanEnvVariables() {
		final Map<String, String> envVar = System.getenv();
		assertTrue(envVar.size() >= 2);

		final String[] envVarKeys = envVar.keySet().toArray(new String[0]);

		final String envVarKey1 = envVarKeys[0];
		final String envVarKey1Normalized = envVarKey1.toLowerCase().replace("_", ".");
		final String envVarValue1 = envVar.get(envVarKey1);

		final String envVarKey2 = envVarKeys[1];
		final String envVarKey2Normalized = envVarKey2.toLowerCase().replace("_", ".");
		final String envVarValue2 = envVar.get(envVarKey2);

		try {
			// Override an environment variable with a system property
			final String overriddenValue = UUID.randomUUID().toString();
			System.setProperty(envVarKey1Normalized, overriddenValue);

			final Properlty prop = Properlty.builder().build();

			assertEquals(overriddenValue, prop.get(envVarKey1Normalized).get());
			assertEquals(envVarValue1, prop.get(envVarKey1).get());

			assertEquals(envVarValue2, prop.get(envVarKey2Normalized).get());
			assertEquals(envVarValue2, prop.get(envVarKey2).get());

		} finally {
			System.clearProperty(envVarKey1Normalized);
		}
	}

	@Test
	public void envVariablesShouldHaveHigherPriorityThanCustomProperties() {

		final Map<String, String> envVar = System.getenv();
		assertTrue(envVar.size() >= 1);

		final String[] envVarKeys = envVar.keySet().toArray(new String[0]);

		final String envVarKey1 = envVarKeys[0];
		final String envVarKey1Normalized = envVarKey1.toLowerCase().replace("_", ".");
		final String envVarValue1 = envVar.get(envVarKey1);

		final String customValue = UUID.randomUUID().toString();
		final String customKey2 = UUID.randomUUID().toString();

		final Properlty prop = Properlty.builder()
				.add(new DoNothingReader(ImmutableMap.of(envVarKey1Normalized, customValue, customKey2, customValue)))
				.build();

		assertEquals(envVarValue1, prop.get(envVarKey1Normalized).get());
		assertEquals(customValue, prop.get(customKey2).get());
	}

	@Test
	public void shouldIgnoreFileNotFound() {

		final String key = UUID.randomUUID().toString();
		try {
			System.setProperty(key, key);

			final Properlty prop = Properlty.builder()
					.add(PropertiesResourceReader.build("NOT VALID PATH").ignoreNotFound(true).charset(StandardCharsets.UTF_8))
					.build();
			assertNotNull(prop);

			assertTrue(prop.get(key).isPresent());
		} finally {
			System.clearProperty(key);
		}

	}

	@Test
	public void shouldFailIfFileNotFound() {
		try {
			Properlty.builder()
					.add("NOT VALID PATH")
					.build();
			fail("Should fail before");
		} catch (final RuntimeException e) {
			assertTrue(e.getCause() instanceof FileNotFoundException);
		}
	}

	@Test
	public void shouldConsiderFileAddPriority() {
		final Properlty prop = Properlty.builder()
					.add("file:./src/test/files/test1.properties")
					.add("classpath:resource1.properties")
					.add("classpath:inner/resource2.properties")
					.build();

		// from file:./src/test/files/test1.properties
		assertEquals( "firstvalue", prop.get("keyOne").get() );

		// from classpath:resource1.properties AND classpath:resource2.properties
		assertEquals( "resource2", prop.get("name").get() );

	}

	@Test
	public void shouldBePossibleTosetCustomPriority() {

		final String key = UUID.randomUUID().toString();
		try {
			System.setProperty(key, "SystemProperty");

			final Properlty prop = Properlty.builder()
					.add(new DoNothingReader(ImmutableMap.of(key, "customReader")), Properlty.HIGHEST_PRIORITY )
					.build();
			assertNotNull(prop);

			assertEquals("customReader", prop.get(key).get());
		} finally {
			System.clearProperty(key);
		}

	}

	@Test
	public void shouldReplacePlaceHolders() {

		final String key1 = "key1";
		final String value1 = UUID.randomUUID().toString();
		try {
			System.setProperty(key1, value1);

			final Properlty prop = Properlty.builder()
					.add(new DoNothingReader(ImmutableMap.of("key2", "${${key3}}__${key1}")), Properlty.HIGHEST_PRIORITY )
					.add(new DoNothingReader(ImmutableMap.of("key3", "key1")), Properlty.HIGHEST_PRIORITY )
					.build();
			assertNotNull(prop);

			assertEquals(value1 + "__" + value1, prop.get("key2").get());
		} finally {
			System.clearProperty(key1);
		}

	}

	@Test
	public void shouldReplaceUsingCustomDelimiters() {

		final String startDelimiter = "((";
		final String endDelimiter = "))";

			final Properlty prop = Properlty.builder()
					.delimiters(startDelimiter, endDelimiter)
					.add(new DoNothingReader(ImmutableMap.of("key1", "value1", "key2", "((((key3))))__((key1))")), Properlty.HIGHEST_PRIORITY )
					.add(new DoNothingReader(ImmutableMap.of("key3", "key1")), Properlty.HIGHEST_PRIORITY )
					.build();
			assertNotNull(prop);

			assertEquals("value1__value1", prop.get("key2").get());

	}

	@Test
	public void shouldIgnoreNotResolvedPlaceHolders() {

			final Properlty prop = Properlty.builder()
					.ignoreUnresolvablePlaceholders(true)
					.add(new DoNothingReader(ImmutableMap.of("key2", "${${key3}}__${key1}")), Properlty.HIGHEST_PRIORITY )
					.add(new DoNothingReader(ImmutableMap.of("key3", "key1")), Properlty.HIGHEST_PRIORITY )
					.build();
			assertNotNull(prop);

			assertEquals("${key1}__${key1}", prop.get("key2").get());

	}

	@Test(expected=UnresolvablePlaceholdersException.class)
	public void shouldFailIfNotResolvedPlaceHolders() {
			Properlty.builder()
					.add(new DoNothingReader(ImmutableMap.of("key2", "${${key3}}__${key1}")), Properlty.HIGHEST_PRIORITY )
					.add(new DoNothingReader(ImmutableMap.of("key3", "key1")), Properlty.HIGHEST_PRIORITY )
					.build();
	}


}
