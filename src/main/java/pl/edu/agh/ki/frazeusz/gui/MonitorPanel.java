package pl.edu.agh.ki.frazeusz.gui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.general.SeriesException;
import org.jfree.data.time.*;
import org.jfree.data.xy.XYDataset;
import pl.edu.agh.ki.frazeusz.model.monitor.TimeFrame;
import pl.edu.agh.ki.frazeusz.model.monitor.metrics.CrawlerMetrics;
import pl.edu.agh.ki.frazeusz.model.monitor.metrics.CrawlerSummary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


public class MonitorPanel extends JPanel {
    private static final int STAT_PROCESSED_PAGES = 0;
    private static final int STAT_PROCESSED_SIZE = 1;

    private TimeFrame timeFrame = TimeFrame.LAST_TEN_SECONDS;
    private int statisticsType = STAT_PROCESSED_PAGES;

    private final JFreeChart chart;
    private final TimeSeriesCollection dataSet;

    private JTextField processedPagesField;
    private JTextField processedSizeField;
    private JTextField pagesQueueField;

    public MonitorPanel() {
        dataSet = initDataset();
        chart = initChart(dataSet);
        prepareGUI();
    }

    private void prepareGUI() {
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setDomainZoomable(false);
        chartPanel.setRangeZoomable(false);

        final JPanel bottomPanel = new JPanel(new GridLayout(1, 2));
        final JComboBox<String> statisticsTypeBox = addComboBoxToPanel(bottomPanel,
                "Ilość przetworzonych stron", "Rozmiar przetworzonych stron (kB)");
        final JComboBox<String> timeFrameBox = addComboBoxToPanel(bottomPanel,
                "z ostatnich 10 s", "z ostatniej godziny", "od uruchomienia");
        registerComboBoxListeners(statisticsTypeBox, timeFrameBox);

        final JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(chartPanel, BorderLayout.CENTER);
        leftPanel.add(bottomPanel, BorderLayout.SOUTH);

        final JPanel rightPanel = new JPanel(new GridLayout(6, 1));
        processedPagesField = addStatisticsToPanel(rightPanel, "Przetworzonych stron:", "0");
        processedSizeField = addStatisticsToPanel(rightPanel, "Przetworzonych kB:", "0.0");
        pagesQueueField = addStatisticsToPanel(rightPanel, "Stron w kolejce:", "0");

        add(leftPanel);
        add(rightPanel);
    }

    private JComboBox<String> addComboBoxToPanel(JPanel panel, String... listElements) {
        final JComboBox<String> comboBox = new JComboBox<>(listElements);
        panel.add(comboBox);
        return comboBox;
    }

    private JTextField addStatisticsToPanel(JPanel panel, String labelText, String defaultValue) {
        final JLabel label = new JLabel(labelText);
        final JTextField field = new JTextField(defaultValue);
        field.setEditable(false);
        panel.add(label);
        panel.add(field);
        return field;
    }

    private void registerComboBoxListeners(final JComboBox<String> statisticsTypeBox, final JComboBox<String> timeFrameBox) {
        statisticsTypeBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                statisticsType = statisticsTypeBox.getSelectedIndex();
            }
        });

        timeFrameBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                switch (timeFrameBox.getSelectedIndex()) {
                    case 0:
                        timeFrame = TimeFrame.LAST_TEN_SECONDS;
                        break;
                    case 1:
                        timeFrame = TimeFrame.LAST_HOUR;
                        break;
                    case 2:
                        timeFrame = TimeFrame.ALL_TIME;
                        break;
                }
            }
        });
    }

    /**
     * On each update rewrite whole time series.
     * Adding to series is easy, but I don't know
     * how to remove first item from the series.
     * Unfortunately this causes flickering when updating chart
     * since grid in the background changes on each update :/
     */
    public void update(CrawlerSummary summary) {
        final List<CrawlerMetrics> metricsList = summary.getMetrics();
        int processedPagesAcc = 0;
        double processedSizeAcc = 0;

        Date first = metricsList.get(0).getTime();
        Date last = new Date();
        chart.getXYPlot().getDomainAxis().setRange(new DateRange(first, last));

        TimeSeries series = new TimeSeries("processed pages");
        for (CrawlerMetrics metrics : metricsList) {
            try {
                final RegularTimePeriod period = RegularTimePeriod.createInstance(
                        Millisecond.class,
                        metrics.getTime(),
                        TimeZone.getDefault()
                );

                processedPagesAcc += metrics.getProcessedPages();
                processedSizeAcc += metrics.getProcessedSize() / 1024.0;

                double value;
                if (statisticsType == STAT_PROCESSED_PAGES) {
                    value = processedPagesAcc;
                } else {
                    value = processedSizeAcc;
                }
                series.add(period, value);
            } catch (SeriesException e) {
                System.err.println("Error adding to series");
            }
        }

        processedPagesField.setText(String.valueOf(processedPagesAcc));
        processedSizeField.setText(String.format("%.2f", processedSizeAcc));
        pagesQueueField.setText(String.valueOf(summary.getPagesQueueSize()));
        dataSet.removeAllSeries();
        dataSet.addSeries(series);
    }

    private TimeSeriesCollection initDataset() {
        final TimeSeries series = new TimeSeries("processed pages");
        return new TimeSeriesCollection(series);
    }

    private JFreeChart initChart(XYDataset dataset) {
        final JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Crawler status",
                "time",
                "processed pages",
                dataset,
                false,
                false,
                false);
        final XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.GRAY);
        plot.setRangeGridlinePaint(Color.GRAY);
        plot.getDomainAxis().setFixedAutoRange(11 * 1000);
        return chart;
    }

    public TimeFrame getTimeFrame() {
        return timeFrame;
    }
}