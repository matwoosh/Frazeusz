package pl.edu.agh.ki.frazeusz.model.nlp;

import java.util.List;

/**
 * Created by matwoosh on 16/01/2017.
 */
public interface INLProcessor {

    List<String> getSynonyms(String word);

    List<String> getForms(String word);

}
