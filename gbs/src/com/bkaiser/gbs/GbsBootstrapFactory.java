package com.bkaiser.gbs;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import org.jdom2.*;

import static java.nio.file.StandardCopyOption.*;

/**
 * Abstract class representing a factory that generates Twitter Bootstrap
 * Elements in JDOM format.
 * Contains static methods the generate simple html constructs.
 * Concrete subclasses implement some more complex bootstrap constructs.
 * 
 * @author Bruno Kaiser
 */
public abstract class GbsBootstrapFactory {
	private static final String CN = "GbsBootstrapFactory";
	protected Element destHtmlEl = null;   // the root html element of the page template 
	protected File srcDir = null;
	protected File destDir = null;
	protected static Namespace ns = null;
	
	public static Element getPageSkeleton() {
		Element _htmlEl = new Element("html", ns);
		Element _headEl = new Element("head", ns);
		Element _metaEl = new Element("meta", ns)
			.setAttribute("name", "viewport")
			.setAttribute("content", "width=device-width, initial-scale=1.0");
		_htmlEl.addContent(_headEl.addContent(_metaEl));
		Element _containerEl = getDivElement("container-fluid", null);
		_containerEl.addContent(getDivElement("row-fluid", "header"));
		_containerEl.addContent(getDivElement("row-fluid", "content"));
		_htmlEl.addContent(new Element("body", ns).addContent(_containerEl));
		return _htmlEl;
	}
	
	/**
	 * Generates a JDOM element representing a body text.
	 * Bootstrap's global default font-size is 14px, with a line-height of 1.428. 
	 * This is applied to the <body> and all paragraphs. In addition, <p> (paragraphs) 
	 * receive a bottom margin of half their computed line-height (10px by default).
	 * Can also be used for generating heading elements (H1, H2, H3, H4)
	 * 
	 * @param text the body text
	 * @param lead set this to true in order to make a paragraph stand out. Default is false.
	 * @param alignment possible values are: text-left (default), text-center, text-right, text-justify
	 * @param emphasis possible values are: p (default), small, strong, em
	 * @param textColor the color of the text
	 * @param backgroundColor the background color of the contextual element
	 */

	public static Element getBodyElement(String text, boolean lead, 
			TextAlignment alignment, 
			TextEmphasis emphasis,
			TextColor textColor,
			TextColor backgroundColor
			)	
	{
		String _elName = "p";
		switch (emphasis) {
		case SMALL: 	_elName = "small"; 	break;
		case STRONG: 	_elName = "strong"; break;
		case EM:		_elName = "em"; 	break;
		case H1:		_elName = "h1"; 	break;
		case H2:		_elName = "h2"; 	break;
		case H3:		_elName = "h3"; 	break;
		case H4:		_elName = "h4"; 	break;
		default:		break;  // keep 'p'
		}
		
		String _alignment = "text-left";
		switch (alignment) {
		case CENTER:	_alignment = "text-center"; 	break;
		case RIGHT:		_alignment = "text-right";		break;
		case JUSTIFY:	_alignment = "text-justify"; 	break;
		default:		break;	// keep 'text-left'
		}
		
		switch (textColor) {
		case MUTED:		_alignment = _alignment + " text-muted"; 	break;
		case PRIMARY:	_alignment = _alignment + " text-primary"; 	break;
		case SUCCESS:	_alignment = _alignment + " text-success"; 	break;
		case INFO:		_alignment = _alignment + " text-info"; 	break;
		case WARNING:	_alignment = _alignment + " text-warning"; 	break;
		case DANGER:	_alignment = _alignment + " text-danger"; 	break;
		default:	break;  // keep default, i.e. no text color
		}
		switch (backgroundColor) {
		case PRIMARY:	_alignment = _alignment + " bg-primary"; 	break;
		case SUCCESS:	_alignment = _alignment + " bg-success"; 	break;
		case INFO:		_alignment = _alignment + " bg-info"; 		break;
		case WARNING:	_alignment = _alignment + " bg-warning"; 	break;
		case DANGER:	_alignment = _alignment + " bg-danger"; 	break;
		default:	break;  // keep default, i.e. no text color		
		}
		if (lead == true) {
			_alignment = _alignment + " lead";
		}
		return new Element(_elName, ns).setAttribute("class", _alignment).addContent(text);
	}
	
