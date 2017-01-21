/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.agh.ki.frazeusz.parser;

import java.util.Arrays;
import java.util.List;
import pl.edu.agh.ki.frazeusz.parser.helpers.UrlContent;

/**
 *
 * @author Micha³ Zgliñski
 */
public class TxtParser  extends AbstractParser {

	protected List<String> validMimeTypes = Arrays.asList(
		"text/plain"
	);

	@Override
	void parseInternal(UrlContent url) {
		url.text = url.content;
	}
}

