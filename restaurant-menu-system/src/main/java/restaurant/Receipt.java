package restaurant;

import java.time.format.DateTimeFormatter;

import javax.swing.text.*;

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

    public StyledDocument printReceipt() {
        // Create a new StyledDocument
        StyledDocument doc = new DefaultStyledDocument();

        // Attribute set for center alignment
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        StyleConstants.setFontFamily(center, "Arial");
        StyleConstants.setFontSize(center, 12);

        // Attribute set for normal (left) alignment
        SimpleAttributeSet normal = new SimpleAttributeSet();
        StyleConstants.setAlignment(normal, StyleConstants.ALIGN_LEFT);
        StyleConstants.setFontFamily(normal, "Arial");
        StyleConstants.setFontSize(normal, 12);

        try {
            // Insert the centered text (header)
            String header = "\tRESTAURANT\n"
                    + "\tMacArthur Highway Brgy.\n"
                    + "\tSan Roque, Tarlac City\n"
                    + "\tTIN: 000-1234-5678\n"
                    + "\twww.restaurant.com.ph\n"
                    + "\trestaurant@gmail.com\n"
                    + "\t09123456789\n"
                    + "===================================================\n";
            doc.insertString(doc.getLength(), header, null);

            // Apply center alignment to the header
            doc.setParagraphAttributes(0, doc.getLength(), center, false);

            // Format the order date
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, MM/dd/yyyy");
            String formattedDate = order.getOrderDate().format(dateFormatter);

            // Insert the normal aligned text
            String details = "Date: " + formattedDate + "\tTime: " + order.getOrderTime() + "\nOrder ID: "
                    + order.getOrderId() + "\n===================================================\n\n"
                    + "Item Name:\t\t\tPrice(₱):\n" + order.getOrderDetails();
            doc.insertString(doc.getLength(), details, null);

            // Apply normal alignment to the details
            doc.setParagraphAttributes(header.length(), doc.getLength(), normal, false);

        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        return doc;
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