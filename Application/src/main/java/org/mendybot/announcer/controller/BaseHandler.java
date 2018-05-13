package org.mendybot.announcer.controller;

import java.net.URI;
import java.util.Properties;
import java.util.StringTokenizer;

import org.mendybot.announcer.model.AnnouncerModel;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public abstract class BaseHandler implements HttpHandler {
	private AnnouncerModel model;

	public BaseHandler(AnnouncerModel model) {
		this.model = model;
	}

	protected AnnouncerModel getModel() {
		return model;
	}

	protected Properties getProperties(HttpExchange ex) {
		URI uri = ex.getRequestURI();
		Properties p = new Properties();
		String working = uri.toString();
		working = working.substring(working.indexOf("?"));
		StringTokenizer st = new StringTokenizer(working, "?&");
		while (st.hasMoreTokens()) {
			String key = st.nextToken();
			int index = key.indexOf("=");
			String value = key.substring(index + 1);
			key = key.substring(0, index);
			p.setProperty(key, value);
		}
		return p;
	}

}
