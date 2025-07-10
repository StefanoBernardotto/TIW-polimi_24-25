package it.polimi.tiw.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.UnavailableException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import it.polimi.tiw.misc.ThymeleafInit;

/**
 * Servlet per la pagina di scelta profilo (Studente / Docente)
 */
@WebServlet("")
public class Index extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;

	@Override
	public void init() throws UnavailableException {
		templateEngine = ThymeleafInit.initialize(getServletContext());
	}

	/**
	 * Gestione della richiesta GET: mostra il template "index.html" per la scelta del profilo
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		IWebExchange webExchange = JakartaServletWebApplication.buildApplication(getServletContext())
				.buildExchange(request, response);
		WebContext context = new WebContext(webExchange);

		templateEngine.process("index", context, response.getWriter());
	}
	
	@Override
	public void destroy() {
		
	}

}
