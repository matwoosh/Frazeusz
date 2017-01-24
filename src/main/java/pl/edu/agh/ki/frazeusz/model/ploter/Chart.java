package pl.edu.agh.ki.frazeusz.model.ploter;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import java.util.HashMap;
import java.util.Map;

public class Chart {

    private ChartPanel chart;
    private Map<String, Integer> data = new HashMap<>();

    public Chart(String[] phrases) {
        for (String phrase: phrases) {
            data.put(phrase, 0);
        }
        this.createChart();
    }

    private void createChart() {
        JFreeChart barChart = ChartFactory.createBarChart(
                "Ilość wystąpnień poszczególnych fraz",
                "Fraza",
                "Ilośc",
                createDataset(),
                PlotOrientation.VERTICAL,
                false, true, false);

        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(650, 400));
        this.chart = chartPanel;
    }

    public ChartPanel getChart() {
        return this.chart;
    }

    public ChartPanel refresh(Result result) {
        String matchedPhrase = result.matchedPhrase;

        Integer value = data.get(matchedPhrase);
        value++;

        data.put(matchedPhrase, value);

        createChart();

        return chart;
    }

    private CategoryDataset createDataset() {

        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for(Map.Entry<String, Integer> phrase : data.entrySet()) {
            String key = phrase.getKey();
            Integer value = phrase.getValue();
            dataset.addValue(value, key, key);
        }

        return dataset;
    }
}