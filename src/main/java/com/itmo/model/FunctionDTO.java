package com.itmo.model;

import java.util.function.BiFunction;
import java.util.function.Function;

public class FunctionDTO {
    private final String name;
    private final BiFunction<Double, Double, Double> function;
    private final Function<Double, Double> exactFunction;

    public FunctionDTO(String name, BiFunction<Double, Double, Double> function, Function<Double, Double> exactFunction) {
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

    public Function<Double, Double> getExactFunction() {
        return exactFunction;
    }
}
