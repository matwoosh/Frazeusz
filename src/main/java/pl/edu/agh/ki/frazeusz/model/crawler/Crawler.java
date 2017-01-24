package pl.edu.agh.ki.frazeusz.model.crawler;

import pl.edu.agh.ki.frazeusz.gui.crawler.CrawlerConfiguration;
import pl.edu.agh.ki.frazeusz.model.monitor.CrawlerStatus;
import pl.edu.agh.ki.frazeusz.model.parser.IParser;

/**
 * Created by matwoosh on 14/01/2017.
 */
public class Crawler {

    private IParser parser;
    private CrawlerStatus monitor;

    public Crawler(IParser parser, CrawlerStatus monitor) {
        this.parser=parser;
        this.monitor = monitor;
    }

    public void start(CrawlerConfiguration crawlerConfiguration){
        //TODO implement
    }

    public void stop(){
        //TODO implement
    }

}
