package com.bkaiser.gbs;

import org.jdom2.*;

/**
 * Generate JDOM elements representing HTML5 Bootstrap Tooltips. 
 * 
 * @author Bruno Kaiser
 */
public class GbsTooltip extends GbsFactory  {
//	private static final String CN = "GbsTooltip";
	boolean isAnimated = true;
	boolean isHtml = false;
	// todo: placement (top |bottom | left | right | auto) should be restricted to an enum
	String placement = "auto top"; 
	String selector = "false";
	String title = "tooltip";
	// todo: trigger (click | hover | focus | manual) should be restricted to an enum
	String trigger = "hover focus";
	String delay = "0";
	String container = "false";
	
	/**
	 * Default Constructor.
	 * 
	 */
	public GbsTooltip(String title)
	{
		this.title = title;
	}
	
	public Element addTooltipAttributes(Element el)
	{
		el.setAttribute("data-toggle", "tooltip");
		el.setAttribute("title", title);
		if (isAnimated == false) el.setAttribute("data-animation", "false");
		if (isHtml == true)  el.setAttribute("data-html", "true");
		el.setAttribute("data-placement", placement);
		if (! selector.equals("false")) el.setAttribute("data-selector", selector);
		el.setAttribute("data-trigger", trigger);
		el.setAttribute("data-delay", delay);
		return el;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the text shown by the tooltip. Can be set to a function.
	 * @default "tooltip"
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Sets the tooltip option 'animation' that applies a CSS fade transition to the tooltip.
	 * @default true
	 * @param isAnimated the isAnimated to set
	 */
	public void setAnimated(boolean isAnimated) {
		this.isAnimated = isAnimated;
	}

	/**
	 * Defines whether html will be used to insert content into the DOM. Default is false which 
	 * uses jQuery's text method. Be aware of possible XSS attacks when using html insertions.
	 * @default false uses jQuery's text method to insert content into the DOM
	 * @param isHtml the isHtml to set
	 */
	public void setHtml(boolean isHtml) {
		this.isHtml = isHtml;
	}

	/**
	 * Defines how to position the tooltip. Possible values are: top | bottom | left | right | auto.
	 * A function may be set as well.
	 * When 'auto' is specified, it will dynamically reorient the tooltip. For example, if placement
	 * is 'auto left', the tooltip will display to the left when possible, otherweise it will display right.
	 * @default auto top
	 * @param placement the placement to set
	 */
	// todo: placement (top |bottom | left | right | auto) should be restricted to an enum
	public void setPlacement(String placement) {
		this.placement = placement;
	}

	/**
	 * Defines whether tooltip objects will be delegated to the specified targets.
	 * @default false
	 * @param selector the selector to set
	 */
	public void setSelector(String selector) {
		this.selector = selector;
	}

	/**
	 * Defines how the tooltip is triggered. Can be set to 'click | hover | focus | manual'.
	 * Multiple triggers may be passed, separated with a space.
	 * @default hover focus
	 * @param trigger the trigger to set
	 */
	public void setTrigger(String trigger) {
		this.trigger = trigger;
	}

	/**
	 * Delay showing and hiding of the tooltip in millisecs (ms).
	 * does not apply to manual trigger type. If a number is supplied, delay is applied to 
	 * both hide/show.
	 * Object structure is: delay: { show: 500, hide: 100 }
	 * @default 0
	 * @param delay the delay to set
	 */
	public void setDelay(String delay) {
		this.delay = delay;
	}

	/**
	 * Appends a tooltip to a specific element. Example: container: 'body'.
	 * @default false
	 * @param container the container to set
	 */
	public void setContainer(String container) {
		this.container = container;
	}	
};