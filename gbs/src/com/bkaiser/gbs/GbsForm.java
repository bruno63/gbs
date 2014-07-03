package com.bkaiser.gbs;

import org.jdom2.*;

/**
 * Generate JDOM elements representing HTML5 Bootstrap Forms. 
 * 
 * @author Bruno Kaiser
 */
public class GbsForm extends GbsFactory  {
//	private static final String CN = "GbsForm";

	/**
	 * Constructor.
	 * 
	 */
	public GbsForm(String txt)  
	{
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
		_formEl.addContent(new GbsButton("Login").getButton());
		return _formEl;
	}
	
	public static Element getInteractiveLoginForm() 
	{
		Element _formEl = new Element("form", ns)
		.setAttribute("class", "form-signin")
		.setAttribute("role", "form");
	_formEl.addContent(new Element("input", ns)
		.setAttribute("type", "text")
		.setAttribute("class", "form-control")
		.setAttribute("placeholder", "Benutzername")
		.setAttribute("required", "")
		.setAttribute("autofocus", "")
		.setAttribute("ng-model", "nameInput"));
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
	_formEl.addContent(new GbsButton("Login").getButton());
		return _formEl;
	}
	
};
	