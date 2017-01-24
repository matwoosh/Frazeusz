package pl.edu.agh.ki.frazeusz.model.nlp;


import java.util.LinkedList;

/**
 * Created by kamil on 15.12.2016.
 */
public class NLProcessor {
    private WordProcessor inflectionFinder, synonymFinder;

    public NLProcessor() {
        inflectionFinder = new InflectionFinder();
        synonymFinder = new SynonymFinder();
    }

    public LinkedList<String> getForms(String word) {
        LinkedList<String> words = null;

        words = inflectionFinder.processWord(word);

        return words;
    }

    public LinkedList<String> getSynonyms(String word) {
        LinkedList<String> words = null;

        words = synonymFinder.processWord(word);

        return words;
    }

}
