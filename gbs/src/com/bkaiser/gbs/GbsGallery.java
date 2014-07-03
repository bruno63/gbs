package com.bkaiser.gbs;
import java.io.*;

import org.jdom2.*;

/**
 * Utility to generate a static Twitter Bootstrap website based
 * on raw content material (such as images) and a site-template.
 * 
 * @author Bruno Kaiser
 */
public class GbsGallery extends GbsPage  {
//	private static final String CN = "GbsGallery";
	File[] images = null;
	GbsTemplate templatePage = null;

	/**
	 * Constructor.
	 * 
	 */
	public GbsGallery(GbsTemplate template, File[] srcImgs)  {
		templatePage = template;
		images = srcImgs;
	}

	// <node name="NAME" url="FILENAME.html" type="gallery" resDir="DESTDIRNAME" />
	public Element preparePage(Element nodeEl) throws IOException, JDOMException 
	{	
		String _name = nodeEl.getAttributeValue("name");
		String _resDir = nodeEl.getAttributeValue("resDir");
		String _url = nodeEl.getAttributeValue("url");
		
		pageContent = templatePage.preparePage(nodeEl);
		templatePage.addTitle(_name + " Gallery");
		
		// add the necessary css stylesheets
		templatePage.addStylesheet("css/bootstrap.min.css");
		templatePage.addStylesheet("css/prettyPhoto.css");
		templatePage.addStylesheet("css/custom.css");
		
		templatePage.addContent(new GbsBody(_name, TextAlignment.CENTER, TextEmphasis.H1, TextColor.INFO, TextColor.DEFAULT).getBody());
		Element _linkEl = getDivElement(null, "links");
		templatePage.addContent(_linkEl);			
		
		for (int i = 0; i < images.length; i++) {	
			_linkEl.addContent(
					new GbsImage(_resDir + File.separator + "thumbs" + File.separator + images[i].getName(), 
							_name, _url, _name, ImageShapes.DEFAULT)
					.getThumbnailElement(
							_resDir + File.separator + images[i].getName(),
							nodeEl.getAttributeValue("colAttr")));
		}
		// javascript scripts
		templatePage.addScriptLink("js/jquery.min.js");
		templatePage.addScriptLink("js/bootstrap.min.js");
		templatePage.addScriptLink("js/jquery.prettyPhoto.js");
		templatePage.addScriptElement(getScriptElement(null, 
				"$(document).ready(function(){$(\"a[rel^='prettyPhoto']\").prettyPhoto({theme:'pp_default',slideshow:3000,autoplay_slideshow:true,social_tools:false});});"));
		
		return pageContent;
	}
}