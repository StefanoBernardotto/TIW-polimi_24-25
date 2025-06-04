package it.polimi.tiw.misc;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.IWebApplication;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import jakarta.servlet.ServletContext;

public class ThymeleafInit {
	
	public static TemplateEngine initialize(ServletContext ctx){
		JakartaServletWebApplication application = JakartaServletWebApplication.buildApplication(ctx);
		return getTemplateEngine(application);
	}

	private static TemplateEngine getTemplateEngine(IWebApplication application) {
		TemplateEngine templateEngine = new TemplateEngine();

		WebApplicationTemplateResolver templateResolver = getTemplateResolver(application);
		templateEngine.setTemplateResolver(templateResolver);

		return templateEngine;
	}

	private static WebApplicationTemplateResolver getTemplateResolver(IWebApplication application) {
		WebApplicationTemplateResolver templateResolver = new WebApplicationTemplateResolver(application);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateResolver.setPrefix("/WEB-INF/views/");
		templateResolver.setSuffix(".html");
		templateResolver.setCacheTTLMs(Long.valueOf(3600000L));
		templateResolver.setCacheable(true);

		return templateResolver;
	}
}
