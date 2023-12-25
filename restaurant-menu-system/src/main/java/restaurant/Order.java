package restaurant;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private List<MenuItem> orderItems;
    private String orderId;
    private LocalDate orderDate;
    private LocalTime orderTime;

    public Order(String orderId) {
        this.orderId = orderId;
        this.orderItems = new ArrayList<>();
        this.orderDate = LocalDate.now();
        this.orderTime = LocalTime.now();
    }

    public void addItem(MenuItem item) {
        orderItems.add(item);
    }

    public double calculateTotal() {
        double total = 0;
        for (MenuItem item : orderItems) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }

    public String getOrderDetails() {
        StringBuilder details = new StringBuilder();
        int index = 1;
        for (MenuItem item : orderItems) {
            if (item.getQuantity() > 0) {
                details.append(index++)
                        .append(". ")
                        .append(item.getName())
                        .append("\t\t")
                        .append(String.format("%.2f", item.getPrice() * item.getQuantity()))
                        .append("\n");
            }
        }
        return details.toString();
    }

    // Getter for orderDate
    public LocalDate getOrderDate() {
        return orderDate;
    }

    // Getter for orderTime
    public LocalTime getOrderTime() {
        return orderTime;
    }

    // Getter for orderId
    public String getOrderId() {
        return orderId;
    }
}