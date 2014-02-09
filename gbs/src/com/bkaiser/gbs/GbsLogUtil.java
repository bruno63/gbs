package com.bkaiser.gbs;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.*;

/**
 * Provides Log-related helper functions.
 * All functions are stateless and static.
 *
 * @author <a href="mailto:bruno@bkaiser.ch">Bruno Kaiser</a>
 */
public class GbsLogUtil
{
	private static final String CN = "GbsLogUtil"; 		// class name
	public static Logger logger = null; // initialized by main()

	/**
	 * Checks whether an object is null and prints out a nice error message.
	 * This is better than waiting for a NullPointerException to happen,
	 * because it easies the debugging.
	 *
	 * @param obj the object to check on null
	 * @param name the object name to use in the trace log
	 * @return the object if it is not null
	 */
	public static Object checkNull(Object obj, String name)
	{
        if (obj == null) {
            throwFatal(CN, "checkNull", name + " is null");
        }
        return(obj);
	}

	/**
	 * Write a formatted log message with log level INFO.
	 * 
	 * @param clazz the classname 
	 * @param meth the methodname
	 * @param msg the message
	 */
	public static void info(String clazz, String meth, String msg) {
		if (logger != null) {
			logger.info(clazz + "." + meth + ": " + msg);
		}
	}

	/**
	 * Write a formatted log message with log level WARNING.
	 * 
	 * @param clazz the classname 
	 * @param meth the methodname
	 * @param msg the message
	 */
	public static void warning(String clazz, String meth, String msg) {
		if (logger != null) {
			logger.warning(clazz + "." + meth + ": " + msg);
		}
	}

	/**
	 * Write a formatted log message with log level THROW, and 
	 * throw a GbsFatalException. Such an exception will terminate
	 * the program.
	 * 
	 * @param clazz the classname 
	 * @param method the methodname
	 * @param msg the message
	 */
	public static void throwFatal(String clazz, String method, String msg)
	{
		RuntimeException _fatal = new GbsFatalException(msg);
		if (logger != null) {
			logger.throwing(clazz, method, _fatal);
		}
		throw _fatal;
	}

	/**
	 * Write a formatted log message with log level THROW, and
	 * throw a GbsNotImplementedException. 
	 */
	public static void throwNotImplemented(String clazz, String method, String msg)
	{
		if (msg == null) {
			msg = "method " + clazz + "." + method + "() is not implemented";
		}
		RuntimeException _noi = new GbsNotImplementedException(msg);
		if (logger != null) {
			logger.throwing(clazz, method, _noi);
		}
		throw _noi;
	}

	public static void copyFile(File inputF, File outputF) throws IOException {

		// create all parent directories if they do not already exist
	    outputF.getParentFile().mkdirs();
		
		FileReader in = new FileReader(inputF);
	    FileWriter out = new FileWriter(outputF);
	    int c;

	    // copy the file byte-wise
	    while ((c = in.read()) != -1)
	      out.write(c);

	    in.close();
	    out.close();
	}
}

