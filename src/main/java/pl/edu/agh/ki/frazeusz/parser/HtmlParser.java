package pl.edu.agh.ki.frazeusz.parser;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
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

	public HtmlParser(ITargetedParser nextParser) {
		super(nextParser);
		this.validMimeTypes = Arrays.asList(
				"text/html",
				"text/webviewhtml",
				"text/x-server-parsed-html"
		);
	}

	@Override
	void parseInternal(UrlContent url) {
		Document doc = Jsoup.parse(url.content, url.url);

		url.text = doc.body().text();

		Elements links = doc.select("a[href]");
		for (Element link : links) {
			String absolute = link.attr("abs:href");
			if (this.isOneSubdomainOfTheOther(url.url, absolute)) {
				url.urls.add(absolute);
			}
		}
	}

	protected boolean isOneSubdomainOfTheOther(String a, String b) {
		String firstHost, secondHost;
		try {
			URL first = new URL(a);
			firstHost = first.getHost();
			firstHost = firstHost.startsWith("www.") ? firstHost.substring(4) : firstHost;
		} catch (MalformedURLException ex) {
			System.out.println("> Malformed url: " + a);
			return false;
		}
		try {
			URL second = new URL(b);
			secondHost = second.getHost();
			secondHost = secondHost.startsWith("www.") ? secondHost.substring(4) : secondHost;
		} catch (MalformedURLException ex) {
			System.out.println("> Malformed url: " + b);
			return false;
		}
		
		/*
             Test if one is a substring of the other
		 */
		if (firstHost.contains(secondHost) || secondHost.contains(firstHost)) {

			String[] firstPieces = firstHost.split("\\.");
			String[] secondPieces = secondHost.split("\\.");

			String[] longerHost = {""};
			String[] shorterHost = {""};

			if (firstPieces.length >= secondPieces.length) {
				longerHost = firstPieces;
				shorterHost = secondPieces;
			} else {
				longerHost = secondPieces;
				shorterHost = firstPieces;
			}
			//int longLength = longURL.length;
			int minLength = shorterHost.length;
			int i = 1;

			/*
                 Compare from the tail of both host and work backwards
			 */
			while (minLength > 0) {
				String tail1 = longerHost[longerHost.length - i];
				String tail2 = shorterHost[shorterHost.length - i];

				if (tail1.equalsIgnoreCase(tail2)) {
					//move up one place to the left
					minLength--;
				} else {
					//domains do not match
					return false;
				}
				i++;
			}
			if (minLength == 0) //shorter host exhausted. Is a sub domain
			{
				return true;
			}
		}
		return false;
	}
}
