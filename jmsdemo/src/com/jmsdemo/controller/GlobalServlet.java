package com.jmsdemo.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
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
 * Servlet implementation class GlobalServlet
 */
public class GlobalServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Connection connection=null;
		try {
			String ss=request.getParameter("m");
			Context iniCtx = new InitialContext();//from here 
			Queue que = (Queue) iniCtx.lookup("java:/zensarqueue");
			Destination dest =(Destination) que; //to here queue is created
			
			
			QueueConnectionFactory qcf = (QueueConnectionFactory)iniCtx.lookup("java:/ConnectionFactory");//step1 asking for connection
			connection = qcf.createConnection();//step 2 call create connection using connection factory
			Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);//step3 creating a session
			MessageProducer producer = session.createProducer(dest);//step4 based on session create producer inside producer and pass destination
			TextMessage message = session.createTextMessage(ss);//step5 ready to create message here message from ss i.e string
			System.out.println("Sending message: "+ message.getText());
			producer.send(message);//step 6 send message and it will go to queue
			
			PrintWriter out = response.getWriter();
			response.setContentType("text/html");
			out.println("<html><body>");
			out.println("message "+message.getText()+" sent successfully ");
			out.println("to receive message please <a href=ReceiveServlet>click here </a>");
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

