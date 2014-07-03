package com.bkaiser.gbs;

import java.util.List;

import org.jdom2.*;

/**
 * Generate JDOM elements representing HTML5 Bootstrap Lists. 
 * 
 * @author Bruno Kaiser
 */
public class GbsList extends GbsFactory  {
	private static final String CN = "GbsList";
	List<String> listItems = null;
	ListType listType = ListType.BULLETS;
	boolean isHorizontal = false;
	List<String> listDescriptions = null;

	/**
	 * Constructor.
	 * 
	 */
	public GbsList(List<String> items, ListType type)  
	{
		listItems = items;
		listType = type;
	}

	/**
	 * Default Constructor for a bulleted list.
	 * 
	 */
	public GbsList(List<String> items)  
	{
		listItems = items;
	}	

	/**
	 * Constructor for a description list.
	 * A list of terms with their associated descriptions.
	 * 
	 */
	public GbsList(List<String> items, List<String> descriptions)  
	{
		listItems = items;
		listDescriptions = descriptions;
	}	
	
	public void setHorizontal()
	{
		isHorizontal = true;
	}

	/* 
	 * Return a JDOM element containing a html list.
	 * Currently, only list of strings of one level depth are supported.
	 * 
	 * BULLETS = unordered:  <ul><li>
	 * NUMBERS = ordered:    <ol><li>
	 * NONE =    unstyled:   <ul class="list-unstyled"><li>
	 * INLINE (horizontal):     <ul class="list-inline"><li>
	 * description: <dl><dt><dd>
	 * horizontal description:  <dl class="dl-horizontal"><dt><dd>
	 */
	public Element getList()
	{
		Element _listEl = null;
		if (listDescriptions != null) {
			_listEl = getDescriptionList(listItems, listDescriptions, isHorizontal);
		}
		else {
			String _elName = "ul";
			String _classAttr = null;
			switch (listType) {
			case NUMBERS:  _elName = "ol"; break;
			case UNSTYLED:	   _classAttr = "list-unstyled"; break;
			case INLINE:   _classAttr = "list-inline"; break;
			case BULLETS:  
			default:		break;  // keep default: _elName="ul", _classAttr=null
			}
			_listEl = new Element(_elName, ns);
			if (_classAttr != null) {
				_listEl.setAttribute("class", _classAttr);
			}
			// add all list items
			for (int i = 0; i < listItems.size(); i++) {
				_listEl.addContent(new Element("li", ns).addContent(listItems.get(i)));
			}
		}
		return _listEl;
	}
	
	/*
	 * A list of terms with their associated descriptions.
	 * description: <dl><dt><dd>
	 * horizontal description:  <dl class="dl-horizontal"><dt><dd>
	 */
	private static Element getDescriptionList(List<String> terms, List<String> descriptions, boolean horizontal)
	{
		Element _listEl = new Element("dl", ns);
		if (horizontal == true) {
			_listEl.setAttribute("class", "dl-horizontal");
		}
		// add all list items
		if (terms.size() != descriptions.size()) {
			GbsLogUtil.throwFatal(CN, "getDescriptionList", "size of terms and descriptions is not equal");
		}
		for (int i = 0; i < terms.size(); i++) {
			_listEl.addContent(new Element("dt", ns).addContent(terms.get(i)));
			_listEl.addContent(new Element("dd", ns).addContent(descriptions.get(i)));
		}
		return _listEl;	
	}
	
	// <li><a href="url">text</a></li>
	public static Element getLinkingListItem(String text, String url) 
	{
		return new Element("li", ns).addContent(getLinkElement(url, text, null));
	}

};
