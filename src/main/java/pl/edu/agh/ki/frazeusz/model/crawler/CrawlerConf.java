package pl.edu.agh.ki.frazeusz.model.crawler;

import java.util.ArrayList;

/**
 * Created by Wojtek on 2017-01-14.
 */
public class CrawlerConf {
    private ArrayList<String> urlsToCrawl;
    private int nrOfChosenThreads;
    private int nrOfChosenDepth;

    public CrawlerConf(ArrayList<String> urlsToCrawl, int nrOfChosenThreads, int nrOfChosenDepth) {
        this.urlsToCrawl = urlsToCrawl;
        this.nrOfChosenThreads = nrOfChosenThreads;
        this.nrOfChosenDepth = nrOfChosenDepth;
    }

    public ArrayList<String> getUrlsToCrawl() {
        return urlsToCrawl;
    }

    public int getNrOfChosenThreads() {
        return nrOfChosenThreads;
    }

    public int getNrOfChosenDepth() {
        return nrOfChosenDepth;
    }
}
