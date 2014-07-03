package com.bkaiser.gbs;

import org.jdom2.*;

/**
 * Generate JDOM elements representing HTML5 Bootstrap Addresses.
 * Present contact information for the nearest ancestor or the 
 * entire body of work. Preserve formatting by ending all lines with <br>.
 * 
 * @author Bruno Kaiser
 */
public class GbsAddress extends GbsFactory  {
//	private static final String CN = "GbsAddress";
	Element address = null;
	
	/**
	 * Constructor.
	 * 
	 */
	public GbsAddress()  
	{
		address = new Element("address", ns);
	}
	
	public void addAddressLine(String line)
	{
		addBreak();
		address.addContent(line);
	}
	
	public void addStrongAddressLine(String line)
	{
		addBreak();
		address.addContent(new Element("strong", ns).addContent(line));
	}
	
	public void addAddressLine(Element lineEl)
	{
		addBreak();
		address.addContent(lineEl);
	}
	
	public void addPhone(String phoneNr, AddressType type)
	{
		addBreak();	
		address.addContent(new GbsAbbreviation(getAbbreviation(type), getDescription(type)).getAbbreviation());
		address.addContent(phoneNr);
	}
	
	public void addEmail(String emailAddress, AddressType type)
	{
		addBreak();
		address.addContent(new GbsAbbreviation(getAbbreviation(type), getDescription(type)).getAbbreviation());
		address.addContent(getLinkElement("mailto:" + emailAddress, emailAddress, null));
	}
		
	public Element getAddress()
	{
		return address;
	}
	
	private void addBreak()
	{
		if (address.getContentSize() > 0) {
			address.addContent(new Element("br", ns));
		}
	}
	
	private String getDescription(AddressType type)
	{
		String _abbr = "Private";
		switch(type) {
		case MOBILE:	_abbr = "Mobile"; break;
		case BUSINESS:	_abbr = "Business"; break;
		case PRIVATE:
		default: 		break;  // keep default Private
		}
		return _abbr;		
	}
	
	private String getAbbreviation(AddressType type)
	{
		String _abbr = "P:";
		switch(type) {
		case MOBILE:	_abbr = "M:"; break;
		case BUSINESS:	_abbr = "B:"; break;
		case PRIVATE:
		default: 		break;  // keep default P:
		}
		return _abbr;
	}
};
