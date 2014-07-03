package com.bkaiser.gbs;

import org.jdom2.*;

/**
 * Generate JDOM elements representing HTML5 Bootstrap Labels. 
 * 
 * @author Bruno Kaiser
 */
public class GbsLabel extends GbsFactory  {
//	private static final String CN = "GbsLabel";
	String text = null;
	String attr = null;

	/**
	 * Constructor.
	 * 
	 */
	public GbsLabel(String txt, TextColor tColor)  
	{
		text = txt;
		attr = convertColor(tColor);
	}
	
	private static String convertColor(TextColor tColor)
	{
		String _attr = "label ";
		switch(tColor) {
		case PRIMARY:	_attr += "label-primary"; 	break;
		case SUCCESS:	_attr += "label-success"; 	break;
		case INFO:		_attr += "label-info"; 		break;
		case WARNING:	_attr += "label-warning"; 	break;
		case DANGER:	_attr += "label-danger"; 	break;
		default:		_attr += "label-default"; 	break;
		}
		return _attr;
	}
		
	public Element getLabel()
	{
		return new Element("span", ns).setAttribute("class", attr).addContent(text);
	}
};