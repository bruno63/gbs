package com.bkaiser.gbs;

import org.jdom2.*;

/**
 * Generate JDOM elements representing HTML5 Bootstrap Abbreviations.
 * Stylized implementation of HTML's <abbr> element for abbreviations
 * and acronyms to show the expanded version on hover. 
 * Abbreviations with a title attribute have a light dotted bottom 
 * border and a help cursor on hover, providing additional context on hover.
 * 
 * @author Bruno Kaiser
 */
public class GbsAbbreviation extends GbsFactory  {
//	private static final String CN = "GbsAbbreviation";
	String abbreviation = null;
	String description = null;
	boolean isInitialism = false;
	
	/**
	 * Constructor.
	 * 
	 */
	public GbsAbbreviation(String abbr, String title)  
	{
		abbreviation = abbr;
		description = title;
	}
	
	// Add .initialism to an abbreviation for a slightly smaller font-size.
	public void setInitialism()
	{
		isInitialism = true;
	}
	
	public Element getAbbreviation()
	{
		return getAbbreviation(abbreviation, description, isInitialism);
	}
	
	/*
	 * Stylized implementation of HTML's <abbr> element for abbreviations and acronyms 
	 * to show the expanded version on hover. Abbreviations with a title attribute have
	 * a light dotted bottom border and a help cursor on hover, providing additional 
	 * context on hover.
	 * @param abbreviation the abbreviation
	 * @param title the explanation of the abbreviation.
	 * @param init, if this is set to true, a slightly smaller font-size is used.
	 */
	public static Element getAbbreviation(String abbr, String title, boolean init)
	{
		Element _abbrEl = new Element("abbr", ns)
			.setAttribute("title", title)
			.addContent(abbr);
		if (init == true) {
			_abbrEl.setAttribute("class", "initialism");
		}
		return _abbrEl;
	}

};
