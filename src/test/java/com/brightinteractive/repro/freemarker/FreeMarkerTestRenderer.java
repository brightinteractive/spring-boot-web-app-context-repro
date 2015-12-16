package com.brightinteractive.repro.freemarker;

/*
 * Copyright 2014-2015 Bright Interactive, All Rights Reserved.
 */

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.StringWebResponse;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;

/**
 * @author Bright Interactive
 */
@Component
@ConditionalOnWebApplication
public class FreeMarkerTestRenderer
{
	private Configuration freemarkerConfiguration;

	@Inject
	private FreeMarkerViewResolver freeMarkerViewResolver;

	@Inject
	private ServletContext servletContext;

	@Inject
	public void setFreemarkerConfig(FreeMarkerConfigurer configurer)
	{
		freemarkerConfiguration = configurer.getConfiguration();
	}

	// Note: Currently because @include_page starts an independent HTTP request processing
	// if your view has content via an @include_page it will not be rendered
	public String renderView(String viewName, Map<String, ?> model)
	{
		try
		{
			final View view = freeMarkerViewResolver.resolveViewName(viewName, Locale.UK);
			final MockHttpServletRequest request = new MockHttpServletRequest(servletContext);
			final MockHttpServletResponse response = new MockHttpServletResponse();
			view.render(model, request, response);
			return response.getContentAsString();
		}
		catch (Exception e)
		{
			throw new RuntimeTemplateException(e);
		}
	}

	public HtmlPage renderViewAsPage(String viewName, Map<String, ?> model)
	{
		try
		{
			URL url;
			url = new URL("http://localhost");
			StringWebResponse stringResponse = new StringWebResponse(renderView(viewName, model), url);
			WebClient client = createWebClient();
			HtmlPage page = HTMLParser.parseHtml(stringResponse, client.getCurrentWindow());
			return page;
		}
		catch (IOException e)
		{
			throw new RuntimeTemplateException(e);
		}
	}

	private WebClient createWebClient()
	{
		final WebClient client = new WebClient(BrowserVersion.CHROME);
		client.getOptions().setJavaScriptEnabled(false);
		return client;
	}

	public String processTemplateIntoString(String templateName, Map<String, Object> model)
	{
		try
		{
			return FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.getTemplate(templateName), model);
		}
		catch (IOException | TemplateException e)
		{
			throw new RuntimeTemplateException(e);
		}
	}
}
