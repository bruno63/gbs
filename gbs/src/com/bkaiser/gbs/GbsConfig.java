package com.bkaiser.gbs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.bkaiser.gbs.GbsLogUtil;

/**
 * Utility to handle the configuration.
 * 
 * @author Bruno Kaiser
 */
public class GbsConfig {
	public static final String[] SEPARATORS = { "msdos", "unix", "mac" };
	public static final String[] STYLES = { "compact", "raw", "pretty" };
	public static final String[] DOCTYPES = { "strict", "transitional", "frameset", "lat1", "symbol", "special"};
	private static final String CN = "GbsConfig";
	
	private static boolean testMode = false;  
	private static boolean debugMode = false;
	private static String srcDirName = ".";
	private static String destDirName = ".";
	private static String siteName = "mySite";
	private static String siteTemplateName = "siteTemplate.xml";
	private static String siteSkeletonName = "siteSkeleton.html";
	private static String pageTemplateName = "pageTemplate.html";
	private File srcDir = null;
	private File destDir = null;
	private File templateF = null;
	private File skeletonF = null;
	private File pageTemplateF = null;
	private String encoding = "UTF-8";
	private String style = "pretty";
	private int indentation = 4;
	private String indentationStr = "    ";
	private String lineSeparator = "unix";

	/**
	 * Initializes the configuration. It uses the defaults defined in this class
	 * and overwrites with properties.
	 * 
	 * @throws IOException
	 */
	public GbsConfig() throws IOException {
		// load default configuration in the project root directory
		Properties _props = new Properties();
		_props.load(new FileInputStream("gbs.properties"));
		
		// set all config attributes
		destDirName = safeReadStringProperty(_props, "destDirName", destDirName);
		srcDirName = safeReadStringProperty(_props, "srcDirName", srcDirName);
		testMode = safeReadBooleanProperty(_props, "testMode", testMode);
		debugMode = safeReadBooleanProperty(_props, "debugMode", debugMode);
		siteName = safeReadStringProperty(_props, "siteName", siteName);
		siteTemplateName = safeReadStringProperty(_props, "siteTemplateName", siteTemplateName);
		siteSkeletonName = safeReadStringProperty(_props, "siteSkeletonName", siteSkeletonName);
		pageTemplateName = safeReadStringProperty(_props, "pageTemplateName", pageTemplateName);
		srcDir = new File(srcDirName).getCanonicalFile();
		destDir = new File(destDirName).getCanonicalFile();
		templateF = new File(srcDir, siteTemplateName).getCanonicalFile();
		skeletonF = new File(srcDir, siteSkeletonName).getCanonicalFile();
		pageTemplateF = new File(destDir, pageTemplateName).getCanonicalFile();
		encoding = safeReadStringProperty(_props, "encoding", encoding);
		style = getEnum(_props, "style", style, STYLES);
		indentation = getInt(_props, "indentation", indentation);
		switch (indentation) {
			case 2: indentationStr = "  "; break;
			case 8: indentationStr = "        "; break;
			default: indentationStr = "    "; break;   // 4
		}
		lineSeparator = getEnum(_props, "lineSeparator", lineSeparator, SEPARATORS);
	}
			
	/**
	 * @return the testMode
	 */
	public boolean isTestMode() {
		return testMode;
	}

	/**
	 * @return the debugMode
	 */
	public boolean isDebugMode() {
		return debugMode;
	}

	/**
	 * @return the destDirName
	 */
	public String getDestDirName() {
		return destDirName;
	}

	/**
	 * @return the siteName
	 */
	public String getSiteName() {
		return siteName;
	}

	/**
	 * @return the srcDir
	 */
	public File getSourceDir() {
		return srcDir;
	}
	
	public File getDestDir() {
		return destDir;
	}

	/** 
	 * Returns the site template file.
	 * @return   the site template file
	 */
	public File getSiteTemplateFile() {
		return templateF;
	}
	
	/** 
	 * Returns the site skeleton file.
	 * @return   the site skeleton file
	 */
	public File getSiteSkeletonFile() {
		return skeletonF;
	}
	
