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
public class GbsIconIndex extends GbsPage  {
//	private static final String CN = "GbsGallery";
	File[] images = null;
	GbsTemplate templatePage = null;

	/**
	 * Constructor.
	 * 
	 */
	public GbsIconIndex(GbsTemplate template)  {
		templatePage = template;
	}

	// <node name="NAME" url="resdir/FILENAME.html" type="iconIndex"  />
	public Element preparePage(Element nodeEl) throws IOException, JDOMException 
	{	
		String _name = nodeEl.getAttributeValue("name");
		
		pageContent = templatePage.preparePage(nodeEl);
		templatePage.addTitle(nodeEl.getAttributeValue("title"));
		
		// add the necessary css stylesheets
		templatePage.addStylesheet("css/bootstrap.min.css");
		templatePage.addStylesheet("css/custom.css");
		
		templatePage.addContent(new GbsBody(_name, TextAlignment.CENTER, TextEmphasis.H1, TextColor.INFO, TextColor.DEFAULT).getBody());
		Element _linkEl = getDivElement(null, "links");
		templatePage.addContent(_linkEl);
		
		List<Element> _elems = nodeEl.getChildren("image");
		GbsImage _img = null;
		
		for (int i = 0; i < _elems.size(); i++) {	
			_img = new GbsImage(_elems.get(i));
			_img.setTooltip(new GbsTooltip(_elems.get(i).getAttributeValue("title")));
			_linkEl.addContent(_img.getThumbnailElement(
					_elems.get(i).getAttributeValue("url"),
					nodeEl.getAttributeValue("colAttr")));
		}
		// javascript scripts
		templatePage.addScriptLink("js/jquery.min.js");
		templatePage.addScriptLink("js/bootstrap.min.js");
		
		return pageContent;
	}
}