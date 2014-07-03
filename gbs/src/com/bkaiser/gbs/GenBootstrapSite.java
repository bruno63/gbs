package com.bkaiser.gbs;
import java.io.*;

import org.jdom2.*;
import org.jdom2.input.SAXBuilder;

/**
 * Static entry point (main routine) to generate a static Twitter Bootstrap website based
 * on raw content material (such as images) and a site-template.
 * 
 * @author Bruno Kaiser
 */
public class GenBootstrapSite {
//	private static final String CN = "GenBootstrapSite";
	private static GbsConfig cfg = null;

	/**
	 * Static entry point of the program (main function). It instantiates a GenBootstrapSite object,
	 * loads the site template file and generates the site according to the directives in the template
	 * file.
	 *
	 * @param args	the command line parameters (not used).
	 * @throws IOException 
	 * @throws JDOMException 
	 */
	public static void main(String[] args) throws JDOMException, IOException {
		try {
			cfg = new GbsConfig();

			if (cfg.isDebugMode()) {  
				cfg.dumpConfig();
			}

			GbsSite _site = new GbsSite(
					loadSiteDescription(cfg.getSiteTemplateFile()), cfg.getSourceDir(), cfg.getDestDir());
			
			_site.createTemplate();
			// generate all pages
			_site.generatePages();

			System.out.println("****** completed successfully **********"); 
		}
		catch (Exception _ex) {
			System.out.println("***** failed with " + _ex.toString() + "**********" );
			if (cfg.isDebugMode()) {
				_ex.printStackTrace();
			}
		}
	}

	private static Element loadSiteDescription(File f) throws JDOMException, IOException {
		return (Element) new SAXBuilder().build(f).getRootElement().detach();
	}
}