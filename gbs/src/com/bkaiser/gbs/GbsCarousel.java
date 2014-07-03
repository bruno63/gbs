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
public class GbsCarousel extends GbsPage {
//	private static final String CN = "GbsCarousel";
	GbsTemplate templatePage = null;

	/**
	 * Constructor.
	 * 
	 */
	public GbsCarousel(GbsTemplate template)  {
		templatePage = template;
	}

	public Element preparePage(Element nodeEl) throws IOException, JDOMException {
		// move the images to destDir
		// <node name="NAME" url="FILENAME.html" type="carousel">
		//     [<image src="resdir/FILENAME_N.gif" alt="DESCRIPTION" />]
		//     [<p>TEXT</p>]
		// </node>

		pageContent = templatePage.preparePage(nodeEl);
		templatePage.addTitle(nodeEl.getAttributeValue("name"));
		templatePage.addStylesheet("css/bootstrap.min.css");
		templatePage.addStylesheet("css/custom.css");

		// add the carousel div
		Element _carouselEl = getDivElement("carousel", "myCarousel");
		templatePage.addContent(_carouselEl);
		Element _carouselInnerEl = getDivElement("carousel-inner", null);
		_carouselEl.addContent(_carouselInnerEl);
		
		List<Element> _images = nodeEl.getChildren("image");
		for (int i = 0; i < _images.size(); i++) {
           _carouselInnerEl.addContent(new GbsImage(_images.get(i)).getCarouselElement(i));
		}
		// add carousel controls
		Element _leftEl = getLinkElement("#myCarousel", null, "carousel-control left").setAttribute("data-slide", "prev");
		_leftEl.addContent(new Element("span", ns).setAttribute("class", "glyphicon glyphicon-chevron-left"));
		_carouselEl.addContent(_leftEl);
		Element _rightEl = getLinkElement("#myCarousel", null, "carousel-control right").setAttribute("data-slide", "next");
		_rightEl.addContent(new Element("span", ns).setAttribute("class", "glyphicon glyphicon-chevron-right"));
		_carouselEl.addContent(_rightEl);
		
		// add all other content
		List<Element> _contentElems = nodeEl.getChildren();
		Element _contentEl = getDivElement("row-fluid", null);
		Element _contentRowEl = getDivElement("col-md-11 col-md-offset-1", null);
		_contentEl.addContent(_contentRowEl);
		_contentRowEl.addContent(new Element("h2", ns).addContent(nodeEl.getAttributeValue("name")));
		for (int i = 0; i < _contentElems.size(); i++) {
			if (! _contentElems.get(i).getName().equalsIgnoreCase("image")) {
				_contentRowEl.addContent((Element) _contentElems.get(i).clone().setNamespace(ns)); 
			}
		}
		templatePage.addContent(_contentEl);

		templatePage.addScriptLink("js/jquery.min.js");
		templatePage.addScriptLink("js/bootstrap.min.js");
		
		return pageContent;
	}
}