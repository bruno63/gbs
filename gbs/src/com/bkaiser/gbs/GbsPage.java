package com.bkaiser.gbs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.*;

/**
 * This class represents a single Bootstrap page.
 * It handles the page structure, meta info, stylesheets, javascripts etc.
 * and finally writes the page into a file. 
 * 
 * @author Bruno Kaiser
 */
public abstract class GbsPage extends GbsFactory  {
//	private static final String CN = "GbsPage";
	protected Element pageContent = null;   // the root html element of the page template 
	protected File htmlF = null;   // the file representation of the page
	
	
	public Element getContent()
	{
		return pageContent;
	}
	
	public abstract Element preparePage(Element nodeEl) throws JDOMException, IOException;
	
	public void savePage(File f)
	{
		new GbsXmlExport().write(pageContent, f);
	}
		
	public static Element getFooter(String text, boolean drawSeparatorLine) 
	{
		Element _footerEl = new Element("footer", ns).setAttribute("class", "row-fluid");
		Element _colEl = getDivElement("col-xs-12", null);
		_footerEl.addContent(_colEl);
		if (drawSeparatorLine == true) {
			_colEl.addContent(new Element("hr", ns));
		}
		if (text != null & text.length() > 0) {
			_colEl.addContent(new Element("p", ns).addContent(text));
		}
		else {
			_colEl.addContent(new Element("p", ns).addContent("undefined footer text"));			
		}
		return _footerEl;
	}
	
	protected static Element getScriptElement(String url, String code) {
		Element _scriptEl = new Element("script", ns);
		if (url != null) {
			_scriptEl.setAttribute("src", url);
		}
		if (code != null) {
			_scriptEl.addContent(code);
		}
		return _scriptEl;
	}
	
	protected static Element getSingleBackgroundScript(String imgUrl, String fade) {
		if (fade == null) {
			fade = "750";
		}
		return getScriptElement(null, "$.backstretch([\"" + imgUrl + "\"],{fade: " + fade + "});");
	}
	
	protected static Element getMultiBackgroundScript(ArrayList<File> images, String resDir, String fade, String duration)
	{
		String _script = "$.backstretch([";
		for (int i = 0; i < images.size(); i++) {
			if (i>0) {
				_script = _script + ",";
			}
			_script = _script + "\"" + resDir + File.separator + images.get(i).getName() + "\"";
		}
		if (fade == null) {
			fade = "750";
		}
		if (duration == null) {
			duration = "3000";
		}
		return getScriptElement(null, _script + "],{fade: " + fade + ", duration: " + duration + "});");
	}
	
	protected static Element getStylesheetElement(String url) {
		Element _cssEl = new Element("link", ns)
			.setAttribute("rel", "stylesheet")
			.setAttribute("href", url)
			.setAttribute("type", "text/css");
		return (_cssEl);
	}

	public static Element copyContent(List<Element> fromElems, Element toEl, boolean filterImages)
	{
		if (toEl != null && fromElems != null) {
			for (int i = 0; i < fromElems.size(); i++) {
				if (filterImages == true && fromElems.get(i).getName().equalsIgnoreCase("image")) {
					// do nothing, i.e. filter image elements
				}
				else {
					toEl.addContent(fromElems.get(i).clone().setNamespace(ns));
				}
			}
		}
		return toEl;
	}
};