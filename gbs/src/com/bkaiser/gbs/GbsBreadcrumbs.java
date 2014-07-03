package com.bkaiser.gbs;

import org.jdom2.*;

import java.util.ArrayList;;

/**
 * Generate JDOM elements representing HTML5 Bootstrap Breadcrumbs. 
 * Indicate the current page's location within a navigational hierarchy.
 * Separators are automatically added in CSS through :before and content.
 * 
 * @author Bruno Kaiser
 */
public class GbsBreadcrumbs extends GbsFactory  {
	private static final String CN = "GbsBreadcrumbs";
	ArrayList<String> links = null;
	ArrayList<String> descs = null;

	/**
	 * Constructor.
	 * 
	 */
	public GbsBreadcrumbs()  
	{
		links = new ArrayList<String>();
		descs = new ArrayList<String>();
	}
	
	public void addElement(String link, String desc)
	{
		if (link == null || link.length()== 0) {
			link = "#";
		}
		links.add(link);
		if (desc == null || desc.length() == 0) {
			desc = "tbd";
		}
		descs.add(desc);
	}
	
	public void stepUp() 
	{
		if (links.size() == 0) {
			GbsLogUtil.throwFatal(CN, "stepUp", "link-list is empty");
		}
		links.remove(links.size() - 1);
		descs.remove(descs.size() - 1);
	}
	
	public Element getBreadcrumb()
	{
		Element _el = new Element("ol", ns).setAttribute("class", "breadcrumb");
		for (int i = 0; i < links.size(); i++) {
			_el.addContent(
					new Element("li", ns).addContent(
							new Element("a", ns).setAttribute("href", links.get(i)).addContent(descs.get(i))));
		
		}
		return _el;
	}
};
	