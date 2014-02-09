package com.bkaiser.gbs;

/**
 * Fatal Exception. Program should stop after this exception.
 *
 * @author <a href="mailto:bruno@bkaiser.ch">Bruno Kaiser</a>
 */
public class GbsFatalException extends java.lang.RuntimeException
{
	/**
	 * serialVersionUID is needed by Interface Serializable
	 */
	private static final long serialVersionUID = 1041468694571312573L;
	
	/**
	 * Constructor
	 *
	 * @param msg the error message
	 */
	public GbsFatalException(String msg)
	{
		super(msg);
	}
}

