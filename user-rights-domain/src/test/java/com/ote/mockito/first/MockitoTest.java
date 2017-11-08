package com.ote.mockito.first;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.stream.IntStream;

public class MockitoTest {

    public class Calculator {
        public int add(int... elements) {
            return IntStream.of(elements).sum();
        }
    }

    @Mock
    private Calculator mockCalculator;

    private Calculator calculator = new Calculator();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(mockCalculator.add(Mockito.any())).thenReturn(1);
    }

    @Test
    public void testMockito() {
        int res = mockCalculator.add(1, 2, 3, 4);

        //The mock is called
        Assertions.assertEquals(1, res);
    }

    @Test
    public void testReal() {
        int res = calculator.add(1, 2, 3, 4);

        //The mock is called
        Assertions.assertEquals(10, res);

    }
}
