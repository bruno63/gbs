package com.bkaiser.gbs;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.jdom2.*;
import org.jdom2.input.SAXBuilder;

import static java.nio.file.StandardCopyOption.*;


/**
 * Utility to generate a static Twitter Bootstrap website based
 * on raw content material (such as images) and a site-template.
 * 
 * @author Bruno Kaiser
 */
public class GenBootstrapSite {
	private static final String CN = "GenBootstrapSite";
	private static GbsConfig cfg = null;
	private Element destHtmlEl = null;   // the root html element of the page template 
	private Element siteDescEl = null;   // the site description
	Namespace ns = null;

	/**
	 * Constructor.
	 * The idea is to start this programm out of Eclipse and to control it via properties settings. 
	 * Therefore, command line parameters are not supported.
	 * 
	 * @throws IOException
	 * @throws JDOMException 
	 */
	public GenBootstrapSite() throws IOException, JDOMException {
		cfg = new GbsConfig();

		if (cfg.isDebugMode()) {  
			cfg.dumpConfig();
		}
		loadSiteDescription();		
		loadTemplate();
	}
	
	/**
	 * Static entry point of the program (main function). It instantiates a GenBootstrapSite object,
	 * loads the site template file and generates the site according to the directives in the template
	 * file.
	 *
	 * @param args	the command line parameters (not used).
	 */
	public static void main(String[] args) {
		try {
			GenBootstrapSite _gbs = new GenBootstrapSite();

			// add the navigation to the destination template file
			_gbs.addNavigation();
			System.out.println("navigation added"); System.out.flush();
			
			// add the footer to the destination template file
			_gbs.addFooter();
			// new GbsXmlExport(cfg).write(_destHtmlEl, cfg.getPageTemplateFile());

			// generate all pages; using the page template
			_gbs.generatePages(_gbs.siteDescEl.getChildren("node"));
			
			System.out.println("****** completed successfully **********"); 
		}
		catch (Exception _ex) {
			System.out.println("***** failed with " + _ex.toString() + "**********" );
			if (cfg.isDebugMode()) {
				_ex.printStackTrace();
			}
		}
	}

	private void loadTemplate() throws JDOMException, IOException {
		destHtmlEl = (Element) new SAXBuilder().build(cfg.getSiteSkeletonFile()).getRootElement();
		ns = destHtmlEl.getNamespace();
	}
	
	private void loadSiteDescription() throws JDOMException, IOException {
		siteDescEl = (Element) new SAXBuilder().build(cfg.getSiteTemplateFile()).getRootElement().detach();
	}

	private void addFooter() {
		Element _destFooterEl = destHtmlEl.getChild("body", ns).getChild("div", ns).getChild("footer", ns);
		Element _newEl = siteDescEl.getChild("footer").getChild("p").clone();
		_newEl.setNamespace(ns);
		_destFooterEl.addContent(_newEl);
	}
	
	private void addNavigation() throws JDOMException, IOException {
		Element _navbar = new Element("nav", ns).setAttribute("class", "navbar navbar-default navbar-inverse").setAttribute("role", "navigation");
		Element _navbarHeader = new Element("div", ns).setAttribute("class", "navbar-header");
		_navbar.addContent(_navbarHeader);
		Element _button = new Element("button", ns).setAttribute("type", "button").setAttribute("class", "navbar-toggle").setAttribute("data-toggle", "collapse").setAttribute("data-target", ".navbar-ex1-collapse");
		_navbarHeader.addContent(_button);
		_button.addContent(new Element("span", ns).setAttribute("class", "sr-only").addContent("Toggle navigation"));
		_button.addContent(new Element("span", ns).setAttribute("class", "icon-bar"));
		_button.addContent(new Element("span", ns).setAttribute("class", "icon-bar"));
		_button.addContent(new Element("span", ns).setAttribute("class", "icon-bar"));
		Element _menu = new Element("div", ns).setAttribute("class", "collapse navbar-collapse navbar-ex1-collapse");
		_navbar.addContent(_menu);
		Element _menuItems = new Element("ul", ns).setAttribute("class", "nav navbar-nav");
		_menu.addContent(_menuItems);
		
		// iterate over all node Elements
		addMenuItems(_menuItems, siteDescEl.getChildren("node"));
		
		destHtmlEl.getChild("body", ns).getChild("div", ns).getChild("div", ns).addContent(_navbar);
	}
	
