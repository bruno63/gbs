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
public class GbsDocList extends GbsPage {
//	private static final String CN = "GbsDocList";
	File[] files = null;
	GbsTemplate templatePage = null;

	/**
	 * Constructor.
	 * 
	 */
	public GbsDocList(GbsTemplate template, File[] srcFiles)  {
		templatePage = template;
		files = srcFiles;
	}

	public Element preparePage(Element nodeEl) throws JDOMException, IOException 
	{
        // <node name="menuentry" type="doclist" resDir="dirName" url="pageName.html">
		pageContent = templatePage.preparePage(nodeEl);
		templatePage.addTitle(nodeEl.getAttributeValue("name") + "DocList");
		templatePage.addStylesheet("css/bootstrap.min.css");
		templatePage.addStylesheet("css/custom.css");
		
		// prepare the table with header fields
		String[] _fileAttrs = { "Dateiname", "Groesse", "Link" };
		GbsTable _gbsTable = new GbsTable(true);
		_gbsTable.addTableType(TableType.HOVER);
		_gbsTable.addTableType(TableType.STRIPED);
		_gbsTable.addHeader(_fileAttrs);
		_gbsTable.setRowContext(TableContextColor.INFO);
		_gbsTable.setFieldContext(TableContextColor.SUCCESS);
		
		// fill one row for each file found
		for (int i = 0; i < files.length; i++) {			
			// collect all file attributes into a row with fields filename and filesize per row
			_fileAttrs[0] = files[i].getName();
			_fileAttrs[1] = new Long(files[i].length() / 1024).toString() + " KB";
			_fileAttrs[2] = "@DWN:" + nodeEl.getAttributeValue("resDir") + File.separator + files[i].getName();
			
			// add the row to the table
			_gbsTable.addBodyRow(_fileAttrs, true);
		}

		// generate the table element
		templatePage.addContent(getDivElement("row-fluid col-sm-6 col-sm-offset-3", null).addContent(_gbsTable.getTable()));
		
		// add all other content from site template
		List<Element> _nodeElems = nodeEl.getChildren();
		for (int i = 0; i < _nodeElems.size(); i++) {
			templatePage.addContent((Element) _nodeElems.get(i).clone().setNamespace(ns)); 
		}
				
		templatePage.addScriptLink("js/jquery.min.js");
		templatePage.addScriptLink("js/bootstrap.min.js");
		
		return pageContent;
	}

}