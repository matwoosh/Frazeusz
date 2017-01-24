package pl.edu.agh.ki.frazeusz.model.crawler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mateusz on 14/12/2016.
 */
public class Url<T> {
    private Url<T> parent = null;
    private final List<Url<T>> children = new ArrayList<>();
    private final String absoluteUrl;

    public Url(String absoluteUrl) {
        this.absoluteUrl = absoluteUrl;
    }

    public void setParent(Url<T> parent) {
        parent.addChild(this);
        this.parent = parent;
    }

    public void removeParent() {
        this.parent = null;
    }

    public List<Url<T>> getChildren() {
        return children;
    }

    public void addChild(Url<T> child) {
        child.setParent(this);
        this.children.add(child);
    }

    public void addChildren(List<Url<T>> children) {
        this.children.addAll(children);
    }

    public String getAbsoluteUrl() {
        return absoluteUrl;
    }

    public boolean isRoot() {
        return (this.parent == null);
    }

    public boolean isLeaf() {
        if (this.children.size() == 0)
            return true;
        else
            return false;
    }

}