	private void addMenuItems(Element destEl, List<Element> nodeElementList) throws JDOMException, IOException
	{
		String _nodeType = null;
		Element _nodeEl = null;
		System.out.println("entering addMenuItems"); System.out.flush();

		for (int i = 0; i < nodeElementList.size(); i++) {
			_nodeEl = nodeElementList.get(i);
			_nodeType = _nodeEl.getAttributeValue("type").toLowerCase();
			System.out.println("handling nodetype " + _nodeType); System.out.flush();

			if (_nodeType.equals("jumbotron")) {
				destEl.addContent(generateLinkingListItem(_nodeEl.getAttributeValue("name"), _nodeEl.getAttributeValue("url")));
			}
			else if (_nodeType.equals("menu")) {
				int _level = new Integer(_nodeEl.getAttributeValue("level")).intValue();
				switch (_level) {
				case 1: 
					destEl.addContent(generateMenuEntry(_nodeEl, 1));
					break;
				case 2: 
					destEl.addContent(new Element("li", ns).setAttribute("class", "divider"));
					destEl.addContent(generateMenuEntry(_nodeEl, 2));
					break;
				default: GbsLogUtil.throwFatal(CN, "addMenuItems", "invalid level: <" + _nodeEl.getAttributeValue("level") + ">");
				}
			}
			else if (_nodeType.equals("gallery")) {
				destEl.addContent(generateLinkingListItem(_nodeEl.getAttributeValue("name"), _nodeEl.getAttributeValue("url")));
			}
			else if (_nodeType.equals("link")) {
				destEl.addContent(generateLinkingListItem(_nodeEl.getAttributeValue("name"), _nodeEl.getAttributeValue("url")));
			}
			else if (_nodeType.equals("carousel")) {
			}
			else {
				GbsLogUtil.throwFatal(CN, "addMenuItems", "type " + _nodeType + " unknown");
			}
		}
	}

	private void generatePages(List<Element> nodeElementList) throws JDOMException, IOException {
		String _nodeType = null;
		Element _nodeEl = null;
		System.out.println("entering generatePages"); System.out.flush();

		for (int i = 0; i < nodeElementList.size(); i++) {
			
			/*
			// add title -> TODO: needs to be done for each page
			_gbs.setPageTitle();
			Element _titleEl = new Element("title", ns).addContent(_srcSiteEl.getAttributeValue("title"));
			_gbs.destHtmlEl.getChild("head").addContent(_titleEl);
			//_destHtmlEl.getChild("head").getChild("title").addContent(_srcSiteEl.getAttributeValue("title"));
*/
			_nodeEl = (Element) nodeElementList.get(i);
			_nodeType = _nodeEl.getAttributeValue("type").toLowerCase();
			System.out.println("handling nodetype " + _nodeType); System.out.flush();

			if (_nodeType.equals("jumbotron")) {
				generateJumbotron(_nodeEl);
			}
			else if (_nodeType.equals("menu")) {
				generatePages(_nodeEl.getChildren("node"));
			}
			else if (_nodeType.equals("gallery")) {
				generateGallery(_nodeEl);
			}
			else if (_nodeType.equals("link")) {
				// don't have to do anything as linked pages will exist already
			}
			else if (_nodeType.equals("carousel")) {
				generateCarousel(_nodeEl);
			}
			else {
				GbsLogUtil.throwFatal(CN, "generatePages", "type " + _nodeType + " unknown");
			}
		}
	}

	private Element generateMenuEntry(Element nodeEl, int level) throws JDOMException, IOException {
		Element _listEl = null;
		Element _linkEl = null;
		Element _submenuEl = null;
		if (level == 1) {		// dropdown menu
			_listEl = new Element("li", ns).setAttribute("class", "dropdown");
			_linkEl = generateLinkElement("#", nodeEl.getAttributeValue("name"));
			_linkEl.setAttribute("class", "dropdown-toggle").setAttribute("data-toggle", "dropdown");
			_linkEl.addContent(new Element("b", ns).setAttribute("class", "caret").setText("."));
			_listEl.addContent(_linkEl);
			_submenuEl = new Element("ul", ns).setAttribute("class", "dropdown-menu");
			_listEl.addContent(_submenuEl);		
		}
		else  {   // level = 2          dropdown submenu
			_listEl = new Element("li", ns).setAttribute("class", "dropdown-submenu");
			_linkEl = generateLinkElement("#", nodeEl.getAttributeValue("name"));
			_linkEl.setAttribute("tabindex", "-1");
			_listEl.addContent(_linkEl);
			_submenuEl = new Element("ul", ns).setAttribute("class", "dropdown-menu");
			_listEl.addContent(_submenuEl);
		}
		// iterate over all node Elements
		addMenuItems(_submenuEl, nodeEl.getChildren("node"));
		return _listEl;
	}

	private Element generateLinkingListItem(String text, String url) 
	{
		return new Element("li", ns).addContent(generateLinkElement(url, text));
	}
	
