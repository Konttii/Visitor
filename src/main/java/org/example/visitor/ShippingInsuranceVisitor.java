package org.example.visitor;

import org.example.element.DigitalSubscription;
import org.example.element.LogisticsService;
import org.example.element.PhysicalProduct;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Concrete Visitor 3: Evaluates transit risk and calculates cargo insurance premiums.
 * Demonstrates Clean Code Chapter 10 (Organizing for Change / Open-Closed Principle):
 * added as a 3rd operation without modifying PhysicalProduct, DigitalSubscription, or LogisticsService.
 */
public final class ShippingInsuranceVisitor implements OrderVisitor {

    private static final double STANDARD_CARGO_RATE = 0.015;
    private static final double FRAGILE_CARGO_RATE = 0.030;
    private static final double WEIGHT_SURCHARGE_PER_KG = 250.0;

    private double totalInsurancePremiumKzt = 0.0;
    private double totalInsuredWeightKg = 0.0;
    private final List<String> assessmentLines = new ArrayList<>();

    @Override
    public void visitPhysicalProduct(PhysicalProduct product) {
        double rate = product.isFragile() ? FRAGILE_CARGO_RATE : STANDARD_CARGO_RATE;
        double valuePremium = product.getBasePriceKzt() * rate;
        double weightFee = product.getTotalWeightKg() * WEIGHT_SURCHARGE_PER_KG;
        double itemPremium = valuePremium + weightFee;

        totalInsurancePremiumKzt += itemPremium;
        totalInsuredWeightKg += product.getTotalWeightKg();

        String riskLevel = product.isFragile() ? "HIGH (Fragile 3.0%)" : "NORMAL (Standard 1.5%)";
        assessmentLines.add(String.format(
                Locale.US,
                "  [Cargo]    %-36s | Risk: %-22s | Premium: %9.2f KZT",
                product.getTitle(),
                riskLevel,
                itemPremium
        ));
    }

    @Override
    public void visitDigitalSubscription(DigitalSubscription subscription) {
        // Digital subscriptions are delivered via cloud with zero physical transit risk
        assessmentLines.add(String.format(
                Locale.US,
                "  [Cloud]    %-36s | Risk: %-22s | Premium: %9.2f KZT",
                subscription.getTitle(),
                "NONE (Electronic Delivery)",
                0.00
        ));
    }

    @Override
    public void visitLogisticsService(LogisticsService logistics) {
        double rate = logistics.isExpressAirDelivery() ? 0.025 : 0.010;
        double routePremium = logistics.getBasePriceKzt() * rate;
        totalInsurancePremiumKzt += routePremium;

        String riskLevel = logistics.isExpressAirDelivery() ? "AIR-CARRIER (2.5%)" : "GROUND-CARRIER (1.0%)";
        assessmentLines.add(String.format(
                Locale.US,
                "  [Transit]  %-36s | Risk: %-22s | Premium: %9.2f KZT",
                logistics.getRouteDescription(),
                riskLevel,
                routePremium
        ));
    }

    public double getTotalInsurancePremiumKzt() {
        return totalInsurancePremiumKzt;
    }

    public double getTotalInsuredWeightKg() {
        return totalInsuredWeightKg;
    }

    public String getInsuranceAssessmentReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("--------------------------------------------------------------------------------------------------\n");
        sb.append("SHIPPING RISK & INSURANCE REPORT (ShippingInsuranceVisitor — Extensibility Demo)\n");
        sb.append("--------------------------------------------------------------------------------------------------\n");
        for (String line : assessmentLines) {
            sb.append(line).append("\n");
        }
        sb.append("--------------------------------------------------------------------------------------------------\n");
        sb.append(String.format(Locale.US, "  Total Insured Physical Weight : %10.2f kg\n", totalInsuredWeightKg));
        sb.append(String.format(Locale.US, "  Total Insurance Premium       : %10.2f KZT\n", totalInsurancePremiumKzt));
        sb.append("--------------------------------------------------------------------------------------------------");
        return sb.toString();
    }
}
