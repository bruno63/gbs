package com.bkaiser.gbs;

import org.jdom2.*;

/**
 * Generate JDOM elements representing HTML5 Bootstrap Glyphs.
 * Bootstrap includes 200 glyphs in font format from the 
 * Glyphicon Halflings set.  
 * 
 * @author Bruno Kaiser
 */
public class GbsGlyph extends GbsFactory  {
//	private static final String CN = "GbsGlyph";
	Glyphicon glyph = null;

	/**
	 * Constructor.
	 * 
	 */
	public GbsGlyph(Glyphicon myGlyph)  
	{
		glyph = myGlyph;
	}
	
	public Element getGlyph()
	{
		return getGlyph(glyph);
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
};