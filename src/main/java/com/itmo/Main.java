package com.itmo;

import com.itmo.algo.*;
import com.itmo.exceptions.IncorrectInputException;
import com.itmo.gui.MainGUI;
import com.itmo.model.FunctionDTO;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        List<FunctionDTO> functions = new ArrayList<>();
        functions.add(new FunctionDTO(
                "y + (1 + x) * y^2",
                (x, y) -> y + (1 + x) * Math.pow(y, 2),
                (x, x0, y0) -> -Math.exp(x) / (x*Math.exp(x)+Math.exp(x0)*(-1/y0-x0)))
        );
//        functions.add(new FunctionDTO(
//                "2x - y",
//                (x, y) -> 2 * x + y,
//                x -> 3 * Math.exp(-x) + 2 * x - 2)
//        );
//        functions.add(new FunctionDTO(
//                "y / (1 + x^2)",
//                (x, y) -> y / (1 + Math.pow(x, 2)),
//                x -> Math.exp(Math.atan(x)))
//        );
//
//        functions.add(new FunctionDTO(
//                "3x^2",
//                (x, y) -> 3*Math.pow(x,2),
//                x -> Math.pow(x,3))
//        );

        List<AbstractODESolver> odeSolvers = new ArrayList<>();
        odeSolvers.add(new ImprovedEulerMethod());
        odeSolvers.add(new RungeKutta4Solver());
        odeSolvers.add(new AdamsSolver());

        SwingUtilities.invokeLater(() -> {
            MainGUI gui = new MainGUI(odeSolvers, functions);
            gui.setVisible(true);
        });
    }
}
