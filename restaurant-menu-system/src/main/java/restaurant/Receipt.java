package restaurant;

public class Receipt {
    private Order order;
    private double totalPrice;
    private double VAT;
    private double subtotal;

    public Receipt(Order order) {
        this.order = order;
        this.totalPrice = order.calculateTotal();
        // Calculate VAT and subtotal here
    }

    public void printReceipt() {
        // Implementation for printing the receipt
    }
}