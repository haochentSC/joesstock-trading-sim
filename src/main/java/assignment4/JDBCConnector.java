package assignment4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
public class JDBCConnector {
	public static int registerUser(String username,String uPassword, String email) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		Connection conn=null;
		Statement st=null;
		ResultSet rs=null;
		int userID=-1;
		try {
			conn=DriverManager.getConnection("jdbc:mysql://localhost/assignment4database?user=root&password=root");
			st=conn.createStatement();
			rs=st.executeQuery("SELECT * FROM users WHERE email='"+ email+ "'");
			if(!rs.next()) {
				rs.close();
				st.execute("INSERT INTO Users (username, uPassword, email, balance) VALUES ('"+ username +
						"','"+ uPassword +"','" + email +"',50000.0)");
				rs= st.executeQuery("SELECT LAST_INSERT_ID()");
				rs.next();
				userID=rs.getInt(1); 
				System.out.println("JDBCConnector : inserted");
			}
			else {
				System.out.println("JDBCConnector : UserID=-2");
				userID=-2;
			}
		}
		catch(SQLException sqle) {
			System.out.println("SQLException in registerUSer.");
			sqle.printStackTrace();
		}
		finally {
			try {
				if(rs!=null) {
					rs.close();
				}
				if(st!=null) {
					st.close();
				}
				if(conn!=null) {
					conn.close();
				}
			}
				catch(SQLException sqle) {
					System.out.println("sqle: "+sqle.getMessage());
				}
			}
		return userID;
		}
	
	public static int loginCheck(String username, String uPassword) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		Connection conn=null;
		Statement st=null;
		ResultSet rs=null;
		int user_id=-1;
		try {
			conn=DriverManager.getConnection("jdbc:mysql://localhost/assignment4database?user=root&password=root");
			st=conn.createStatement();
			rs=st.executeQuery("SELECT user_id, uPassword FROM users WHERE username = '" + username + "'");
			if(rs.next()) {
				user_id = rs.getInt("user_id");
				String database_password= rs.getString("uPassword");
				if(!database_password.equals(uPassword)) {
					user_id=-2;
				}
			}
		}
		catch(SQLException sqle) {
			System.out.println("SQLException in loginCheck.");
			sqle.printStackTrace();
		}
		finally {
			try {
				if(rs!=null) {
					rs.close();
				}
				if(st!=null) {
					st.close();
				}
				if(conn!=null) {
					conn.close();
				}
			}
				catch(SQLException sqle) {
					System.out.println("sqle: "+sqle.getMessage());
				}
			}
		return user_id;
	}
	public static int purchaseStock(int user_id, String ticker,int numStock, double price) {
		//code meaning: -1 not enough balance;
		//-2 some how tradeID is not assigned
		int tradeID=-2;
		double totalPrice=price* numStock;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		Connection conn=null;
		Statement stUser=null;
		Statement stPortfolio=null;
		ResultSet rsUser=null;
		ResultSet rsPortfolio=null;
		try {
			conn=DriverManager.getConnection("jdbc:mysql://localhost/assignment4database?user=root&password=root");
			stUser=conn.createStatement();
			rsUser=stUser.executeQuery("SELECT balance FROM users WHERE user_id="+ user_id);
			if(rsUser.next()) {
				double balance=rsUser.getDouble("balance");
				balance-=totalPrice;
				if(balance<0) {
					return -1;  
				}
				stUser.execute("UPDATE users SET balance = "+balance+" WHERE user_id = "+user_id);
				//rsUser=stUser.executeQuery("SELECT balance FROM users WHERE user_id="+ user_id);
				//System.out.println("inPurchase Stock, balance updated, check now: balance="+rsUser.getDouble("balance"));
			}
			stPortfolio=conn.createStatement();
			stPortfolio.execute("INSERT INTO portfolio (user_id, ticker, numStock, price) VALUES ("+ user_id +
					",'"+ ticker +"'," + numStock +","+price+")");
			rsPortfolio= stPortfolio.executeQuery("SELECT LAST_INSERT_ID()");
			rsPortfolio.next();
			tradeID=rsPortfolio.getInt(1); 
		}
		catch(SQLException sqle) {
			System.out.println("SQLException in purchaseStock.");
			sqle.printStackTrace();
		}
		finally {
			try {
				if(rsUser!=null) {
					rsUser.close();
				}
				if(rsPortfolio!=null) {
					rsPortfolio.close();
				}
				if(stUser!=null) {
					stUser.close();
				}
				if(stPortfolio!=null) {
					stPortfolio.close();
				}
				if(conn!=null) {
					conn.close();
				}
			}
				catch(SQLException sqle) {
					System.out.println("sqle: "+sqle.getMessage());
				}
		}
		return tradeID;
	}
	
	public static List<Portfolio> getPortfolioList(int user_id){
		List<Portfolio> portfolios = new ArrayList<>();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		Connection conn = null;
	    Statement st = null;
	    ResultSet rs = null;
		try {
			conn=DriverManager.getConnection("jdbc:mysql://localhost/assignment4database?user=root&password=root");
			st = conn.createStatement();
	        String query = "SELECT ticker, numStock, price FROM portfolio WHERE user_id = " + user_id;
	        rs = st.executeQuery(query);
			while(rs.next()) {
				String ticker = rs.getString("ticker");
	            int numStock = rs.getInt("numStock");
	            double price = rs.getDouble("price");	
	            boolean repeate=false;
	            for(int i=0; i<portfolios.size();i++) {
	            	//Portfolio p=portfolios.get(i);
	            	if(portfolios.get(i).getTicker().equals(ticker)) {
	            		repeate=true;
	            		double newPrice=(portfolios.get(i).getPrice()* portfolios.get(i).getNumStock()+price * numStock )/(portfolios.get(i).getNumStock()+numStock);
	            		int newNumStock = portfolios.get(i).getNumStock() + numStock;
	            		portfolios.get(i).setPrice(newPrice);
	            		portfolios.get(i).setNumStock(newNumStock);
	                    break;
	            	}
	            }
	            if(!repeate) {
	            portfolios.add(new Portfolio(user_id,ticker,numStock,price));
	            }
			}
		}
		catch(SQLException sqle) {
			System.out.println("SQLException in getAllStocks.");
			sqle.printStackTrace();
		}
		finally {
			try {
				if(rs!=null) {
					rs.close();
				}
				if(st!=null) {
					st.close();
				}
				if(conn!=null) {
					conn.close();
				}
			}
				catch(SQLException sqle) {
					System.out.println("sqle: "+sqle.getMessage());
				}
			}
		return portfolios;
	}
	
	public static double searchBalance(int user_id) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
			return -1.0;
		}
		double balance=-1;
		Connection conn = null;
	    Statement st = null;
	    ResultSet rs = null;
	    try {
			conn=DriverManager.getConnection("jdbc:mysql://localhost/assignment4database?user=root&password=root");
			st=conn.createStatement();
			rs=st.executeQuery("SELECT balance FROM users WHERE user_id="+ user_id);
			if(rs.next()) {
			 balance=rs.getDouble("balance");
		}
	    }
		catch(SQLException sqle) {
			System.out.println("SQLException in purchaseStock.");
			sqle.printStackTrace();
		}
	    finally {
			try {
				if(rs!=null) {
					rs.close();
				}
				if(st!=null) {
					st.close();
				}
				if(conn!=null) {
					conn.close();
				}
			}
				catch(SQLException sqle) {
					System.out.println("sqle: "+sqle.getMessage());
				}
			}
	    return balance;
	}
	
	
	public static int searchNumStock(int user_id,String ticker) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		Connection conn=null;
		Statement st=null;
		ResultSet rs3=null;
		int totalNumStocks=0;
		try {
			conn=DriverManager.getConnection("jdbc:mysql://localhost/assignment4database?user=root&password=root");
			st=conn.createStatement();
			rs3=st.executeQuery("SELECT numStock FROM portfolio WHERE user_id = "+ user_id+" AND ticker = '"+ticker+"'");
			while(rs3.next()) {
				totalNumStocks+=rs3.getInt("numStock");
			}
		}catch(SQLException sqle) {
			System.out.println("SQLException in purchaseStock.");
			sqle.printStackTrace();
		}
		finally {
			try {
				if(rs3!=null) {
					rs3.close();
				}
				if(st!=null) {
					st.close();
				}
				if(conn!=null) {
					conn.close();
				}
			}
				catch(SQLException sqle) {
					System.out.println("sqle: "+sqle.getMessage());
				}
			}
		return totalNumStocks;
	}
	public static void updateBalance(double price, int user_id) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		Connection conn=null;
		Statement st=null;
		double balance=0;
		try {
			conn=DriverManager.getConnection("jdbc:mysql://localhost/assignment4database?user=root&password=root");
			Statement	st1=conn.createStatement();
			ResultSet	rs1=st1.executeQuery("SELECT balance FROM users WHERE user_id="+ user_id);
			if(rs1.next()) {
			 balance=rs1.getDouble("balance");
		}
			balance+=price;
			st=conn.createStatement();
			String updateScript = "UPDATE users SET balance = " + balance + " WHERE user_id = " + user_id;
			 int row = st.executeUpdate(updateScript);
		}catch(SQLException sqle) {
			System.out.println("SQLException in purchaseStock.");
			sqle.printStackTrace();
		}
	}
	
	public static int sellStock(int user_id, String ticker,int numStock, double price) {
		//code meaning: -1 not enough stock in inventory;
		//1 good
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		Connection conn=null;
		Statement stk=null;
		Statement st=null;
		ResultSet resultSet=null;
		if(searchNumStock(user_id,ticker)<numStock) {
			return -1;
		}
		try {
			conn=DriverManager.getConnection("jdbc:mysql://localhost/assignment4database?user=root&password=root");
			stk=conn.createStatement();
			st=conn.createStatement();
			resultSet=stk.executeQuery("SELECT trade_id, numStock FROM portfolio WHERE user_id = "+ user_id+" AND ticker = '"+ticker+"'");
			while(resultSet.next()&& numStock>0) {
				int curNumStock=resultSet.getInt("numStock");
				int trade_id=resultSet.getInt("trade_id");
				if(curNumStock<=numStock) {
				Statement st1=conn.createStatement();
				String sql = "DELETE FROM portfolio WHERE trade_id = " + trade_id;
			    int row= st1.executeUpdate(sql);
				updateBalance(price*curNumStock,user_id);
				numStock-=curNumStock;
				}
				else {  //curNumStock>numStock
					updateBalance(price*numStock,user_id);
					curNumStock-=numStock;
					numStock=0;
					 String uQ = "UPDATE portfolio SET numStock=" + curNumStock + " WHERE trade_id=" + trade_id;
					 int   r = st.executeUpdate(uQ);
				}
		}	
		}
		catch(SQLException sqle) {
			System.out.println("SQLException in SellStock.");
			sqle.printStackTrace();
		}
		finally {
			try {
				if(resultSet!=null) {
					resultSet.close();
				}
				if(st!=null) {
					st.close();
				}
				if(conn!=null) {
					conn.close();
				}
			}
				catch(SQLException sqle) {
					System.out.println("sqle: "+sqle.getMessage());
				}
			}
		return 1;
	}
	}

