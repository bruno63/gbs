package com.bkaiser.gbs;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.*;

/**
 * Generate JDOM elements representing HTML5 Bootstrap Pagination. 
 * structures (Navs and Navbar).
 * 
 * @author Bruno Kaiser
 */
public class GbsPagination extends GbsFactory  {
	private static final String CN = "GbsPagination";
	List<String> links = null;
	String leftTag = "&laquo;";
	String rightTag = "&raquo;";
	boolean disableLeft = false;
	boolean disableRight = false;
	PaginationSize size = PaginationSize.DEFAULT;
	int activeIndex = 0;
	boolean pagerIsCentered = false;

	/**
	 * Constructor.
	 * 
	 */
	public GbsPagination(List<String> addrs)  
	{
		if (addrs.size() < 2) {
			GbsLogUtil.throwFatal(CN, "GbsPagination", "pagination needs to contain at least two links");
		}
		links = addrs;
	}
			
	/**
	 * Constructor.
	 * 
	 */
	public GbsPagination(String leftAddr, String rightAddr)  
	{
		links = new ArrayList<String>();
		links.add(leftAddr);
		links.add(rightAddr);
	}
	
	public void activate(int index)
	{
		if (index > 2 && index < (links.size() - 1)) {
			activeIndex = index;
		}
	}
	
	public void disableLeft() 
	{
		disableLeft = true;
	}
	
	public void disableRight()
	{
		disableRight = true;
	}
	
	public void enableLeft()
	{
		disableLeft = false;
	}
	
	public void enableRight()
	{
		disableRight = false;
	}	
	
	public void setLeftTag(String tag)
	{
		leftTag = tag;
	}
	
	public void setRightTag(String tag)
	{
		rightTag = tag;
	}
	
	public void setLargeSize()
	{
		size = PaginationSize.LARGE;
	}
	
	public void setSmallSize()
	{
		size = PaginationSize.SMALL;
	}
	
	public void setPagerCentered()
	{
		pagerIsCentered = true;
	}
	
	public Element getElement()
	{
		Element _rootEl = new Element("ul", ns);
		Element _listEl = null;
		if (links.size() > 2) { 		// multi-page pagination
			_rootEl.setAttribute("class", "pagination");
			switch(size) {
			case LARGE: 	addClassAttribute(_rootEl, "pagination-lg"); break;
			case SMALL:		addClassAttribute(_rootEl, "pagination-sm"); break;
			default: 		break;
			}
			// add all list items
			for (int i = 0; i < links.size(); i++) {
				_listEl = new Element("li", ns);
				if (i == 0) {
					_listEl.addContent(getLinkElement(links.get(i), leftTag, null));
					if (disableLeft == true) {
						_listEl.setAttribute("class", "disabled");
					}
				}
				else if (i == links.size()-1) {
					_listEl.addContent(getLinkElement(links.get(i), rightTag, null));
					if (disableRight == true) {
						_listEl.setAttribute("class", "disabled");
					}
				}
				else {
					_listEl.addContent(getLinkElement(links.get(i), new Integer(i).toString(), null));
					if (i == activeIndex) {
						_listEl.setAttribute("class", "active");
					}
				}
			}
		}
		else {							// simple pager
			_rootEl.setAttribute("class", "pager");
			_rootEl.addContent(createSimplePager(links.get(0), leftTag, "previous"));
			_rootEl.addContent(createSimplePager(links.get(1), rightTag, "next"));
		}
		return _rootEl;
	}
	
	private Element createSimplePager(String link, String tag, String alignAttr)
	{
		Element _listEl = new Element("li", ns).addContent(getLinkElement(link, tag, null));
		if (pagerIsCentered == true) {
			_listEl.setAttribute("class", alignAttr);
		}
		if (disableLeft == true) {
			addClassAttribute(_listEl, "disabled");
		}
		return _listEl;
	}
};
