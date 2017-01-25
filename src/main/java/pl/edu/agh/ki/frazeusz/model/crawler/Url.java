package pl.edu.agh.ki.frazeusz.model.crawler;

/**
 * Created by Mateusz on 14/12/2016.
 */
public class Url {

    private final String absoluteUrl;

    private final int nestingDepth;

    public Url(String absoluteUrl, int nestingDepth) {
        this.absoluteUrl = absoluteUrl;
        this.nestingDepth = nestingDepth;
    }

    public String getAbsoluteUrl() {
        return absoluteUrl;
    }

    public int getNestingDepth() {
        return nestingDepth;
    }

}
