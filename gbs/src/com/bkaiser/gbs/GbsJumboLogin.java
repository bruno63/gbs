package com.bkaiser.gbs;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.*;

/**
 * Utility to generate a static Twitter Bootstrap website based
 * on raw content material (such as images) and a site-template.
 * 
 * @author Bruno Kaiser
 */
public class GbsJumboLogin extends GbsPage {
	private static final String CN = "GbsBootstrapJumboLogin";
	int fade = 0;
	int duration = 0;
	ArrayList<File> bgImages = null;
	GbsTemplate templatePage = null;

	/**
	 * Constructor.
	 * 
	 */
	public GbsJumboLogin(GbsTemplate template)  {
		templatePage = template;
		bgImages = new ArrayList<File>();
		setNamespace();
	}

	public void setFadingEffect(int fadeTime)
	{
		fade = fadeTime;
	}

	public void setDurationEffect(int durationTime)
	{
		duration = durationTime;
	}

	public void addBackgroundImage(File bgImg)
	{
		bgImages.add(bgImg);
	}

	public Element preparePage(Element nodeEl) throws JDOMException, IOException
	{
		// <node name="Home" type="jumboLogin" resDir="dirName" url="pageName.html">
		//     <heroUnit image="imageName" alt="topic">
		//         <h1>title</h1>
		//     </heroUnit>
		// </node>
		pageContent = templatePage.preparePage(nodeEl);
		templatePage.addTitle(nodeEl.getAttributeValue("name"));
		templatePage.addStylesheet("css/bootstrap.min.css");
		templatePage.addStylesheet("css/custom.css");

		Element _divEl = getDivElement("row-fluid col-sm-6 col-sm-offset-3", "content");
		List<Element> _elems = nodeEl.getChild("heroUnit").getChildren();
		for (int i = 0; i < _elems.size(); i++) {
			if (! _elems.get(i).getName().equalsIgnoreCase("image")) {
				_divEl.addContent(_elems.get(i).clone().setNamespace(ns));
			}
		}
//		templatePage.addContent(_divEl.addContent(GbsForm.getLoginForm()));
		templatePage.addContent(_divEl.addContent(GbsForm.getInteractiveLoginForm()));

		templatePage.addScriptLink("js/angular.min.js");
		templatePage.addScriptLink("js/jquery.min.js");
		templatePage. addScriptLink("js/bootstrap.min.js");
		templatePage.addScriptLink("js/jquery.backstretch.min.js");

		switch (bgImages.size()) {
		case 0:
			GbsLogUtil.throwFatal(CN, "preparePage", "background image is not specified");
		case 1:		
			templatePage.addScriptElement(getSingleBackgroundScript(((File) bgImages.get(0)).getPath(), new Integer(fade).toString()));
			break;
			
		default: 	
			templatePage.addScriptElement(getMultiBackgroundScript(
					bgImages, 
					nodeEl.getAttributeValue("src"), 
					new Integer(fade).toString(), 
					new Integer(duration).toString())); 
			break;
		}
		
		return pageContent;
	}
}