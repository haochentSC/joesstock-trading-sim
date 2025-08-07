package assignment4;

public class Portfolio {
 private int user_id;
 private String ticker;
 private int numStock;
 private double price;
 public int getUserId() {
     return user_id;
 }
 public void setUserId(int user_id) {
     this.user_id = user_id;
 }
 public String getTicker() {
     return ticker;
 }
 public void setTicker(String ticker) {
     this.ticker = ticker;
 }
 public int getNumStock() {
     return numStock;
 }
 public void setNumStock(int numStock) {
     this.numStock = numStock;
 }
 public double getPrice() {
     return price;
 }
 public void setPrice(double price) {
     this.price = price;
 }
 public Portfolio(int userId, String ticker, int numStock, double price) {
     this.user_id = userId;
     this.ticker = ticker;
     this.numStock = numStock;
     this.price = price;
 }
 public String toString() {
     return "Portfolio{" +
             " user_id=" + user_id +
             ", ticker='" + ticker + '\'' +
             ", numStock=" + numStock +
             ", price=" + price +
             '}';
 }
}
