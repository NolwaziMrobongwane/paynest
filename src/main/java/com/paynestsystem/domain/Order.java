package com.paynestsystem.domain;

import com.paynestsystem.payment.PaymentMethod;
import com.paynestsystem.payment.PaymentProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an order placed by a customer.
 * Contains the customer, a list of items, and methods to calculate totals and print a summary.
 */
public class Order {

    private final int id;
    private final Customer customer;
    private final List<OrderItem> items;

    /**
     * Creates a new order for the given customer.
     *
     * @param id       unique identifier for the order
     * @param customer the customer placing the order
     */
    public Order(int id, Customer customer) {
        this.id = id;
        this.customer = customer;
        this.items = new ArrayList<>();
    }

    /**
     * Adds a product to the order with the specified quantity.
     *
     * @param product  the product to add
     * @param quantity the number of units
     */
    public void addItem(Product product, int quantity) {
        // Students can extend here: add validation (e.g. quantity > 0, product not null)
        OrderItem orderItem = new OrderItem(product, quantity);
        items.add(orderItem);
    }

    /**
     * Calculates the total cost of all items in the order.
     *
     * @return the total amount
     */
    public double calculateTotal() {
        double total = 0.0;
        for (OrderItem item : items) {
            total = total + item.calculateTotal();
        }
        return total;
    }

    /**
     * Prints a summary of the order to the console.
     * Shows customer name, each item with quantity and price, and the total.
     */
    public void printSummary() {
        System.out.println("Order Summary");
        System.out.println("Customer: " + customer.getName());
        System.out.println();
        System.out.println("Items:");
        for (OrderItem item : items) {
            String line = item.getProduct().getName() + " x" + item.getQuantity()
                    + " - R" + String.format("%.0f", item.calculateTotal());
            System.out.println(line);
        }
        System.out.println();
        System.out.println("Total: R" + String.format("%.0f", calculateTotal()));
    }

    public int getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    /**
     * Completes the order by processing payment with the given payment method.
     *
     * @param paymentMethod the payment method to use for checkout
     */
    public void checkout(PaymentMethod paymentMethod) {
        double total = calculateTotal();
        PaymentProcessor processor = new PaymentProcessor();
        processor.processPayment(paymentMethod, total);
        System.out.println("Order completed successfully.");
    }
}
