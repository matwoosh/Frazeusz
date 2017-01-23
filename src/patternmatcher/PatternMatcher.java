package patternmatcher;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by bartoszszafran on 12/01/2017.
 */
public class PatternMatcher {
    private Pattern regex;
    private String readableRegex;

    void processSentences(List<String> sentences, String URL) {
        for (String sentence : sentences) {
            if (isMatching(sentence)) {
                Sentence result = new Sentence(sentence, URL, readableRegex);
            }
        }
    }

    PatternMatcher(List<Word> words) {
        this.regex = createRegex(words);

        StringBuilder readableRegexBuilder = new StringBuilder();
        for (Word word : words) {
            readableRegexBuilder.append(word.word).append(" ");
        }
        this.readableRegex = readableRegexBuilder.toString();
    }

    boolean isMatching(String toMatch) {
        return regex.matcher(toMatch).matches();
    }


    private Pattern createRegex(List<Word> words) {
        StringBuilder regexBuilder = new StringBuilder(50);
        Iterator<Word> iter = words.iterator();

        regexBuilder.append("(?i).*"); // turn off case sensitive and match leading characters
        while (iter.hasNext()) {
            Word word = iter.next();

            regexBuilder.append("(" + word.word); // start group for word
            if (word.synonyms) {
                if (word.word.contentEquals("ma")) {
                    regexBuilder.append("|posiada");
                }
            }
            if (word.forms) {

            }
            regexBuilder.append(").*"); // end group for word and match everything after
        }
        return Pattern.compile(regexBuilder.toString());
    }
}