	private Element generateLinkElement(String url, String text)
	{
		return (new Element("a", ns)).setAttribute("href", url).addContent(text);
	}
	
	private Element generateImageElement(String url, String alt, String classValue)
	{
		Element _imgEl = new Element("img", ns).setAttribute("src", url);
		if (alt != null) {
			_imgEl.setAttribute("alt", alt);
		}
		if (classValue != null) {
			_imgEl.setAttribute("class", classValue);
		}
		return _imgEl;
	}

	private void generateGallery(Element _nodeEl) {
		// <node name="NAME" url="FILENAME.html" type="gallery" resDir="DESTDIRNAME" />
		// generate slide images
		// generate thunbnail images
		// generate the gallery page		
	}

	private void generateCarousel(Element _nodeEl) {
		// move the images to destDir
		// <node name="NAME" url="FILENAME.html" type="carousel" resDir="DIRNAME">
        // [<image name="FILENAME_N.gif" text="DESCRIPTION" />]
		// [<p>TEXT</p>]
		// </node>1G
		
	}

		private void generateJumbotron(Element nodeEl) throws JDOMException, IOException {
        // <node name="NAME" type="jumbotron" resDir="DIRNAME" url="FILENAME.html">
        //      <heroUnit image="IMAGENAME.jpg" alt="DESCRIPTION">
        //        		<h1>TITLE</h1>
        //        		<p>TEXT</p>
        //		</heroUnit>
        // 		<abstractRow>
        //       	[<linkUnit title="TITLE"  link="URL" image="FILENAME.jpg" />]
        //      </abstractRow>
        // </node>
		
		// move the resources into the destination directory
		System.out.println("entering generateJumbotron"); System.out.flush();
		Element _heroDescEl = nodeEl.getChild("heroUnit");
		String _relImageName = File.separator + nodeEl.getAttributeValue("resDir")
				+ File.separator + _heroDescEl.getAttributeValue("image");
		System.out.println("Copying" + _relImageName + "from srcDir to destDir"); System.out.flush();

		Files.copy(
				Paths.get(cfg.getSourceDir() + _relImageName),
				Paths.get(cfg.getDestDir() + _relImageName),
				REPLACE_EXISTING);
		System.out.println("file copied, starting cloning of destHtmlEl"); System.out.flush();


		// load the page template and get <html> Element
		Element _htmlEl = destHtmlEl.clone();
		System.out.println("clone completed: <" + _htmlEl + ">"); System.out.flush();

		Element _bodyEl = _htmlEl.getChild("body", ns);
		if (_bodyEl == null) {
			GbsLogUtil.throwFatal(CN, "generateJumbotron", "type mismatch in template file " + 
					cfg.getPageTemplateFile().getAbsolutePath() + 
					"; body-element not found");
		}
		
		
		Element _containerEl = _bodyEl.getChild("div", ns);
		if (_containerEl == null | ! _containerEl.getAttributeValue("class").equalsIgnoreCase("container")) {
			GbsLogUtil.throwFatal(CN, "generateJumbotron", "type mismatch in template file " + 
					cfg.getPageTemplateFile().getAbsolutePath() + 
					"; div-element container not found");
		}
		// get the right row element with the container	
		Element _rowEl = getContentRowElement(_containerEl);
	
		// add the jumbotron div
		Element _divEl = new Element("div", ns).setAttribute("class", "jumbotron clearfix");
		_divEl.addContent(generateImageElement(
				nodeEl.getAttributeValue("resDir") + File.separator + _heroDescEl.getAttributeValue("image"),
				_heroDescEl.getAttributeValue("alt"),
				"img-circle pull-right img-responsive"));
		List<Element> _heroContentElems = _heroDescEl.getChildren();
		System.out.println(_heroContentElems.size() + " elements in heroUnit");

		for (int i = 0; i < _heroContentElems.size(); i++) {
			System.out.println(_heroContentElems.get(i));
			_divEl.addContent((Element) _heroContentElems.get(i).clone().setNamespace(ns)); 
		}
		_rowEl.addContent(_divEl);
		
		// print the page
		new GbsXmlExport(cfg).write(_htmlEl, new File(cfg.getDestDir(), nodeEl.getAttributeValue("url")));
	}

	private Element getContentRowElement(Element containerEl) {
		Element _el = null;
		if (containerEl != null) {
			List<Element> _divElems = containerEl.getChildren("div", ns);
			for (int i = 0; i < _divElems.size(); i++) {
				if (_divElems != null) {
					if (_divElems.get(i).getAttributeValue("id").equalsIgnoreCase("content")) {
						_el = (Element) _divElems.get(i);
					}
				}
			}
		}
		return _el;
	}
}