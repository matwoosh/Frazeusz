package pl.edu.agh.ki.frazeusz.parser;

import pl.edu.agh.ki.frazeusz.parser.helpers.UrlContent;

/**
 *
 * @author Micha³ Zgliñski
 */
public interface ITargetedParser {
	public boolean parse(UrlContent url);
}
