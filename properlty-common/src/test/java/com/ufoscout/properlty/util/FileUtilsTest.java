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
package com.ufoscout.properlty.util;

import com.ufoscout.properlty.ProperltyBaseTest;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class FileUtilsTest extends ProperltyBaseTest {

	@Test
	public void shouldReadFileFromRelativePath() throws IOException {
		try (InputStream is = FileUtils.getStream("./src/test/files/test1.properties")) {
			final String content = toString(is);
			assertNotNull(content);
			assertTrue(content.contains("keyOne"));
		}
	}

	@Test
	public void shouldReadFileFromRelativePathWithPrefix() throws IOException {
		try (InputStream is = FileUtils.getStream("file:src/test/files/test1.properties")) {
			final String content = toString(is);
			assertNotNull(content);
			assertTrue(content.contains("keyOne"));
		}
	}

	@Test
	public void shouldReadFileFromAbsolutePath() throws IOException {

		final String absolutePath = new File("src/test/files/test1.properties").getAbsolutePath();

		try (InputStream is = FileUtils.getStream(absolutePath)) {
			final String content = toString(is);
			assertNotNull(content);
			assertTrue(content.contains("keyOne"));
		}
	}

	@Test
	public void shouldReadFileFromAbsolutePathWithPrefix() throws IOException {

		final String absolutePath = new File("src/test/files/test1.properties").getAbsolutePath();

		try (InputStream is = FileUtils.getStream("file:" + absolutePath)) {
			final String content = toString(is);
			assertNotNull(content);
			assertTrue(content.contains("keyOne"));
		}
	}

	@Test
	public void shouldReadFileFromClasspath() throws IOException {
		try (InputStream is = FileUtils.getStream("classpath:resource1.properties")) {
			final String content = toString(is);
			assertNotNull(content);
			assertTrue(content.contains("name=resource1"));
		}
	}

	@Test
	public void shouldReadFileFromClasspathFolder() throws IOException {
		try (InputStream is = FileUtils.getStream("classpath:./inner/resource2.properties")) {
			final String content = toString(is);
			assertNotNull(content);
			assertTrue(content.contains("name=resource2"));
		}
	}

	@Test
	public void shouldThrowFileNotFoundExceptionForMissingFileFromClasspath() throws IOException {
		try {
			FileUtils.getStream("classpath:NOT_EXISTING_FILE");
			fail("Should have thrown a FileNotFoundException");
		} catch (final FileNotFoundException e) {
			assertTrue(e.getMessage().contains("NOT_EXISTING_FILE"));
		}
	}

	@Test
	public void shouldThrowFileNotFoundExceptionForMissingFile() throws IOException {
		try {
			FileUtils.getStream("file:NOT_EXISTING_FILE");
			fail("Should have thrown a FileNotFoundException");
		} catch (final FileNotFoundException e) {
			assertTrue(e.getMessage().contains("NOT_EXISTING_FILE"));
		}
	}

	private String toString(java.io.InputStream is) throws IOException {
		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

}