	public static Element getBodyElement(String text)
	{
		return getBodyElement(text, false, 
				TextAlignment.LEFT, 
				TextEmphasis.DEFAULT,
				TextColor.DEFAULT,
				TextColor.DEFAULT);
	}
	

	public static Element getCloseIcon()
	{
		return new Element("button", ns)
			.setAttribute("type", "button")
			.setAttribute("class", "close")
			.setAttribute("aria-hidden", "true")
			.addContent("&times;");
	}
	
	public static Element getCaret()
	{
		return new Element("span", ns).setAttribute("class", "caret");
	}
	
	/*
	 * For performance reasons, all icons require a base class and individual icon class. 
	 * To use, place the following code just about anywhere. Be sure to leave a space 
	 * between the icon and text for proper padding.
	 * Icon classes cannot be directly combined with other components. 
	 * They should not be used along with other classes on the same element. 
	 * Instead, add a nested <span> and apply the icon classes to the <span>.
	 * Use them in buttons, button groups for a toolbar, navigation, or prepended form inputs
	 */
	public static Element getGlyph(Glyphicon glyph)
	{
		String _ga = null;
		switch(glyph) {
		case ASTERISK: _ga = "asterisk"; break;
		case PLUS: _ga = "plus"; break;
		case EURO: _ga = "euro"; break;
		case MINUS: _ga = "minus"; break;
		case CLOUD: _ga = "cloud"; break;
		case ENVELOPE: _ga = "envelope"; break;
		case PENCIL: _ga = "pencil"; break;
		case GLASS: _ga = "glass"; break;
		case MUSIC: _ga = "music"; break;
		case SEARCH: _ga = "search"; break;
		case HEART: _ga = "heart"; break;
		case STAR: _ga = "star"; break;
		case STAREMPTY: _ga = "star-empty"; break;
		case USER: _ga = "user"; break;
		case FILM: _ga = "film"; break;
		case THLARGE: _ga = "th-large"; break;
		case TH: _ga = "th"; break;
		case THLIST: _ga = "th-list"; break;
		case OK: _ga = "ok"; break;
		case REMOVE: _ga = "remove"; break;
		case ZOOMIN: _ga = "zoom-in"; break;
		case ZOOMOUT: _ga = "zoom-out"; break;
		case OFF: _ga = "off"; break;
		case SIGNAL: _ga = "signal"; break;
		case COG: _ga = "cog"; break;
		case TRASH: _ga = "trash"; break;
		case HOME: _ga = "home"; break;
		case FILE: _ga = "file"; break;
		case TIME: _ga = "time"; break;
		case ROAD: _ga = "road"; break;
		case DOWNLOADALT: _ga = "download-alt"; break;
		case DOWNLOAD: _ga = "download"; break;
		case UPLOAD: _ga = "upload"; break;
		case INBOX: _ga = "inbox"; break;
		case CIRCLE: _ga = "play-circle"; break;
		case REPEAT: _ga = "repeat"; break;
		case REFRESH: _ga = "refresh"; break;
		case LISTALT: _ga = "list-alt"; break;
		case LOCK: _ga = "lock"; break;
		case FLAG: _ga = "flag"; break;
		case HEADPHONES: _ga = "headphones"; break;
		case VOLUMEOFF: _ga = "volume-off"; break;
		case VOLUMEDOWN: _ga = "volume-down"; break;
		case VOLUMEUP: _ga = "volume-up"; break;
		case QRCODE: _ga = "qrcode"; break;
		case BARCODE: _ga = "barcode"; break;
		case TAG: _ga = "tag"; break;
		case TAGS: _ga = "tags"; break;
		case BOOK: _ga = "book"; break;
		case BOOKMARK: _ga = "bookmark"; break;
		case PRINT: _ga = "print"; break;
		case CAMERA: _ga = "camera"; break;
		case FONT: _ga = "font"; break;
		case BOLD: _ga = "bold"; break;
		case ITALIC: _ga = "italic"; break;
		case TEXTHEIGHT: _ga = "text-height"; break;
		case TEXTWIDTH: _ga = "text-width"; break;
		case ALIGNLEFT: _ga = "align-left"; break;
		case ALIGNCENTER: _ga = "align-center"; break;
		case ALIGNRIGHT: _ga = "align-right"; break;
		case ALIGNJUSTIFY: _ga = "align-justify"; break;
		case LIST: _ga = "list"; break;
		case INDENTLEFT: _ga = "indent-left"; break;
		case INDENTRIGHT: _ga = "indent-right"; break;
		case FACETIMEVIDEO: _ga = "facetime-video"; break;
		case PICTURE: _ga = "picture"; break;
		case MAPMARKER: _ga = "map-marker"; break;
		case ADJUST: _ga = "adjust"; break;
		case TINT: _ga = "tint"; break;
		case EDIT: _ga = "edit"; break;
		case SHARE: _ga = "share"; break;
		case CHECK: _ga = "check"; break;
		case MOVE: _ga = "move"; break;
		case STEPBACKWARD: _ga = "step-backward"; break;
		case FASTBACKWARD: _ga = "fast-backward"; break;
		case BACKWARD: _ga = "backward"; break;
		case PLAY: _ga = "play"; break;
		case PAUSE: _ga = "pause"; break;
		case STOP: _ga = "stop"; break;
		case FORWARD: _ga = "forward"; break;
		case FASTFORWARD: _ga = "fast-forward"; break;
		case STEPFORWARD: _ga = "step-forward"; break;
		case EJECT: _ga = "eject"; break;
		case CHEVRONLEFT: _ga = "chevron-left"; break;
		case CHEVRONRIGHT: _ga = "chevron-right"; break;
		case PLUSSIGN: _ga = "plus-sign"; break;
		case MINUSSIGN: _ga = "minus-sign"; break;
		case REMOVESIGN: _ga = "remove-sign"; break;
		case OKSIGN: _ga = "ok-sign"; break;
		case QUESTIONSIGN: _ga = "question-sign"; break;
		case INFOSIGN: _ga = "info-sign"; break;
		case SCREENSHOT: _ga = "screenshot"; break;
		case REMOVECIRCLE: _ga = "remove-circle"; break;
		case OKCIRCLE: _ga = "ok-circle"; break;
		case BANCIRCLE: _ga = "ban-circle"; break;
		case ARROWLEFT: _ga = "arrow-left"; break;
		case ARROWRIGHT: _ga = "arrow-right"; break;
		case ARROWUP: _ga = "arrow-up"; break;
		case ARROWDOWN: _ga = "arrow-down"; break;
		case SHAREALT: _ga = "share-alt"; break;
		case RESIZEFULL: _ga = "resize-full"; break;
		case RESIZESMALL: _ga = "resize-small"; break;
		case EXCLAMATIONSIGN: _ga = "exclamation-sign"; break;
		case GIFT: _ga = "gift"; break;
		case LEAF: _ga = "leaf"; break;
		case FIRE: _ga = "fire"; break;
		case EYEOPEN: _ga = "eye-open"; break;
		case EYECLOSE: _ga = "eye-close"; break;
		case WARNINGSIGN: _ga = "warning-sign"; break;
		case PLANE: _ga = "plane"; break;
		case CALENDAR: _ga = "calendar"; break;
		case RANDOM: _ga = "random"; break;
		case COMMENT: _ga = "comment"; break;
		case MAGNET: _ga = "magnet"; break;
		case CHEVRONUP: _ga = "chevron-up"; break;
		case CHEVRONDOWN: _ga = "chevron-down"; break;
		case RETWEET: _ga = "retweet"; break;
		case SHOPPINGCART: _ga = "shopping-cart"; break;
		case FOLDERCLOSE: _ga = "folder-close"; break;
		case FOLDEROPEN: _ga = "folder-open"; break;
		case RESIZEVERTICAL: _ga = "resize-vertical"; break;
		case RESIZEHORIZONTAL: _ga = "resize-horizontal"; break;
		case HDD: _ga = "hdd"; break;
		case BULLHORN: _ga = "bullhorn"; break;
		case BELL: _ga = "bell"; break;
		case CERTIFICATE: _ga = "certificate"; break;
		case THUMBSUP: _ga = "thumbs-up"; break;
		case THUMBSDOWN: _ga = "thumbs-down"; break;
		case HANDRIGHT: _ga = "hand-right"; break;
		case HANDLEFT: _ga = "hand-left"; break;
		case HANDUP: _ga = "hand-up"; break;
		case HANDDOWN: _ga = "hand-down"; break;
		case CIRCLEARROWRIGHT: _ga = "circle-arrow-right"; break;
		case CIRCLEARROWLEFT: _ga = "circle-arrow-left"; break;
		case CIRCLEARROWUP: _ga = "circle-arrow-up"; break;
		case CIRCLEARROWDOWN: _ga = "circle-arrow-down"; break;
		case GLOBE: _ga = "globe"; break;
		case WRENCH: _ga = "wrench"; break;
		case TASKS: _ga = "tasks"; break;
		case FILTER: _ga = "filter"; break;
		case BRIEFCASE: _ga = "briefcase"; break;
		case FULLSCREEN: _ga = "fullscreen"; break;
		case DASHBOARD: _ga = "dashboard"; break;
		case PAPERCLIP: _ga = "paperclib"; break;
		case HEARTEMPTY: _ga = "heart-empty"; break;
		case LINK: _ga = "link"; break;
		case PHONE: _ga = "phone"; break;
		case PUSHPIN: _ga = "pushpin"; break;
		case USD: _ga = "usd"; break;
		case GBP: _ga = "gbp"; break;
		case SORT: _ga = "sort"; break;
		case ALPHABET: _ga = "sort-by-alphabet"; break;
		case ALPHABETALT: _ga = "sort-by-alphabet-alt"; break;
		case SORTBYORDER: _ga = "sort-by-order"; break;
		case SORTBYORDERALT: _ga = "sort-by-order-alt"; break;
		case SORTBYATTRS: _ga = "sort-by-attributes"; break;
		case SORTBYATTRSALT: _ga = "sort-by-attributes-alt"; break;
		case UNCHECKED: _ga = "unchecked"; break;
		case EXPAND: _ga = "expand"; break;
		case COLLAPSEDOWN: _ga = "collapse-down"; break;
		case COLLAPSEUP: _ga = "collapse-up"; break;
		case LOGIN: _ga = "log-in"; break;
		case FLASH: _ga = "flash"; break;
		case LOGOUT: _ga = "log-out"; break;
		case NEWWINDOW: _ga = "new-window"; break;
		case RECORD: _ga = "record"; break;
		case SAVE: _ga = "save"; break;
		case OPEN: _ga = "open"; break;
		case SAVED: _ga = "saved"; break;
		case IMPORT: _ga = "import"; break;
		case EXPORT: _ga = "export"; break;
		case SEND: _ga = "send"; break;
		case FLOPPYDISK: _ga = "floppy-disk"; break;
		case FLOPPYSAVED: _ga = "floppy-saved"; break;
		case FLOPPYREMOVE: _ga = "floppy-remove"; break;
		case FLOPPYSAVE: _ga = "floppy-save"; break;
		case FLOPPYOPEN: _ga = "floppy-open"; break;
		case CREDITCARD: _ga = "credit-card"; break;
		case TRANSFER: _ga = "transfer"; break;
		case CUTLERY: _ga = "cutlery"; break;
		case HEADER: _ga = "header"; break;
		case COMPRESSED: _ga = "compressed"; break;
		case EARPHONE: _ga = "earphone"; break;
		case PHONEALT: _ga = "phone-alt"; break;
		case TOWER: _ga = "tower"; break;
		case STATS: _ga = "stats"; break;
		case SDVIDEO: _ga = "sd-video"; break;
		case HDVIDEO: _ga = "hd-video"; break;
		case SUBTITLES: _ga = "subtitles"; break;
		case SOUNDSTEREO: _ga = "sound-stereo"; break;
		case SOUNDDOLBY: _ga = "sound-dolby"; break;
		case SOUND51: _ga = "sound-5-1"; break;
		case SOUND61: _ga = "sound-6-1"; break;
		case SOUND71: _ga = "sound-7-1"; break;
		case COPYRIGHTMARK: _ga = "copyright-mark"; break;
		case REGISTRATIONMARK: _ga = "registration-mark"; break;
		case CLOUDDOWNLOAD: _ga = "cloud-download"; break;
		case CLOUDUPLOAD: _ga = "cloud-upload"; break;
		case TREECONIFER: _ga = "tree-conifer"; break;
		case TREEDECIDUOUS: _ga = "tree-deciduous"; break;
		default: break;
		}
		_ga = "glyphicon glyphicon-" + _ga;
		return new Element("span", ns).setAttribute("class", _ga);
	}
	
