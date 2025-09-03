package com.itmo.model;

import com.itmo.utils.TriFunction;

import java.util.function.BiFunction;


public class FunctionDTO {
    private final String name;
    private final BiFunction<Double, Double, Double> function;
    private final TriFunction<Double, Double, Double, Double> exactFunction;

    public FunctionDTO(String name, BiFunction<Double, Double, Double> function, TriFunction<Double, Double, Double, Double> exactFunction) {
        this.name = name;
        this.function = function;
        this.exactFunction = exactFunction;
    }

    public String getName() {
        return name;
    }

    public BiFunction<Double, Double, Double> getFunction() {
        return function;
    }

    public TriFunction<Double, Double, Double, Double> getExactFunction() {
        return exactFunction;
    }
}
