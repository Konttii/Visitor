package org.example.visitor;

import org.example.element.DigitalSubscription;
import org.example.element.LogisticsService;
import org.example.element.PhysicalProduct;

/**
 * Visitor Interface in the Visitor pattern.
 * Declares a dedicated visitX() method for each Concrete Element type,
 * enabling polymorphic double dispatch without instanceof checks.
 */
public interface OrderVisitor {

    void visitPhysicalProduct(PhysicalProduct product);

    void visitDigitalSubscription(DigitalSubscription subscription);

    void visitLogisticsService(LogisticsService logistics);
}
