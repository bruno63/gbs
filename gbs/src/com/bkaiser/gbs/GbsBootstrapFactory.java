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

import static java.nio.file.StandardCopyOption.*;


/**
 * Abstract class representing a factory that generates Twitter Bootstrap
 * Elements in JDOM format.
 * Contains static methods the generate simple html constructs.
 * Concrete subclasses implement some more complex bootstrap constructs.
 * 
 * @author Bruno Kaiser
 */
public abstract class GbsBootstrapFactory {
	private static final String CN = "GbsBootstrapFactory";
	protected Element destHtmlEl = null;   // the root html element of the page template 
	protected File srcDir = null;
	protected File destDir = null;
	protected static Namespace ns = null;
	
	public static Element generatePageSkeleton() {
		Element _htmlEl = new Element("html", ns);
		Element _headEl = new Element("head", ns);
		Element _metaEl = new Element("meta", ns)
			.setAttribute("name", "viewport")
			.setAttribute("content", "width=device-width, initial-scale=1.0");
		_htmlEl.addContent(_headEl.addContent(_metaEl));
		Element _containerEl = generateDivElement("container-fluid", null);
		_containerEl.addContent(generateDivElement("row-fluid", "header"));
		_containerEl.addContent(generateDivElement("row-fluid", "content"));
		_htmlEl.addContent(new Element("body", ns).addContent(_containerEl));
		return _htmlEl;
	}

	public static Element generateFooter(String text, boolean drawSeparatorLine) {
		Element _footerEl = new Element("footer", ns).setAttribute("class", "row-fluid");
		Element _colEl = generateDivElement("col-xs-12", null);
		_footerEl.addContent(_colEl);
		if (drawSeparatorLine == true) {
			_colEl.addContent(new Element("hr", ns));
		}
		if (text != null & text.length() > 0) {
			_colEl.addContent(new Element("p", ns).addContent(text));
		}
		else {
			_colEl.addContent(new Element("p", ns).addContent("undefined footer text"));			
		}
		return _footerEl;
	}
	
	public static Element generateDropDownMenu(String name)
	{
		Element _listEl = new Element("li", ns).setAttribute("class", "dropdown");
		Element _linkEl = generateLinkElement("#", name, null);
		_linkEl.setAttribute("class", "dropdown-toggle").setAttribute("data-toggle", "dropdown");
		_linkEl.addContent(new Element("b", ns).setAttribute("class", "caret"));
		_listEl.addContent(_linkEl);
		return _listEl;
	}
	
	public static Element generateDropDownSubmenu(String name)
	{
		Element _listEl = new Element("li", ns).setAttribute("class", "dropdown-submenu");
		Element _linkEl = generateLinkElement("#", name, null);
		_linkEl.setAttribute("tabindex", "-1");
		_listEl.addContent(_linkEl);
		return _listEl;
	}
	
	public static Element generateDivElement(String classValue, String idValue)
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

	// <li><a href="url">text</a></li>
	public static Element generateLinkingListItem(String text, String url) 
	{
		return new Element("li", ns).addContent(generateLinkElement(url, text, null));
	}

	// <a href="url" [class="classValue"]>text</a>
	public static Element generateLinkElement(String url, String text, String classValue)
	{
		Element _aEl = new Element("a", ns).setAttribute("href", url);
		if (classValue != null) {
			_aEl.setAttribute("class", classValue);
		}
		return (_aEl.addContent(text));
	}

	// <img src="url" [alt="alt"] [class="classValue"] /> 
	public static Element generateImageElement(String url, String alt, String classValue)
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
				
	public static Element generateScriptElement(String url, String code) {
		Element _scriptEl = new Element("script", ns);
		if (url != null) {
			_scriptEl.setAttribute("src", url);
		}
		if (code != null) {
			_scriptEl.addContent(code);
		}
		return _scriptEl;
	}

