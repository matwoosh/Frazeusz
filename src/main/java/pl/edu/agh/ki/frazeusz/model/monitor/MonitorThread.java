package pl.edu.agh.ki.frazeusz.model.monitor;

import pl.edu.agh.ki.frazeusz.gui.MonitorPanel;
import pl.edu.agh.ki.frazeusz.model.monitor.metrics.CrawlerSummary;
import pl.edu.agh.ki.frazeusz.model.monitor.metrics.MetricsBucket;
import pl.edu.agh.ki.frazeusz.model.monitor.metrics.MetricsProvider;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;


public class MonitorThread extends Thread {

    private final MonitorPanel panel;
    private final MetricsProvider provider;

    public MonitorThread(MonitorPanel panel, MetricsBucket bucket) {
        this.panel = panel;
        this.provider = new MetricsProvider(bucket);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(100);
                refreshPanel();
            } catch (InvocationTargetException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void refreshPanel() throws InvocationTargetException, InterruptedException {
        final CrawlerSummary summary = provider.getSummary(panel.getTimeFrame());

        EventQueue.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                panel.update(summary);
            }
        });
    }
}
