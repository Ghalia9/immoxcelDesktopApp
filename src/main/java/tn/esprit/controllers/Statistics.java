package tn.esprit.controllers;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import tn.esprit.models.Employees;
import tn.esprit.services.ServiceEmployees;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class Statistics implements Initializable {

    @FXML
    private PieChart pieChart;

    private final ServiceEmployees serviceEmployees = new ServiceEmployees();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Fetch employees grouped by function and count the number of employees in each group
        Map<String, Long> employeesByFunction = serviceEmployees.getAll().stream()
                .collect(Collectors.groupingBy(Employees::getEmpFunction, Collectors.counting()));

        // Convert the map entries to PieChart.Data objects
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        employeesByFunction.forEach((function, count) ->
                pieChartData.add(new PieChart.Data(function, count)));

        // Bind the name of each data point to its function name and the value to the count of employees
        pieChartData.forEach(data -> {
            String formattedCount = String.format("%.0f", data.getPieValue()); // Format the count as an integer
            data.setName(data.getName() + " count: " + formattedCount); // Set the name with the formatted integer count
        });

        // Add the data to the PieChart
        pieChart.getData().addAll(pieChartData);
    }
}
