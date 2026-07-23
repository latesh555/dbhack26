package com.regintel.ai.enterpriseimpact.knowledge;

import com.regintel.ai.enterpriseimpact.entity.ComponentType;
import com.regintel.ai.enterpriseimpact.entity.ImpactCategory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MockEnterpriseKnowledgeBase {

    public List<KnowledgeBaseComponent> getAllComponents() {
        return List.of(
                component("Trade Finance Portal", ComponentType.APPLICATION, ImpactCategory.APPLICATION,
                        List.of("trade", "finance", "letter of credit", "lc", "documentary", "export", "import"),
                        List.of("EU", "UK", "US", "SG"),
                        List.of("Banking", "Trade Finance"),
                        List.of("Trade Finance", "Corporate Banking"),
                        "Primary portal for trade finance operations and LC management"),

                component("Payment Gateway", ComponentType.APPLICATION, ImpactCategory.APPLICATION,
                        List.of("payment", "transfer", "swift", "ach", "cross-border", "settlement"),
                        List.of("EU", "US", "UK"),
                        List.of("Banking", "Payments"),
                        List.of("Payments", "Retail Banking"),
                        "Customer-facing payment initiation and tracking application"),

                component("Customer Onboarding Platform", ComponentType.APPLICATION, ImpactCategory.APPLICATION,
                        List.of("kyc", "onboarding", "customer", "identity", "cdd", "due diligence"),
                        List.of("EU", "US", "UK"),
                        List.of("Banking", "Financial Services"),
                        List.of("Customer Lifecycle", "Compliance"),
                        "Digital onboarding for corporate and retail customers"),

                component("AML Screening Console", ComponentType.APPLICATION, ImpactCategory.APPLICATION,
                        List.of("aml", "sanctions", "screening", "watchlist", "pep", "compliance"),
                        List.of("EU", "US", "UK", "SG"),
                        List.of("Banking", "Financial Services"),
                        List.of("Compliance", "Risk Management"),
                        "Anti-money laundering and sanctions screening operations console"),

                component("payment-processor", ComponentType.MICROSERVICE, ImpactCategory.MICROSERVICE,
                        List.of("payment", "transaction", "settlement", "swift", "ach"),
                        List.of("EU", "US"),
                        List.of("Banking", "Payments"),
                        List.of("Payments"),
                        "Core payment processing microservice"),

                component("trade-finance-engine", ComponentType.MICROSERVICE, ImpactCategory.MICROSERVICE,
                        List.of("trade", "finance", "lc", "documentary", "guarantee"),
                        List.of("EU", "UK", "SG"),
                        List.of("Banking", "Trade Finance"),
                        List.of("Trade Finance"),
                        "Trade finance product orchestration engine"),

                component("kyc-service", ComponentType.MICROSERVICE, ImpactCategory.MICROSERVICE,
                        List.of("kyc", "identity", "cdd", "customer", "onboarding"),
                        List.of("EU", "US", "UK"),
                        List.of("Banking", "Financial Services"),
                        List.of("Compliance", "Customer Lifecycle"),
                        "Know-your-customer verification microservice"),

                component("sanctions-checker", ComponentType.MICROSERVICE, ImpactCategory.MICROSERVICE,
                        List.of("sanctions", "ofac", "screening", "watchlist", "embargo"),
                        List.of("EU", "US", "UK"),
                        List.of("Banking", "Financial Services"),
                        List.of("Compliance", "Risk Management"),
                        "Real-time sanctions and embargo screening service"),

                component("POST /api/v1/payments", ComponentType.API, ImpactCategory.API,
                        List.of("payment", "transfer", "transaction", "settlement"),
                        List.of("EU", "US"),
                        List.of("Banking", "Payments"),
                        List.of("Payments"),
                        "Payment initiation and status API"),

                component("POST /api/v1/trades", ComponentType.API, ImpactCategory.API,
                        List.of("trade", "finance", "lc", "documentary"),
                        List.of("EU", "UK", "SG"),
                        List.of("Banking", "Trade Finance"),
                        List.of("Trade Finance"),
                        "Trade finance deal creation and amendment API"),

                component("GET /api/v1/customers/{id}/kyc", ComponentType.API, ImpactCategory.API,
                        List.of("kyc", "customer", "cdd", "identity"),
                        List.of("EU", "US", "UK"),
                        List.of("Banking", "Financial Services"),
                        List.of("Compliance", "Customer Lifecycle"),
                        "Customer KYC profile retrieval API"),

                component("payments_db", ComponentType.DATABASE, ImpactCategory.DATABASE,
                        List.of("payment", "transaction", "settlement", "retention", "audit"),
                        List.of("EU", "US"),
                        List.of("Banking", "Payments"),
                        List.of("Payments"),
                        "Primary payments transactional database"),

                component("trade_finance_db", ComponentType.DATABASE, ImpactCategory.DATABASE,
                        List.of("trade", "finance", "lc", "documentary", "retention"),
                        List.of("EU", "UK", "SG"),
                        List.of("Banking", "Trade Finance"),
                        List.of("Trade Finance"),
                        "Trade finance deals and documents database"),

                component("customer_db", ComponentType.DATABASE, ImpactCategory.DATABASE,
                        List.of("customer", "kyc", "pii", "identity", "retention"),
                        List.of("EU", "US", "UK"),
                        List.of("Banking", "Financial Services"),
                        List.of("Customer Lifecycle", "Compliance"),
                        "Customer master and KYC records database"),

                component("Compliance Team", ComponentType.BUSINESS_TEAM, ImpactCategory.BUSINESS_TEAM,
                        List.of("compliance", "regulatory", "audit", "sanctions", "aml", "deadline"),
                        List.of("EU", "US", "UK", "SG"),
                        List.of("Banking", "Financial Services"),
                        List.of("Compliance"),
                        "Enterprise compliance and regulatory affairs team"),

                component("Trade Finance Operations", ComponentType.BUSINESS_TEAM, ImpactCategory.BUSINESS_TEAM,
                        List.of("trade", "finance", "lc", "documentary", "operations"),
                        List.of("EU", "UK", "SG"),
                        List.of("Banking", "Trade Finance"),
                        List.of("Trade Finance"),
                        "Trade finance back-office operations team"),

                component("Payments Team", ComponentType.BUSINESS_TEAM, ImpactCategory.BUSINESS_TEAM,
                        List.of("payment", "settlement", "swift", "operations"),
                        List.of("EU", "US"),
                        List.of("Banking", "Payments"),
                        List.of("Payments"),
                        "Payments operations and settlement team"),

                component("EU Corporate Clients", ComponentType.CUSTOMER, ImpactCategory.CUSTOMER,
                        List.of("eu", "gdpr", "corporate", "cross-border"),
                        List.of("EU"),
                        List.of("Banking", "Corporate Banking"),
                        List.of("Corporate Banking"),
                        "Corporate banking customers domiciled in the EU"),

                component("US Financial Institutions", ComponentType.CUSTOMER, ImpactCategory.CUSTOMER,
                        List.of("us", "ofac", "financial institution", "bank"),
                        List.of("US"),
                        List.of("Banking", "Financial Services"),
                        List.of("Corporate Banking", "Markets"),
                        "US-based bank and FI counterparties"),

                component("Asia-Pacific Exporters", ComponentType.CUSTOMER, ImpactCategory.CUSTOMER,
                        List.of("export", "trade", "asia", "sg", "documentary"),
                        List.of("SG", "HK"),
                        List.of("Trade Finance", "Corporate Banking"),
                        List.of("Trade Finance"),
                        "Export-oriented corporate clients in APAC"),

                component("SWIFT Cross-Border Payments", ComponentType.TRANSACTION, ImpactCategory.TRANSACTION,
                        List.of("swift", "payment", "cross-border", "transfer", "settlement"),
                        List.of("EU", "US", "UK", "SG"),
                        List.of("Banking", "Payments"),
                        List.of("Payments"),
                        "Cross-border SWIFT payment transaction flow"),

                component("ACH Domestic Transfers", ComponentType.TRANSACTION, ImpactCategory.TRANSACTION,
                        List.of("ach", "payment", "domestic", "transfer"),
                        List.of("US"),
                        List.of("Banking", "Payments"),
                        List.of("Payments"),
                        "US domestic ACH transfer processing"),

                component("Import Letter of Credit", ComponentType.TRADE_FINANCE_DEAL, ImpactCategory.TRANSACTION,
                        List.of("import", "lc", "letter of credit", "trade", "documentary"),
                        List.of("EU", "UK", "SG"),
                        List.of("Trade Finance"),
                        List.of("Trade Finance"),
                        "Import LC trade finance product"),

                component("Export Documentary Collection", ComponentType.TRADE_FINANCE_DEAL, ImpactCategory.TRANSACTION,
                        List.of("export", "documentary", "collection", "trade"),
                        List.of("EU", "UK", "SG"),
                        List.of("Trade Finance"),
                        List.of("Trade Finance"),
                        "Export documentary collection deals"),

                component("Cross-Border Payment Records", ComponentType.PAYMENT_RECORD, ImpactCategory.TRANSACTION,
                        List.of("payment", "record", "retention", "audit", "cross-border"),
                        List.of("EU", "US", "UK"),
                        List.of("Banking", "Payments"),
                        List.of("Payments", "Compliance"),
                        "Archived cross-border payment audit records"),

                component("Regulatory Reporting Pipeline", ComponentType.PROCESS, ImpactCategory.OPERATIONAL,
                        List.of("reporting", "regulatory", "deadline", "compliance", "audit"),
                        List.of("EU", "US", "UK"),
                        List.of("Banking", "Financial Services"),
                        List.of("Compliance"),
                        "Operational pipeline for regulatory report submission"),

                component("Sanctions Screening Workflow", ComponentType.PROCESS, ImpactCategory.OPERATIONAL,
                        List.of("sanctions", "screening", "ofac", "watchlist", "operational"),
                        List.of("EU", "US", "UK"),
                        List.of("Banking", "Financial Services"),
                        List.of("Compliance", "Risk Management"),
                        "Daily sanctions screening operational workflow"),

                component("Payment Platform Engineering", ComponentType.PROCESS, ImpactCategory.ENGINEERING,
                        List.of("payment", "engineering", "api", "microservice", "change"),
                        List.of("EU", "US"),
                        List.of("Banking", "Payments"),
                        List.of("Payments"),
                        "Engineering squad responsible for payment platform changes"),

                component("Trade Finance Engineering", ComponentType.PROCESS, ImpactCategory.ENGINEERING,
                        List.of("trade", "finance", "engineering", "lc", "change"),
                        List.of("EU", "UK", "SG"),
                        List.of("Trade Finance"),
                        List.of("Trade Finance"),
                        "Engineering squad responsible for trade finance systems"),

                component("Transaction Monitoring Control", ComponentType.COMPLIANCE_CONTROL, ImpactCategory.COMPLIANCE_RISK,
                        List.of("aml", "monitoring", "transaction", "suspicious", "compliance"),
                        List.of("EU", "US", "UK"),
                        List.of("Banking", "Financial Services"),
                        List.of("Compliance", "Risk Management"),
                        "AML transaction monitoring compliance control"),

                component("Sanctions Compliance Control", ComponentType.COMPLIANCE_CONTROL, ImpactCategory.COMPLIANCE_RISK,
                        List.of("sanctions", "ofac", "embargo", "compliance", "screening"),
                        List.of("EU", "US", "UK"),
                        List.of("Banking", "Financial Services"),
                        List.of("Compliance"),
                        "Sanctions list screening compliance control")
        );
    }

    private KnowledgeBaseComponent component(
            String name,
            ComponentType componentType,
            ImpactCategory category,
            List<String> tags,
            List<String> jurisdictions,
            List<String> industries,
            List<String> businessDomains,
            String description) {
        return KnowledgeBaseComponent.builder()
                .name(name)
                .componentType(componentType)
                .category(category)
                .tags(tags)
                .jurisdictions(jurisdictions)
                .industries(industries)
                .businessDomains(businessDomains)
                .description(description)
                .build();
    }
}
