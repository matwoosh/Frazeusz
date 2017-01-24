package pl.edu.agh.ki.frazeusz.model.monitor;

import pl.edu.agh.ki.frazeusz.gui.MonitorPanel;
import pl.edu.agh.ki.frazeusz.model.monitor.metrics.MetricsBucket;

import javax.swing.*;


public class Monitor implements CrawlerStatus, MonitorGui {
    private final MonitorPanel panel = new MonitorPanel();
    private final MetricsBucket bucket = new MetricsBucket();

    public Monitor() {
        final Thread monitorThread = new MonitorThread(panel, bucket);
        monitorThread.start();
    }

    public void addProcessedPages(int pagesCount, long pagesSizeInBytes) {
        bucket.addProcessedPages(pagesCount, pagesSizeInBytes);
    }

    public void setPagesQueueSize(int queueSize) {
        bucket.setPagesQueueSize(queueSize);
    }

    public JPanel getPanel() {
        return panel;
    }
}
