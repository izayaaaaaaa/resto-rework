package restaurant;

public class Receipt {
    private Order order;
    private double VAT;
    private double subtotal;
    private double total;

    public Receipt(Order order) {
        this.order = order;
        this.total = order.calculateTotal();
        this.VAT = getTax(this.total);
        this.subtotal = this.total - this.VAT;
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
                .append(order.getOrderDetails());
        return receipt.toString();
    }

    public double getTax(double total) {
        if (total >= 10.00 && total <= 20.00) {
            return 0.50;
        } else if (total > 20.00 && total <= 40.00) {
            return 1.00;
        } else if (total > 40.00 && total <= 60.00) {
            return 2.00;
        } else if (total > 60.00 && total <= 80.00) {
            return 3.00;
        } else if (total > 80.00 && total <= 100.00) {
            return 4.00;
        } else if (total > 100.00 && total <= 150.00) {
            return 8.00;
        } else if (total > 150.00 && total <= 200.00) {
            return 10.00;
        } else if (total > 200.00) {
            return 15.00;
        }

        return 0.0; // Default case if none of the conditions are met
    }
}