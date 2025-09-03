package com.itmo.algo;

import com.itmo.exceptions.IncorrectInputException;
import com.itmo.model.FunctionDTO;
import com.itmo.utils.MathUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class MultiStepODESolver extends AbstractODESolver {
    protected abstract List<Double> compute(BiFunction<Double, Double, Double> function, List<Double> xList, double y0);

    @Override
    protected List<Double> computeSolution(FunctionDTO functionDTO, double y0, double start, double end, double h, double epsilon) throws IncorrectInputException {
        BiFunction<Double, Double, Double> function = functionDTO.getFunction();
        Function<Double, Double> exactFunction = functionDTO.getExactFunction();


        List<Double> xList = MathUtils.createGrid(start, end, h);
        List<Double> yList = compute(function, xList, y0);
        List<Double> yListExact = new ArrayList<>();
        for (Double x: xList) {
            yListExact.add(exactFunction.apply(x));
        }
        if (checkEndRequirements(yList, yListExact, epsilon)) {
            return yList;
        } else {
            throw new IncorrectInputException("Невозможно достичь требуемой точности, попробуйте уменьшить шаг разбиения");
        }
    }

    @Override
    protected boolean checkEndRequirements(List<Double> yList1, List<Double> yList2, double epsilon) {
        double max = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < yList1.size(); i++) {
            max = Math.max(max, Math.abs(yList1.get(i) - yList2.get(i)));
        }
        return max < epsilon;
    }
}
