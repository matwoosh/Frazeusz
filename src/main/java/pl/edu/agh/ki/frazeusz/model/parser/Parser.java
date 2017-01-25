package pl.edu.agh.ki.frazeusz.model.parser;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Collections;
import pl.edu.agh.ki.frazeusz.model.pm.PatternMatcher;

import java.util.List;
import java.util.Locale;
import pl.edu.agh.ki.frazeusz.parser.DocParser;
import pl.edu.agh.ki.frazeusz.parser.DocxParser;
import pl.edu.agh.ki.frazeusz.parser.HtmlParser;
import pl.edu.agh.ki.frazeusz.parser.ITargetedParser;
import pl.edu.agh.ki.frazeusz.parser.PdfParser;
import pl.edu.agh.ki.frazeusz.parser.TxtParser;
import pl.edu.agh.ki.frazeusz.parser.helpers.UrlContent;

/**
 * Created by matwoosh on 14/01/2017.
 */
public class Parser implements IParser {

    private final List<PatternMatcher> patternMatchers;

	private ITargetedParser firstParser = null;
	
    public Parser(List<PatternMatcher> patternMatchers) {
        this.patternMatchers = patternMatchers;
		this.firstParser = this.initParsers();
    }

    public synchronized List<String> parseContent(String httpHeader, String content, String baseAbsoluteUrl) throws Exception { 
        boolean debugCrawler = true;
    	if (debugCrawler) {
    		final ArrayList<String> urls = new ArrayList<>();
    		urls.add("http://wiadomosci.onet.pl/kraj/wypadek-auta-antoniego-macierewicza-kolo-torunia/4c0bf7s");
    		urls.add("http://eurosport.onet.pl/siatkowka/orlen-liga/orlen-liga-jacek-skrok-stracil-prace-w-developresie-skyres-rzeszow/dvpezx");
        	return urls;
		} else {
			UrlContent urlContent = new UrlContent(baseAbsoluteUrl, content, httpHeader);

			if (firstParser.parse(urlContent)) {

				urlContent.sentences = this.splitIntoSenteces(urlContent.text);

				for (PatternMatcher patternMatcher : this.patternMatchers)
					patternMatcher.processSentences(urlContent.sentences, urlContent.url);

				return new ArrayList<>(urlContent.urls);
			}

			return Collections.emptyList();
		}
    }

	protected ITargetedParser initParsers() {
		
		ITargetedParser currentParser = null;
		
		currentParser = new TxtParser(currentParser);
		currentParser = new DocParser(currentParser);
		currentParser = new DocxParser(currentParser);
		currentParser = new PdfParser(currentParser);
		currentParser = new HtmlParser(currentParser);
		
		return currentParser;
	}	
	
	protected List<String> splitIntoSenteces(String source) {
		BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.US);
		iterator.setText(source);
		List<String> sentences = new ArrayList();
		int start = iterator.first();
		for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator.next()) {
			sentences.add(source.substring(start, end));
		}
		return sentences;
	}
}
