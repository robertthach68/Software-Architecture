package esb;

public class Order {
          private String orderNumb
          private double amou
          private String orderType; // "international" or "domest

          public Order(
        

          public Order(String orderNumber, double amount, String orderType
                    su
                    this.orderNumber = orderN
                    this.amount = a
                    this.orderType = orde
        

          public String getOrderNumber(
                    return orderN
        

          public void setOrderNumber(String orderNumber
                    this.orderNumber = orderN
        

          public double getAmount(
                    return a
        

          public void setAmount(double amount
                    this.amount = a
        

          public String getOrderType(
                    return orde
        

          public void setOrderType(String orderType
                    this.orderType = orde
        

          @Overr
          public String toString(
                    return "Ord
                                        "orderNumber='" + orderNum
                                        ", amount=
                                        ", orderType='" + orderT
                                
          }
} 