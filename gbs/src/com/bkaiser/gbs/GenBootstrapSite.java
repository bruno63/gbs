package com.bkaiser.gbs;
import java.io.*;
import java.util.List;
import org.jdom2.*;
import org.jdom2.input.SAXBuilder;

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
		destHtmlEl = GbsBootstrapFactory.generatePageSkeleton();
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
// todo: check whether title is set in every page.
			// add the navigation to the destination template file
			_gbs.addNavigation();

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

	private void loadSiteDescription() throws JDOMException, IOException {
		siteDescEl = (Element) new SAXBuilder().build(cfg.getSiteTemplateFile()).getRootElement().detach();
	}

	// currently, only a string is supported as footer element
	// TODO: support arbitrary content in footer
	private void addFooter() {
		Element _containerEl = destHtmlEl.getChild("body", ns).getChild("div", ns);
		_containerEl.addContent(GbsBootstrapFactory.generateFooter(siteDescEl.getAttributeValue("footerText"), true));
	}

	private void addNavigation() throws JDOMException, IOException {
		Element _navbar = new Element("nav", ns).setAttribute("class", "navbar navbar-default navbar-inverse").setAttribute("role", "navigation");
		_navbar.addContent(GbsBootstrapFactory.generateNavbarHeader());
		
		Element _menu = GbsBootstrapFactory.generateDivElement("collapse navbar-collapse navbar-ex1-collapse", null);
		_navbar.addContent(_menu);
		Element _menuItems = new Element("ul", ns).setAttribute("class", "nav navbar-nav");
		_menu.addContent(_menuItems);

		// iterate over all node Elements
		addMenuItems(_menuItems, siteDescEl.getChildren("node"));
		
		GbsBootstrapFactory.getHeaderRowElement(destHtmlEl.getChild("body", ns).getChild("div", ns)).addContent(_navbar);
		//destHtmlEl.getChild("body", ns).getChild("div", ns).getChild("div", ns).addContent(_navbar);
	}

	private void addMenuItems(Element destEl, List<Element> nodeElementList) throws JDOMException, IOException
	{
		String _nodeType = null;
		Element _nodeEl = null;

		for (int i = 0; i < nodeElementList.size(); i++) {
			_nodeEl = nodeElementList.get(i);
			_nodeType = _nodeEl.getAttributeValue("type").toLowerCase();

			if (_nodeType.equals("jumbotron")) {
				destEl.addContent(GbsBootstrapFactory.generateLinkingListItem(_nodeEl.getAttributeValue("name"), _nodeEl.getAttributeValue("url")));
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
				destEl.addContent(GbsBootstrapFactory.generateLinkingListItem(_nodeEl.getAttributeValue("name"), _nodeEl.getAttributeValue("url")));
			}
			else if (_nodeType.equals("link")) {
				destEl.addContent(GbsBootstrapFactory.generateLinkingListItem(_nodeEl.getAttributeValue("name"), _nodeEl.getAttributeValue("url")));
			}
			else if (_nodeType.equals("carousel")) {
				destEl.addContent(GbsBootstrapFactory.generateLinkingListItem(_nodeEl.getAttributeValue("name"), _nodeEl.getAttributeValue("url")));
			}
			else {
				GbsLogUtil.throwFatal(CN, "addMenuItems", "type " + _nodeType + " unknown");
			}
		}
	}

	private void generatePages(List<Element> nodeElementList) throws JDOMException, IOException {
		String _nodeType = null;
		Element _nodeEl = null;

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

			if (_nodeType.equals("jumbotron")) {
				GbsBootstrapJumbotron _jumbotronPage = new GbsBootstrapJumbotron(destHtmlEl.clone(), cfg.getSourceDir(), cfg.getDestDir());
				_jumbotronPage.generateJumbotron(_nodeEl);
			}
			else if (_nodeType.equals("menu")) {
				// make sure the destination directory exists
				String _resDir = _nodeEl.getAttributeValue("resDir");
				if (_resDir != null) {
					new File(cfg.getDestDir(), _resDir).mkdir();
				}
				generatePages(_nodeEl.getChildren("node"));
			}
			else if (_nodeType.equals("gallery")) {
				GbsBootstrapGallery _galleryPage = new GbsBootstrapGallery(destHtmlEl.clone(), cfg.getSourceDir(), cfg.getDestDir());
				_galleryPage.generateGallery(_nodeEl);
			}
			else if (_nodeType.equals("link")) {
				// don't have to do anything as linked pages will exist already
			}
			else if (_nodeType.equals("carousel")) {
				GbsBootstrapCarousel _carouselPage = new GbsBootstrapCarousel(destHtmlEl.clone(), cfg.getSourceDir(), cfg.getDestDir());
				_carouselPage.generateCarousel(_nodeEl);
			}
			else {
				GbsLogUtil.throwFatal(CN, "generatePages", "type " + _nodeType + " unknown");
			}
		}
	}

	private Element generateMenuEntry(Element nodeEl, int level) throws JDOMException, IOException {
		Element _listEl = null;
		Element _submenuEl = new Element("ul", ns).setAttribute("class", "dropdown-menu");
		
		// TODO: add class="active"
		if (level == 1) {		// dropdown menu
			_listEl = GbsBootstrapFactory.generateDropDownMenu(nodeEl.getAttributeValue("name"));
		}
		else  {   // level = 2          dropdown submenu
			_listEl = GbsBootstrapFactory.generateDropDownSubmenu(nodeEl.getAttributeValue("name"));
		}
		_listEl.addContent(_submenuEl);
		// iterate over all node Elements
		addMenuItems(_submenuEl, nodeEl.getChildren("node"));
		return _listEl;
	}		
}