package com.itmo.algo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class ImprovedEulerMethod extends OneStepODESolver {
    private static final int ORDER = 2;

    public ImprovedEulerMethod() {
        super(ORDER);
    }

    @Override
    public List<Double> compute(BiFunction<Double, Double, Double> function, List<Double> xList, double y0) {
        List<Double> result = new ArrayList<>();
        result.add(y0);

        double h = xList.get(1) - xList.get(0);

        for (int i = 0; i < xList.size() - 1; i++) {
            double xPrev = xList.get(i);
            double yPrev = result.get(i);

            double yPredictor = yPrev + h * function.apply(xPrev, yPrev);
            double yCorrector = yPrev + h / 2 * (function.apply(xPrev, yPrev) + function.apply(xPrev + h, yPredictor));
            result.add(yCorrector);
        }
        return result;
    }
}
