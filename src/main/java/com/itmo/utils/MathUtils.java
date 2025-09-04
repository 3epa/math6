package com.itmo.utils;

import com.itmo.exceptions.IncorrectInputException;

import java.util.ArrayList;
import java.util.List;

public class MathUtils {

    private final static double EPSILON = 1e-8;

    public static List<Double> createGrid(double start, double end, double h) throws IncorrectInputException {
        List<Double> grid = new ArrayList<>();
        int n = (int) Math.round((end - start) / h);
        double actualStep = (end - start) / n;

        if (Math.abs(actualStep - h) > EPSILON) {
            throw new IncorrectInputException("Текущий шаг не позволяет составить равномерную сетку, попробуйте выбрать другой");
        }

        for (int i = 0; i <= n; i++) {
            grid.add(start + i * actualStep);
        }
        return grid;
    }
}
