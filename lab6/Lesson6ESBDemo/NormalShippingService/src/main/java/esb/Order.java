package esb;

public class Order {
       private String orderNumb
       private double amou

       public Order(
     

       public Order(String orderNumber, double amount
              su
              this.orderNumber = orderN
              this.amount = a
     

       public String getOrderNumber(
              return orderN
     

       public void setOrderNumber(String orderNumber
              this.orderNumber = orderN
     

       public double getAmount(
              return a
     

       public void setAmount(double amount
              this.amount = a
     

       @Overr
       public String toString(
              return "Ord
                            "orderNumber='" + orderNum
                            ", amount=
                    
       }
} 