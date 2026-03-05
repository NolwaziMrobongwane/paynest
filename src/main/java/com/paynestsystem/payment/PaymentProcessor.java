package com.paynestsystem.payment;

/**
 * Processes payments using any PaymentMethod implementation.
 * Accepts a payment method and amount, then prints the result.
 */
public class PaymentProcessor {

    /**
     * Processes a payment using the given payment method.
     *
     * @param method the payment method to use
     * @param amount the amount to charge
     */
    public void processPayment(PaymentMethod method, double amount) {
        boolean success = method.processPayment(amount);
        if (success) {
            System.out.println("Payment successful via " + method.getPaymentType());
            System.out.println("Amount: R" + String.format("%.0f", amount));
        } else {
            // Students can extend here: handle payment failure
            System.out.println("Payment failed via " + method.getPaymentType());
        }
    }
}
