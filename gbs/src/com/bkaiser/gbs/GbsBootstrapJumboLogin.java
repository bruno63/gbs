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
public class GbsBootstrapJumboLogin extends GbsBootstrapFactory {
	private static final String CN = "GbsBootstrapJumboLogin";
	int fade = 0;
	int duration = 0;
	File[] bgImages = null;

	/**
	 * Constructor.
	 * 
	 */
	public GbsBootstrapJumboLogin(Element htmlEl, File sourceDir, File destinationDir)  {
		destHtmlEl = htmlEl;
		srcDir = sourceDir;
		destDir = destinationDir;
	}
	
	public void setFadingEffect(int fadeTime)
	{
		fade = fadeTime;
	}
	
	public void setDurationEffect(int durationTime)
	{
		duration = durationTime;
	}
	public void setBackgroundImage(File bgImg)
	{
		if (bgImages == null) {
			bgImages = new File[1];
			bgImages[0] = bgImg;
		}
	}

	public void savePage(Element nodeEl) throws JDOMException, IOException 
	{
        // <node name="Home" type="jumbotronLogin" resDir="dirName" url="pageName.html">
        //     <heroUnit image="imageName" alt="topic">
        //         <h1>title</h1>
        //     </heroUnit>
        // </node>


		// move the resources into the destination directory
		Element _heroDescEl = nodeEl.getChild("heroUnit");
		copyFile(new File(srcDir, nodeEl.getAttributeValue("resDir")),
				new File(destDir, nodeEl.getAttributeValue("resDir")), 
				_heroDescEl.getAttributeValue("image"));
		
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

		// add the jumbotron div
		Element _divEl = getDivElement("row-fluid col-sm-6 col-sm-offset-3", "content");
		
		// add all other content within hero unit
		List<Element> _heroContentElems = _heroDescEl.getChildren();
		for (int i = 0; i < _heroContentElems.size(); i++) {
			_divEl.addContent((Element) _heroContentElems.get(i).clone().setNamespace(ns)); 
		}
		_divEl.addContent(getLoginForm());
		_rowEl.addContent(_divEl);
		
		_bodyEl.addContent(getScriptElement("js/jquery.min.js", null));
		_bodyEl.addContent(getScriptElement("js/bootstrap.min.js", null));
		_bodyEl.addContent(getScriptElement("js/jquery.backstretch.min.js", null));
		_bodyEl.addContent(getScriptElement(null, "$.backstretch(\"" + 
				nodeEl.getAttributeValue("resDir") + File.separator + _heroDescEl.getAttributeValue("image") +
				"\");"));

		// save the page
		new GbsXmlExport().write(destHtmlEl, new File(destDir, nodeEl.getAttributeValue("url")));
	}

}