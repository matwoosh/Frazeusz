package pl.edu.agh.ki.frazeusz.parser;

import java.util.Arrays;
import java.util.List;
import pl.edu.agh.ki.frazeusz.parser.helpers.UrlContent;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Micha³ Zgliñski
 */
public class HtmlParser extends AbstractParser {

	protected List<String> validMimeTypes = Arrays.asList(
		"text/html", 
		"text/webviewhtml", 
		"text/x-server-parsed-html"
	);

	public HtmlParser(ITargetedParser nextParser) {
		super(nextParser);
	}
	
	@Override
	void parseInternal(UrlContent url) {
		Document doc = Jsoup.parse(url.content);
		
		url.text = doc.text();
		
		Elements links = doc.select("a[href]");
		for (Element link : links) {
			url.urls.add(link.attr("abs:href"));
		}
	}
}
