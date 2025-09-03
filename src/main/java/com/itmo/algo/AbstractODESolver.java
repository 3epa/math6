package com.itmo.algo;

import com.itmo.exceptions.IncorrectInputException;
import com.itmo.model.DataPoint;
import com.itmo.model.FunctionDTO;

import java.util.List;


public abstract class AbstractODESolver implements ODESolver {

    protected abstract List<DataPoint> computeSolution(FunctionDTO functionDTO, double y0, double start, double end, double h, double epsilon) throws IncorrectInputException;

    protected abstract boolean checkEndRequirements(List<DataPoint> dataPointList1, List<DataPoint> dataPointList2, double epsilon);

    @Override
    public List<DataPoint> solve(FunctionDTO functionDTO, double y0, double start, double end, double h, double epsilon) throws IncorrectInputException {
        validateParameters(start, end, h, epsilon);

        return computeSolution(functionDTO, y0, start, end, h, epsilon);
    }


    protected void validateParameters(double start, double end, double h, double epsilon) throws IncorrectInputException {
        if (h <= 0) throw new IncorrectInputException("Шаг h должен быть положительным");

        if (epsilon <= 0) throw new IncorrectInputException("Погрешность epsilon должна быть положительной");
        if (start >= end) throw new IncorrectInputException("Конец рассматриваемого отрезка должен быть больше начала");
    }
}
