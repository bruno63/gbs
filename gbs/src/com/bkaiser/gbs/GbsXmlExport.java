package com.bkaiser.gbs;

import java.io.*;

import org.jdom2.*;
import org.jdom2.output.*;

/**
* Exports a JDOM document into a file in XML format.
* The output can be configured with the following attributes:
* <table>
* <tr><td>Attribute</td><td>description</td><td>default</td></tr>
* <tr><td>destFile</td><td>XML output file</td><td>mandatory</td></tr>
* <tr><td>encoding</td><td>text encoding</td><td>ISO-8859-1</td></tr>
* <tr><td>indent</td><td>indentation of elements</td><td>4 blanks</td></tr>
* <tr><td>lineSeparator</td><td>defines the line separator; msdos | unix | mac</td><td>unix</td></tr>
* <tr><td>style</td><td>output format style: compact | raw | pretty</td><td>pretty</td></tr>
* <tr><td>localDtd</td><td>local DTD for testing purposes</td><td>http://www.w3.org/TR/xhtml1/DTD/</td></tr>
* <tr><td>docType</td><td>XML document type</td><td>xml, strict, transitional...</td></tr>
* </table>
* 
* @author <a href="mailto:bruno@bkaiser.ch">Bruno Kaiser</a>
*/
public class GbsXmlExport
{
	private static final String CN = "GbsXmlExport";
	private GbsConfig cfg = null;
	
	/**
	 * Constructor
	 *
	 * @param config the configuration 
	 */
	public GbsXmlExport(GbsConfig config)
	{
		cfg = config;
	}
		
	/**
	 * Write data into a file.
	 *
	 * @param rootElem JDOM root element of the data
	 * @param destF the file to write the data to
	 */
	public void write(Element rootElem, File destF) {
		GbsLogUtil.checkNull(rootElem, "rootElem");
		GbsLogUtil.checkNull(destF, "destF");
		if (cfg.isDebugMode()) {
			GbsLogUtil.info(CN, "write", "writing file " + destF.getAbsolutePath());
		}
		try {
			// format leads to problems because of removing empty content, e.g. <script ..></script> -> <script .. />
			Format _fmt = Format.getPrettyFormat();
			//_fmt.setIndent(cfg.getIndentationStr());
			//_fmt.setEncoding(cfg.getEncoding());
			//_fmt.setLineSeparator(cfg.getLineSeparator());
			//_fmt.setOmitDeclaration(true); // omit the output of the XML declaration  (<?xml version="1.0"?>)

			XMLOutputter _outp = new XMLOutputter(_fmt);
			
			OutputStream _os = new FileOutputStream(destF);
			_outp.output(new Document((Element) rootElem.detach(), new DocType("html", "-//W3C//DTD XHTML 2.0//EN", "http://www.w3.org/MarkUp/DTD/xhtml2.dtd")), _os);
			_os.close();
		}
		catch (FileNotFoundException _fnf) {
			GbsLogUtil.throwFatal(CN, "write", "file " + 
				destF.getAbsolutePath() + " not found: " + _fnf.toString());
		}
		catch (IOException _ex) {
			GbsLogUtil.throwFatal(CN, "write", _ex.toString());
		}
	}
}

