package com.itmo.algo;

import com.itmo.model.DataPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class RungeKutta4Solver extends OneStepODESolver {
    private final static int ORDER = 4;

    public RungeKutta4Solver() {
        super(ORDER);
    }

    @Override
    protected List<DataPoint> compute(BiFunction<Double, Double, Double> function, List<Double> xList, double y0) {
        List<DataPoint> result = new ArrayList<>();
        result.add(new DataPoint(xList.getFirst(), y0));

        double h = xList.get(1) - xList.get(0);

        for (int i = 0; i < xList.size() - 1; i++) {
            double xPrev = xList.get(i);
            double yPrev = result.get(i).getY();

            double k1 = h * function.apply(xPrev, yPrev);
            double k2 = h * function.apply(xPrev + h / 2, yPrev + k1 / 2);
            double k3 = h * function.apply(xPrev + h / 2, yPrev + k2 / 2);
            double k4 = h * function.apply(xPrev + h, yPrev + k3);
            double yNext = yPrev + (k1 + 2 * k2 + 2 * k3 + k4) / 6;
            result.add(new DataPoint(xList.get(i + 1), yNext));
        }

        return result;
    }
}