	public static Element generateStylesheetElement(String url) {
		Element _cssEl = new Element("link", ns)
			.setAttribute("rel", "stylesheet")
			.setAttribute("href", url)
			.setAttribute("type", "text/css");
		return (_cssEl);
	}
	
	public static void generateSlide(File originalF, File destF, double scalingFactor) throws IOException
	{
		BufferedImage _originalImg = ImageIO.read(originalF);
		BufferedImage _slideImg = Thumbnails.of(_originalImg)
				.scale(scalingFactor)
				.asBufferedImage();
		ImageIO.write(_slideImg, "jpg", destF);

	}

	public static void generateThumbnail(File originalF, File destF, int height, int width) throws IOException {
		BufferedImage _originalImg = ImageIO.read(originalF);
		BufferedImage _thumbnailImg = Thumbnails.of(_originalImg)
				.size(height, width)
				.crop(Positions.CENTER)
				.asBufferedImage();
		ImageIO.write(_thumbnailImg, "jpg", destF);
	}

	public static void copyFile(File srcDir, File destDir, String imageName) throws IOException
	{
		destDir.mkdir();
		String _srcDirPath = new File(srcDir, imageName).getAbsolutePath();
		String _destDirPath = new File(destDir, imageName).getAbsolutePath();
		Files.copy(Paths.get(_srcDirPath), Paths.get(_destDirPath), REPLACE_EXISTING);
	}


	public static Element generateLinkUnit(String link, String title, String image, String text, String resDir, String buttonText)
	{
		String _imgStr = resDir + File.separator + image;
		Element _el = generateDivElement("col-sm-3 col-xs-6", null);
		_el.addContent(new Element("h3", ns).addContent(title));
		Element _aEl = new Element("a", ns).setAttribute("href", link);
		_aEl.addContent(generateImageElement(_imgStr, title, "img-responsive"));
		_el.addContent(new Element("p", ns).addContent(_aEl));
		_el.addContent(new Element("p", ns).addContent(text));
		_aEl = generateLinkElement(link, buttonText, "btn btn-info btn-sm");
		_el.addContent(new Element("p", ns).addContent(_aEl));
		return _el;
	}

	/**
	 * Filters all files within directory dir according to a file extension.
	 * @param   dir         the current directory to look for the files
	 * @param   extension   the file name extension is the selection criteria
	 * @return	an array of pdf files
	 */
	public static File[] selectFiles(File dir, String extension) {
		try {
			FilenameFilter _filter = new GbsFileFilter(extension);
			return (dir.listFiles(_filter));
		}
		catch (Exception _ex) {
			GbsLogUtil.throwFatal(CN, "selectFiles", _ex.toString());
			return null;
		}
	}
	
	protected static Element getContentRowElement(Element containerEl) {
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
	
	protected static Element getHeaderRowElement(Element containerEl) {
		Element _el = null;
		if (containerEl != null) {
			List<Element> _divElems = containerEl.getChildren("div", ns);
			for (int i = 0; i < _divElems.size(); i++) {
				if (_divElems != null) {
					if (_divElems.get(i).getAttributeValue("id").equalsIgnoreCase("header")) {
						_el = (Element) _divElems.get(i);
					}
				}
			}
		}
		return _el;
	}

	public static Element generateNavbarHeader() {
		Element _navbarHeader = generateDivElement("navbar-header", null);
		Element _button = new Element("button", ns).setAttribute("type", "button").setAttribute("class", "navbar-toggle").setAttribute("data-toggle", "collapse").setAttribute("data-target", ".navbar-ex1-collapse");
		_navbarHeader.addContent(_button);
		_button.addContent(new Element("span", ns).setAttribute("class", "sr-only").addContent("Toggle navigation"));
		_button.addContent(new Element("span", ns).setAttribute("class", "icon-bar"));
		_button.addContent(new Element("span", ns).setAttribute("class", "icon-bar"));
		_button.addContent(new Element("span", ns).setAttribute("class", "icon-bar"));
		return _navbarHeader;
	}
}