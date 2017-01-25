package pl.edu.agh.ki.frazeusz;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import java.util.LinkedList;
import java.util.List;

import pl.edu.agh.ki.frazeusz.gui.crawler.CrawlerConfiguration;
import pl.edu.agh.ki.frazeusz.gui.crawler.CrawlerGui;
import pl.edu.agh.ki.frazeusz.gui.pm.PatternMatcherGui;
import pl.edu.agh.ki.frazeusz.model.crawler.Crawler;
import pl.edu.agh.ki.frazeusz.model.monitor.Monitor;
import pl.edu.agh.ki.frazeusz.model.nlp.INLProcessor;
import pl.edu.agh.ki.frazeusz.model.nlp.NLProcessor;
import pl.edu.agh.ki.frazeusz.model.nlp.WordDatabase;
import pl.edu.agh.ki.frazeusz.model.parser.Parser;
import pl.edu.agh.ki.frazeusz.model.ploter.Ploter;
import pl.edu.agh.ki.frazeusz.model.pm.PatternMatcher;

/**
 * Created by matwoosh on 14/01/2017.
 */
public class Main implements ActionListener {
	
	private JTabbedPane patternMatcherTabs;
	private CrawlerGui crawlerGui;
	private JPanel main_panel;
	private JFrame main_frame;
	private int phraseNumber = 1;
	
	
	public Main() {
		main_frame = new JFrame("Frazeusz");
		main_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.main_panel = new JPanel();
		this.main_panel.setVisible(true);
		main_frame.getContentPane().add(this.main_panel, BorderLayout.CENTER);
		
		this.main_panel.setLayout(new BoxLayout(this.main_panel, BoxLayout.PAGE_AXIS));
		this.main_panel.setPreferredSize(new Dimension(900, 650));
		
		this.patternMatcherTabs = new JTabbedPane();
		this.crawlerGui = new CrawlerGui();
		
		main_frame.pack();
		main_frame.setVisible(true);
	}
	
	private void addPhraseTab() {
		this.patternMatcherTabs.addTab("phrase " + phraseNumber++, new PatternMatcherGui());
	}
	
	private void createAndShowGUI() {
        //Create and set up the window.
		addPhraseTab();
        
        JButton startButton = new JButton("Start");
        startButton.setEnabled(true);
        startButton.addActionListener(this);
        startButton.setActionCommand("start");
        
        JButton addPhraseButton = new JButton("Add phrase");
        addPhraseButton.setEnabled(true);
        addPhraseButton.addActionListener(this);
        addPhraseButton.setActionCommand("phrase");
        
        addPhraseButton.setAlignmentX( Component.CENTER_ALIGNMENT );
        startButton.setAlignmentX( Component.CENTER_ALIGNMENT );
        
        main_panel.add(this.patternMatcherTabs);
        main_panel.add(addPhraseButton);
        main_panel.add(this.crawlerGui);
        main_panel.add(startButton);
	}
	
	public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Main main = new Main();
                main.createAndShowGUI();
            }
        });
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if("start".equals(e.getActionCommand())) {
			main_frame.getContentPane().removeAll();
			
			
			// initialize all modules
			INLProcessor nLProcessor = new NLProcessor();
			WordDatabase.startLoading();
			
			Ploter ploter = new Ploter();
			Monitor monitor = new Monitor();
			
			List<PatternMatcher> pmList = new LinkedList<PatternMatcher>();
		
			for(Component c : this.patternMatcherTabs.getComponents()) {
				PatternMatcherGui pmGUI = (PatternMatcherGui) c;
				pmList.add(new PatternMatcher(nLProcessor, ploter, pmGUI.getConfiguration()));
			}
			
			CrawlerConfiguration crawlerConfiguration = this.crawlerGui.getConfiguration();
			
			
			
			Parser parser = new Parser(pmList);
			Crawler crawler = new Crawler(parser, monitor);
			crawler.start(crawlerConfiguration);
			
			
			// create new view
			JTabbedPane tabbed_pane = new JTabbedPane();
			tabbed_pane.addTab("List", ploter.getListView());
			tabbed_pane.addTab("Chart", ploter.getChartView());
			tabbed_pane.addTab("Monitor", monitor.getPanel());
			
			
			main_frame.getContentPane().add(tabbed_pane);
			main_frame.revalidate();
			main_frame.repaint();
		}
		
		else if("phrase".equals(e.getActionCommand())) {
			addPhraseTab();
		}
	}
}
