package com.itmo.algo;

import com.itmo.exceptions.IncorrectInputException;
import com.itmo.model.DataPoint;
import com.itmo.model.FunctionDTO;
import com.itmo.utils.MathUtils;

import java.util.List;
import java.util.function.BiFunction;


public abstract class OneStepODESolver extends AbstractODESolver {

    private final int order;

    protected OneStepODESolver(String name, int order) {
        super(name);
        this.order = order;
    }

    protected abstract List<DataPoint> compute(BiFunction<Double, Double, Double> function, List<Double> xList, double y0);

    @Override
    protected List<DataPoint> computeSolution(FunctionDTO functionDTO, double y0, double start, double end, double h, double epsilon) throws IncorrectInputException {
        BiFunction<Double, Double, Double> function = functionDTO.getFunction();

        List<DataPoint> dataPointList1;
        List<DataPoint> dataPointList2;
        do {
            List<Double> xList1 = MathUtils.createGrid(start, end, h);
            h /= 2;
            List<Double> xList2 = MathUtils.createGrid(start, end, h);
            dataPointList1 = compute(function, xList1, y0);
            dataPointList2 = compute(function, xList2, y0);
        } while (!checkEndRequirements(dataPointList1, dataPointList2, epsilon));
        return dataPointList1;
    }

    @Override
    protected boolean checkEndRequirements(List<DataPoint> dataPointList1, List<DataPoint> dataPointList2, double epsilon) {
        for (int i = 0; i < dataPointList1.size(); i++) {
            if (Math.abs(dataPointList1.get(i).getY() - dataPointList2.get(2 * i).getY()) / (Math.pow(2, order) - 1) > epsilon) {
                return false;
            }
        }
        return true;
    }
}

