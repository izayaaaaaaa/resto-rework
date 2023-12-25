package restaurant;

public class Receipt {
    private Order order;
    private double totalPrice;
    private double VAT;
    private double subtotal;

    public Receipt(Order order) {
        this.order = order;
        this.totalPrice = order.calculateTotal();
        // Assuming VAT is 12% of the total price
        this.VAT = totalPrice * 0.12;
        this.subtotal = totalPrice - VAT;
    }

    public String printReceipt() {
        StringBuilder receipt = new StringBuilder();
        receipt.append("\t\t\t\tRESTAURANT\n")
                .append("\tMacArthur Highway Brgy.\n")
                .append("\tSan Roque, Tarlac City\n")
                .append("\tTIN: 000-1234-5678\n")
                .append("\twww.restaurant.com.ph\n")
                .append("\trestaurant@gmail.com\n")
                .append("\t09123456789\n")
                .append("===================================================\n")
                .append("Date: ")
                .append(order.getOrderDate())
                .append("\t\tTime: ")
                .append(order.getOrderTime())
                .append("\nOrder ID: ")
                .append(order.getOrderId())
                .append("\n===================================================\n\n")
                .append("Item Name:\t\t\t\tPrice(₱):\n")
                .append(order.getOrderDetails())
                .append("\nSubtotal:\t\t\t")
                .append(String.format("%.2f", subtotal))
                .append("\nVAT:\t\t\t")
                .append(String.format("%.2f", VAT))
                .append("\nTotal:\t\t\t")
                .append(String.format("%.2f", totalPrice))
                .append("\n");

        return receipt.toString();
    }
}