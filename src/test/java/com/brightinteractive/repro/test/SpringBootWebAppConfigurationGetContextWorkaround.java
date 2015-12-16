package com.brightinteractive.repro.test;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.springframework.web.context.WebApplicationContext;

import org.junit.Before;

/**
 * Workaround for
 * java.lang.IllegalStateException: No WebApplicationContext found: no ContextLoaderListener registered?
 * at org.springframework.web.context.support.WebApplicationContextUtils.getRequiredWebApplicationContext(WebApplicationContextUtils.java:83)
 * in @WebAppConfiguration tests run in Spring Boot.
 */
public abstract class SpringBootWebAppConfigurationGetContextWorkaround
{
	@Inject
	private WebApplicationContext webApplicationContext;

	@Inject
	private ServletContext servletContext;

	/**
	 * When Spring Boot is not in use, {@link org.springframework.web.context.support.WebApplicationContextUtils#getRequiredWebApplicationContext(ServletContext)}
	 * successfully returns the application context in tests annotated with
	 * {@code @WebAppConfiguration}. This does not work by default in Spring
	 * Boot (probably a bug); this method works around this <a
	 * href="https://github.com/spring-projects/spring-boot/issues/4370">issue</a>.
	 */
	@Before
	public void springBootWebApplicationContextWorkaround() throws Exception
	{
		servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, webApplicationContext);
	}
}
