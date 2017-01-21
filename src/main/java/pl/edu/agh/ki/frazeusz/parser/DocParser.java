package pl.edu.agh.ki.frazeusz.parser;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import pl.edu.agh.ki.frazeusz.parser.helpers.UrlContent;

/**
 *
 * @author Micha³ Zgliñski
 */
public class DocParser extends AbstractParser {

	protected List<String> validMimeTypes = Arrays.asList(
			"application/msword"
	);

	@Override
	void parseInternal(UrlContent url) {
		try {
			HWPFDocument wordDoc = new HWPFDocument(new ByteArrayInputStream(url.content.getBytes(StandardCharsets.UTF_8)));
			WordExtractor wordExtractor = new WordExtractor(wordDoc);
			url.content = wordExtractor.getText();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
