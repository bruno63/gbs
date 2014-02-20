package com.bkaiser.gbs;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

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
		Element _colEl = new Element("div", ns).setAttribute("class", "col-xs-12");
		_colEl.addContent(new Element("hr", ns));
		Element _newEl = siteDescEl.getChild("footer").getChild("p").clone();
		_newEl.setNamespace(ns);
		_colEl.addContent(_newEl);
		_destFooterEl.addContent(_colEl);
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

		for (int i = 0; i < nodeElementList.size(); i++) {
			_nodeEl = nodeElementList.get(i);
			_nodeType = _nodeEl.getAttributeValue("type").toLowerCase();

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
				destEl.addContent(generateLinkingListItem(_nodeEl.getAttributeValue("name"), _nodeEl.getAttributeValue("url")));
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
				generateJumbotron(_nodeEl);
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
		
		// TODO: add class="active"
		if (level == 1) {		// dropdown menu
			_listEl = new Element("li", ns).setAttribute("class", "dropdown");
			_linkEl = generateLinkElement("#", nodeEl.getAttributeValue("name"), null);
			_linkEl.setAttribute("class", "dropdown-toggle").setAttribute("data-toggle", "dropdown");
			_linkEl.addContent(new Element("b", ns).setAttribute("class", "caret"));
			_listEl.addContent(_linkEl);
			_submenuEl = new Element("ul", ns).setAttribute("class", "dropdown-menu");
			_listEl.addContent(_submenuEl);		
		}
		else  {   // level = 2          dropdown submenu
			_listEl = new Element("li", ns).setAttribute("class", "dropdown-submenu");
			_linkEl = generateLinkElement("#", nodeEl.getAttributeValue("name"), null);
			_linkEl.setAttribute("tabindex", "-1");
			_listEl.addContent(_linkEl);
			_submenuEl = new Element("ul", ns).setAttribute("class", "dropdown-menu");
			_listEl.addContent(_submenuEl);
		}
		// iterate over all node Elements
		addMenuItems(_submenuEl, nodeEl.getChildren("node"));
		return _listEl;
	}

	// <li><a href="url">text</a></li>
	private Element generateLinkingListItem(String text, String url) 
	{
		return new Element("li", ns).addContent(generateLinkElement(url, text, null));
	}

	// <a href="url" [class="classValue"]>text</a>
	private Element generateLinkElement(String url, String text, String classValue)
	{
		Element _aEl = new Element("a", ns).setAttribute("href", url);
		if (classValue != null) {
			_aEl.setAttribute("class", classValue);
		}
		return (_aEl.addContent(text));
	}

	// <img src="url" [alt="alt"] [class="classValue"] /> 
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

	// <node name="NAME" url="FILENAME.html" type="gallery" resDir="DESTDIRNAME" />
	private void generateGallery(Element nodeEl) throws IOException 
	{	
		// load the page template and get <html> Element
		Element _htmlEl = destHtmlEl.clone();
		Element _bodyEl = _htmlEl.getChild("body", ns);
		String _name = nodeEl.getAttributeValue("name");
		String _resDir = nodeEl.getAttributeValue("resDir");
		
		Element _headEl = _htmlEl.getChild("head", ns);
		_headEl.addContent(new Element("title", ns).addContent(_name + " Gallery"));
		// add the necessary css stylesheets
		addStylesheetElement(_headEl, "css/bootstrap.min.css");
		addStylesheetElement(_headEl, "css/prettyPhoto.css");
		addStylesheetElement(_headEl, "css/custom.css");
		
		if (_bodyEl == null) {
			GbsLogUtil.throwFatal(CN, "generateGallery", "type mismatch in destHtmlEl; body-element not found");
		}	
		List<Element> _scriptElems = _bodyEl.getChildren("script", ns);
		for (int i = 0; i > _scriptElems.size(); i++) {
			_scriptElems.get(i).detach();
		}

		Element _containerEl = _bodyEl.getChild("div", ns);
		if (_containerEl == null | ! _containerEl.getAttributeValue("class").toLowerCase().startsWith("container")) {
			GbsLogUtil.throwFatal(CN, "generateGallery", "type mismatch in destHtmlEl; div-element container not found");
		}
		
		Element _linkEl = new Element("div", ns).setAttribute("id", "links");
		List<Element> _divElems = _containerEl.getChildren("div", ns);
		Element _contentRowEl = null;
		for (int i = 0; i < _divElems.size(); i++) {
			if (_divElems.get(i).getAttributeValue("id").equalsIgnoreCase("content")) {
				_contentRowEl = _divElems.get(i);
				break;
			}
		}
		if (_contentRowEl != null) {
			_contentRowEl.addContent(new Element("h1", ns).addContent(_name));
			_contentRowEl.addContent(_linkEl);
		}
		else {
			_containerEl.addContent(new Element("h1", ns).addContent(_name));
			_containerEl.addContent(_linkEl);			
		}
		
		File _destDir = new File(cfg.getDestDir(), _resDir);
		File _srcDir = new File(cfg.getSourceDir(), _resDir);
		_destDir.mkdir();
		File _destThumbDir = new File(_destDir, "thumbs");
		_destThumbDir.mkdir();
		File[] _images = selectFiles(_srcDir, ".jpg"); // select all jpg files
		Element _imgEl = null;
		Element _colEl = null;
		File _slideImg = null;
		File _thumbImg = null;
		// for each image in resDir...
		for (int i = 0; i < _images.length; i++) {
			_slideImg = new File(_destDir, _images[i].getName());
			_thumbImg = new File(_destThumbDir, _images[i].getName());
			
			// generate slide image
			if (! _slideImg.exists()) {
				// TODO: only scale if the images is larger than a certain size
				generateSlide(_images[i], _slideImg, 0.5f); 
			}
			
			// generate thumbnail image
			if (! _thumbImg.exists()) {
				generateThumbnail(_images[i], _thumbImg, 200, 200); 
			}

			// generate link entry element
			_imgEl = generateImageElement(
					_resDir + File.separator + "thumbs" + File.separator + _images[i].getName(), 
					_name, "img-responsive");
			
			_colEl = new Element("div", ns).setAttribute("class", "col-xs-3 thumb");
			_colEl.addContent(new Element("a", ns)
				.setAttribute("href", _resDir + File.separator + _images[i].getName())
				.setAttribute("title", _name)
				.setAttribute("class", "thumbnail")
				.setAttribute("rel", "prettyPhoto[Sommer]")
				.addContent(_imgEl));
			_linkEl.addContent(_colEl);
		}
				
		/*
		// add blueimp-gallery element
		// modal_header
		Element _modalHeaderEl = new Element("div", ns).setAttribute("class", "modal-header");
		_modalHeaderEl.addContent(new Element("button", ns)
			.setAttribute("type", "button")
			.setAttribute("class", "close")
			.setAttribute("aria-hidden", "true")
			.addContent(new String("\u00D7")));
		_modalHeaderEl.addContent(new Element("h4", ns).setAttribute("class", "modal_title"));

		// modal-footer
		// previous button
		Element _modalFooterEl = new Element("div", ns).setAttribute("class", "modal-footer");
		Element _buttonEl = new Element("button", ns)
			.setAttribute("type", "button")
			.setAttribute("class", "btn btn-default pull-left prev");
		_buttonEl.addContent(new Element("i", ns).setAttribute("class", "glyphicon glyphicon-chevron-left"));
		_buttonEl.addContent("Previous");
		_modalFooterEl.addContent(_buttonEl);
		
		// next button
		Element _button2El = new Element("button", ns)
			.setAttribute("type", "button")
			.setAttribute("class", "btn btn-primary next");
		_button2El.addContent("Next");
		_button2El.addContent(new Element("i", ns).setAttribute("class", "glyphicon glyphicon-chevron-right"));
		_modalFooterEl.addContent(_button2El);
		
		// modal-content
		Element _modalContentEl = new Element("div", ns).setAttribute("class", "modal-content")
			.addContent(_modalHeaderEl)
			.addContent(new Element("div", ns).setAttribute("class", "modal-body next"))
			.addContent(_modalFooterEl);
			
		// model-dialog
		// TODO: after a slideshow, the images are not clickable anymore -> reload is necessary
		Element _modalDialogEl = new Element("div", ns).setAttribute("class", "modal-dialog")
				.addContent(_modalContentEl);
		Element _blueimpGalleryEl = new Element("div", ns)
				.setAttribute("id", "blueimp-gallery")
				.setAttribute("class", "blueimp-gallery")
				.addContent(new Element("div", ns).setAttribute("class", "slides"))
				.addContent(new Element("h3", ns).setAttribute("class", "title").addContent(_name))
				.addContent(new Element("a", ns).setAttribute("class", "prev").addContent(new String("\u02C2")))
				.addContent(new Element("a", ns).setAttribute("class", "next").addContent(new String("\u02C3")))
				.addContent(new Element("a", ns).setAttribute("class", "close").addContent(new String("\u00D7")))
				.addContent(new Element("a", ns).setAttribute("class", "play-pause"))
				.addContent(new Element("ol", ns).setAttribute("class", "indicator"))
				.addContent(new Element("div", ns).setAttribute("class", "modal fade").addContent(_modalDialogEl));
		_bodyEl.addContent(_blueimpGalleryEl);
		*/
		
		// javascript scripts
		for (int i = 0; i > _scriptElems.size(); i++) {
			_bodyEl.addContent(_scriptElems.get(i));
		}
		addScriptElement(_bodyEl, "js/jquery.min.js");
		addScriptElement(_bodyEl, "js/bootstrap.min.js");
		addScriptElement(_bodyEl, "js/jquery.prettyPhoto.js");		

		Element _scriptEl = new Element("script", ns)
			.setAttribute("type", "text/javascript")
			.setAttribute("charset", "utf-8")
			.addContent("$(document).ready(function(){$(\"a[rel^='prettyPhoto']\").prettyPhoto({theme:'pp_default',slideshow:3000,autoplay_slideshow:true,social_tools:false});});");
		_bodyEl.addContent(_scriptEl);
		
		// print the page
		new GbsXmlExport(cfg).write(_htmlEl, new File(cfg.getDestDir(), nodeEl.getAttributeValue("url")));
	
	}
	
	private void generateSlide(File originalF, File destF, double scalingFactor) throws IOException
	{
		BufferedImage _originalImg = ImageIO.read(originalF);
		BufferedImage _slideImg = Thumbnails.of(_originalImg)
				.scale(scalingFactor)
				.asBufferedImage();
		ImageIO.write(_slideImg, "jpg", destF);

	}

	private void generateThumbnail(File originalF, File destF, int height, int width) throws IOException {
		BufferedImage _originalImg = ImageIO.read(originalF);
		BufferedImage _thumbnailImg = Thumbnails.of(_originalImg)
				.size(height, width)
				.crop(Positions.CENTER)
				.asBufferedImage();
		ImageIO.write(_thumbnailImg, "jpg", destF);
	}

	private void addScriptElement(Element bodyEl, String url) {
		bodyEl.addContent(new Element("script", ns)
			.setAttribute("src", url));
	}

	private void addStylesheetElement(Element headEl, String url) {
		headEl.addContent(new Element("link", ns)
		.setAttribute("rel", "stylesheet")
		.setAttribute("href", url)
		.setAttribute("type", "text/css"));
	}

	private void generateCarousel(Element nodeEl) throws IOException {
		// move the images to destDir
		// <node name="NAME" url="FILENAME.html" type="carousel" resDir="DIRNAME">
		//	   <carouselImages>
		//          [<image name="FILENAME_N.gif" text="DESCRIPTION" />]
		//	   <carouselImages>
		//     [<p>TEXT</p>]
		// </node>
				
		// load the page template and get <html> Element
		Element _htmlEl = destHtmlEl.clone();
		
		Element _headEl = _htmlEl.getChild("head", ns);
		_headEl.addContent(new Element("title", ns).addContent(nodeEl.getAttributeValue("name")));
		// add the necessary css stylesheets
		addStylesheetElement(_headEl, "css/bootstrap.min.css");
		addStylesheetElement(_headEl, "css/custom.css");
		
		Element _bodyEl = _htmlEl.getChild("body", ns);
		if (_bodyEl == null) {
			GbsLogUtil.throwFatal(CN, "generateCarousel", "type mismatch in destHtmlEl; body-element not found");
		}	

		Element _containerEl = _bodyEl.getChild("div", ns);
		if (_containerEl == null | ! _containerEl.getAttributeValue("class").toLowerCase().startsWith("container")) {
			GbsLogUtil.throwFatal(CN, "generateCarousel", "type mismatch in destHtmlEl; div-element container not found");
		}
		// get the right row element with the container	
		Element _rowEl = getContentRowElement(_containerEl);

		// add the carousel div
		Element _carouselEl = new Element("div", ns).setAttribute("id", "myCarousel").setAttribute("class", "carousel");
		_rowEl.addContent(_carouselEl);
		Element _carouselInnerEl = new Element("div", ns).setAttribute("class", "carousel-inner");
		_carouselEl.addContent(_carouselInnerEl);
		
		Element _carouselImagesEl = nodeEl.getChild("carouselImages");
		List<Element> _imageElems = _carouselImagesEl.getChildren();
		for (int i = 0; i < _imageElems.size(); i++) {
			// move the image into the destination directory
			copyFileFromSourceToDestDir(nodeEl.getAttributeValue("resDir"), _imageElems.get(i).getAttributeValue("name"));
            _carouselInnerEl.addContent(
            		generateCarouselImageElement(nodeEl.getAttributeValue("resDir"), _imageElems.get(i), i));
		}
		// add carousel controls
		Element _leftEl = new Element("a", ns).setAttribute("class", "carousel-control left").setAttribute("href", "#myCarousel").setAttribute("data-slide", "prev");
		_leftEl.addContent(new Element("span", ns).setAttribute("class", "glyphicon glyphicon-chevron-left"));
		_carouselEl.addContent(_leftEl);
		Element _rightEl = new Element("a", ns).setAttribute("class", "carousel-control right").setAttribute("href", "#myCarousel").setAttribute("data-slide", "next");
		_rightEl.addContent(new Element("span", ns).setAttribute("class", "glyphicon glyphicon-chevron-right"));
		_carouselEl.addContent(_rightEl);
		
		// add all other content
		List<Element> _contentElems = nodeEl.getChildren();
		Element _contentEl = new Element("div", ns).setAttribute("class", "row-fluid");
		Element _contentRowEl = new Element("div", ns).setAttribute("class", "col-md-11 col-md-offset-1");
		_contentEl.addContent(_contentRowEl);
		_contentRowEl.addContent(new Element("h2", ns).addContent(nodeEl.getAttributeValue("name")));
		for (int i = 0; i < _contentElems.size(); i++) {
			if (! _contentElems.get(i).getName().equalsIgnoreCase("carouselImages")) {
				_contentRowEl.addContent((Element) _contentElems.get(i).clone().setNamespace(ns)); 
			}
		}
		_rowEl.addContent(_contentEl);

		addScriptElement(_bodyEl, "js/jquery.min.js");
		addScriptElement(_bodyEl, "js/bootstrap.min.js");

		// print the page
		new GbsXmlExport(cfg).write(_htmlEl, new File(cfg.getDestDir(), nodeEl.getAttributeValue("url")));
	}
	
	// convert: 
	//  <image name="FILENAME_N.gif" text="DESCRIPTION" />
	// to:
	// <div class="item">
	//      <img src="resDir/imageName" alt="text">
	//      <div class="carousel-caption">
	//            <p>Text</p>
	//      </div>
	// </div>
	private Element generateCarouselImageElement(String resDir, Element imageEl, int index)
	{
		String _classAttr = index == 1 ? "item active" : "item";
		Element _itemEl = new Element("div", ns).setAttribute("class", _classAttr);
		_itemEl.addContent(generateImageElement(
				resDir + File.separator + imageEl.getAttributeValue("name"), 
				imageEl.getAttributeValue("text"), null));
		Element _captionEl = new Element("div", ns).setAttribute("class", "carousel-caption");
		_captionEl.addContent(new Element("p", ns).addContent(imageEl.getAttributeValue("text")));
		_itemEl.addContent(_captionEl);
		return _itemEl;
	}

	private void copyFileFromSourceToDestDir(String resDir, String imageName) throws IOException
	{
		File _destDir = new File(cfg.getDestDir(), resDir);
		File _srcDir = new File(cfg.getSourceDir(), resDir);
		_destDir.mkdir();
		String _srcDirPath = new File(_srcDir, imageName).getAbsolutePath();
		String _destDirPath = new File(_destDir, imageName).getAbsolutePath();
		Files.copy(Paths.get(_srcDirPath), Paths.get(_destDirPath), REPLACE_EXISTING);
	}

	private void generateJumbotron(Element nodeEl) throws JDOMException, IOException 
	{
		// <node name="NAME" type="jumbotron" resDir="DIRNAME" url="FILENAME.html">
		//      <heroUnit image="IMAGENAME.jpg" alt="DESCRIPTION">
		//        		<h1>TITLE</h1>
		//        		<p>TEXT</p>
		//		</heroUnit>
		// </node>

		// move the resources into the destination directory
		Element _heroDescEl = nodeEl.getChild("heroUnit");
		copyFileFromSourceToDestDir(nodeEl.getAttributeValue("resDir"), _heroDescEl.getAttributeValue("image"));

		// load the page template and get <html> Element
		Element _htmlEl = destHtmlEl.clone();
		
		Element _headEl = _htmlEl.getChild("head", ns);
		_headEl.addContent(new Element("title", ns).addContent(nodeEl.getAttributeValue("name")));
		// add the necessary css stylesheets
		addStylesheetElement(_headEl, "css/bootstrap.min.css");
		addStylesheetElement(_headEl, "css/custom.css");

		Element _bodyEl = _htmlEl.getChild("body", ns);
		if (_bodyEl == null) {
			GbsLogUtil.throwFatal(CN, "generateJumbotron", "type mismatch in destHtmlEl; body-element not found");
		}	

		Element _containerEl = _bodyEl.getChild("div", ns);
		if (_containerEl == null | ! _containerEl.getAttributeValue("class").toLowerCase().startsWith("container")) {
			GbsLogUtil.throwFatal(CN, "generateJumbotron", "type mismatch in destHtmlEl; div-element container not found");
		}
		// get the right row element with the container	
		Element _rowEl = getContentRowElement(_containerEl);

		// add the jumbotron div
		Element _divEl = new Element("div", ns).setAttribute("class", "jumbotron clearfix");
		_divEl.addContent(generateImageElement(
				nodeEl.getAttributeValue("resDir") + File.separator + _heroDescEl.getAttributeValue("image"),
				_heroDescEl.getAttributeValue("alt"),
				"img-circle pull-right img-responsive"));
		
		// add all other content within hero unit
		List<Element> _heroContentElems = _heroDescEl.getChildren();
		for (int i = 0; i < _heroContentElems.size(); i++) {
			_divEl.addContent((Element) _heroContentElems.get(i).clone().setNamespace(ns)); 
		}
		_rowEl.addContent(_divEl);

		// add some image links below (abstractRow)
		// 		<abstractRow buttonText="STRING">
		//       	[<linkUnit title="TITLE"  link="URL" image="FILENAME.jpg" text="TEXT" />]
		//      </abstractRow>		
		Element _abstractRowDescEl = nodeEl.getChild("abstractRow");
		if (_abstractRowDescEl != null) {
			Element _abstractRowEl = new Element("div", ns).setAttribute("class", "row-fluid");
			// add all linkUnits ...
			List<Element> _linkUnitElems = _abstractRowDescEl.getChildren("linkUnit");
			for (int i = 0; i < _linkUnitElems.size(); i++) {
				copyFileFromSourceToDestDir(nodeEl.getAttributeValue("resDir"), _linkUnitElems.get(i).getAttributeValue("image"));
				_abstractRowEl.addContent(
						addLinkUnit((Element) _linkUnitElems.get(i), 
								nodeEl.getAttributeValue("resDir"),
								_abstractRowDescEl.getAttributeValue("buttonText"))); 
			}
			_rowEl.addContent(_abstractRowEl);
		}
		
		addScriptElement(_bodyEl, "js/jquery.min.js");
		addScriptElement(_bodyEl, "js/bootstrap.min.js");


		// print the page
		new GbsXmlExport(cfg).write(_htmlEl, new File(cfg.getDestDir(), nodeEl.getAttributeValue("url")));
	}

	private Element addLinkUnit(Element linkUnitEl, String resDir, String buttonText)
	{
		String _imgStr = resDir + File.separator + linkUnitEl.getAttributeValue("image");
		Element _el = new Element("div", ns).setAttribute("class", "col-sm-3 col-xs-6");
		_el.addContent(new Element("h3", ns).addContent(linkUnitEl.getAttributeValue("title")));
		Element _aEl = new Element("a", ns).setAttribute("href", linkUnitEl.getAttributeValue("link"));
		_aEl.addContent(generateImageElement(_imgStr, linkUnitEl.getAttributeValue("title"), "img-responsive"));
		_el.addContent(new Element("p", ns).addContent(_aEl));
		_el.addContent(new Element("p", ns).addContent(linkUnitEl.getAttributeValue("text")));
		_aEl = generateLinkElement(linkUnitEl.getAttributeValue("link"), buttonText, "btn btn-info btn-sm");
		_el.addContent(new Element("p", ns).addContent(_aEl));
		return _el;
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
	
	/**
	 * Filters all files within directory dir according to a file extension.
	 * @param   dir         the current directory to look for the files
	 * @param   extension   the file name extension is the selection criteria
	 * @return	an array of pdf files
	 */
	private File[] selectFiles(File dir, String extension) {
		try {
			FilenameFilter _filter = new GbsFileFilter(extension);
			return (dir.listFiles(_filter));
		}
		catch (Exception _ex) {
			GbsLogUtil.throwFatal(CN, "selectFiles", _ex.toString());
			return null;
		}
	}

}