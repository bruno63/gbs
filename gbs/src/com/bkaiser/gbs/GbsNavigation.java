package com.bkaiser.gbs;

import java.io.IOException;
import java.util.List;

import org.jdom2.*;


/**
 * Generate a JDOM elements representing HTML5 Bootstrap Navigation 
 * structures (Navs and Navbar).
 * Navs available in Bootstrap have shared markup, starting with 
 * the base .nav class, as well as shared states.
 * Swap modifier classes to switch between each style.
 * 
 * @author Bruno Kaiser
 */
public class GbsNavigation extends GbsFactory  {
	private static final String CN = "GbsNavigation";
	Element rootEl = null;			// the root element of the site description
	NavigationType navType = null;
	NavigationHorizontalAlignment aligH = NavigationHorizontalAlignment.NONE;
	NavigationVerticalAlignment aligV = NavigationVerticalAlignment.NONE;
	boolean isJustified = false;
	boolean isInverse = true;
	String activatedMenuEntry = null;

	/**
	 * Constructor.
	 * 
	 */
	public GbsNavigation(Element rootNodeElement, NavigationType type)  {
		rootEl = rootNodeElement;
		navType = type;
		setNamespace();
	}
	
	public void setJustified()
	{
		isJustified = true;
	}
	
	// //emt[@name = 'hello'] -> select all elements with an attribute called 'name'
	// where the name attribute's value is 'hello'.
	public void disableMenuEntry(String xpathQuery)
	{
		changeClassAttribute(rootEl, xpathQuery, "disabled");
	}
	
	public void enableMenuEntry(String xpathQuery)
	{
		changeClassAttribute(rootEl, xpathQuery, null);
	}

	public void setActivatedMenuEntry(String menuEntry)
	{
		activatedMenuEntry = menuEntry;
	}
	
	public void setInversion(boolean isInverted)
	{
		isInverse = isInverted;
	}
	
	public void setHorizontalAlignment(NavigationHorizontalAlignment alig)
	{
		aligH = alig;
	}
	
	public void setVerticalAlignment(NavigationVerticalAlignment alig)
	{
		aligV = alig;
	}
	
	// generates a menu of different types (TABS, PILLS, PILLSTACK, NAVABAR) without submenus.
	// _nodes needs to be a list of JDOM Elements, each containing a name and a url attribute
	// activate a menu entry afterwards with activateMenuEntry
	// disable a menu entry afterwards with disableMenuEntry
	public Element getNavigation() throws JDOMException, IOException
	{
		String _dataTarget = "mynav";
		Element _listEl = null;
		Element _menuItems = new Element("ul", ns).setAttribute("class", getNavTypeString());
		if (navType == NavigationType.NAVBAR) {
			_listEl = new Element("nav", ns).setAttribute("class", "navbar navbar-default").setAttribute("role", "navigation");
			_listEl.addContent(getDivElement("container-fluid", null).addContent(getNavbarHeader(_dataTarget)));
			if (isInverse == true) {
				addClassAttribute(_listEl, "navbar-inverse");
			}
			
			_listEl.addContent(getDivElement("collapse navbar-collapse " + _dataTarget, null).addContent(_menuItems));
		}
		else {
			_listEl = _menuItems;
		}
		if (isJustified == true && (navType == NavigationType.PILLS || navType == NavigationType.TABS)) {
			addClassAttribute(_menuItems, "nav-justified");
		}
		String _alig = null;
		if ((_alig = getHorizontalAlignmentString()) != null) {
			addClassAttribute(_menuItems, _alig);
		}
		if ((_alig = getVerticalAlignmentString()) != null) {
			addClassAttribute(_menuItems, _alig);
		}
	
		// iterate over all node Elements
		addMenuItems(_menuItems, rootEl.getChildren("node"));
		return _listEl;
	}
	
	private void addMenuItems(Element ulEl, List<Element> nodes) throws JDOMException, IOException
	{
		String _nodeType = null;
		Element _nodeEl = null;

		for (int i = 0; i < nodes.size(); i++) {
			_nodeEl = nodes.get(i);
			_nodeType = _nodeEl.getAttributeValue("type").toLowerCase();
			if (_nodeType.equals("menu")) {
				int _level = new Integer(_nodeEl.getAttributeValue("level")).intValue();
				switch (_level) {
				case 1: 
					ulEl.addContent(generateMenuEntry(_nodeEl, 1));
					break;
				case 2: 
					ulEl.addContent(new Element("li", ns).setAttribute("class", "divider"));
					ulEl.addContent(generateMenuEntry(_nodeEl, 2));
					break;
				default: GbsLogUtil.throwFatal(CN, "addMenuItems", "invalid level: <" + _nodeEl.getAttributeValue("level") + ">");
				}
			}
			else if (_nodeType.equals("iconindex")) {
				ulEl.addContent(generateMenuEntry(_nodeEl, 1));
			}
			else if (_nodeType.equals("link")) {
				ulEl.addContent(GbsList.getLinkingListItem(_nodeEl.getAttributeValue("name"), _nodeEl.getAttributeValue("url")));
				if (_nodeEl.getAttributeValue("drawDivider") != null) {
					ulEl.addContent(new Element("li", ns).setAttribute("class", "divider"));
				}
			}
			else if (_nodeType.equals("gallery") || _nodeType.equals("jumbologin") || _nodeType.equals("doclist")
					 || _nodeType.equals("carousel") || _nodeType.equals("jumbotron")) {
				ulEl.addContent(GbsList.getLinkingListItem(_nodeEl.getAttributeValue("name"), _nodeEl.getAttributeValue("url")));
			}
			else {
				GbsLogUtil.throwFatal(CN, "addMenuItems", "type " + _nodeType + " unknown");
			}
		}

	}
	
