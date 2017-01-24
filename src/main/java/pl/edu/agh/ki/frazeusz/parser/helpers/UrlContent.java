/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.agh.ki.frazeusz.parser.helpers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Micha³ Zgliñski
 */
public class UrlContent {
	public String url;
	public String content;
	public String mimeType;
	public List<String> sentences;
	public Set<String> urls;
	public String text;
	
	
	public UrlContent(String url, String content, String mimeType) {
		this.url = url;
		this.content = content;
		this.mimeType = mimeType;
		this.sentences = new ArrayList();
		this.urls = new HashSet<String>();
	}
}
