package pl.edu.agh.ki.frazeusz.model.nlp;

import java.util.HashSet;
import java.util.LinkedList;

/**
 * Created by kamil on 15.12.2016.
 */

class InflectionFinder implements WordProcessor {
    public LinkedList<String> processWord(String word) {
        LinkedList<LinkedList<String>> inflections = WordDatabase.getForms();

        LinkedList<String> results = new LinkedList<String>();

        for(LinkedList<String> words : inflections){


            if(words.contains(word)){
                results.addAll(words);
            }
        }



        HashSet<String> hs = new HashSet<String>();
        hs.addAll(results);
        hs.remove(word);
        results.clear();
        results.addAll(hs);


        return results;
    }
}
