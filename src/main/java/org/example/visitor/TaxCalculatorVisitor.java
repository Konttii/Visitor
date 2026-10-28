package org.example.visitor;

import org.example.element.DigitalSubscription;
import org.example.element.LogisticsService;
import org.example.element.PhysicalProduct;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Concrete Visitor 1: Computes VAT, digital service taxes, and luxury import duties
 * across all order item types.
 * Adheres to Single Responsibility Principle (Chapter 10): encapsulates only tax logic.
 */
public final class TaxCalculatorVisitor implements OrderVisitor {

    private static final double STANDARD_VAT_RATE = 0.12;
    private static final double LUXURY_IMPORT_DUTY_RATE = 0.05;

    private double totalNetAmountKzt = 0.0;
    private double totalTaxAmountKzt = 0.0;
    private final List<String> taxBreakdownLines = new ArrayList<>();

    @Override
    public void visitPhysicalProduct(PhysicalProduct product) {
        double basePrice = product.getBasePriceKzt();
        double vat = basePrice * STANDARD_VAT_RATE;
        double luxuryDuty = product.isImportedLuxury() ? basePrice * LUXURY_IMPORT_DUTY_RATE : 0.0;
        double itemTax = vat + luxuryDuty;

        totalNetAmountKzt += basePrice;
        totalTaxAmountKzt += itemTax;

        String rateLabel = product.isImportedLuxury() ? "12% VAT + 5% Luxury Duty" : "12% Standard VAT";
        taxBreakdownLines.add(String.format(
                Locale.US,
                "  [Physical] %-36s | Base: %10.2f KZT | Tax (%s): %9.2f KZT",
                product.getTitle(),
                basePrice,
                rateLabel,
                itemTax
        ));
    }

    @Override
    public void visitDigitalSubscription(DigitalSubscription subscription) {
        double basePrice = subscription.getBasePriceKzt();
        double itemTax = subscription.isAstanaHubResidentVendor() ? 0.0 : basePrice * STANDARD_VAT_RATE;

        totalNetAmountKzt += basePrice;
        totalTaxAmountKzt += itemTax;

        String rateLabel = subscription.isAstanaHubResidentVendor()
                ? "0% Astana Hub IT Exemption"
                : "12% Digital Services VAT (" + subscription.getVendorCountry() + ")";
        taxBreakdownLines.add(String.format(
                Locale.US,
                "  [Digital]  %-36s | Base: %10.2f KZT | Tax (%s): %9.2f KZT",
                subscription.getTitle(),
                basePrice,
                rateLabel,
                itemTax
        ));
    }

    @Override
    public void visitLogisticsService(LogisticsService logistics) {
        double basePrice = logistics.getBasePriceKzt();
        double itemTax = basePrice * STANDARD_VAT_RATE;

        totalNetAmountKzt += basePrice;
        totalTaxAmountKzt += itemTax;

        taxBreakdownLines.add(String.format(
                Locale.US,
                "  [Logistics] %-35s | Base: %10.2f KZT | Tax (12%% Transport VAT): %9.2f KZT",
                logistics.getRouteDescription(),
                basePrice,
                itemTax
        ));
    }

    public double getTotalNetAmountKzt() {
        return totalNetAmountKzt;
    }

    public double getTotalTaxAmountKzt() {
        return totalTaxAmountKzt;
    }

    public double getTotalGrossAmountKzt() {
        return totalNetAmountKzt + totalTaxAmountKzt;
    }

    public String getTaxSummaryReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("--------------------------------------------------------------------------------------------------\n");
        sb.append("TAX AUDIT REPORT (TaxCalculatorVisitor)\n");
        sb.append("--------------------------------------------------------------------------------------------------\n");
        for (String line : taxBreakdownLines) {
            sb.append(line).append("\n");
        }
        sb.append("--------------------------------------------------------------------------------------------------\n");
        sb.append(String.format(Locale.US, "  Total Net Amount   : %10.2f KZT\n", totalNetAmountKzt));
        sb.append(String.format(Locale.US, "  Total Accrued Taxes: %10.2f KZT\n", totalTaxAmountKzt));
        sb.append(String.format(Locale.US, "  Total Gross Payable: %10.2f KZT\n", getTotalGrossAmountKzt()));
        sb.append("--------------------------------------------------------------------------------------------------");
        return sb.toString();
    }
}
