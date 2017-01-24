package pl.edu.agh.ki.frazeusz.model.nlp;

/**
 * Created by kamil on 23.01.2017.
 */

import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.*;

public class NLPTest {

    @Test
    public void formsWorking() {
        WordDatabase.startLoading();

        NLProcessor proc = new NLProcessor();

        LinkedList<String> infl = proc.getForms("acylacja");
        assertTrue(infl.contains("acylacjom"));

        infl = proc.getForms("adwent");
        assertTrue(infl.contains("adwentowi"));

        infl = proc.getForms("acylacja");
        assertTrue(infl.contains("acylacjom"));

        infl = proc.getForms("aerodynamicznej");
        assertTrue(infl.contains("aerodynamicznie"));

        infl = proc.getForms("aficydowi");
        assertTrue(infl.contains("aficydzie"));

    }

    @Test
    public void synonymsWorking() {
        WordDatabase.startLoading();

        NLProcessor proc = new NLProcessor();

        LinkedList<String> syns = proc.getSynonyms("glob");
        assertTrue(syns.contains("geoida"));

        syns = proc.getSynonyms("glejt");
        assertTrue(syns.contains("przepustka"));

        syns = proc.getSynonyms("gniewać");
        assertTrue(syns.contains("złościć"));

        syns = proc.getSynonyms("heretyk");
        assertTrue(syns.contains("innowierca"));

        syns = proc.getSynonyms("facet");
        assertTrue(syns.contains("ziomek"));


    }

    @Test
    public void emptyInput() {
        WordDatabase.startLoading();

        NLProcessor proc = new NLProcessor();
        LinkedList<String> syns = proc.getSynonyms("");
        assertTrue(syns.isEmpty());

        LinkedList<String> infl = proc.getForms("");
        assertTrue(infl.isEmpty());
    }

    @Test
    public void notAWord() {
        WordDatabase.startLoading();

        NLProcessor proc = new NLProcessor();
        LinkedList<String> syns = proc.getSynonyms("asdasfa");
        assertTrue(syns.isEmpty());

        LinkedList<String> infl = proc.getForms("fagsgsgsg");
        assertTrue(infl.isEmpty());
    }
}
