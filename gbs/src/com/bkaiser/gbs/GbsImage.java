package com.bkaiser.gbs;

import org.jdom2.*;

/**
 * Generate JDOM elements representing HTML5 Bootstrap Images. 
 * 
 * @author Bruno Kaiser
 */
public class GbsImage extends GbsFactory  {
//	private static final String CN = "GbsImage";
	String imageRelPath = null;
	String altDesc = null;
	boolean isResponsive = true;
	String imageType = null;
	String url = null;
	String title = null;
	public String halign = null;
	GbsTooltip tooltip = null;

	/**
	 * Constructor.
	 * <image fname="relative filepath incl. resDir" alt="text" [url] [title] [halign] [imgType]
	 * 
	 */
	public GbsImage(String relPath, String alt)
	{
		imageRelPath = relPath;
		altDesc = alt;
	}
	
	public GbsImage(Element imgEl) {
		imageRelPath = imgEl.getAttributeValue("src");
		altDesc = imgEl.getAttributeValue("alt");
		url = imgEl.getAttributeValue("url");
		title = imgEl.getAttributeValue("title");
		halign = getAlignmentTypeString(imgEl.getAttributeValue("halign"));
		imageType = getImageTypeString(imgEl.getAttributeValue("imgType"));		
	}
	
	public GbsImage(String relPath, String alt, String myurl, String tit, ImageShapes shape)
	{
		imageRelPath = relPath;
		altDesc = alt;
		url = myurl;
		title = tit;
		imageType = getImageTypeString(shape);
	}
	
	public void disableResponsiveness()
	{
		isResponsive = false;
	}
	
	public void setImageLook(ImageShapes type)
	{
		imageType = getImageTypeString(type);
	}
	
	public String getRelativePath()
	{
		return imageRelPath;
	}
	
	
	// todo: add linked image and thumbnail ?
	// <img src="imageRelPath" [alt="altDesc"] [class="imageType [img-responsive]"] /> 
	public Element getImageElement()
	{
		Element _retEl = null;
		Element _imgEl = new Element("img", ns).setAttribute("src", imageRelPath);
		if (halign != null) {
			_retEl = getDivElement(halign, null).addContent(_imgEl);
		}
		else {
			_retEl = _imgEl;
		}

		if (altDesc != null) {
			_imgEl.setAttribute("alt", altDesc);
		}
		else {
			if (title != null) {
				_imgEl.setAttribute("alt", title);
			}
		}
		if (isResponsive == true) {
			addClassAttribute(_imgEl, "img-responsive");
		}
		if (imageType != null) {
			addClassAttribute(_imgEl, imageType);
		}
		if (tooltip != null) {
			_retEl = tooltip.addTooltipAttributes(_retEl);
		}
		return _retEl;
	}
	
	public Element getImageLinkElement(String url, String classAttr)
	{
		Element _aEl = new Element("a", ns).setAttribute("href", url);
		if (classAttr != null) {
			_aEl.setAttribute("class", classAttr);
		}
		return _aEl.addContent(getImageElement());
	}
	
	private static String getImageTypeString(ImageShapes type)
	{
		String _ret = null;
		switch(type) {
		case ROUNDED: 	 _ret = "img-rounded"; break;
		case CIRCLE:	_ret = "img-circle"; break;
		case THUMBNAIL: _ret = "img-thumbnail"; break;
		default:		break;   // keep _ret = null = no type set
		}
		return _ret;
	}
	
	private static String getImageTypeString(String type)
	{
		String _ret = null;
		if (type != null)  {
		if (type.equalsIgnoreCase("rounded")) {
			_ret = "img-rounded"; 
		}
		else if (type.equalsIgnoreCase("circle")) {
			_ret = "img-circle";
		}
		else if (type.equalsIgnoreCase("thumbnail")) {
			_ret = "img-thumbnail";
		}
		}
		return _ret;
	}
	
	public static String getAlignmentTypeString(String align)
	{
		String _ret = null;
		if (align != null) {
			if (align.equalsIgnoreCase("left")) {
				_ret = "pull-left";
			}
			else if (align.equalsIgnoreCase("right")) {
				_ret = "pull-right";
			}
		}
		return _ret;
	}

	public Element getAbstractElement(String buttonText, String colAttr)
	{
		Element _el = getDivElement(colAttr, null);
		if (title != null) {
			_el.addContent(new Element("h3", ns).addContent(title));
		}
		if (url != null) {
			_el.addContent(new Element("p", ns).addContent(getImageLinkElement(url, null)));		
		}
		if (buttonText != null) {
			GbsButton _button = new GbsButton(buttonText);
			_button.setColor(ButtonClass.INFO);
			_button.setSize(ButtonSize.SMALL);
			if (url != null) {
				_el.addContent(new Element("p", ns).addContent(_button.getLinkButton(url)));
			}
		}
		return _el;
	}
	
	// convert: 
	//  <image name="FILENAME_N.gif" text="DESCRIPTION" />
	// to:
	// <div class="item">
	//      <img src="resDir/imageName" alt="text">
	//      <div class="carousel-caption">
	//            <p>Text</p>
	//      </div>
	// </div>
	public Element getCarouselElement(int index)
	{
		Element _itemEl = getDivElement(index == 0 ? "item active" : "item", null);		
		_itemEl.addContent(getImageElement());
		Element _captionEl = getDivElement("carousel-caption", null);
		GbsBody _caption = new GbsBody(altDesc);
		_caption.setSmall();
		_captionEl.addContent(_caption.getBody());
		_itemEl.addContent(_captionEl);
		return _itemEl;
	}
	
	public Element getThumbnailElement(String slideUrl, String colAttr)
	{
		return getDivElement(colAttr == null ? "col-xs-3 thumb": colAttr, null)
				.addContent(getImageLinkElement(slideUrl, "thumbnail")
						.setAttribute("rel", "prettyPhoto[Sommer]"));
	}
	
	public void setTooltip(GbsTooltip tooltip)
	{
		this.tooltip = tooltip;
	}
};