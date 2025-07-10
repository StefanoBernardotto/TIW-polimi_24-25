package it.polimi.tiw.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import it.polimi.tiw.misc.ThymeleafInit;

/**
 * Servlet per la pagina di logout
 */
@WebServlet("/Logout")
public class Logout extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	
	@Override
	public void init() {
		templateEngine = ThymeleafInit.initialize(getServletContext());
	}

	/**
	 * Gestione della richiesta GET. Se esiste una sessione valida, viene invalidata e viene mostrata la pagina di logout (template "logout")
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		IWebExchange webExchange = JakartaServletWebApplication.buildApplication(getServletContext())
				.buildExchange(request, response);
		WebContext context = new WebContext(webExchange);
		
		HttpSession session = request.getSession();
		if(session != null) {
			session.invalidate();
		}
		templateEngine.process("logout", context, response.getWriter());
	}

}
