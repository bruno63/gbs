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
public class GbsBootstrapJumbotron extends GbsBootstrapFactory {
	private static final String CN = "GbsBootstrapJumbotron";

	/**
	 * Constructor.
	 * 
	 */
	public GbsBootstrapJumbotron(Element htmlEl, File sourceDir, File destinationDir)  {
		destHtmlEl = htmlEl;
		srcDir = sourceDir;
		destDir = destinationDir;
	}

	public void generateJumbotron(Element nodeEl) throws JDOMException, IOException 
	{
		// <node name="NAME" type="jumbotron" resDir="DIRNAME" url="FILENAME.html">
		//      <heroUnit image="IMAGENAME.jpg" alt="DESCRIPTION">
		//        		<h1>TITLE</h1>
		//        		<p>TEXT</p>
		//		</heroUnit>
		// </node>

		// move the resources into the destination directory
		Element _heroDescEl = nodeEl.getChild("heroUnit");
		copyFile(new File(srcDir, nodeEl.getAttributeValue("resDir")),
				new File(destDir, nodeEl.getAttributeValue("resDir")), 
				_heroDescEl.getAttributeValue("image"));
		
		Element _headEl = destHtmlEl.getChild("head", ns);
		_headEl.addContent(new Element("title", ns).addContent(nodeEl.getAttributeValue("name")));
		// add the necessary css stylesheets
		_headEl.addContent(generateStylesheetElement("css/bootstrap.min.css"));
		_headEl.addContent(generateStylesheetElement("css/custom.css"));

		Element _bodyEl = destHtmlEl.getChild("body", ns);
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
		Element _divEl = generateDivElement("jumbotron clearfix", null);
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
			Element _abstractRowEl = generateDivElement("row-fluid", null);
			// add all linkUnits ...
			List<Element> _linkUnitElems = _abstractRowDescEl.getChildren("linkUnit");
			for (int i = 0; i < _linkUnitElems.size(); i++) {
				copyFile(new File(srcDir, nodeEl.getAttributeValue("resDir")),
						new File(destDir, nodeEl.getAttributeValue("resDir")), 
						_linkUnitElems.get(i).getAttributeValue("image"));
				_abstractRowEl.addContent(
						generateLinkUnit(
								_linkUnitElems.get(i).getAttributeValue("link"),
								_linkUnitElems.get(i).getAttributeValue("title"),
								_linkUnitElems.get(i).getAttributeValue("image"),
								_linkUnitElems.get(i).getAttributeValue("text"),
								nodeEl.getAttributeValue("resDir"),
								_abstractRowDescEl.getAttributeValue("buttonText")));
			}
			_rowEl.addContent(_abstractRowEl);
		}
		
		_bodyEl.addContent(generateScriptElement("js/jquery.min.js", null));
		_bodyEl.addContent(generateScriptElement("js/bootstrap.min.js", null));


		// save the page
		new GbsXmlExport().write(destHtmlEl, new File(destDir, nodeEl.getAttributeValue("url")));
	}

}