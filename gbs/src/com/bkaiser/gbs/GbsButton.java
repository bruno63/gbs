package com.bkaiser.gbs;

import org.jdom2.*;

/**
 * Generate JDOM elements representing HTML5 Bootstrap Labels. 
 * 
 * @author Bruno Kaiser
 */
public class GbsButton extends GbsFactory  {
//	private static final String CN = "GbsButton";
	String text = null;
	ButtonSize size = ButtonSize.DEFAULT;
	ButtonClass color = ButtonClass.DEFAULT;
	boolean isBlockLevel = false;
	boolean isActive = false;
	boolean isDisabled = false;
	boolean isNavbar = false;
	GbsGlyph glyph = null;

	/**
	 * Constructor.
	 * 
	 */
	public GbsButton(String txt)  
	{
		text = txt;
	}
	
	/**
	 * Constructor.
	 * 
	 */
	public GbsButton(String txt, ButtonSize bSize, ButtonClass bColor)  
	{
		text = txt;
		size = bSize;
		color = bColor;
	}
	
	public void setColor(ButtonClass bColor)
	{
		color = bColor;
	}
	
	public void setSize(ButtonSize bSize)
	{
		size = bSize;
	}
	
	public void setGlyph(Glyphicon myGlyph)
	{
		glyph = new GbsGlyph(myGlyph);		
	}
	
	// block level buttons span the full width of a parent.
	public void setBlockLevel() 
	{
		isBlockLevel = true;
	}
	
	// buttons appear pressed (with a darker background, darker border,
	// and inset shadow) when active. 
	public void setActive()
	{
		isActive = true;
	}
	
	// make buttons look unclickable by fading them back 50%
	public void setDisabled()
	{
		isDisabled = true;
	}
	
	// adds the navbar-btn class to button elements not residing in
	// a <form> to vertically center them in the navbar.
	// like the standard button classes, navbar-btn can be used on
	// <a> and <input> elements. However, neither navbar-btn nor the
	// standard button classes should be used on <a> elements within
	// navbar-nav.
	public void setNavbar()
	{
		isNavbar = true;
	}

	private static String convertColor(ButtonClass tColor)
	{
		String _attr = null;
		switch(tColor) {
		case PRIMARY:	_attr = "btn-primary"; 	break;
		case SUCCESS:	_attr = "btn-success"; 	break;
		case INFO:		_attr = "btn-info"; 	break;
		case WARNING:	_attr = "btn-warning"; 	break;
		case DANGER:	_attr = "btn-danger"; 	break;
		case LINK:		_attr = "btn-link"; 	break;
		default:		_attr = "btn-default"; 	break;
		}
		return _attr;
	}
	
	private static String convertSize(ButtonSize bSize)
	{
		String _attr = null;
		switch(bSize) {
		case LARGE: 	_attr = "btn-lg"; break;
		case SMALL: 	_attr = "btn-sm"; break;
		case XSMALL: 	_attr = "btn-xs"; break;
		default: 		_attr = ""; 	break;
		}
		return _attr;
	}
	
	private Element addClassAttributes(Element el)
	{
		addClassAttribute(el, convertColor(color));
		addClassAttribute(el, convertSize(size));
		if (isBlockLevel == true) {
			addClassAttribute(el, "btn-block");
		}
		if (isActive == true) {
			addClassAttribute(el, "active");
		}
		if (isDisabled == true) {
			el.setAttribute("disabled", "disabled");
		}
		if (isNavbar == true) {
			addClassAttribute(el, "navbar-btn");
		}
		return el;
	}
		
	public Element getButton()
	{
		Element _el = new Element("button", ns)
			.setAttribute("class", "btn")
			.setAttribute("type", "button");
		if (glyph != null) {
			_el.addContent(glyph.getGlyph());
		}
		if (text != null) {
			_el.addContent(text);
		}
		return addClassAttributes(_el);
	}

	public Element getLinkButton(String link)
	{
		Element _el = new Element("a", ns)
			.setAttribute("href", link)
			.setAttribute("role", "button");
		if (glyph != null) {
			_el.addContent(glyph.getGlyph());
		}
		if (text != null) {
			_el.addContent(text);
		}
		return addClassAttributes(_el);
	}
	
	public Element getInputButton()
	{
		Element _el = new Element("input", ns)
			.setAttribute("class", "btn")
			.setAttribute("type", "button")
			.setAttribute("value", "Input");
		if (glyph != null) {
			_el.addContent(glyph.getGlyph());
		}
		return addClassAttributes(_el);
	}
	
	public Element getSubmitButton()
	{
		Element _el = new Element("input", ns)
			.setAttribute("class", "btn")
			.setAttribute("type", "submit")
			.setAttribute("value", "Submit");
		if (glyph != null) {
			_el.addContent(glyph.getGlyph());
		}
		return addClassAttributes(_el);		
	}
	
	public static Element getCloseIcon()
	{
		return new Element("button", ns)
			.setAttribute("type", "button")
			.setAttribute("class", "close")
			.setAttribute("aria-hidden", "true")
			.addContent("&times;");
	}

};