	/*
	 * Stylized implementation of HTML's <abbr> element for abbreviations and acronyms 
	 * to show the expanded version on hover. Abbreviations with a title attribute have
	 * a light dotted bottom border and a help cursor on hover, providing additional 
	 * context on hover.
	 * @param abbreviation
	 * @param explanation the explanation of the abbreviation.
	 * @param isInitialism, if this is set to true, a slightly smaller font-size is used.
	 */
	public static Element getAbbreviationElement(String abbreviation, String explanation, boolean isInitialism)
	{
		Element _abbrEl = new Element("abbr", ns)
			.setAttribute("title", explanation)
			.addContent(abbreviation);
		if (isInitialism == true) {
			_abbrEl.setAttribute("class", "initialism");
		}
		return _abbrEl;
	}
	
	/*
	 * TODO: Addresses:
	 * no helper method is available for addresses, because there is no standard form.
	 * examples:
	 * <address>
  			<strong>Twitter, Inc.</strong><br>
  			795 Folsom Ave, Suite 600<br>
  			San Francisco, CA 94107<br>
  			<abbr title="Phone">P:</abbr> (123) 456-7890
		</address>

		<address>
  			<strong>Full Name</strong><br>
  			<a href="mailto:#">first.last@example.com</a>
		</address>
	 */
	