	private Element generateMenuEntry(Element nodeEl, int level) throws JDOMException, IOException {
		Element _listEl = null;
		Element _submenuEl = new Element("ul", ns).setAttribute("class", "dropdown-menu");
		String _menuEntryName = nodeEl.getAttributeValue("name");
		
		if (level == 1) {		// dropdown menu
			_listEl = GbsNavigation.getDropDownMenu(_menuEntryName, nodeEl.getAttributeValue("url"));
		}
		else  {   // level > 1          dropdown submenu
			_listEl = GbsNavigation.getDropDownSubmenu(_menuEntryName);
		}
		if (_menuEntryName.equalsIgnoreCase(activatedMenuEntry)) {
			addClassAttribute(_listEl, "active");
		}
		_listEl.addContent(_submenuEl);
		// iterate over all node Elements
		addMenuItems(_submenuEl, nodeEl.getChildren("node"));
		return _listEl;
	}		

	// todo: url noch nicht unterstützt; muss man genauer anschauen, funktioniert noch nicht richtig
	// weil die url zwar gesetzt ist, aber nicht angeklickt werden kann
	public static Element getDropDownMenu(String name, String url)
	{
		url = null;
		Element _listEl = new Element("li", ns).setAttribute("class", "dropdown");
		Element _linkEl = getLinkElement(url == null ? "#" : url, name, null);
		_linkEl.setAttribute("class", "dropdown-toggle").setAttribute("data-toggle", "dropdown");
		_linkEl.addContent(new Element("span", ns).setAttribute("class", "caret"));
		_listEl.addContent(_linkEl);
		return _listEl;
	}

	public static Element getDropDownSubmenu(String name)
	{
		Element _listEl = new Element("li", ns).setAttribute("class", "dropdown-submenu");
		Element _linkEl = getLinkElement("#", name, null);
		_linkEl.setAttribute("tabindex", "-1");
		_listEl.addContent(_linkEl);
		return _listEl;
	}
	
	public static Element getNavbarHeader(String target) {
		Element _navbarHeader = getDivElement("navbar-header", null);
		Element _button = new Element("button", ns).setAttribute("type", "button").setAttribute("class", "navbar-toggle").setAttribute("data-toggle", "collapse").setAttribute("data-target", "." + target);
		_navbarHeader.addContent(_button);
		_button.addContent(new Element("span", ns).setAttribute("class", "sr-only").addContent("Toggle navigation"));
		_button.addContent(new Element("span", ns).setAttribute("class", "icon-bar"));
		_button.addContent(new Element("span", ns).setAttribute("class", "icon-bar"));
		_button.addContent(new Element("span", ns).setAttribute("class", "icon-bar"));
		return _navbarHeader;
	}
	
	private String getNavTypeString()
	{
		String _navTypeStr = null;
		switch(navType) {
		case TABS:		_navTypeStr = "nav nav-tabs"; break;
		case PILLS:		_navTypeStr = "nav nav-pills"; break;
		case PILLSTACK:	_navTypeStr = "nav nav-pills nav-stacked"; break;
		case NAVBAR: 	_navTypeStr = "nav navbar-nav"; break;
		default: GbsLogUtil.throwFatal(CN, "getNavTypeString", "undefined NavigationType");
		}
		if (isJustified == true) {
			_navTypeStr += " nav-justified";
		}
		return _navTypeStr;
	}
		
	private String getVerticalAlignmentString()
	{
		String _vAlig = null;
		switch(aligV) {
		case FIXED_TOP: 	_vAlig = "navbar-fixed-top"; break;
		case FIXED_BOTTOM:	_vAlig = "navbar-fixed-bottom"; break;
		case STATIC_TOP:	_vAlig = "navbar-static-top"; break;
		default:			break;  // default is null String,i.e. no attribute value
		}
		return _vAlig;
	}
	
	private String getHorizontalAlignmentString()
	{
		String _hAlig = null;
		switch(aligH) {
		case LEFT:		_hAlig = "navbar-left"; break;
		case RIGHT:		_hAlig = "navbar-right"; break;
		default:		break; // default is null String, i.e. no attribute value set
		}
		return _hAlig;
	}
};