package com.bkaiser.gbs;

import org.jdom2.*;

/**
 * Generate JDOM elements representing HTML5 Bootstrap Blockquotes.
 * For quoting blocks of content from another source within your document.
 * 
 * @author Bruno Kaiser
 */
public class GbsBlockquote extends GbsFactory  {
//	private static final String CN = "GbsBlockquote";
	String quote = null;
	String source = null;
	boolean isReverse = false;

	/**
	 * Constructor.
	 * 
	 */
	public GbsBlockquote(String quoteStr, String srcStr)  
	{
		quote = quoteStr;
		source = srcStr;
	}
	
	public void setReverse()
	{
		isReverse = true;
	}
	
	// TODO: cite is not yet supported
	public Element getBlockquote()
	{
		Element _pEl = new Element("p", ns).addContent(quote);
		Element _quoteEl = new Element("blockquote", ns).addContent(_pEl);
		if (source != null) {
			_pEl.addContent(new Element("footer", ns).addContent(source));
		}
		if (isReverse == true) {
			_quoteEl.setAttribute("class", "blockquote-reverse");
		}
		return _quoteEl;
	}
};