	/*
	 * For quoting blocks of content from another source within your document.
	 * @param quote the blockquote text
	 * @param footer a text identifying the source.
	 * @param reverse if true the content is right-aligned
	 */
	public static Element getQuote(String quote, String footer, boolean reverse)
	{
		Element _pEl = new Element("p", ns).addContent(quote);
		Element _quoteEl = new Element("blockquote", ns).addContent(_pEl);
		if (footer != null) {
			_pEl.addContent(new Element("footer", ns).addContent(footer));
		}
		if (reverse == true) {
			_quoteEl.setAttribute("class", "blockquote-reverse");
		}
		return _quoteEl;	
	}
	
	/* 
	 * Return a JDOM element containing a html list.
	 * Currently, only list of strings of one level depth are supported.
	 * 
	 * BULLETS = unordered:  <ul><li>
	 * NUMBERS = ordered:    <ol><li>
	 * NONE =    unstyled:   <ul class="list-unstyled"><li>
	 * INLINE (horizontal):     <ul class="list-inline"><li>
	 * description: <dl><dt><dd>
	 * horizontal description:  <dl class="dl-horizontal"><dt><dd>
	 */
	public static Element getList(List<String> items, ListType type)
	{
		String _elName = "ul";
		String _classAttr = null;
		switch (type) {
		case NUMBERS:  _elName = "ol"; break;
		case NONE:	   _classAttr = "list-unstyled"; break;
		case INLINE:   _classAttr = "list-inline"; break;
		case BULLETS:  
		default:		break;  // keep default: _elName="ul", _classAttr=null
		}
		Element _listEl = new Element(_elName, ns);
		if (_classAttr != null) {
			_listEl.setAttribute("class", _classAttr);
		}
		// add all list items
		for (int i = 0; i < items.size(); i++) {
			_listEl.addContent(new Element("li", ns).addContent(items.get(i)));
		}
		return _listEl;
	}
	
