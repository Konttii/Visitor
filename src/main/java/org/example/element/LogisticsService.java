package org.example.element;

import org.example.visitor.OrderVisitor;

/**
 * Concrete Element 3: Represents a courier or express logistics service item in an order.
 * Strictly immutable (private final fields, no setters) for thread-safe traversal.
 */
public final class LogisticsService implements OrderItemElement {

    private final String sku;
    private final String routeDescription;
    private final double distanceKm;
    private final double ratePerKmKzt;
    private final boolean expressAirDelivery;

    public LogisticsService(
            String sku,
            String routeDescription,
            double distanceKm,
            double ratePerKmKzt,
            boolean expressAirDelivery
    ) {
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("SKU must not be blank");
        }
        if (routeDescription == null || routeDescription.isBlank()) {
            throw new IllegalArgumentException("Route description must not be blank");
        }
        if (distanceKm <= 0 || ratePerKmKzt <= 0) {
            throw new IllegalArgumentException("Distance and rate per km must be positive");
        }
        this.sku = sku;
        this.routeDescription = routeDescription;
        this.distanceKm = distanceKm;
        this.ratePerKmKzt = ratePerKmKzt;
        this.expressAirDelivery = expressAirDelivery;
    }

    @Override
    public void accept(OrderVisitor visitor) {
        // Double dispatch step: dispatches to visitLogisticsService(this)
        visitor.visitLogisticsService(this);
    }

    @Override
    public String getSku() {
        return sku;
    }

    @Override
    public String getTitle() {
        return routeDescription;
    }

    public String getRouteDescription() {
        return routeDescription;
    }

    public double getDistanceKm() {
        return distanceKm;
    }

    public double getRatePerKmKzt() {
        return ratePerKmKzt;
    }

    @Override
    public double getBasePriceKzt() {
        double base = distanceKm * ratePerKmKzt;
        return expressAirDelivery ? base * 1.35 : base;
    }

    public boolean isExpressAirDelivery() {
        return expressAirDelivery;
    }
}
