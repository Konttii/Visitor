package org.example.element;

import org.example.visitor.OrderVisitor;

/**
 * Concrete Element 1: Represents a tangible physical product in a customer order.
 * Designed as a strictly immutable class (private final fields, read-only getters)
 * to satisfy Clean Code Chapter 13 (Concurrency: Limit the Scope of Data).
 */
public final class PhysicalProduct implements OrderItemElement {

    private final String sku;
    private final String title;
    private final double unitPriceKzt;
    private final int quantity;
    private final double weightKg;
    private final boolean fragile;
    private final boolean importedLuxury;

    public PhysicalProduct(
            String sku,
            String title,
            double unitPriceKzt,
            int quantity,
            double weightKg,
            boolean fragile,
            boolean importedLuxury
    ) {
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("SKU must not be blank");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Product title must not be blank");
        }
        if (unitPriceKzt < 0 || quantity <= 0 || weightKg <= 0) {
            throw new IllegalArgumentException("Price, quantity, and weight must be positive");
        }
        this.sku = sku;
        this.title = title;
        this.unitPriceKzt = unitPriceKzt;
        this.quantity = quantity;
        this.weightKg = weightKg;
        this.fragile = fragile;
        this.importedLuxury = importedLuxury;
    }

    @Override
    public void accept(OrderVisitor visitor) {
        // Double dispatch step: element passes itself with its static type (PhysicalProduct)
        visitor.visitPhysicalProduct(this);
    }

    @Override
    public String getSku() {
        return sku;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public double getUnitPriceKzt() {
        return unitPriceKzt;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public double getBasePriceKzt() {
        return unitPriceKzt * quantity;
    }

    public double getWeightKg() {
        return weightKg;
    }

    public double getTotalWeightKg() {
        return weightKg * quantity;
    }

    public boolean isFragile() {
        return fragile;
    }

    public boolean isImportedLuxury() {
        return importedLuxury;
    }
}
