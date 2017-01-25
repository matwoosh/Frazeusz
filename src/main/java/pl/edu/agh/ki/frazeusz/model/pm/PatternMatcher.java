package pl.edu.agh.ki.frazeusz.model.pm;

import pl.edu.agh.ki.frazeusz.model.nlp.INLProcessor;
import pl.edu.agh.ki.frazeusz.model.ploter.Ploter;
import pl.edu.agh.ki.frazeusz.model.ploter.Result;
import pl.edu.agh.ki.frazeusz.utilities.Word;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by matwoosh on 14/01/2017.
 **/
public class PatternMatcher implements IPatternMatcher{
    private INLProcessor nLProcessor;
    private Ploter ploter;
    private Pattern regex;
    private String readableRegex;

    public PatternMatcher(INLProcessor nLProcessor, Ploter ploter, List<Word> words) {
        this.nLProcessor = nLProcessor;
        this.ploter = ploter;
        this.regex = createRegex(words);

        StringBuilder readableRegexBuilder = new StringBuilder();
        for (Word word : words) {
            readableRegexBuilder.append(word.getWord()).append(" ");
        }
        this.readableRegex = readableRegexBuilder.toString();
    }

    public void processSentences(List<String> sentences, String URL) {
        for (String sentence : sentences) {
            if (isMatching(sentence)) {
                Result result = new Result(URL, this.readableRegex, sentence);
                ploter.addResult(result);
            }
        }
    }

    private boolean isMatching(String toMatch) {
        return regex.matcher(toMatch).matches();
    }


    private Pattern createRegex(List<Word> words) {
        StringBuilder regexBuilder = new StringBuilder(50);
        Iterator<Word> iter = words.iterator();

        regexBuilder.append("(?i).*"); // turn off case sensitive and match leading characters
        while (iter.hasNext()) {
            Word word = iter.next();

            regexBuilder.append("\\b(" + word.getWord()); // start group for word
            if (word.doesUseSynonyms()) {
                for (String form: nLProcessor.getSynonyms(word.getWord())) {
                    regexBuilder.append(form+"|");
                }
                }
            if (word.doesUseForms()) {
                for (String form: nLProcessor.getForms(word.getWord())) {
                    regexBuilder.append(form+"|");
                }
            }
            regexBuilder.append(").*"); // end group for word and match everything after
        }
        return Pattern.compile(regexBuilder.toString());
    }
}
