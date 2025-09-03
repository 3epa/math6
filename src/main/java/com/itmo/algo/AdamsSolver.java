package com.itmo.algo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class AdamsSolver extends MultiStepODESolver {

    private static final double EPSILON = 0.001;
    private static final int MAX_ITERATIONS = 100;

    @Override
    protected List<Double> compute(BiFunction<Double, Double, Double> function, List<Double> xList, double y0) {
        RungeKutta4Solver solver = new RungeKutta4Solver();
        List<Double> initialXList = new ArrayList<>();

        for (int i = 0; i < Math.min(xList.size(), 4); i++) {
            initialXList.add(xList.get(i));
        }
        List<Double> yList = solver.compute(function, initialXList, y0);

        double h = xList.get(1) - xList.get(0);
        for (int i = 3; i < xList.size() - 1; i++) {
            double f0 = function.apply(xList.get(i), yList.get(i));
            double f1 = function.apply(xList.get(i - 1), yList.get(i - 1));
            double f2 = function.apply(xList.get(i - 2), yList.get(i - 2));
            double f3 = function.apply(xList.get(i - 3), yList.get(i - 3));

            double yPredictor = yList.get(i) + h / 24 * (55 * f0 - 59 * f1 + 37 * f2 - 9 * f3);
            double yNext = yPredictor;
            int iteration = 0;
            while (true) {
                double yCorrector = yList.get(i) + h / 24 * (9 * function.apply(xList.get(i), yNext) + 19 * f0 - 5 * f1 + f2);
                if (Math.abs(yNext - yCorrector) < EPSILON || iteration >= MAX_ITERATIONS) {
                    yNext = yCorrector;
                    break;
                }
                yNext = yCorrector;
                iteration++;
            }
            yList.add(yNext);
        }
        return yList;
    }
}
