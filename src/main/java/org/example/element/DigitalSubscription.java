package org.example.element;

import org.example.visitor.OrderVisitor;

/**
 * Concrete Element 2: Represents a digital software subscription or cloud license.
 * Strictly immutable (private final fields, no setters) for thread-safe traversal.
 */
public final class DigitalSubscription implements OrderItemElement {

    private final String sku;
    private final String title;
    private final double monthlyFeeKzt;
    private final int billingMonths;
    private final String vendorCountry;
    private final boolean astanaHubResidentVendor;

    public DigitalSubscription(
            String sku,
            String title,
            double monthlyFeeKzt,
            int billingMonths,
            String vendorCountry,
            boolean astanaHubResidentVendor
    ) {
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("SKU must not be blank");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Subscription title must not be blank");
        }
        if (monthlyFeeKzt < 0 || billingMonths <= 0) {
            throw new IllegalArgumentException("Fee and billing months must be positive");
        }
        this.sku = sku;
        this.title = title;
        this.monthlyFeeKzt = monthlyFeeKzt;
        this.billingMonths = billingMonths;
        this.vendorCountry = vendorCountry;
        this.astanaHubResidentVendor = astanaHubResidentVendor;
    }

    @Override
    public void accept(OrderVisitor visitor) {
        // Double dispatch step: resolves DigitalSubscription at compile time for the visitor
        visitor.visitDigitalSubscription(this);
    }

    @Override
    public String getSku() {
        return sku;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public double getMonthlyFeeKzt() {
        return monthlyFeeKzt;
    }

    public int getBillingMonths() {
        return billingMonths;
    }

    @Override
    public double getBasePriceKzt() {
        return monthlyFeeKzt * billingMonths;
    }

    public String getVendorCountry() {
        return vendorCountry;
    }

    public boolean isAstanaHubResidentVendor() {
        return astanaHubResidentVendor;
    }
}
