package com.bkaiser.gbs;

import org.jdom2.*;

/**
 * Generate JDOM elements representing HTML5 Bootstrap Badge.
 * Highlight new or unread items by adding a badge element to
 * links, boostrap navs, and more. 
 * 
 * @author Bruno Kaiser
 */
public class GbsBadge extends GbsFactory  {
//	private static final String CN = "GbsBadge";
	int badgeNumber = 0;
	boolean pullRight = false;
	boolean pullLeft = false;

	/**
	 * Constructor.
	 * 
	 */
	public GbsBadge(int nrOfItems)  
	{
		badgeNumber = nrOfItems;
	}
	
	public void pullRight()
	{
		pullRight = true;
	}
			
	public void pullLeft()
	{
		pullLeft = true;
	}
			
	public Element getBadge()
	{
		Element _el = new Element("span", ns).setAttribute("class", "badge");
		if (pullRight == true) {
			addClassAttribute(_el, "pull-right");
		}
		if (pullLeft == true) {
			addClassAttribute(_el, "pull-left");
		}
		return _el.addContent(new Integer(badgeNumber).toString());
	}
};