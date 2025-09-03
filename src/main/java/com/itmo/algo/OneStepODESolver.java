package com.itmo.algo;

import com.itmo.exceptions.IncorrectInputException;
import com.itmo.model.FunctionDTO;
import com.itmo.utils.MathUtils;

import java.util.List;
import java.util.function.BiFunction;


public abstract class OneStepODESolver extends AbstractODESolver {

    private final int order;

    protected OneStepODESolver(int order) {
        this.order = order;
    }

    protected abstract List<Double> compute(BiFunction<Double, Double, Double> function, List<Double> xList, double y0);

    @Override
    protected List<Double> computeSolution(FunctionDTO functionDTO, double y0, double start, double end, double h, double epsilon) throws IncorrectInputException {
        BiFunction<Double, Double, Double> function = functionDTO.getFunction();

        List<Double> yList1;
        List<Double> yList2;
        do {
            List<Double> xList1 = MathUtils.createGrid(start, end, h);
            h /= 2;
            List<Double> xList2 = MathUtils.createGrid(start, end, h);
            yList1 = compute(function, xList1, y0);
            yList2 = compute(function, xList2, y0);
        } while (!checkEndRequirements(yList1, yList2, epsilon));
        return yList1;
    }

    @Override
    protected boolean checkEndRequirements(List<Double> yList1, List<Double> yList2, double epsilon) {
        for (int i = 0; i < yList1.size(); i++) {
            if (Math.abs(yList1.get(i) - yList2.get(2 * i)) / (Math.pow(2, order) - 1) > epsilon) {
                return false;
            }
        }
        return true;
    }
}

