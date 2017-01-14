package pl.edu.agh.ki.frazeusz.parser;

import pl.edu.agh.ki.frazeusz.pm.PatternMatcher;

import java.util.List;

/**
 * Created by matwoosh on 14/01/2017.
 */
public class Parser {

    private final List<PatternMatcher> patternMatchers;

    public Parser(List<PatternMatcher> patternMatchers) {
        this.patternMatchers = patternMatchers;
    }

    public List<String> parseContent(String httpHeader, String content, String baseAbsoluteUrl) throws Exception {
        //TODO implement method
        return null;
    }

}
