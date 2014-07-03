package com.bkaiser.gbs;
import java.io.*;
import java.util.List;

import org.jdom2.*;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.*;

/**
 * Abstract class representing a factory that generates Twitter Bootstrap
 * Elements in JDOM format.
 * Contains static methods the generate simple html constructs.
 * Concrete subclasses implement some more complex bootstrap constructs.
 * 
 * @author Bruno Kaiser
 */
public abstract class GbsFactory {
	private static final String CN = "GbsBootstrapFactory";
	protected Element destHtmlEl = null;   // the root html element of the page template 
	protected File srcDir = null;
	protected File destDir = null;
	protected static Namespace ns = null;
	
	
	public Element addClassAttribute(Element el, String attr) 
	{
		if (attr != null && attr.length() > 0) {
			String _classAttr = el.getAttributeValue("class");
			if (_classAttr == null) {
				el.setAttribute("class", attr);
			}
			else {
				el.setAttribute("class", _classAttr + " " + attr);		
			}
		}
		return el;
	}
	
	public static Element getDivElement(String classValue, String idValue)
	{
		Element _divEl = new Element("div", ns);
		if (classValue != null) {
			_divEl.setAttribute("class", classValue);
		}
		if (idValue != null) {
			_divEl.setAttribute("id", idValue);
		}
		return _divEl;
	}

	// <a href="url" [class="classValue"]>text</a>
	public static Element getLinkElement(String url, String text, String classValue)
	{
		String _url = url == null ? "#" : url;
		Element _aEl = new Element("a", ns).setAttribute("href", _url);
		if (classValue != null) {
			_aEl.setAttribute("class", classValue);
		}
		return (_aEl.addContent(text));
	}

	protected static Element selectDivElement(Element containerEl, String id) 
	{
		Element _el = null;
		if (containerEl != null) {
			List<Element> _divElems = containerEl.getChildren("div", ns);
			for (int i = 0; i < _divElems.size(); i++) {
				if (_divElems != null) {
					if (_divElems.get(i).getAttributeValue("id").equalsIgnoreCase(id)) {
						_el = (Element) _divElems.get(i);
					}
				}
			}
		}
		return _el;
	}
	
	protected void setNamespace()
	{
		// xhtml local
//		 ns = Namespace.getNamespace("xhtml", "dtd/xhtml1-strict.dtd");
		
		// xhtml default
		// ns = Namespace.getNamespace("http://www.w3.org/1999/xhtml");
	}
	
	public Namespace getNamespace() 
	{
		return ns;
	}
	
	// todo: currently, it is not checked for colliding 'disabled' and 'active' changes.
	public static void changeClassAttribute(Element rootElem, String xpathQuery, String classAttr)
	{
		XPathExpression<Element> _xpath = XPathFactory.instance().compile(xpathQuery, Filters.element());
		Element _el = _xpath.evaluateFirst(rootElem);
		if (_el != null) {
			if (classAttr == null) {
				_el.removeAttribute("class");
			}
			else {
				_el.setAttribute("class", classAttr);
			}
		}
	}
	
	public static void changeAttribute(Element rootElem, String xpathQuery, String attributeName, String attributeValue)
	{
		XPathExpression<Element> _xpath = XPathFactory.instance().compile(xpathQuery, Filters.element());
		Element _el = _xpath.evaluateFirst(rootElem);
		if (_el != null) {
			if (attributeValue == null) {
				_el.removeAttribute(attributeName);
			}
			else {
				_el.setAttribute(attributeName, attributeValue);
			}
		}
	}
	
	public static void changeContent(Element rootElem, String xpathQuery, Element contentEl)
	{		
		if (contentEl == null) {
			GbsLogUtil.throwFatal(CN, "changeContent", "contentEl is null");
		}

		System.out.println("Namespace=" + ns.toString());
		System.out.println("xpathQuery=" + xpathQuery);
		System.out.println("rootElem=" + rootElem.toString());
		
		List<Element> _rootElems = rootElem.getChildren();
		for (int i = 0; i < _rootElems.size(); i++) {
			System.out.println(_rootElems.get(i).toString());
		}

		XPathExpression<Element> xpath =
			    XPathFactory.instance().compile(xpathQuery, Filters.element(), null, ns);
//		XPathExpression<Element> xpath =
//				XPathFactory.instance().compile("//html:div[@id='content']", Filters.element(), null, ns);

			List<Element> _elems = xpath.evaluate(rootElem);
			if (_elems != null) {
			    System.out.println("XPath has result: " + _elems.size());
			}
			else {
				System.out.println("XPath results in null selection");
			}
		/*
		// XPathExpression<Element> _expr = XPathFactory.instance().compile("//node()", Filters.element(), null, rootElem.getNamespace());
		XPathExpression<Element> _expr = XPathFactory.instance().compile("//node()", Filters.element());
		System.out.println("expr: " + _expr.toString());
		System.out.println("rootElem: " + rootElem.toString());
		List<Element> _el = _expr.evaluate(rootElem);
		if (_el == null) {
			GbsLogUtil.throwFatal(CN, "changeContent", "result of xpath evaluation is null");
		}
		else {
			System.out.println("xpath evaluation found " + _el.size() + " elements.");
			System.out.println(_el.toString());
		}
		*/
		System.exit(0);
	//	_el.addContent(contentEl);
	}
	
	public static String getAlignmentTypeString(String align)
	{
		String _ret = null;
		if (align != null) {
		if (align.equalsIgnoreCase("left")) {
			_ret = "pull-left";
		}
		else if (align.equalsIgnoreCase("right")) {
			_ret = "pull-right";
		}
		}
		return _ret;
	}
}