	/*
	 * A list of terms with their associated descriptions.
	 * description: <dl><dt><dd>
	 * horizontal description:  <dl class="dl-horizontal"><dt><dd>
	 */
	public static Element getDescriptionList(List<String> terms, List<String> descriptions, boolean horizontal)
	{
		Element _listEl = new Element("dl", ns);
		if (horizontal == true) {
			_listEl.setAttribute("class", "dl-horizontal");
		}
		// add all list items
		if (terms.size() != descriptions.size()) {
			throw new GbsFatalException(CN + "/getDescriptionList: size of terms and descriptions is not equal");
		}
		for (int i = 0; i < terms.size(); i++) {
			_listEl.addContent(new Element("dt", ns).addContent(terms.get(i)));
			_listEl.addContent(new Element("dd", ns).addContent(descriptions.get(i)));
		}
		return _listEl;	
	}

	public static Element getFooter(String text, boolean drawSeparatorLine) {
		Element _footerEl = new Element("footer", ns).setAttribute("class", "row-fluid");
		Element _colEl = getDivElement("col-xs-12", null);
		_footerEl.addContent(_colEl);
		if (drawSeparatorLine == true) {
			_colEl.addContent(new Element("hr", ns));
		}
		if (text != null & text.length() > 0) {
			_colEl.addContent(new Element("p", ns).addContent(text));
		}
		else {
			_colEl.addContent(new Element("p", ns).addContent("undefined footer text"));			
		}
		return _footerEl;
	}
	
