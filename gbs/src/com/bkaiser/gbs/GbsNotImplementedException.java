package com.bkaiser.gbs;

/**
 * Not Implemented Exception.
 *
 * @author <a href="mailto:bruno@bkaiser.ch">Bruno Kaiser</a>
 */
public class GbsNotImplementedException extends java.lang.RuntimeException
{

	/**
	 * Compiler generated version id (needed by Serializable)
	 */
	private static final long serialVersionUID = 3561842631726690454L;

	/**
	 * Constructor
	 *
	 * @param msg the error message
	 */
	public GbsNotImplementedException(String msg)
	{
		super(msg);
	}
}

