package com.brightinteractive.repro;

import static org.junit.Assert.*;

import javax.inject.Inject;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.ui.ExtendedModelMap;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.brightinteractive.repro.test.SpringBootWebAppConfigurationGetContextWorkaround;
import com.brightinteractive.repro.freemarker.FreeMarkerTestRenderer;
import com.brightinteractive.repro.test.ReproIntegrationTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ReproIntegrationTest
@WebAppConfiguration
public class WorkaroundTemplateIntegrationTest
	extends SpringBootWebAppConfigurationGetContextWorkaround
{
	private ExtendedModelMap model;

	@Inject
	private FreeMarkerTestRenderer renderer;

	@Before
	public void setUp() throws Exception
	{
		model = new ExtendedModelMap();
	}

	@Test
	public void shouldContainTestLink()
	{
		confirmPageContainsTestLink(whenViewingDashboard());
	}

	private void confirmPageContainsTestLink(HtmlPage page)
	{
		final HtmlAnchor newAbsenceLink = page.getAnchorByHref("/test");
		assertNotNull(newAbsenceLink);
		assertEquals("button", newAbsenceLink.getAttribute("class"));
	}

	private HtmlPage whenViewingDashboard()
	{
		return renderer.renderViewAsPage("content/repro", model);
	}
}
