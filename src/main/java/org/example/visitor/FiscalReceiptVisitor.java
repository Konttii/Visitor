package org.example.visitor;

import org.example.element.DigitalSubscription;
import org.example.element.LogisticsService;
import org.example.element.PhysicalProduct;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Concrete Visitor 2: Generates an official OFD (Fiscal Data Operator) receipt
 * with item-specific fiscal classification codes and line item formatting.
 * Adheres to Single Responsibility Principle (Chapter 10): encapsulates only fiscal formatting.
 */
public final class FiscalReceiptVisitor implements OrderVisitor {

    private final String ofdTerminalId;
    private final List<String> receiptLines = new ArrayList<>();
    private double subtotalKzt = 0.0;
    private int totalPositions = 0;

    public FiscalReceiptVisitor(String ofdTerminalId) {
        if (ofdTerminalId == null || ofdTerminalId.isBlank()) {
            throw new IllegalArgumentException("OFD Terminal ID must not be blank");
        }
        this.ofdTerminalId = ofdTerminalId;
    }

    @Override
    public void visitPhysicalProduct(PhysicalProduct product) {
        totalPositions++;
        double lineTotal = product.getBasePriceKzt();
        subtotalKzt += lineTotal;

        receiptLines.add(String.format(
                Locale.US,
                "  %d. [GOODS-TNVED] %s (SKU: %s)\n     %d pcs x %.2f KZT | Weight: %.2f kg | Line Total: %.2f KZT",
                totalPositions,
                product.getTitle(),
                product.getSku(),
                product.getQuantity(),
                product.getUnitPriceKzt(),
                product.getTotalWeightKg(),
                lineTotal
        ));
    }

    @Override
    public void visitDigitalSubscription(DigitalSubscription subscription) {
        totalPositions++;
        double lineTotal = subscription.getBasePriceKzt();
        subtotalKzt += lineTotal;

        String taxRegime = subscription.isAstanaHubResidentVendor() ? "ASTANA-HUB-EXEMPT" : "FOREIGN-E-SERVICE";
        receiptLines.add(String.format(
                Locale.US,
                "  %d. [DIGITAL-SAAS] %s (SKU: %s)\n     %d mos x %.2f KZT/mo | Regime: %s | Line Total: %.2f KZT",
                totalPositions,
                subscription.getTitle(),
                subscription.getSku(),
                subscription.getBillingMonths(),
                subscription.getMonthlyFeeKzt(),
                taxRegime,
                lineTotal
        ));
    }

    @Override
    public void visitLogisticsService(LogisticsService logistics) {
        totalPositions++;
        double lineTotal = logistics.getBasePriceKzt();
        subtotalKzt += lineTotal;

        String tier = logistics.isExpressAirDelivery() ? "EXPRESS-AIR (+35%)" : "STANDARD-GROUND";
        receiptLines.add(String.format(
                Locale.US,
                "  %d. [SERVICE-LOG]  %s (SKU: %s)\n     %.1f km @ %.2f KZT/km | Tier: %s | Line Total: %.2f KZT",
                totalPositions,
                logistics.getRouteDescription(),
                logistics.getSku(),
                logistics.getDistanceKm(),
                logistics.getRatePerKmKzt(),
                tier,
                lineTotal
        ));
    }

    public String renderFiscalReceipt(String orderId, String customerName) {
        StringBuilder sb = new StringBuilder();
        sb.append("==================================================================================================\n");
        sb.append("OFD FISCAL RECEIPT (FiscalReceiptVisitor)\n");
        sb.append(String.format("Terminal ID : %s | Order ID: %s | Customer: %s\n", ofdTerminalId, orderId, customerName));
        sb.append("==================================================================================================\n");
        for (String line : receiptLines) {
            sb.append(line).append("\n");
        }
        sb.append("--------------------------------------------------------------------------------------------------\n");
        sb.append(String.format(Locale.US, "  Positions Count : %d\n", totalPositions));
        sb.append(String.format(Locale.US, "  Fiscal Subtotal : %.2f KZT\n", subtotalKzt));
        sb.append("==================================================================================================");
        return sb.toString();
    }
}
