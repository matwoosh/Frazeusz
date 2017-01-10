package patternmatcher;

public class Word {
    String word;
    boolean synonyms;
    boolean forms;

    public Word(String word, boolean synonyms, boolean forms) {
        this.word = word;
        this.synonyms = synonyms;
        this.forms = forms;
    }
}
