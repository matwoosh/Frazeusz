package pl.edu.agh.ki.frazeusz.model.ploter;

import javax.swing.*;

/**
 * Created by matwoosh on 14/01/2017.
 */
public class Ploter {

	private ResultsChart chart;
	private ResultsList resultList;
	private JPanel chartPanel;
	private JPanel tablePanel;
	
	
	public Ploter() {
		this.chartPanel = new JPanel();
		this.tablePanel = new JPanel();
		
		this.resultList = new ResultsList();
		this.chart = new ResultsChart(new String[]{"Fraza 1", "Fraza 2", "Fraza 3"});
	
		initializePanels();
		
//		this.addResult(new Result("http://www.ifp.pl/", "Fraza 1", "Zdanie w którym znaleziono fraze"));
//      this.addResult(new Result("https://www.example.com/", "Fraza 2", "Zdanie w którym znaleziono fraze"));
	}
	
	private void initializePanels() {
		tablePanel.add(resultList.createResultTable());
		
	}
	
	public void addResult(Result result) {
        resultList.addRow(result);
        chartPanel.removeAll();
        chartPanel.add(chart.refresh(result));
        chartPanel.revalidate();
    }
	
	public JPanel getChartView() {
		return this.chartPanel;
	}

	public JPanel getListView() {
		return this.tablePanel;
	}


}
