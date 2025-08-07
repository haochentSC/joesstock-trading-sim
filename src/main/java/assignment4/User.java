package assignment4;

public class User {
	private String email;
	private String username;
	private String uPassword;
	private double balance;
	public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getuPassword() {
        return uPassword;
    }
    public void setuPassword(String uPassword) {
        this.uPassword = uPassword;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public double getBalance() {
        return balance;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }
}
