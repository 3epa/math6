package com.itmo.utils;

import com.itmo.exceptions.IncorrectInputException;

import java.util.ArrayList;
import java.util.List;

public class MathUtils {

    private final static double EPSILON = 0.0001;

    public static List<Double> createGrid(double start, double end, double h) throws IncorrectInputException {
        List<Double> grid = new ArrayList<>();
        int steps = (int) Math.ceil((end - start) / h);
        for (int i = 0; i < steps; i++) {
            double x = start + i * h;
            grid.add(x);
        }
        if (Math.abs(end - (grid.getLast() + h)) < EPSILON) {
            grid.add(end);
        } else {
            throw new IncorrectInputException("Текущий шаг не позволяет составить равномерную сетку, попробуйте выбрать другой");
        }
        return grid;
    }
}
