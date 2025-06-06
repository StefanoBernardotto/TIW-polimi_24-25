package it.polimi.tiw.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.UnavailableException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.catalina.SessionEvent;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import com.mysql.cj.Session;

import it.polimi.tiw.beans.Studente;
import it.polimi.tiw.daos.StudenteDAO;
import it.polimi.tiw.exceptions.LoginException;
import it.polimi.tiw.misc.DatabaseInit;
import it.polimi.tiw.misc.ThymeleafInit;

@WebServlet("/LoginStudente")
public class LoginStudente extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection;
	private TemplateEngine templateEngine;
	
	@Override
	public void init() throws UnavailableException {
		connection = DatabaseInit.initDB(getServletContext());
		templateEngine = ThymeleafInit.initialize(getServletContext());
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		IWebExchange webExchange = JakartaServletWebApplication.buildApplication(getServletContext())
				.buildExchange(request, response);
		WebContext context = new WebContext(webExchange);
		templateEngine.process("studente/login_studente", context, response.getWriter());
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		IWebExchange webExchange = JakartaServletWebApplication.buildApplication(getServletContext())
				.buildExchange(request, response);
		WebContext context = new WebContext(webExchange);
		
		StudenteDAO studenteDAO = new StudenteDAO(connection);
		int matricola = 0;
		String password = null;
		
		try {
			matricola = Integer.parseInt(request.getParameter("matricola"));
			password = (String) request.getParameter("password");
			if(password == null) {
				context.setVariable("messaggioErroreLogin", "Parametri mancanti");
				templateEngine.process("studente/login_studente", context, response.getWriter());
				return;
			}
		}catch(NumberFormatException e) {
			context.setVariable("messaggioErroreLogin", "Formato matricola non valido, inserire solo valori numerici");
			templateEngine.process("studente/login_studente", context, response.getWriter());
			return;
		}
		
		try {
			Studente s = studenteDAO.login(matricola, password);
			if(s == null) {
				context.setVariable("messaggioErroreLogin", "Credenziali non valide, si prega di riprovare");
				templateEngine.process("studente/login_studente", context, response.getWriter());
				return;
			}
			HttpSession session = request.getSession();
			session.setAttribute("matricola_studente", s.getMatricola());
			response.sendRedirect(request.getContextPath() + "/HomeStudente");
		}catch(SQLException e) {
			// errore nel db
			context.setVariable("messaggioErroreLogin", "Errore durante il login, si prega di riprovare");
			templateEngine.process("studente/login_studente", context, response.getWriter());
			return;
		}
	}
	
	@Override
	public void destroy() {
		try {
			if(!connection.isClosed()) {
				connection.close();
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
