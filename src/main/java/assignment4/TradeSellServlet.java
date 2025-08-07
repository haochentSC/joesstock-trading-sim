package assignment4;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

@WebServlet("/TradeSellServlet")
public class TradeSellServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	public TradeSellServlet () {
		super();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
		PrintWriter out=response.getWriter();
		HttpSession session= request.getSession(false);
        if(session==null) {
        response.sendRedirect("LoginRegister.html");
        return;
        }
		int user_id=(Integer)session.getAttribute("user_id");
		double balance=JDBCConnector.searchBalance(user_id);
		out.print(balance);
		out.flush();
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        Gson gson=new Gson();
        PrintWriter out=response.getWriter();
        try {
        Portfolio portfolio=new Gson().fromJson(request.getReader(), Portfolio.class);
        if(portfolio.getNumStock()<=0) {
        	String error="FAILED: Sell not possible";
    		out.write(gson.toJson(Collections.singletonMap("error", error)));   //cite: cite: see citation line71 in RegisterServlet.java
    		out.flush();
    		return;
        }
        HttpSession session= request.getSession(false);
        if(session==null) {
        response.sendRedirect("http://localhost:9090/Assignment4/LoginRegister.html");
        return;
        }
        portfolio.setUserId((Integer)session.getAttribute("user_id"));
        int user_id=portfolio.getUserId();
        String ticker=portfolio.getTicker();
        int numStock=portfolio.getNumStock();
        double price=portfolio.getPrice();
        double totalPrice=price*(double)numStock;
        int JDBCSellStock_code=JDBCConnector.sellStock(user_id,ticker,numStock,price);
        if(JDBCSellStock_code<0 ) {
            String error="FAILED: Sell not possible";
    		out.write(gson.toJson(Collections.singletonMap("error", error)));   //cite: see citation line71 in RegisterServlet.java
    		out.flush();	
            }
        else {
        	 response.setStatus(HttpServletResponse.SC_OK);
             String msg="Sell "+numStock + " shares of "+ticker +" for $"+totalPrice;
             out.write(gson.toJson(Collections.singletonMap("msg", msg)));   //cite: see citation line71 in RegisterServlet.java
     		out.flush();
        }
        }
        catch (Exception e) {
        	String error="FAILED: Sell not possible";
    		out.write(gson.toJson(Collections.singletonMap("error", error)));   //cite: see citation line71 in RegisterServlet.java
    		out.flush();
            System.err.println("Error processing request: " + e.getMessage()); 
        }   
        
	}
}