	public File getPageTemplateFile() {
		return pageTemplateF;
	}
	
	/** 
	 * Dumps the config onto stdout.
	 */
	public void dumpConfig() {
		System.out.println("srcDirName=" + srcDirName);
		System.out.println("destDirName=" + destDirName);
		System.out.println("debugMode=" + debugMode);
		System.out.println("testMode=" + debugMode);
		System.out.println("siteName=" + siteName);
		System.out.println("siteTemplateName=" + siteTemplateName);
		System.out.println("siteSkeletonName=" + siteSkeletonName);
		System.out.println("siteTemplateFile=" + templateF.getAbsolutePath());
		System.out.println("siteSkeletonName=" + skeletonF.getAbsolutePath());
		System.out.println("pageTemplateFile=" + pageTemplateF.getAbsolutePath());
		System.out.println("encoding=" + encoding);
		System.out.println("style=" + style);
		System.out.println("indentation=" + indentation);
		System.out.println("lineSeparator=" + lineSeparator);
	}

	/**
	 * Reads a String value from configuration properties safely, i.e.
	 * if the value is not set, the default value is returned instead.
	 * 
	 * @param config		the configuration properties
	 * @param key			the key of the configuration attribute
	 * @param defaultValue  the default value of the configuration attribute
	 * @return              a valid configuration value, either from the properties or the default
	 */
	private static String safeReadStringProperty(Properties config, String key, String defaultValue) {
		String _value = config.getProperty(key);
		if (_value != null) {
			return _value;
		}
		else {
			return defaultValue;
		}
	}
	
	/**
	 * Reads a boolean value from configuration properties safely, i.e.
	 * if the value is not set, the default value is returned instead.
	 * 
	 * @param config		the configuration properties
	 * @param key			the key of the configuration attribute
	 * @param defaultValue  the default value of the configuration attribute
	 * @return              a valid boolean configuration value, either from the properties or the default
	 */
	private static boolean safeReadBooleanProperty(Properties config, String key, boolean defaultValue) {
		String _value = config.getProperty(key);
		if (_value != null) {
			return new Boolean(_value).booleanValue();
		}
		return defaultValue;
	}
	
	public static String getEnum(Properties config, String key, String defaultValue, String[] vals)
	{
		String _ret = null;
		String _buf = safeReadStringProperty(config, key, defaultValue).toLowerCase();
		for (int i = 0; i < vals.length; i++) {
			if (vals[i].equals("any") || _buf.equals(vals[i])) {
				_ret = _buf;
				break;
			}
		}
		if (_ret == null) {
			GbsLogUtil.throwFatal(CN, "getEnum", "key " + key + " is unknown; must be one of " + vals);
		}
		return _ret;
	}
	
	/**
	* Gets the integer value for a parameter based on its key. 
	* If no value was found, the default value is returned.
	* 
	* @param config    the configuration properties
	* @param key the key of the parameter
	* @param defaultValue the default value of the configuration parameter
	* @throws NumberFormatException if the config attribute does not represent an integer type.
	* @return the integer value of the configuration attribute
	*/
	public int getInt(Properties config, String key, int defaultValue)
	{
		return new Integer(safeReadStringProperty(config, key, new Integer(defaultValue).toString())).intValue();
	}

	/**
	 * @return the encoding
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * @return the style
	 */
	public String getStyle() {
		return style;
	}

	/**
	 * @return the indentationStr
	 */
	public String getIndentationStr() {
		return indentationStr;
	}

	/**
	 * @return the lineSeparator
	 */
	public String getLineSeparator() {
		return encodeLineSeparator(lineSeparator);
	}
	
	/**
	 * Set line separator (mac, unix, dos).
	 * 
	 * @param sep the line separator descriptor, i.e. mac | unix | dos
	 * @return the encoded line separator, i.e. \r\n | \r | \n
	 */
	private String encodeLineSeparator(String sep)
	{
		String _retSep = "\n";
		if (sep.equalsIgnoreCase("msdos")) {
			_retSep = "\r\n";
		}
		else if (sep.equalsIgnoreCase("mac")) {
			_retSep = "\r";
		}
		return _retSep;
	}
}