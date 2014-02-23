package com.bkaiser.gbs;
import java.io.*;
import java.util.List;

import org.jdom2.*;

/**
 * Utility to generate a static Twitter Bootstrap website based
 * on raw content material (such as images) and a site-template.
 * 
 * @author Bruno Kaiser
 */
public class GbsBootstrapCarousel extends GbsBootstrapFactory {
	private static final String CN = "GbsBootstrapCarousel";

	/**
	 * Constructor.
	 * 
	 */
	public GbsBootstrapCarousel(Element htmlEl, File sourceDir, File destinationDir)  {
		destHtmlEl = htmlEl;
		srcDir = sourceDir;
		destDir = destinationDir;
	}

	public void savePage(Element nodeEl) throws IOException {
		// move the images to destDir
		// <node name="NAME" url="FILENAME.html" type="carousel" resDir="DIRNAME">
		//	   <carouselImages>
		//          [<image name="FILENAME_N.gif" text="DESCRIPTION" />]
		//	   <carouselImages>
		//     [<p>TEXT</p>]
		// </node>
						
		Element _headEl = destHtmlEl.getChild("head", ns);
		_headEl.addContent(new Element("title", ns).addContent(nodeEl.getAttributeValue("name")));
		// add the necessary css stylesheets
		_headEl.addContent(getStylesheetElement("css/bootstrap.min.css"));
		_headEl.addContent(getStylesheetElement("css/custom.css"));
		
		Element _bodyEl = destHtmlEl.getChild("body", ns);
		if (_bodyEl == null) {
			GbsLogUtil.throwFatal(CN, "savePage", "type mismatch in destHtmlEl; body-element not found");
		}	

		Element _containerEl = _bodyEl.getChild("div", ns);
		if (_containerEl == null | ! _containerEl.getAttributeValue("class").toLowerCase().startsWith("container")) {
			GbsLogUtil.throwFatal(CN, "savePage", "type mismatch in destHtmlEl; div-element container not found");
		}
		// get the right row element with the container	
		Element _rowEl = getContentRowElement(_containerEl);

		// add the carousel div
		Element _carouselEl = getDivElement("carousel", "myCarousel");
		_rowEl.addContent(_carouselEl);
		Element _carouselInnerEl = getDivElement("carousel-inner", null);
		_carouselEl.addContent(_carouselInnerEl);
		
		Element _carouselImagesEl = nodeEl.getChild("carouselImages");
		List<Element> _imageElems = _carouselImagesEl.getChildren();
		for (int i = 0; i < _imageElems.size(); i++) {
			// move the image into the destination directory
			copyFile(
					new File(srcDir, nodeEl.getAttributeValue("resDir")),
					new File(destDir, nodeEl.getAttributeValue("resDir")),
					_imageElems.get(i).getAttributeValue("name"));
            _carouselInnerEl.addContent(
            		getCarouselImageElement(nodeEl.getAttributeValue("resDir"), _imageElems.get(i), i));
		}
		// add carousel controls
		Element _leftEl = getLinkElement("#myCarousel", null, "carousel-control left").setAttribute("data-slide", "prev");
	//			new Element("a", ns).setAttribute("class", "carousel-control left").setAttribute("href", "#myCarousel").setAttribute("data-slide", "prev");
		_leftEl.addContent(new Element("span", ns).setAttribute("class", "glyphicon glyphicon-chevron-left"));
		_carouselEl.addContent(_leftEl);
		Element _rightEl = getLinkElement("#myCarousel", null, "carousel-control right").setAttribute("data-slide", "next");
	// new Element("a", ns).setAttribute("class", "carousel-control right").setAttribute("href", "#myCarousel").setAttribute("data-slide", "next");
		_rightEl.addContent(new Element("span", ns).setAttribute("class", "glyphicon glyphicon-chevron-right"));
		_carouselEl.addContent(_rightEl);
		
		// add all other content
		List<Element> _contentElems = nodeEl.getChildren();
		Element _contentEl = getDivElement("row-fluid", null);
		Element _contentRowEl = getDivElement("col-md-11 col-md-offset-1", null);
		_contentEl.addContent(_contentRowEl);
		_contentRowEl.addContent(new Element("h2", ns).addContent(nodeEl.getAttributeValue("name")));
		for (int i = 0; i < _contentElems.size(); i++) {
			if (! _contentElems.get(i).getName().equalsIgnoreCase("carouselImages")) {
				_contentRowEl.addContent((Element) _contentElems.get(i).clone().setNamespace(ns)); 
			}
		}
		_rowEl.addContent(_contentEl);

		_bodyEl.addContent(getScriptElement("js/jquery.min.js", null));
		_bodyEl.addContent(getScriptElement("js/bootstrap.min.js", null));

		// save the page
		new GbsXmlExport().write(destHtmlEl, new File(destDir, nodeEl.getAttributeValue("url")));
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
	private Element getCarouselImageElement(String resDir, Element imageEl, int index)
	{
		String _classAttr = index == 1 ? "item active" : "item";
		Element _itemEl = getDivElement(_classAttr, null);
		_itemEl.addContent(getImageElement(
				resDir + File.separator + imageEl.getAttributeValue("name"), 
				imageEl.getAttributeValue("text"), ImageShapes.DEFAULT, true));
		Element _captionEl = getDivElement("carousel-caption", null);
		_captionEl.addContent(new Element("p", ns).addContent(imageEl.getAttributeValue("text")));
		_itemEl.addContent(_captionEl);
		return _itemEl;
	}
}