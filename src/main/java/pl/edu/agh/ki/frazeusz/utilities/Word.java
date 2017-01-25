package pl.edu.agh.ki.frazeusz.utilities;

/**
 * Created by matwoosh on 16/01/2017.
 */
public class Word {

    private final String word;
    private final boolean useSynonyms;
    private final boolean useForms;

    public Word(String word, boolean useSynonyms, boolean useForms) {
        this.word = word;
        this.useSynonyms = useSynonyms;
        this.useForms = useForms;
    }

    public String getWord() {
        return word;
    }

    public boolean doesUseSynonyms() {
        return useSynonyms;
    }

    public boolean doesUseForms() {
        return useForms;
    }


}