	/*
	 * TODO: Code is not yet supported:
	 * inline:   <code>code-text</code>
	 * user input:  <kbd>user-input</kbd>
	 * multiple lines of code:   <pre>  (optional class:  pre-scrollable)
	 */
	
	private static String getClassAttribute(ButtonClass bclass, ButtonSize bsize, boolean isActive)
	{
		String _classStr = null;
		String _sizeStr = "";
		String _activeStr = "";

		switch (bclass) {
		case PRIMARY: 	_classStr = "btn btn-primary"; 	break;
		case SUCCESS:	_classStr = "btn btn-success"; 	break;
		case INFO:		_classStr = "btn btn-info"; 	break;
		case WARNING:	_classStr = "btn btn-warning"; 	break;
		case DANGER:	_classStr = "btn btn-danger"; 	break;
		case LINK:		_classStr = "btn btn-link"; 	break;
		default: 		_classStr = "btn default"; 		break;
		}
		
		switch (bsize) {
		case LARGE:		_sizeStr = " btn-lg"; break;
		case SMALL:		_sizeStr = " btn-sm"; break;
		case XSMALL:	_sizeStr = " btn-xs"; break;
		default:		break;
		}
		
		if (isActive == true) {
			_activeStr = " active";
		}
		return(_classStr + _sizeStr + _activeStr);
	}
	/* 
	 * Buttons
	 */
	public static Element getButton(String text, ButtonClass bclass, ButtonSize bsize, boolean isActive)
	{
		return new Element("button", ns)
			.setAttribute("type", "button")
			.setAttribute("class", getClassAttribute(bclass, bsize, isActive))
			.addContent(text);
	}

	// todo: attribute disable="disabled" is not yet supported
	public static Element getLinkButton(String link, String text, ButtonClass bclass, ButtonSize bsize, boolean isActive)
	{
		return new Element("a", ns)
			.setAttribute("href", link)
			.setAttribute("class", getClassAttribute(bclass, bsize, isActive))
			.setAttribute("role", "button")
			.addContent(text);
	}
	
	public static Element getDropDownMenu(String name)
	{
		Element _listEl = new Element("li", ns).setAttribute("class", "dropdown");
		Element _linkEl = getLinkElement("#", name, null);
		_linkEl.setAttribute("class", "dropdown-toggle").setAttribute("data-toggle", "dropdown");
		_linkEl.addContent(new Element("b", ns).setAttribute("class", "caret"));
		_listEl.addContent(_linkEl);
		return _listEl;
	}
	
	// todo: attribute class="disabled" is not yet supported
	public static Element getDropDownSubmenu(String name)
	{
		Element _listEl = new Element("li", ns).setAttribute("class", "dropdown-submenu");
		Element _linkEl = getLinkElement("#", name, null);
		_linkEl.setAttribute("tabindex", "-1");
		_listEl.addContent(_linkEl);
		return _listEl;
	}
	
	public static Element getDivElement(String classValue, String idValue)
	{
		Element _divEl = new Element("div", ns);
		if (classValue != null) {
			_divEl.setAttribute("class", classValue);
		}
		if (idValue != null) {
			_divEl.setAttribute("id", idValue);
		}
		return _divEl;
	}

	// <li><a href="url">text</a></li>
	public static Element getLinkingListItem(String text, String url) 
	{
		return new Element("li", ns).addContent(getLinkElement(url, text, null));
	}

	// <a href="url" [class="classValue"]>text</a>
	public static Element getLinkElement(String url, String text, String classValue)
	{
		Element _aEl = new Element("a", ns).setAttribute("href", url);
		if (classValue != null) {
			_aEl.setAttribute("class", classValue);
		}
		return (_aEl.addContent(text));
	}

