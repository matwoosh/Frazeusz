package pl.edu.agh.ki.frazeusz.parser;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import pl.edu.agh.ki.frazeusz.parser.helpers.UrlContent;

/**
 *
 * @author Micha³ Zgliñski
 */
public class DocxParser extends AbstractParser{

	public DocxParser(ITargetedParser nextParser) {
		super(nextParser);
		this.validMimeTypes = Arrays.asList(
			"application/vnd.openxmlformats-officedocument.wordprocessingml.document"
		);
	}

	@Override
	void parseInternal(UrlContent url) {
		try {
			XWPFDocument docx = new XWPFDocument(new ByteArrayInputStream(url.content.getBytes(StandardCharsets.UTF_8)));
			XWPFWordExtractor we = new XWPFWordExtractor(docx);
			url.content = we.getText();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
