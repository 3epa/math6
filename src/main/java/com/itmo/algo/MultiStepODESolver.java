package com.itmo.algo;

import com.itmo.exceptions.IncorrectInputException;
import com.itmo.model.DataPoint;
import com.itmo.model.FunctionDTO;
import com.itmo.utils.MathUtils;
import com.itmo.utils.TriFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class MultiStepODESolver extends AbstractODESolver {
    public MultiStepODESolver(String name) {
        super(name);
    }

    protected abstract List<DataPoint> compute(BiFunction<Double, Double, Double> function, List<Double> xList, double y0) throws IncorrectInputException;

    @Override
    protected List<DataPoint> computeSolution(FunctionDTO functionDTO, double y0, double start, double end, double h, double epsilon) throws IncorrectInputException {
        BiFunction<Double, Double, Double> function = functionDTO.getFunction();
        TriFunction<Double, Double, Double, Double> exactFunction = functionDTO.getExactFunction();

        List<Double> xList = MathUtils.createGrid(start, end, h);
        List<DataPoint> dataPointList = compute(function, xList, y0);
        List<DataPoint> dataPointExactList = new ArrayList<>();
        for (Double x : xList) {
            dataPointExactList.add(new DataPoint(x, exactFunction.apply(x, xList.getFirst(), y0)));
        }
        if (checkEndRequirements(dataPointList, dataPointExactList, epsilon)) {
            return dataPointList;
        } else {
            throw new IncorrectInputException("Невозможно достичь требуемой точности, попробуйте уменьшить шаг разбиения");
        }
    }

    @Override
    protected boolean checkEndRequirements(List<DataPoint> dataPointList1, List<DataPoint> dataPointList2, double epsilon) {
        double max = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < dataPointList1.size(); i++) {
            max = Math.max(max, Math.abs(dataPointList1.get(i).getY() - dataPointList2.get(i).getY()));
        }
        return max < epsilon;
    }
}
