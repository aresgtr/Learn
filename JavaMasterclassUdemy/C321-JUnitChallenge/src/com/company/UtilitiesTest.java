package com.company;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UtilitiesTest {

    private Utilities util;
    @BeforeEach
    void setup() {
        util = new Utilities();
    }

    @Test
    void everyNthChar() {
        char[] output = util.everyNthChar(new char[] {'h', 'e', 'l', 'l', 'o'}, 2);
        assertArrayEquals(new char[] {'e', 'l'}, output);
        char[] output1 = util.everyNthChar(new char[] {'h', 'e', 'l', 'l', 'o'}, 8);
        assertArrayEquals(new char[] {'h', 'e', 'l', 'l', 'o'}, output1);
    }

    @Test
    void removePairs() {
        assertEquals("ABCDEF", util.removePairs("AABCDDEEFF"));
        assertNull(null, util.removePairs(null));
        assertEquals("A", util.removePairs("A"));
        assertEquals("", util.removePairs(""));
    }

    @Test
    void converter() {
        assertEquals(300, util.converter(10, 5));
    }

    //  To throw an exception
    @Test
    void converter_arithmeticException() {
        assertThrows(ArithmeticException.class, () -> util.converter(10, 0));
    }

    @Test
    void nullIfOddLength() {
        assertNull(util.nullIfOddLength("odd"));
        assertNotNull(util.nullIfOddLength("even"));
    }
}