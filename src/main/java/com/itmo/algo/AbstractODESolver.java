package com.itmo.algo;

import com.itmo.exceptions.IncorrectInputException;
import com.itmo.model.DataPoint;
import com.itmo.model.FunctionDTO;

import java.util.List;


public abstract class AbstractODESolver implements ODESolver {
    protected final String name;

    public AbstractODESolver(String name) {
        this.name = name;
    }

    protected abstract List<DataPoint> computeSolution(FunctionDTO functionDTO, double y0, double start, double end, double h, double epsilon) throws IncorrectInputException;

    protected abstract boolean checkEndRequirements(List<DataPoint> dataPointList1, List<DataPoint> dataPointList2, double epsilon);

    @Override
    public List<DataPoint> solve(FunctionDTO functionDTO, double y0, double start, double end, double h, double epsilon) throws IncorrectInputException {
        return computeSolution(functionDTO, y0, start, end, h, epsilon);
    }

    public String getName() {
        return name;
    }
}
