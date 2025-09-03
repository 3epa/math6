package com.itmo.gui;

import com.itmo.algo.AbstractODESolver;
import com.itmo.algo.ODESolver;
import com.itmo.exceptions.IncorrectInputException;
import com.itmo.model.DataPoint;
import com.itmo.model.FunctionDTO;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class MainGUI extends JFrame {

    private JTextArea outputArea;
    private ChartPanel chartPanel;

    private JComboBox<String> functionComboBox;
    private JTextField y0Field;
    private JTextField startIntervalField;
    private JTextField endIntervalField;
    private JTextField stepField;
    private JTextField epsilonField;

    private final List<AbstractODESolver> odeSolvers;
    private final List<FunctionDTO> functions;

    public MainGUI(List<AbstractODESolver> odeSolvers, List<FunctionDTO> functions) {
        this.odeSolvers = odeSolvers;
        this.functions = functions;

        setTitle("Вычмат ЛР6");
        setSize(1400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        createInputPanel();
        createOutputPanel();
        createChartPanel();

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Результат", new JScrollPane(outputArea));
        tabbedPane.addTab("График", chartPanel);

        add(tabbedPane, BorderLayout.CENTER);
    }

    private void createInputPanel() {
        JPanel inputPanel = new JPanel(new BorderLayout());

        JPanel functionPanel = createFunctionPanel();
        JPanel processPanel = createProcessPanel();

        inputPanel.add(functionPanel, BorderLayout.NORTH);
        inputPanel.add(processPanel, BorderLayout.SOUTH);

        add(inputPanel, BorderLayout.NORTH);
    }

    private void createOutputPanel() {
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
    }

    private void createChartPanel() {
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Решение ОДУ",
                "X",
                "Y",
                null,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
    }

    private JPanel createFunctionPanel() {
        JPanel functionPanel = new JPanel(new BorderLayout());
        functionPanel.setBorder(BorderFactory.createTitledBorder("Инициализация стартовых значений"));

        JPanel functionChoosePanel = createFunctionChoosePanel();
        JPanel functionParametersPanel = createFunctionParametersPanel();

        functionPanel.add(functionChoosePanel, BorderLayout.NORTH);
        functionPanel.add(functionParametersPanel, BorderLayout.CENTER);
        return functionPanel;
    }

    private JPanel createFunctionChoosePanel() {
        JPanel functionChoosePanel = new JPanel(new GridLayout(1, 2, 5, 5));
        List<String> functionNames = new ArrayList<>();
        for (FunctionDTO function : functions) {
            functionNames.add(function.getName());
        }
        functionComboBox = new JComboBox<>(functionNames.toArray(new String[0]));
        functionChoosePanel.add(new JLabel("y' = "), BorderLayout.WEST);
        functionChoosePanel.add(functionComboBox, BorderLayout.EAST);

        return functionChoosePanel;
    }

    private JPanel createFunctionParametersPanel() {
        JPanel functionParametersPanel = new JPanel(new GridLayout(5, 2, 5, 5));

        functionParametersPanel.add(new JLabel("y0 = y(x0):"));
        y0Field = new JTextField("-1");
        functionParametersPanel.add(y0Field);

        functionParametersPanel.add(new JLabel("Начало интервала (x0):"));
        startIntervalField = new JTextField("1");
        functionParametersPanel.add(startIntervalField);

        functionParametersPanel.add(new JLabel("Конец интервала (xn):"));
        endIntervalField = new JTextField("1.5");
        functionParametersPanel.add(endIntervalField);

        functionParametersPanel.add(new JLabel("Шаг(h):"));
        stepField = new JTextField("0.1");
        functionParametersPanel.add(stepField);

        functionParametersPanel.add(new JLabel("Точность(ɛ):"));
        epsilonField = new JTextField("0.01");
        functionParametersPanel.add(epsilonField);

        return functionParametersPanel;
    }

    private JPanel createProcessPanel() {
        JPanel processPanel = new JPanel();

        JButton processButton = new JButton("Решить ОДУ");
        processButton.addActionListener(e -> processInput());

        processPanel.add(processButton);

        return processPanel;
    }

    private void processInput() {
        updateOutput();
        updateChart();
    }

    private void updateOutput() {
        StringBuilder output = new StringBuilder();
        for (AbstractODESolver odeSolver : odeSolvers) {
            output.append(String.format("%s:\n", odeSolver.getName()));
            try {
                double y0 = Double.parseDouble(y0Field.getText().replace(",", "."));
                double start = Double.parseDouble(startIntervalField.getText().replace(",", "."));
                double end = Double.parseDouble(endIntervalField.getText().replace(",", "."));
                double step = Double.parseDouble(stepField.getText().replace(",", "."));
                double epsilon = Double.parseDouble(epsilonField.getText().replace(",", "."));

                if (start >= end) {
                    JOptionPane.showMessageDialog(this, "Начало интервала должно быть меньше конца", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (step <= 0) {
                    JOptionPane.showMessageDialog(this, "Шаг должен быть положительным", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (epsilon <= 0) {
                    JOptionPane.showMessageDialog(this, "Точность должен быть положительным", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String selectedFunction = (String) functionComboBox.getSelectedItem();
                FunctionDTO functionDTO = null;
                for (FunctionDTO aFunctionDTO : functions) {
                    if (aFunctionDTO.getName().equals(selectedFunction)) {
                        functionDTO = aFunctionDTO;
                    }
                }

                if (functionDTO == null) {
                    JOptionPane.showMessageDialog(this, "Неизвестная функция", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                List<DataPoint> dataPointList = odeSolver.solve(functionDTO, y0, start, end, step, epsilon);
                for (DataPoint dataPoint: dataPointList) {
                    output.append(dataPoint.getX()).append(" ").append(dataPoint.getY()).append("\n");
                }
            } catch (IncorrectInputException e) {
                output.append("Статус: ОШИБКА\n");
                output.append(e.getMessage()).append("\n");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Неверный формат чисел", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
            output.append("----------------------------------------\n");
        }

        outputArea.setText(output.toString());
        outputArea.setCaretPosition(0);
    }

    private void updateChart() {

    }
}
