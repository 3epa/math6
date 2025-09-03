package com.itmo.algo;

import com.itmo.model.DataPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class ImprovedEulerMethod extends OneStepODESolver {
    private static final int ORDER = 2;

    public ImprovedEulerMethod() {
        super("Модифицированный метод Эйлера", ORDER);
    }

    @Override
    public List<DataPoint> compute(BiFunction<Double, Double, Double> function, List<Double> xList, double y0) {
        List<DataPoint> result = new ArrayList<>();
        result.add(new DataPoint(xList.getFirst(), y0));

        double h = xList.get(1) - xList.get(0);

        for (int i = 0; i < xList.size() - 1; i++) {
            double xPrev = xList.get(i);
            double yPrev = result.get(i).getY();

            double yPredictor = yPrev + h * function.apply(xPrev, yPrev);
            double yCorrector = yPrev + h / 2 * (function.apply(xPrev, yPrev) + function.apply(xPrev + h, yPredictor));
            result.add(new DataPoint(xList.get(i + 1), yCorrector));
        }
        return result;
    }
}