	// <img src="url" [alt="alt"] [class="classValue"] /> 
	public static Element getImageElement(String url, String alt, 
			ImageShapes shape, boolean isResponsive)
	{
		Element _imgEl = new Element("img", ns).setAttribute("src", url);
		if (alt != null) {
			_imgEl.setAttribute("alt", alt);
		}
		String _classAttr = null;
		switch (shape) {
		case ROUNDED:	_classAttr = "img-rounded"; break;
		case CIRCLE:	_classAttr = "img-circle"; 	break;
		case THUMBNAIL:	_classAttr = "img-thumbnail"; break;
		default:		break; // keep default, i.e. no img shape attribute
		}
		if (isResponsive == true) {
			if (_classAttr == null) {
				_classAttr = "img-responsive";
			}
			else {
				_classAttr = _classAttr + " img-responsive";
			}
		}
 		if (_classAttr != null) {
			_imgEl.setAttribute("class", _classAttr);
		}
		return _imgEl;
	}
				
	public static Element getScriptElement(String url, String code) {
		Element _scriptEl = new Element("script", ns);
		if (url != null) {
			_scriptEl.setAttribute("src", url);
		}
		if (code != null) {
			_scriptEl.addContent(code);
		}
		return _scriptEl;
	}
	
	public static Element getSingleBackgroundScript(String imgUrl, String fade) {
		if (fade != null) {
			return getScriptElement(null, "$.backstretch([\"" + imgUrl + "\"],{fade: " + fade + "});");
		}
		else {
			return getScriptElement(null, "$.backstretch(\"" + imgUrl + "\");");			
		}
	}
	
	public static Element getMultiBackgroundScript(List<String> imgUrls, String fade, String duration)
	{
		String _script = "$.backstretch([";
		for (int i = 0; i < imgUrls.size(); i++) {
			if (i>0) {
				_script = _script + ",";
			}
			_script = _script + "\"" + imgUrls.get(i) + "\"";
		}
		if (fade == null) {
			fade = "750";
		}
		if (duration == null) {
			duration = "3000";
		}
		return getScriptElement(null, _script + "],{fade: " + fade + ", duration: " + duration + "});");
	}

	public static Element getStylesheetElement(String url) {
		Element _cssEl = new Element("link", ns)
			.setAttribute("rel", "stylesheet")
			.setAttribute("href", url)
			.setAttribute("type", "text/css");
		return (_cssEl);
	}
	
	public static void generateSlideImage(File originalF, File destF, double scalingFactor) throws IOException
	{
		BufferedImage _originalImg = ImageIO.read(originalF);
		BufferedImage _slideImg = Thumbnails.of(_originalImg)
				.scale(scalingFactor)
				.asBufferedImage();
		ImageIO.write(_slideImg, "jpg", destF);

	}

	public static void generateThumbnailImage(File originalF, File destF, int height, int width) throws IOException {
		BufferedImage _originalImg = ImageIO.read(originalF);
		BufferedImage _thumbnailImg = Thumbnails.of(_originalImg)
				.size(height, width)
				.crop(Positions.CENTER)
				.asBufferedImage();
		ImageIO.write(_thumbnailImg, "jpg", destF);
	}

	public static void copyFile(File srcDir, File destDir, String imageName) throws IOException
	{
		destDir.mkdirs();
		String _srcDirPath = new File(srcDir, imageName).getAbsolutePath();
		String _destDirPath = new File(destDir, imageName).getAbsolutePath();
		Files.copy(Paths.get(_srcDirPath), Paths.get(_destDirPath), REPLACE_EXISTING);
	}


	public static Element getLinkUnit(String link, String title, String image, String text, String resDir, String buttonText)
	{
		String _imgStr = resDir + File.separator + image;
		Element _el = getDivElement("col-sm-3 col-xs-6", null);
		_el.addContent(new Element("h3", ns).addContent(title));
		Element _aEl = new Element("a", ns).setAttribute("href", link);
		_aEl.addContent(getImageElement(_imgStr, title, ImageShapes.DEFAULT, true));
		_el.addContent(new Element("p", ns).addContent(_aEl));
		_el.addContent(new Element("p", ns).addContent(text));
		_aEl = getLinkElement(link, buttonText, "btn btn-info btn-sm");
		_el.addContent(new Element("p", ns).addContent(_aEl));
		return _el;
	}

