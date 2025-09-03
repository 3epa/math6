package com.itmo.algo;

import com.itmo.exceptions.IncorrectInputException;
import com.itmo.model.DataPoint;
import com.itmo.model.FunctionDTO;

import java.util.List;

public interface ODESolver {
    List<DataPoint> solve(FunctionDTO functionDTO, double y0, double start, double end, double h, double epsilon) throws IncorrectInputException;
}
