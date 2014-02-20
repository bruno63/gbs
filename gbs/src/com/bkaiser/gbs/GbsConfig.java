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
	private static final String CN = "GbsConfig";
	
	private static boolean debugMode = false;
	private File srcDir = null;
	private File destDir = null;
	private File templateF = null;

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
		
		// read all config attributes
		debugMode = safeReadBooleanProperty(_props, "debugMode", debugMode);
		srcDir = new File(safeReadStringProperty(_props, "srcDirName", ".")).getCanonicalFile();
		destDir = new File(safeReadStringProperty(_props, "destDirName", ".")).getCanonicalFile();
		templateF = new File(safeReadStringProperty(_props, "siteTemplateName", "./siteTemplate.xml")).getCanonicalFile();
	}
			
	/**
	 * @return the debugMode
	 */
	public boolean isDebugMode() {
		return debugMode;
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
	 * Dumps the config onto stdout.
	 */
	public void dumpConfig() {
		System.out.println("debugMode=" + debugMode);
		System.out.println("sourceDir=" + getSourceDir().getAbsolutePath());
		System.out.println("siteTemplateFile=" + getSiteTemplateFile().getAbsolutePath());
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
}