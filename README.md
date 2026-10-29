# Assignment #6 — Visitor Pattern (FinTech & E-Commerce Billing Engine)

**Course:** ShP-2216 – Software Design Patterns (OP 6B06102)  
**Institution:** Astana IT University  
**Student:** Kuandyk Koshanov  
**Topic:** Option C — FinTech & E-Commerce Billing Engine (`CustomerOrder`)  
**Java Version:** JDK 21 (Maven)

---

## Project Overview

This project implements the **Visitor** behavioral design pattern in Java 21. It models a heterogeneous e-commerce billing structure (`CustomerOrder`) containing three distinct element types (`OrderItemElement`):
1. **`PhysicalProduct`** — tangible goods with unit price, quantity, weight (kg), fragility flag, and luxury import status.
2. **`DigitalSubscription`** — cloud software/SaaS subscriptions with monthly billing cycle, vendor jurisdiction, and Astana Hub resident tax exemption status.
3. **`LogisticsService`** — domestic courier/air cargo transport services priced by distance (km) and express tier.

Without modifying any of the element classes, the system executes multiple independent operations via polymorphic **Double Dispatch** (`accept(OrderVisitor)` → `visitX(this)`):
1. **`TaxCalculatorVisitor`** — computes standard 12% VAT, 5% luxury import customs duty, and 0% Astana Hub IT tax exemptions.
2. **`FiscalReceiptVisitor`** — formats an official Kazakhstan OFD fiscal receipt with item-specific fiscal classification tags (`GOODS-TNVED`, `DIGITAL-SAAS`, `SERVICE-LOG`).
3. **`ShippingInsuranceVisitor`** — demonstrates Open/Closed extensibility by adding transit risk and cargo insurance premium calculations without altering any element class.

## Folder Structure

```text
src/main/java/org/example/
├── Main.java                                     # Client: constructs CustomerOrder and runs Visitors (sequential & concurrent)
├── element/
│   ├── OrderItemElement.java                     # Element Interface: declares accept(OrderVisitor visitor)
│   ├── PhysicalProduct.java                      # Concrete Element 1: immutable physical merchandise
│   ├── DigitalSubscription.java                  # Concrete Element 2: immutable SaaS / cloud subscription
│   └── LogisticsService.java                     # Concrete Element 3: immutable courier & air freight service
├── structure/
│   └── CustomerOrder.java                        # Object Structure: encapsulates elements and centralizes traversal
└── visitor/
    ├── OrderVisitor.java                         # Visitor Interface: declares visitX() per Concrete Element
    ├── TaxCalculatorVisitor.java                 # Concrete Visitor 1: multi-rate VAT & customs tax calculator
    ├── FiscalReceiptVisitor.java                 # Concrete Visitor 2: OFD fiscal receipt generator
    └── ShippingInsuranceVisitor.java             # Concrete Visitor 3: cargo risk & insurance calculator (extensibility demo)
```

## How to Build & Run (Java 21)

```bash
javac --release 21 -d out $(find src/main/java -name "*.java")
java -cp out org.example.Main
```
