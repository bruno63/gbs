package com.bkaiser.gbs;
import java.io.*;
import java.util.List;

import org.jdom2.*;

/**
 * Simple bootstrap page.
 * 
 * @author Bruno Kaiser
 */
public class GbsSimplePage extends GbsPage  {
//	private static final String CN = "GbsSimplePage";
	GbsTemplate templatePage = null;

	/**
	 * Constructor.
	 * 
	 */
	public GbsSimplePage(GbsTemplate template)  {
		templatePage = template;
	}

	// <node name="NAME" url="resdir/FILENAME.html" type="link"  />
	public Element preparePage(Element nodeEl) throws IOException, JDOMException 
	{	
		String _name = nodeEl.getAttributeValue("name");
		
		pageContent = templatePage.preparePage(nodeEl);
		templatePage.addTitle(_name);
		
		// add the necessary css stylesheets
		templatePage.addStylesheet("css/bootstrap.min.css");
		templatePage.addStylesheet("css/custom.css");
		
		templatePage.addContent(new GbsBody(_name, TextAlignment.CENTER, TextEmphasis.H1, TextColor.INFO, TextColor.DEFAULT).getBody());
		
		List<Element> _elems = nodeEl.getChildren();
		
		if (_elems != null) {
			for (int i = 0; i < _elems.size(); i++) {	
				templatePage.addContent(_elems.get(i));
			}
		}
		// javascript scripts
		templatePage.addScriptLink("js/jquery.min.js");
		templatePage.addScriptLink("js/bootstrap.min.js");
		
		return pageContent;
	}
}