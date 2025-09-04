package com.itmo.algo;

import com.itmo.exceptions.IncorrectInputException;
import com.itmo.model.DataPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class AdamsSolver extends MultiStepODESolver {

    private static final double EPSILON = 0.001;
    private static final int MAX_ITERATIONS = 100;

    public AdamsSolver() {
        super("Метод Адамса");
    }

    @Override
    protected List<DataPoint> compute(BiFunction<Double, Double, Double> function, List<Double> xList, double y0) throws IncorrectInputException {
        RungeKutta4Solver solver = new RungeKutta4Solver();
        List<Double> initialXList = new ArrayList<>();

        for (int i = 0; i < Math.min(xList.size(), 4); i++) {
            initialXList.add(xList.get(i));
        }
        List<DataPoint> dataPointList = solver.compute(function, initialXList, y0);

        double h = xList.get(1) - xList.get(0);
        for (int i = 3; i < xList.size() - 1; i++) {
            double f0 = function.apply(xList.get(i), dataPointList.get(i).getY());
            double f1 = function.apply(xList.get(i - 1), dataPointList.get(i - 1).getY());
            double f2 = function.apply(xList.get(i - 2), dataPointList.get(i - 2).getY());
            double f3 = function.apply(xList.get(i - 3), dataPointList.get(i - 3).getY());

            double yPredictor = dataPointList.get(i).getY() + h / 24 * (55 * f0 - 59 * f1 + 37 * f2 - 9 * f3);
            double yNext = yPredictor;
            int iteration = 0;
            while (true) {
                double yCorrector = dataPointList.get(i).getY() + h / 24 * (9 * function.apply(xList.get(i+1), yNext) + 19 * f0 - 5 * f1 + f2);
                if (Math.abs(yNext - yCorrector) < EPSILON || iteration >= MAX_ITERATIONS) {
                    yNext = yCorrector;
                    break;
                }
                yNext = yCorrector;
                iteration++;
            }
            if (Double.isNaN(yNext) || Double.isInfinite(yNext)) {
                throw new IncorrectInputException("Не удалось применить метод на данном интервале, попробуйте выбрать другой");
            }
            dataPointList.add(new DataPoint(xList.get(i+1), yNext));
        }
        return dataPointList;
    }
}
