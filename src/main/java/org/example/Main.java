package org.example;

import org.example.element.DigitalSubscription;
import org.example.element.LogisticsService;
import org.example.element.OrderItemElement;
import org.example.element.PhysicalProduct;
import org.example.structure.CustomerOrder;
import org.example.visitor.FiscalReceiptVisitor;
import org.example.visitor.ShippingInsuranceVisitor;
import org.example.visitor.TaxCalculatorVisitor;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== FinTech & E-Commerce Billing Engine (Visitor Pattern, Java 21) ===\n");

        // Phase 1: System Construction (Clean Code Ch. 11 — Separate Constructing a System from Using It)
        CustomerOrder order = constructSampleCustomerOrder();

        // Phase 2: Runtime Execution of Visitors on the same Object Structure via Double Dispatch
        System.out.println("[Step 1] Executing TaxCalculatorVisitor on CustomerOrder:");
        TaxCalculatorVisitor taxVisitor = new TaxCalculatorVisitor();
        order.accept(taxVisitor);
        System.out.println(taxVisitor.getTaxSummaryReport());

        System.out.println("\n[Step 2] Executing FiscalReceiptVisitor on the same CustomerOrder:");
        FiscalReceiptVisitor receiptVisitor = new FiscalReceiptVisitor("OFD-KZ-ASTANA-7701");
        order.accept(receiptVisitor);
        System.out.println(receiptVisitor.renderFiscalReceipt(order.getOrderId(), order.getCustomerName()));

        System.out.println("\n[Step 3] Extensibility Demo -> Executing newly added ShippingInsuranceVisitor:");
        ShippingInsuranceVisitor insuranceVisitor = new ShippingInsuranceVisitor();
        order.accept(insuranceVisitor);
        System.out.println(insuranceVisitor.getInsuranceAssessmentReport());

        // Phase 3: Concurrent Traversal Demo (Clean Code Ch. 13 — Independent Threads & Immutable Elements)
        System.out.println("\n[Step 4] Concurrency Demo -> Parallel Visitors on Shared Immutable CustomerOrder:");
        runConcurrentVisitorsDemo(order);
    }

    /**
     * Separates object graph construction from runtime visitor execution (Clean Code Chapter 11).
     */
    private static CustomerOrder constructSampleCustomerOrder() {
        List<OrderItemElement> items = List.of(
                new PhysicalProduct(
                        "HW-MBP-16-M4",
                        "MacBook Pro 16 M4 Max Workstation",
                        1_850_000.00,
                        1,
                        2.15,
                        true,
                        true
                ),
                new PhysicalProduct(
                        "HW-KEY-MX",
                        "Logitech MX Mechanical Keyboard",
                        85_000.00,
                        2,
                        0.85,
                        false,
                        false
                ),
                new DigitalSubscription(
                        "SW-JB-ULT",
                        "JetBrains All Products Pack (Corp)",
                        24_500.00,
                        12,
                        "Czech Republic",
                        false
                ),
                new DigitalSubscription(
                        "SW-AH-CLOUD",
                        "AstanaHub Cloud ERP Enterprise",
                        45_000.00,
                        6,
                        "Kazakhstan",
                        true
                ),
                new LogisticsService(
                        "LOG-ALA-NQZ-AIR",
                        "Almaty -> Astana Express Air Courier",
                        1_215.0,
                        18.50,
                        true
                )
        );

        return new CustomerOrder("ORD-KZ-2026-941", "Kuandyk Koshanov", items);
    }

    /**
     * Demonstrates Clean Code Chapter 13 (Concurrency): because all Concrete Elements
     * are strictly immutable and each Visitor isolates its own state, multiple threads
     * can traverse the exact same CustomerOrder simultaneously without locks or race conditions.
     */
    private static void runConcurrentVisitorsDemo(CustomerOrder sharedOrder) {
        TaxCalculatorVisitor parallelTaxVisitor = new TaxCalculatorVisitor();
        ShippingInsuranceVisitor parallelInsuranceVisitor = new ShippingInsuranceVisitor();

        CompletableFuture<Void> taxFuture = CompletableFuture.runAsync(
                () -> sharedOrder.accept(parallelTaxVisitor)
        );
        CompletableFuture<Void> insuranceFuture = CompletableFuture.runAsync(
                () -> sharedOrder.accept(parallelInsuranceVisitor)
        );

        CompletableFuture.allOf(taxFuture, insuranceFuture).join();

        System.out.printf(
                Locale.US,
                "  Thread-1 (TaxCalculatorVisitor)     completed -> Total Tax      : %10.2f KZT%n",
                parallelTaxVisitor.getTotalTaxAmountKzt()
        );
        System.out.printf(
                Locale.US,
                "  Thread-2 (ShippingInsuranceVisitor) completed -> Total Insurance: %10.2f KZT%n",
                parallelInsuranceVisitor.getTotalInsurancePremiumKzt()
        );
    }
}
