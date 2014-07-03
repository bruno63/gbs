package com.bkaiser.gbs;

import java.io.IOException;
import java.util.List;

import org.jdom2.*;

/**
 * Generate JDOM elements representing HTML5 Bootstrap Jumbotron.
 * A Jumbotron is a lightweight, flexible component that can
 * optionally extend the entire viewport to showcase key content. 
 * 
 * @author Bruno Kaiser
 */
public class GbsJumbotron extends GbsPage  {
	private static final String CN = "GbsJumbotron";
	String id = null;
	boolean isClearFixed = false;
	GbsTemplate templatePage = null;

	/**
	 * Constructor.
	 * 
	 */
	public GbsJumbotron(GbsTemplate template)  {
		templatePage = template;
		setNamespace();
	}

	// clear the float on any element. Utilizes the micro clearfix as 
	// popularized by Nicolas Gallagher. 
	public void setClearFixed()
	{
		isClearFixed = true;
	}
			
	// use this to identify a specific jumbotron. 
	// It can then be addressed by css, e.g. for setting background color.
	// example in css:  #myJumbo { background-color: #59d2e3 }
	public void setId(String myId)
	{
		id = myId;
	}
	
	public Element getJumbotron(Element heroUnit)
	{
		boolean _heroUnitIsDefined = false;
		String _value = isClearFixed == true ? "jumbotron clearfix" : "jumbotron"; 
		Element _rootEl = new Element("div", ns).setAttribute("class", _value);
		if (id != null) {
			_rootEl.setAttribute("id", id);
		}
	//	Element _contentContainerEl = new Element("div", ns).setAttribute("class", "container");

		// add all content
		List<Element> _elems = heroUnit.getChildren();
		for (int i = 0; i < _elems.size(); i++) {
			if (_elems.get(i).getName().equalsIgnoreCase("image")) {
				// todo: handle more than one image -> carousel ?
				if (_heroUnitIsDefined == true) {
					GbsLogUtil.throwNotImplemented(CN, "getJumbotron", "only one image is supported within a hero unit");
				}
				else {
					_heroUnitIsDefined = true;	
				}
				GbsImage _heroImage = new GbsImage(_elems.get(i));
				_rootEl.addContent(_heroImage.getImageElement());
			}
			else {
				_rootEl.addContent(_elems.get(i).clone());
			}
		}
//		return _rootEl.addContent(_contentContainerEl);
		return _rootEl;
	}
	
	public Element preparePage(Element nodeEl) throws JDOMException, IOException 
	{
		// <node name="NAME" type="jumbotron" url="FILENAME.html">
		//      <heroUnit>
		//			<image src="resdir/IMAGENAME.jpg" alt="DESCRIPTION" url="link" halign="right|left" imgType="circle">
		//        	<h1>TITLE</h1>
		//        	<p>TEXT</p>
		//			..
		//		</heroUnit>
		//		<abstractRowGroup>
		//			...
		//		</abstractRowGroup>
		// </node>
		
		pageContent = templatePage.preparePage(nodeEl);
		templatePage.addTitle(nodeEl.getAttributeValue("name"));
		templatePage.addStylesheet("css/bootstrap.min.css");
		templatePage.addStylesheet("css/custom.css");
		
		templatePage.addContent(getJumbotron(nodeEl.getChild("heroUnit")));

		List<Element> _abstractRowGroup = nodeEl.getChildren("abstractRowGroup");
		if (_abstractRowGroup != null) {
			for (int i = 0; i < _abstractRowGroup.size(); i++) {
				templatePage.addContent(getAbstractRowGroup(_abstractRowGroup.get(i)));
			}
		}
		templatePage.addScriptLink("js/jquery.min.js");
		templatePage.addScriptLink("js/bootstrap.min.js");
		
		return pageContent;
	}
	
	public Element getAbstractRowGroup(Element nodeEl) 
	{
		//		<abstractRowGroup>
		//			<abstractRow>
		//				<abstract>
		//					...
		//				</abstract>
		//				...
		//			</abstractRow>
		//			...
		//		</abstractRowGroup>

		Element _abstractRowGroup = getDivElement("container abstractRowGroup", null);
		List<Element> _abstractRows = nodeEl.getChildren("abstractRow");
		if (_abstractRows != null) {
			for (int i = 0; i < _abstractRows.size(); i++) {
				_abstractRowGroup.addContent(getAbstractRow(_abstractRows.get(i)));
				addDivider(_abstractRowGroup, _abstractRows.get(i).getAttributeValue("divider"));
			}
		}
		return _abstractRowGroup;
	}
	
	public Element getAbstractRow(Element nodeEl)
	{
		Element _divEl = getDivElement("container-fluid clearfix", null);
		Element _abstractRow = getDivElement("row-fluid", null);
		_divEl.addContent(_abstractRow);
		List<Element> _abstracts = nodeEl.getChildren("abstract");
		if (_abstracts != null) {
			for (int i = 0; i < _abstracts.size(); i++) {
				_abstractRow.addContent(getAbstract(_abstracts.get(i), _abstracts.size()));
			}
		}
		//return addDivider(_abstractRow, nodeEl.getAttributeValue("divider"));
		return _divEl;
	}
	
	public Element getAbstract(Element abstractEl, int nrCols)
	{
		//		<abstract buttonText="weiter">
		//			<image src="resdir/IMAGENAME.jpg" alt="DESCRIPTION" url="link" halign="right|left" imgType="circle">
		//			<p>test</p>
		//			..
		//		</abstract>
		//		...
		//      all elements (buttonText, image, text) are optional
		Element _divEl = null;
		String _classAttr = abstractEl.getAttributeValue("class");
		if (_classAttr == null) {
			_classAttr = getColumnAttribute(nrCols);
		}
		Element _imgEl = abstractEl.getChild("image", ns);
		if (_imgEl != null) {
			_divEl = new GbsImage(_imgEl).
					getAbstractElement(abstractEl.getAttributeValue("buttonText"), _classAttr);
		}
		else {
			_divEl = getDivElement(_classAttr, null);
		}
		// copy all other content
		return copyContent(abstractEl.getChildren(), _divEl, true);
	}
	
	// this method calculates a default column size if it is not given explicitly in siteTemplate.
	public static String getColumnAttribute(int nrCols) 
	{
		String _colAttr = null;
		switch (nrCols) {
		case 12:	_colAttr = "col-sm-1"; break;
		case 6:		_colAttr = "col-sm-2"; break;
		case 4:		_colAttr = "col-sm-3"; break;
		case 3:		_colAttr = "col-sm-4"; break;
		case 2:		_colAttr = "col-sm-6"; break;
		case 1:	 	break;   // no class attribute is set for the full row
		default:  System.out.println(CN + 
				"/getColumnAttribute(): row contains incorrect amount of abstracts: " + 
				new Integer(nrCols).toString()); break;
		}
		return _colAttr;
	}

	private Element addDivider(Element toEl, String type)
	{
		if (type != null) {
			if (type.equalsIgnoreCase("half")) {
				toEl.addContent(new Element("hr", ns).setAttribute("class", "half-rule clearfix"));
			}
			else if (type.equalsIgnoreCase("full")) {
				toEl.addContent(new Element("hr", ns));
			}
			else {
				GbsLogUtil.warning(CN, "getColumnAttribute", "unknown divider type <" + type +
						">. Should be either 'full' or 'half'.");
			}
		}
		return toEl;
	}
};