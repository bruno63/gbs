package com.bkaiser.gbs;
import org.jdom2.*;

/**
 * Utility to generate a JDOM elements representing HTML5 Bootstrap tables.
 * 
 * @author Bruno Kaiser
 */
public class GbsTable extends GbsFactory  {
//	private static final String CN = "GbsBootstrapTable";
	Element tableEl = null;
	Element rootEl = null;
	Element bodyEl = null;
	int numberOfRows = 0;
	int numberOfColumns = 0;
	TableContextColor rowContext = TableContextColor.DEFAULT;
	TableContextColor fieldContext = TableContextColor.DEFAULT;


	/**
	 * Constructor.
	 * 
	 */
	public GbsTable(boolean responsive)  {
		tableEl = new Element("table", ns).setAttribute("class", getTableTypeAttr(TableType.DEFAULT));
		
		if (responsive == true) {
			rootEl = getDivElement("table-responsive", null).addContent(tableEl);
		}
		else {
			rootEl = tableEl;
		}	
	}
	
	public void addTableType(TableType type) 
	{
		addClassAttribute(tableEl, getTableTypeAttr(type));
	}
	
	/* 
	 * basic:   		<table class="table">
	 * striped rows:   	<table class="table table-striped">
	 * bordered:      	<table class="table table-bordered">
	 * hover rows:    	<table class="table table-hover">
	 * condensed:     	<table class="table table-condensed">
	 * contextual classes to color rows/tr or cells/td:  class=active, success, warning, danger, info
	 * responsive:  make table scroll horizontally up to small devices under 768px:
	 *       <div class="table-responsive"><table class="table">
	 */
	private String getTableTypeAttr(TableType type)
	{
		String _classAttr = null;
		switch (type) {
		case STRIPED:		_classAttr = "table-striped"; break;	
		case BORDERED:		_classAttr = "table-bordered"; break;	
		case HOVER:			_classAttr = "table-hover"; break;	
		case CONDENSED:		_classAttr = "table-condensed"; break;	
		default:			_classAttr = "table"; break;	
		}
		return _classAttr;
	}
	
	public void addHeader(String[] headerFields)
	{
		// setHeader -> <thead><tr><th>
		numberOfColumns = headerFields.length;
		Element _trEl = new Element("tr", ns);
		for (int i = 0; i < numberOfColumns; i++) {
			_trEl.addContent(getField(headerFields[i], true));
		}
		tableEl.addContent(new Element("thead", ns).addContent(_trEl));
	}
	
	// setBody -> <tbody><tr><td>
	// keep internal representation in jdom directly, add data row-by-row
	public void addBodyRow(String[] fields, boolean isRowContexted)
	{
		Element _trEl = new Element("tr", ns);
		if (isRowContexted == true) {
			addContextClass(_trEl, rowContext);
		}
		Element _tdEl = null;
		String _fieldContent = null;
		boolean _defineFieldContext = false;

		for (int i = 0; i < fields.length; i++) {
			if (fields[i].startsWith("@CTX:")) {
				_fieldContent = fields[i].substring(5);
				_defineFieldContext = true;
			}
			else {
				_fieldContent = fields[i];
			}
			if (_fieldContent.startsWith("@DWN:")) {
				_tdEl = new Element("td", ns).addContent(
						getLinkElement(_fieldContent.substring(5), "", null).addContent(
						GbsGlyph.getGlyph(Glyphicon.CLOUDDOWNLOAD)));
			}
			else if (_fieldContent.startsWith("@SPAN:")) {
				char _colspan = _fieldContent.charAt(6);
				_tdEl = new Element("td", ns)
						.setAttribute("colspan", new Character(_colspan).toString())
						.addContent(_fieldContent.substring(7));
			}
			else {
				_tdEl = getField(_fieldContent, false);
			}
			if (_defineFieldContext == true) {
				addContextClass(_tdEl, fieldContext);
			}
			_trEl.addContent(_tdEl);
		}
		if (bodyEl == null) {
			bodyEl = new Element("tbody", ns);
			tableEl.addContent(bodyEl);
		}
		bodyEl.addContent(_trEl);
		numberOfColumns++;
	}
	
	public void setRowContext(TableContextColor color)
	{
		rowContext = color;
	}
	
	public void setFieldContext(TableContextColor color)
	{
		fieldContext = color;	
	}
	
	private Element getField(String fieldContent, boolean isHeaderField)
	{
		String _elName = isHeaderField == true ? "th" : "td";
		return new Element(_elName).addContent(fieldContent);
	}
	
	private void addContextClass(Element rowOrFieldElement, TableContextColor color) 
	{
		String _classAttr = null;
		switch (color) {
		case ACTIVE:		_classAttr = "active"; 	break;	
		case SUCCESS:		_classAttr = "success"; break;	
		case WARNING:		_classAttr = "warning"; break;	
		case DANGER:		_classAttr = "danger"; 	break;	
		case INFO:			_classAttr = "info"; 	break;	
		default:			break;	  // add nothing
		}
		if (_classAttr != null) {
			rowOrFieldElement.setAttribute("class", _classAttr);
		}
	}
	
	public Element getTable()
	{
		return rootEl;
	}
}
