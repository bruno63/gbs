package com.bkaiser.gbs;

import org.jdom2.*;

/**
 * Generate JDOM elements representing HTML5 Bootstrap body tags
 * e.g. p, small, strong, h1-h6.
 * Bootstrap's global default font-size is 14px, with a line-height
 * of 1.428. This is applied to <body> and all paragraphs. In addition,
 * <p> (paragraphs) receive a bottom margin of half their computed
 * line height (10px by default).
 * 
 * @author Bruno Kaiser
 */
public class GbsBody extends GbsFactory  {
//	private static final String CN = "GbsBody";
	String bodyText = null;
	boolean isLead = false; 
	TextAlignment alignment = TextAlignment.LEFT;
	TextEmphasis emphasis = TextEmphasis.DEFAULT;
	TextColor textColor = TextColor.DEFAULT;
	TextColor backgroundColor = TextColor.DEFAULT;

	/**
	 * Constructor.
	 * 
	 */
	public GbsBody(String text)  
	{
		bodyText = text;
	}
	
	public GbsBody(String text, TextAlignment alig, TextEmphasis emph)
	{
		bodyText = text;
		alignment = alig;
		emphasis = emph;
	}
	
	public GbsBody(String text, TextAlignment alig, TextEmphasis emph, TextColor txtColor, TextColor bgColor)
	{
		bodyText = text;
		alignment = alig;
		emphasis = emph;
		textColor = txtColor;
		backgroundColor = bgColor;
	}
	
	public Element getBody()
	{
		return getBody(bodyText, isLead, alignment, emphasis, textColor, backgroundColor);
	}
	
	// Make a paragraph stand out.
	public void setAsLead()
	{
		isLead = true;
	}
	
	// For de-emphasizing inline or blocks of text. Sets text at 85% the size of the parent.
	// heading elements receive their own font-size for nested <small> elements.
	public void setSmall()
	{
		emphasis = TextEmphasis.SMALL;
	}
	
	// For highlighting words or phrases without conveying additional importance.
	public void setBold()
	{
		emphasis = TextEmphasis.BOLD;
	}
	
	// For emphasizing a snippet of text with a heavier font-weight.
	public void setStrong()
	{
		emphasis = TextEmphasis.STRONG;
	}

	// For emphasizing a snippet of text with italics.
	public void setEm()
	{
		emphasis = TextEmphasis.EM;
	}
	
	// For Voice, technical terms, etc.
	public void setItalic()
	{
		emphasis = TextEmphasis.ITALIC;
	}
	
	public void setAlignment(TextAlignment alig)
	{
		alignment = alig;
	}
	
	public void setRightAligned()
	{
		alignment = TextAlignment.RIGHT;
	}

	public void setCentered()
	{
		alignment = TextAlignment.CENTER;
	}

	public void setJustified()
	{
		alignment = TextAlignment.JUSTIFY;
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
	private static Element getBody(
			String text, 
			boolean lead, 
			TextAlignment alig, 
			TextEmphasis emph,
			TextColor tColor,
			TextColor bgColor
			)	
	{
		String _emphasis = getEmphasisString(emph);				
		String _alignment = getAlignmentString(alig);
		String _textColor = getTextColorString(tColor);
		String _bgColor = getBackgroundColorString(bgColor);
		
		if (_textColor != null) {
			_alignment = _alignment + " " + _textColor;
		}
		if (_bgColor != null) {
			_alignment = _alignment + " " + _bgColor;			
		}
		if (lead == true) {
			_alignment = _alignment + " lead";
		}
		return new Element(_emphasis, ns).setAttribute("class", _alignment).addContent(text);
	}
	
	public static Element getBody(String text)
	{
		return getBody(text, false, 
				TextAlignment.LEFT, 
				TextEmphasis.DEFAULT,
				TextColor.DEFAULT,
				TextColor.DEFAULT);
	}
	
	public void setEmphasis(TextEmphasis emph)
	{
		emphasis = emph;
	}
	
	// Emphasis makes use of HTML's default emphasis tags with lightweight Bootstrap styles.
	private static String getEmphasisString(TextEmphasis emph)
	{
		String _emphasis = "p";
		switch (emph) {
		case SMALL: 	_emphasis = "small"; 	break;
		case STRONG: 	_emphasis = "strong"; break;
		case BOLD:		_emphasis = "b"; 	break;
		case EM:		_emphasis = "em"; 	break;
		case ITALIC:	_emphasis = "i"; 	break;
		case H1:		_emphasis = "h1"; 	break;
		case H2:		_emphasis = "h2"; 	break;
		case H3:		_emphasis = "h3"; 	break;
		case H4:		_emphasis = "h4"; 	break;
		case H5:		_emphasis = "h5"; 	break;
		case H6:		_emphasis = "h6"; 	break;
		default:		break;  // keep 'p'
		}
		return _emphasis;
	}
	
	private static String getAlignmentString(TextAlignment alig) 
	{
		String _alignment = "text-left";
		switch (alig) {
		case CENTER:	_alignment = "text-center"; 	break;
		case RIGHT:		_alignment = "text-right";		break;
		case JUSTIFY:	_alignment = "text-justify"; 	break;
		default:		break;	// keep 'text-left'
		}
		return _alignment;
	}
	
	private static String getTextColorString(TextColor tColor)
	{
		String _textColor = null;
		switch (tColor) {
		case MUTED:		_textColor = "text-muted"; 		break;
		case PRIMARY:	_textColor = "text-primary"; 	break;
		case SUCCESS:	_textColor = "text-success"; 	break;
		case INFO:		_textColor = "text-info"; 		break;
		case WARNING:	_textColor = "text-warning"; 	break;
		case DANGER:	_textColor = "text-danger"; 	break;
		default:	break;  // keep default, i.e. no text color
		}
		return _textColor;
	}
	
	private static String getBackgroundColorString(TextColor bgColor)
	{
		String _bgColor = null;
		switch (bgColor) {
		case PRIMARY:	_bgColor = "bg-primary"; 	break;
		case SUCCESS:	_bgColor = "bg-success"; 	break;
		case INFO:		_bgColor = "bg-info"; 		break;
		case WARNING:	_bgColor = "bg-warning"; 	break;
		case DANGER:	_bgColor = "bg-danger"; 	break;
		default:	break;  // keep default, i.e. no text color		
		}
		return _bgColor;
	}
	
	// wrap inline snippets of code.
	public static Element getCode(String text)
	{
		return new Element("code", ns).addContent(substituteHtmlCodes(text));
	}
	
	// indicate input that is typically entered via keyboard
	public static Element getUserInput(String text)
	{
		return new Element("kbd", ns).addContent(substituteHtmlCodes(text));
	}
	
	// multi line code blocks (with <pre> and <pre class="pre-scrollable">) 
	public static Element getMultiLineCode(String text, boolean scrollable)
	{
		Element _el = new Element("pre", ns);
		if (scrollable == true) {
			_el.setAttribute("class", "pre-scrollable");
		}
		return _el.addContent(substituteHtmlCodes(text));
	}
	
	public static String substituteHtmlCodes(String srcStr)
	{
		String _resultStr = srcStr.replaceAll("&", "&amp;");
		_resultStr = srcStr.replaceAll("<", "&lt;");
		_resultStr = srcStr.replaceAll(">", "&gt;");
		return _resultStr;
	}
};