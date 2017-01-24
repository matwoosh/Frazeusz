package pl.edu.agh.ki.frazeusz.utilities;

/**
 * Created by matwoosh on 16/01/2017.
 */
public class Word {

    private final String word;
    private final boolean useSynonyms;
    private final boolean useForms;

    public static Word.Builder builder(){
        return new Word.Builder();
    }

    public Word(Word.Builder builder) {
        this.word = builder.word;
        this.useSynonyms = builder.useSynonyms;
        this.useForms = builder.useForms;
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

    public static class Builder {

        private String word;
        private boolean useSynonyms;
        private boolean useForms;

        public Builder setWord(String word) {
            this.word = word;
            return this;
        }

        public Builder withSynonyms(boolean useSynonyms) {
            this.useSynonyms = useSynonyms;
            return this;
        }

        public Builder withForms(boolean useForms) {
            this.useForms = useForms;
            return this;
        }

        public Word build(){
            return new Word(this);
        }

    }

}
