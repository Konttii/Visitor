package org.example.element;

import org.example.visitor.OrderVisitor;

/**
 * Element Interface in the Visitor pattern.
 * Declares the accept() operation that takes the base OrderVisitor interface
 * as an argument to enable the first step of double dispatch.
 */
public interface OrderItemElement {

    /**
     * Accepts an OrderVisitor and dispatches the call to the element-specific visit method.
     *
     * @param visitor the visitor performing an operation on this order item
     */
    void accept(OrderVisitor visitor);

    String getSku();

    String getTitle();

    double getBasePriceKzt();
}
