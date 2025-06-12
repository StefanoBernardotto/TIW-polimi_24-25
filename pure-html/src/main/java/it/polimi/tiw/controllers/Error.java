package it.polimi.tiw.controllers;

import jakarta.servlet.ServletException;
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
 * Servlet implementation class Error
 */
@WebServlet("/Error")
public class Error extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private TemplateEngine templateEngine;
    
    @Override
    public void init() {
    	templateEngine = ThymeleafInit.initialize(getServletContext());
    }
    
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	IWebExchange webExchange = JakartaServletWebApplication.buildApplication(getServletContext())
				.buildExchange(request, response);
		WebContext context = new WebContext(webExchange);
		Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");
		String errorMessage = (String) request.getAttribute("jakarta.servlet.error.message");
		
		if(statusCode == null || errorMessage == null) {
			statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
			errorMessage = "Errore nel server";
		}
		
		context.setVariable("status", statusCode);
		context.setVariable("message", errorMessage);
		templateEngine.process("error", context, response.getWriter());
	}
    
    @Override
    public void destroy() {
    	
    }

}
