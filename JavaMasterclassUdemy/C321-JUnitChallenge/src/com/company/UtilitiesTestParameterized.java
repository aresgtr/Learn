package com.company;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class UtilitiesTestParameterized {

    private Utilities util;

    @BeforeEach
    void setup() {
        util = new Utilities();
    }

    static Stream<Arguments> testConditions() {
        return Stream.of(
                Arguments.of("ABCDEF", "ABCDEF"),
                Arguments.of("AB88EFFG", "AB8EFG"),
                Arguments.of("112233445566", "123456"),
                Arguments.of("A", "A"),
                Arguments.of("电电磁磁", "电磁")
        );
    }

    @ParameterizedTest
    @MethodSource("testConditions")
    void removePairs(String input, String output) {
        assertEquals(output, util.removePairs(input));
    }
}
