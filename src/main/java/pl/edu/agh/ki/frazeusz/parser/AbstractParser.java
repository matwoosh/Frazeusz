package pl.edu.agh.ki.frazeusz.parser;

import java.util.List;
import pl.edu.agh.ki.frazeusz.parser.helpers.UrlContent;

/**
 *
 * @author Micha³ Zgliñski
 */
abstract public class AbstractParser implements ITargetedParser {

	protected ITargetedParser nextParser = null;
	protected List<String> validMimeTypes = null;

	abstract void parseInternal(UrlContent url);
	
	public AbstractParser(ITargetedParser nextParser) {
		this.nextParser = nextParser;
	}

	@Override
	public boolean parse(UrlContent url) {
		if(this.isValidMimeType(url.mimeType)) {
			this.parseInternal(url);
			return true;
		}
		
		if(this.nextParser != null)
			return this.nextParser.parse(url);
		
		return false;
	}	
	
	protected boolean isValidMimeType(String mimeType) {
		return validMimeTypes.contains(mimeType);
	}
}