	/**
	 * Filters all files within directory dir according to a file extension.
	 * @param   dir         the current directory to look for the files
	 * @param   extension   the file name extension is the selection criteria
	 * @return	an array of pdf files
	 */
	public static File[] selectFiles(File dir, String extension) {
		try {
			FilenameFilter _filter = new GbsFileFilter(extension);
			return (dir.listFiles(_filter));
		}
		catch (Exception _ex) {
			GbsLogUtil.throwFatal(CN, "selectFiles", _ex.toString());
			return null;
		}
	}
	
	protected static Element getContentRowElement(Element containerEl) {
		Element _el = null;
		if (containerEl != null) {
			List<Element> _divElems = containerEl.getChildren("div", ns);
			for (int i = 0; i < _divElems.size(); i++) {
				if (_divElems != null) {
					if (_divElems.get(i).getAttributeValue("id").equalsIgnoreCase("content")) {
						_el = (Element) _divElems.get(i);
					}
				}
			}
		}
		return _el;
	}
	
	protected static Element getHeaderRowElement(Element containerEl) {
		Element _el = null;
		if (containerEl != null) {
			List<Element> _divElems = containerEl.getChildren("div", ns);
			for (int i = 0; i < _divElems.size(); i++) {
				if (_divElems != null) {
					if (_divElems.get(i).getAttributeValue("id").equalsIgnoreCase("header")) {
						_el = (Element) _divElems.get(i);
					}
				}
			}
		}
		return _el;
	}

	public static Element getNavbarHeader() {
		Element _navbarHeader = getDivElement("navbar-header", null);
		Element _button = new Element("button", ns).setAttribute("type", "button").setAttribute("class", "navbar-toggle").setAttribute("data-toggle", "collapse").setAttribute("data-target", ".navbar-ex1-collapse");
		_navbarHeader.addContent(_button);
		_button.addContent(new Element("span", ns).setAttribute("class", "sr-only").addContent("Toggle navigation"));
		_button.addContent(new Element("span", ns).setAttribute("class", "icon-bar"));
		_button.addContent(new Element("span", ns).setAttribute("class", "icon-bar"));
		_button.addContent(new Element("span", ns).setAttribute("class", "icon-bar"));
		return _navbarHeader;
	}
	
	/*
	 * TODO: Forms are not yet fully supported; there are just too much variations.
	 * <form role="form"><div class="form-group"><label ...><input ...>
	 * group labels and input fields together with <div class="form-group">
	 * class="form-control" on <input>, <textarea>, <select> sets width to 100%.
	 * see examples on getbootstrap.com/css
	 * <form class="form-inline" role="form"> -> inline within viewports >768px
	 * <form class="form-horizontal" role="form"> -> set col-sm-* e.g. on labels
	 * form controls: text, password, datetime, datetime-local, date, month, time, week, number
	 * email, url, search, tel, color -> <input type="text" class="form-control" placeholder="Textinput"
	 */

	public static Element getLoginForm() {
		/*
	      <form class="form-signin" role="form">
        <input type="email" class="form-control" placeholder="Benutzername" required autofocus>
        <input type="password" class="form-control" placeholder="Passwort" required>
        <label class="checkbox">
          <input type="checkbox" value="remember-me"> Angaben speichern
        </label>
        <button class="btn btn-lg btn-primary btn-block" type="submit">Login</button>
      </form>
	      */
		Element _formEl = new Element("form", ns)
			.setAttribute("class", "form-signin")
			.setAttribute("role", "form");
		_formEl.addContent(new Element("input", ns)
			.setAttribute("type", "email")
			.setAttribute("class", "form-control")
			.setAttribute("placeholder", "Benutzername")
			.setAttribute("required", "")
			.setAttribute("autofocus", ""));
		_formEl.addContent(new Element("input", ns)
			.setAttribute("type", "password")
			.setAttribute("class", "form-control")
			.setAttribute("placeholder", "Passwort")
			.setAttribute("required", ""));
		_formEl.addContent(new Element("label", ns)
			.setAttribute("class", "checkbox")
			.addContent(new Element("input", ns)
					.setAttribute("type", "checkbox")
					.setAttribute("value", "remember-me")
					.addContent(" Angaben speichern")));
		_formEl.addContent(
				getButton("Login", ButtonClass.PRIMARY, ButtonSize.LARGE, false)); 
		// todo: add btn-block
		return _formEl;
	}
}