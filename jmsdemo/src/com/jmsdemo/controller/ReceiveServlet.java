package com.jmsdemo.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ReceiveServlet
 */
public class ReceiveServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Connection connection=null;
		try {
			String ss=request.getParameter("m");
			Context iniCtx = new InitialContext();
			Queue que = (Queue) iniCtx.lookup("java:/zensarqueue");
			Destination dest =(Destination) que;
			
			
			QueueConnectionFactory qcf = (QueueConnectionFactory)iniCtx.lookup("java:/ConnectionFactory");//step1 asking for connection
			connection = qcf.createConnection();//step 2 
			Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);//step3 creating a session
			MessageConsumer consumer = session.createConsumer(dest);//step4 based on session create producer inside producer pass destination
			connection.start();
			
			
			PrintWriter out = response.getWriter();
			response.setContentType("text/html");
			out.println("<html><body>");
			while(true) {
				Message m = consumer.receive(1);
				if(m!=null)
				{
					if(m instanceof TextMessage) {
						TextMessage message = (TextMessage) m;
						out.println("Reading message: "+message.getText());
					}
					else {
						break;
					}
				}
			}
			out.println("to send message please <a href=sender.html>click here </a>");
			out.println("</body></html>");
		}
		catch(Exception e)
		{
			System.err.println("Exception occured: " + e.toString());
		}
		finally {
			try {
				connection.close();
			}
			catch(JMSException e)
			{
				e.printStackTrace();
			}
		}
		
	}

	}

