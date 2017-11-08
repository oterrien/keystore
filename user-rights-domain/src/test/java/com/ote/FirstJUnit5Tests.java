package com.ote;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.IntStream;
import java.util.stream.Stream;

//@Tag("first")
@FastTest //custom annotation
class FirstJUnit5Tests {

    @Test
    void myFirstTest() {
        Assertions.assertEquals(2, 1 + 1);
    }

    @Test
    @DisplayName("My 1st JUnit 5 test")
    void myFirstTest(TestInfo testInfo) {
        Assertions.assertEquals(2, 1 + 1, "1 + 1 should equal 2");
        Assertions.assertEquals("My 1st JUnit 5 test", testInfo.getDisplayName(), "TestInfo is injected correctly");
    }

    @Test
    @Tag("slow")
    void aSlowTest() throws InterruptedException {
        Thread.sleep(1000);
    }

    @ParameterizedTest
    @ValueSource(strings = { "Hello", "World" })
    void testWithStringParameter(String argument) {
        Assertions.assertNotNull(argument);
    }

    @ParameterizedTest
    @MethodSource("range")
    void testWithRangeMethodSource(int argument) {
        Assertions.assertNotEquals(9, argument);
    }

    static IntStream range() {
        return IntStream.range(0, 20).skip(10);
    }

    @ParameterizedTest
    @MethodSource("stringAndIntProvider")
    void testWithMultiArgMethodSource(String first, int second) {
        Assertions.assertNotNull(first);
        Assertions.assertNotEquals(0, second);
    }

    static Stream<Arguments> stringAndIntProvider() {
        return Stream.of(Arguments.of("foo", 1), Arguments.of("bar", 2));
    }

    @ParameterizedTest
    @CsvSource({ "foo, 1", "bar, 2", "'baz, qux', 3" })
    void testWithCsvSource(String first, int second) {
        Assertions.assertNotNull(first);
        Assertions.assertNotEquals(0, second);
    }

}