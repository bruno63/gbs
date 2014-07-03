package com.bkaiser.gbs;

import java.io.IOException;
import java.util.List;

import org.jdom2.*;

/**
 * This class represents a template of a Bootstrap site.
 * This template is the basis for all other concrete pages
 * e.g. GbsJumbotron, GbsJumboLogin, GbsCarousel etc.
 * 
 * @author Bruno Kaiser
 */
public class GbsTemplate extends GbsPage  {
//	private static final String CN = "GbsTemplate";
	GbsNavigation navigation = null;
	private Element headElement = null;
	private Element bodyElement = null;
	private Element contentElement = null;
	private Element headerElement = null;
	private Element footerElement = null;

	/**
	 * Constructor.
	 * 
	 */
	public GbsTemplate()  
	{
		setNamespace();
	}

	/**
	 * @return the contentElement
	 */
	public Element getContentElement() {
		return contentElement;
	}

	/**
	 * @return the headerElement
	 */
	public Element getHeaderElement() {
		return headerElement;
	}
	
	private static Element getPageSkeleton() 
	{
		Element _htmlEl = new Element("html", ns).setAttribute("ng-app", "");
		Element _headEl = new Element("head", ns);
		Element _metaEl = new Element("meta", ns)
			.setAttribute("name", "viewport")
			.setAttribute("content", "width=device-width, initial-scale=1.0");
		_htmlEl.addContent(_headEl.addContent(_metaEl));
		Element _containerEl = getDivElement("container-fluid", null);
		_containerEl.addContent(getDivElement("row-fluid", "header"));
		_containerEl.addContent(getDivElement("row-fluid", "content"));
		_htmlEl.addContent(new Element("body", ns).addContent(_containerEl));
		return _htmlEl;
	}
	
	private void setPointers()
	{
		headElement = pageContent.getChild("head", ns);
		bodyElement = pageContent.getChild("body", ns);
		headerElement = getDivInBody("header");
		contentElement = getDivInBody("content");
	}
	
	private Element getDivInBody(String id)
	{
		List<Element> _divElems = bodyElement.getChild("div", ns).getChildren("div", ns);
		String _id = null;
		for (int i = 0;  i < _divElems.size(); i++) {
			_id = _divElems.get(i).getAttributeValue("id");
			if (_id != null && _id.equalsIgnoreCase(id)) {
				return _divElems.get(i);
			}
		}
		System.out.println("warning: no div element with id " + id + " found");
		return null;
	}
	
	public void addNavigation(GbsNavigation nav)
	{
		navigation = nav;
	}
	
	private void activateMenuEntry(String menuName)
	{
		navigation.setActivatedMenuEntry(menuName);
	}
		
	public Element preparePage(Element nodeEl) throws JDOMException, IOException
	{
		activateMenuEntry(nodeEl.getAttributeValue("name"));
		pageContent = getPageSkeleton();
		setPointers();
		headerElement.addContent(navigation.getNavigation());
		bodyElement.addContent(footerElement.detach());
		return pageContent;
	}
	
	public void addTitle(String title)
	{
		headElement.addContent(new Element("title", ns).addContent(title));
	}
	
	public void addStylesheet(String url)
	{
		headElement.addContent(getStylesheetElement(url));
	}
	
	public void addBodyContent(Element content)
	{
		bodyElement.addContent(content);
	}
	
	public void addContent(Element content) 
	{
		contentElement.addContent(content);
	}
		
	public void addContent(List<Element> elems)
	{
		for (int i = 0; i < elems.size(); i++) {
			contentElement.addContent(elems.get(i).clone().setNamespace(ns));
		}
	}
	
	public void addContent(String content)
	{
		contentElement.addContent(content);		
	}

	public void addScriptLink(String url)
	{
		bodyElement.addContent(getScriptElement(url, null));	
	}

	public void addScriptCode(String code)
	{
		bodyElement.addContent(getScriptElement(null, code));	
	}
	
	public void addScriptElement(Element scriptEl)
	{
		bodyElement.addContent(scriptEl);
	}

	public void addFooter(Element footer)
	{
		footerElement = footer;
	}
};