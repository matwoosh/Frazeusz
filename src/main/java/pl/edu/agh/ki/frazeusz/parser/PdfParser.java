package pl.edu.agh.ki.frazeusz.parser;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import pl.edu.agh.ki.frazeusz.parser.helpers.UrlContent;

/**
 *
 * @author Micha³ Zgliñski
 */
public class PdfParser extends AbstractParser {
	
	protected List<String> validMimeTypes = Arrays.asList(
		"application/pdf"
	);
	
	@Override
	void parseInternal(UrlContent url) {
		try {
			PDDocument doc = PDDocument.load(new ByteArrayInputStream(url.content.getBytes(StandardCharsets.UTF_8)));
			url.content = new PDFTextStripper().getText(doc);
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
