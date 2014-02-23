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
public class GbsBootstrapDocList extends GbsBootstrapFactory {
	private static final String CN = "GbsBootstrapDocList";

	/**
	 * Constructor.
	 * 
	 */
	public GbsBootstrapDocList(Element htmlEl, File sourceDir, File destinationDir)  {
		destHtmlEl = htmlEl;
		srcDir = sourceDir;
		destDir = destinationDir;
	}

	public void savePage(Element nodeEl) throws JDOMException, IOException 
	{
        // <node name="menuentry" type="doclist" resDir="dirName" url="pageName.html">
 		
		Element _headEl = destHtmlEl.getChild("head", ns);
		_headEl.addContent(new Element("title", ns).addContent(nodeEl.getAttributeValue("name") + " DocList"));

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

		// select all files in resDir and build a String[][] table of file attributes
		File _srcDocDir = new File(srcDir, nodeEl.getAttributeValue("resDir"));
		File _destDocDir = new File(destDir, nodeEl.getAttributeValue("resDir"));
		File[] _files = selectFiles(_srcDocDir, "pdf");
		
		// prepare the table with header fields
		String[] _fileAttrs = { "Dateiname", "Groesse", "Link" };
		GbsBootstrapTable _gbsTable = new GbsBootstrapTable(true);
		_gbsTable.addTableType(TableType.HOVER);
		_gbsTable.addTableType(TableType.STRIPED);
		_gbsTable.addHeader(_fileAttrs);
		_gbsTable.setRowContext(TableContextColor.INFO);
		_gbsTable.setFieldContext(TableContextColor.SUCCESS);
		
		// fill one row for each file found
		for (int i = 0; i < _files.length; i++) {
			// move the resources into the destination directory
			copyFile(_srcDocDir, _destDocDir, _files[i].getName());
			
			// collect all file attributes into a row with fields filename and filesize per row
			_fileAttrs[0] = _files[i].getName();
			_fileAttrs[1] = new Long(_files[i].length() / 1024).toString() + " KB";
			_fileAttrs[2] = "@DWN:" + nodeEl.getAttributeValue("resDir") + File.separator + _files[i].getName();
			
			// add the row to the table
			_gbsTable.addBodyRow(_fileAttrs, true);
		}

		_rowEl.setAttribute("class", "row-fluid col-sm-6 col-sm-offset-3");

		// generate the table element
		_rowEl.addContent(_gbsTable.getTable());
		
		// add all other content from site template
		List<Element> _nodeElems = nodeEl.getChildren();
		for (int i = 0; i < _nodeElems.size(); i++) {
			_rowEl.addContent((Element) _nodeElems.get(i).clone().setNamespace(ns)); 
		}
				
		_bodyEl.addContent(getScriptElement("js/jquery.min.js", null));
		_bodyEl.addContent(getScriptElement("js/bootstrap.min.js", null));

		// save the page
		new GbsXmlExport().write(destHtmlEl, new File(destDir, nodeEl.getAttributeValue("url")));
	}

}