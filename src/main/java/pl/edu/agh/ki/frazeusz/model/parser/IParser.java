package pl.edu.agh.ki.frazeusz.model.parser;

import java.util.List;

/**
 * Created by matwoosh on 18/01/2017.
 */
public interface IParser {

    List<String> parseContent(String httpHeader, String content, String baseAbsoluteUrl) throws Exception;

}
