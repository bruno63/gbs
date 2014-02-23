package com.bkaiser.gbs;
import java.io.*;
import java.util.List;

import org.jdom2.*;

/**
 * Utility to generate a static Twitter Bootstrap website based
 * on raw content material (such as images) and a site-template.
 * 
 * @author Bruno Kaiser
 */
public class GbsBootstrapGallery extends GbsBootstrapFactory  {
	private static final String CN = "GbsBootstrapGallery";

	/**
	 * Constructor.
	 * 
	 */
	public GbsBootstrapGallery(Element htmlEl, File sourceDir, File destinationDir)  {
		destHtmlEl = htmlEl;
		srcDir = sourceDir;
		destDir = destinationDir;
	}

	// <node name="NAME" url="FILENAME.html" type="gallery" resDir="DESTDIRNAME" />
	public void savePage(Element nodeEl) throws IOException 
	{	
		Element _bodyEl = destHtmlEl.getChild("body", ns);
		String _name = nodeEl.getAttributeValue("name");
		String _resDir = nodeEl.getAttributeValue("resDir");
		
		Element _headEl = destHtmlEl.getChild("head", ns);
		_headEl.addContent(new Element("title", ns).addContent(_name + " Gallery"));

		// add the necessary css stylesheets
		_headEl.addContent(getStylesheetElement("css/bootstrap.min.css"));
		_headEl.addContent(getStylesheetElement("css/prettyPhoto.css"));
		_headEl.addContent(getStylesheetElement("css/custom.css"));
		
		if (_bodyEl == null) {
			GbsLogUtil.throwFatal(CN, "savePage", "type mismatch in destHtmlEl; body-element not found");
		}	
		List<Element> _scriptElems = _bodyEl.getChildren("script", ns);
		for (int i = 0; i > _scriptElems.size(); i++) {
			_scriptElems.get(i).detach();
		}

		Element _containerEl = _bodyEl.getChild("div", ns);
		if (_containerEl == null | ! _containerEl.getAttributeValue("class").toLowerCase().startsWith("container")) {
			GbsLogUtil.throwFatal(CN, "savePage", "type mismatch in destHtmlEl; div-element container not found");
		}
		
		Element _linkEl = getDivElement(null, "links");
		List<Element> _divElems = _containerEl.getChildren("div", ns);
		Element _contentRowEl = null;
		for (int i = 0; i < _divElems.size(); i++) {
			if (_divElems.get(i).getAttributeValue("id").equalsIgnoreCase("content")) {
				_contentRowEl = _divElems.get(i);
				break;
			}
		}
		if (_contentRowEl != null) {
			_contentRowEl.addContent(
					getBodyElement(_name, false, TextAlignment.CENTER, 
							TextEmphasis.H1, TextColor.INFO, TextColor.DEFAULT));
			_contentRowEl.addContent(_linkEl);
		}
		else {
			_containerEl.addContent(
					getBodyElement(_name, false, TextAlignment.CENTER, 
							TextEmphasis.H1, TextColor.INFO, TextColor.DEFAULT));
			_containerEl.addContent(_linkEl);			
		}
		
		destDir.mkdir();
		File _destThumbDir = new File(destDir, _resDir + File.separator + "thumbs");
		_destThumbDir.mkdirs();
		File[] _images = selectFiles(new File(srcDir, _resDir), ".jpg"); // select all jpg files
		Element _imgEl = null;
		Element _colEl = null;
		File _slideImg = null;
		File _thumbImg = null;
		// for each image in resDir...
		for (int i = 0; i < _images.length; i++) {
			_slideImg = new File(destDir, _resDir + File.separator + _images[i].getName());
			_thumbImg = new File(_destThumbDir, _images[i].getName());
			
			// generate slide image
			if (! _slideImg.exists()) {
				// TODO: only scale if the images is larger than a certain size
				generateSlideImage(_images[i], _slideImg, 0.5f); 
			}
			
			// generate thumbnail image
			if (! _thumbImg.exists()) {
				generateThumbnailImage(_images[i], _thumbImg, 200, 200); 
			}

			// generate link entry element
			_imgEl = getImageElement(
					_resDir + File.separator + "thumbs" + File.separator + _images[i].getName(), 
					_name, ImageShapes.DEFAULT, true);
			_colEl = getDivElement("col-xs-3 thumb", null);
			_colEl.addContent(new Element("a", ns)
				.setAttribute("href", _resDir + File.separator + _images[i].getName())
				.setAttribute("title", _name)
				.setAttribute("class", "thumbnail")
				.setAttribute("rel", "prettyPhoto[Sommer]")
				.addContent(_imgEl));
			_linkEl.addContent(_colEl);
		}
		
		// javascript scripts
		for (int i = 0; i > _scriptElems.size(); i++) {
			_bodyEl.addContent(_scriptElems.get(i));
		}
		_bodyEl.addContent(getScriptElement("js/jquery.min.js", null));
		_bodyEl.addContent(getScriptElement("js/bootstrap.min.js", null));
		_bodyEl.addContent(getScriptElement("js/jquery.prettyPhoto.js", null));
		_bodyEl.addContent(getScriptElement(null, 
				"$(document).ready(function(){$(\"a[rel^='prettyPhoto']\").prettyPhoto({theme:'pp_default',slideshow:3000,autoplay_slideshow:true,social_tools:false});});"));
		
		// save the page
		new GbsXmlExport().write(destHtmlEl, new File(destDir, nodeEl.getAttributeValue("url")));

	}
}