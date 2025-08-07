package assignment4;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
     
	public RegisterServlet () {
		super();
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
		PrintWriter out=response.getWriter();
		String username=request.getParameter("login_username");
		String uPassword=request.getParameter("login_uPassword");
		int loginID=JDBCConnector.loginCheck(username,uPassword);
		if(loginID==-1) {
		out.write("username not found");
		}
		else if(loginID==-2) {
		out.write("password incorrect");	
		}
		else {
		HttpSession session= request.getSession(false);    //Cite: HttpSession and related method from https://www.geeksforgeeks.org/the-httpsession-interface-in-servlet/
		if(session != null && session.getAttribute("user_id")!=null ) {
			if((Integer)session.getAttribute("user_id")==loginID) {
			out.write("You already logged in,user_id:"+ session.getAttribute("user_id"));
			}
		}
		else {
			session=request.getSession();
			session.setAttribute("user_id", loginID);
			out.write("login success");
		}
		}
		out.flush();
		out.close();
	    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out=response.getWriter();
        	
		User user=new Gson().fromJson(request.getReader(), User.class);
		String username=user.getUsername();
		String password=user.getuPassword();
		String email=user.getEmail();
		double balance = user.getBalance();
		System.out.println("balance: "+balance );
		Gson gson=new Gson();
		String jsonResponse= new Gson().toJson(user);
		if(username==null || username.isBlank() 
		|| password==null ||password.isBlank()||
		email ==null || email.isBlank()) {
		//response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		System.out.println("server in 1");
		String error="User info missing";
		//Cite: method Collections.singletonMap() from https://www.geeksforgeeks.org/collections-singletonmap-method-in-java-with-examples/
		out.write(gson.toJson(Collections.singletonMap("error", error)));
		out.flush();
        }
		int userID = JDBCConnector.registerUser(username, password, email);
		if(userID==-1) {
			//response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			System.out.println("server in 2");
			String error="Username is taken";
			out.write(gson.toJson(Collections.singletonMap("error", error)));  //cite: see citation line71
			out.flush();
		}
		else if(userID==-2) {
			//response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			System.out.println("server in 3");
			String error="Email is already registerd";
			out.write(gson.toJson(Collections.singletonMap("error", error)));  //cite: see citation line71
			out.flush();
		}
		else {
			response.setStatus(HttpServletResponse.SC_OK);
			System.out.println("server in 4");
			out.print(jsonResponse);
			out.flush();
		}
		out.close();
	    }
}

