package org.example.structure;

import org.example.element.OrderItemElement;
import org.example.visitor.OrderVisitor;

import java.util.List;
import java.util.Objects;

/**
 * Object Structure in the Visitor pattern.
 * Holds an immutable collection of OrderItemElement objects and centralizes
 * traversal logic (Clean Code Chapter 12: No Duplication; Chapter 13: Use Copies of Data).
 */
public final class CustomerOrder {

    private final String orderId;
    private final String customerName;
    private final List<OrderItemElement> items;

    public CustomerOrder(String orderId, String customerName, List<OrderItemElement> items) {
        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("Order ID must not be blank");
        }
        if (customerName == null || customerName.isBlank()) {
            throw new IllegalArgumentException("Customer name must not be blank");
        }
        Objects.requireNonNull(items, "Order items list must not be null");
        this.orderId = orderId;
        this.customerName = customerName;
        // Defensive unmodifiable copy for thread-safe concurrent visitor traversal
        this.items = List.copyOf(items);
    }

    /**
     * Traverses all elements in the order and dispatches each element to the visitor.
     *
     * @param visitor the concrete visitor performing an operation across the order
     */
    public void accept(OrderVisitor visitor) {
        Objects.requireNonNull(visitor, "Visitor must not be null");
        for (OrderItemElement item : items) {
            item.accept(visitor);
        }
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public List<OrderItemElement> getItems() {
        return items;
    }
}
