package com.itmo.gui;

import com.itmo.algo.AbstractODESolver;
import com.itmo.exceptions.IncorrectInputException;
import com.itmo.model.DataPoint;
import com.itmo.model.FunctionDTO;
import com.itmo.utils.MathUtils;
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
        double y0;
        double start;
        double end;
        double step;
        double epsilon;
        try {
            y0 = Double.parseDouble(y0Field.getText().replace(",", "."));
            start = Double.parseDouble(startIntervalField.getText().replace(",", "."));
            end = Double.parseDouble(endIntervalField.getText().replace(",", "."));
            step = Double.parseDouble(stepField.getText().replace(",", "."));
            epsilon = Double.parseDouble(epsilonField.getText().replace(",", "."));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Неверный формат чисел", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }
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

        updateOutput(functionDTO, y0, start, end, step, epsilon);
        updateChart(functionDTO, y0, start, end, step, epsilon);
    }

    private void updateOutput(FunctionDTO functionDTO, double y0, double start, double end, double step, double epsilon) {
        StringBuilder output = new StringBuilder();
        for (AbstractODESolver odeSolver : odeSolvers) {
            output.append(String.format("%s:\n", odeSolver.getName()));
            try {
                List<DataPoint> dataPointList = odeSolver.solve(functionDTO, y0, start, end, step, epsilon);
                output.append("Метод сошелся за ").append(dataPointList.size()).append(" шагов.\n");
                output.append(String.format("%-20s %-20s\n", "X", "Y"));
                for (DataPoint dataPoint : dataPointList) {
                    output.append(String.format("%-20f %-20f\n", dataPoint.getX(), dataPoint.getY()));
                }
            } catch (IncorrectInputException e) {
                output.append("Статус: ОШИБКА\n");
                output.append(e.getMessage()).append("\n");
            }
            output.append("----------------------------------------\n");
        }

        outputArea.setText(output.toString());
        outputArea.setCaretPosition(0);
    }

    private void updateChart(FunctionDTO functionDTO, double y0, double start, double end, double step, double epsilon) {
        try {
            XYSeriesCollection dataset = new XYSeriesCollection();

            XYSeries exactSeries = new XYSeries("Точное решение");
            List<Double> xList = MathUtils.createGrid(start, end, step);
            for (Double x : xList) {
                Double yExact = functionDTO.getExactFunction().apply(x, start, y0);
                exactSeries.add(x, yExact);
            }
            dataset.addSeries(exactSeries);

            for (AbstractODESolver odeSolver : odeSolvers) {
                try {
                    List<DataPoint> dataPointList = odeSolver.solve(functionDTO, y0, start, end, step, epsilon);
                    XYSeries series = new XYSeries(odeSolver.getName());

                    for (DataPoint dataPoint : dataPointList) {
                        series.add(dataPoint.getX(), dataPoint.getY());
                    }

                    dataset.addSeries(series);
                } catch (Exception ignored) {
                }
            }

            String selectedFunction = (String) functionComboBox.getSelectedItem();
            JFreeChart chart = ChartFactory.createXYLineChart(
                    "Решение ОДУ: " + "y' = " + selectedFunction,
                    "X",
                    "Y",
                    dataset,
                    PlotOrientation.VERTICAL,
                    true,
                    true,
                    false
            );

            XYPlot plot = chart.getXYPlot();
            XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

            renderer.setSeriesPaint(0, Color.BLACK);
            renderer.setSeriesPaint(1, Color.RED);
            renderer.setSeriesPaint(2, Color.BLUE);
            renderer.setSeriesPaint(3, Color.GREEN);

            renderer.setSeriesStroke(0, new BasicStroke(
                    2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                    1.0f, new float[]{6.0f, 6.0f}, 0.0f
            ));

            for (int i = 1; i < dataset.getSeriesCount(); i++) {
                renderer.setSeriesStroke(i, new BasicStroke((float) 2.0 + i * 5));
            }

            plot.setRenderer(renderer);

            chartPanel.setChart(chart);
            chartPanel.repaint();

        } catch (Exception ignored) {
        }
    }
}
