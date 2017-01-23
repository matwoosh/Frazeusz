package pl.edu.agh.ki.frazeusz.model.nlp;


import java.util.LinkedList;

/**
 * Created by kamil on 15.12.2016.
 */
interface WordProcessor {
    LinkedList<String> processWord(String word);
}
