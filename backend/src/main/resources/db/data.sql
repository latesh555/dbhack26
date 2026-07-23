
INSERT INTO Business
(req_id, revenue_impact_per_day, settlement_delay, operations_review_count, investigation_hours, criticality, payment_disruption_percentage)
VALUES
    (1001, '500000', '2 hours', 12, 8, 'HIGH', 15.50);

INSERT INTO Business
(req_id, revenue_impact_per_day, settlement_delay, operations_review_count, investigation_hours, criticality, payment_disruption_percentage)
VALUES
    (1002, '250000', '4 hours', 8, 5, 'MEDIUM', 10.25);

INSERT INTO Business
(req_id, revenue_impact_per_day, settlement_delay, operations_review_count, investigation_hours, criticality, payment_disruption_percentage)
VALUES
    (1003, '750000', '1 hour', 20, 12, 'CRITICAL', 25.75);

INSERT INTO Business
(req_id, revenue_impact_per_day, settlement_delay, operations_review_count, investigation_hours, criticality, payment_disruption_percentage)
VALUES
    (1004, '150000', '6 hours', 5, 3, 'LOW', 5.00);

INSERT INTO Business
(req_id, revenue_impact_per_day, settlement_delay, operations_review_count, investigation_hours, criticality, payment_disruption_percentage)
VALUES
    (1005, '400000', '3 hours', 10, 7, 'HIGH', 18.00);







INSERT INTO Customer
(req_id, matches, pending_payments, trade_finance_deals, corporate_accounts, swift_messages, transactions_to_rescreen)
VALUES
    (1001, 125, 35, 18, 250, 1250, 85);

INSERT INTO Customer
(req_id, matches, pending_payments, trade_finance_deals, corporate_accounts, swift_messages, transactions_to_rescreen)
VALUES
    (1002, 80, 20, 12, 180, 850, 45);

INSERT INTO Customer
(req_id, matches, pending_payments, trade_finance_deals, corporate_accounts, swift_messages, transactions_to_rescreen)
VALUES
    (1003, 250, 75, 35, 420, 2100, 150);

INSERT INTO Customer
(req_id, matches, pending_payments, trade_finance_deals, corporate_accounts, swift_messages, transactions_to_rescreen)
VALUES
    (1004, 45, 10, 5, 95, 450, 20);

INSERT INTO Customer
(req_id, matches, pending_payments, trade_finance_deals, corporate_accounts, swift_messages, transactions_to_rescreen)
VALUES
    (1005, 160, 42, 22, 310, 1500, 95);






INSERT INTO Enterprise
(req_id, applications, trade_finance_risk, swift_gateway_risk, customer_onboarding_hours, trade_settlements_affected, aml_monitoring)
VALUES
    (1001, 120, 'HIGH', 'MEDIUM', 48, 35, 'ACTIVE');

INSERT INTO Enterprise
(req_id, applications, trade_finance_risk, swift_gateway_risk, customer_onboarding_hours, trade_settlements_affected, aml_monitoring)
VALUES
    (1002, 85, 'MEDIUM', 'LOW', 24, 20, 'ACTIVE');

INSERT INTO Enterprise
(req_id, applications, trade_finance_risk, swift_gateway_risk, customer_onboarding_hours, trade_settlements_affected, aml_monitoring)
VALUES
    (1003, 250, 'CRITICAL', 'HIGH', 72, 85, 'REQUIRES_REVIEW');

INSERT INTO Enterprise
(req_id, applications, trade_finance_risk, swift_gateway_risk, customer_onboarding_hours, trade_settlements_affected, aml_monitoring)
VALUES
    (1004, 50, 'LOW', 'LOW', 12, 8, 'ACTIVE');

INSERT INTO Enterprise
(req_id, applications, trade_finance_risk, swift_gateway_risk, customer_onboarding_hours, trade_settlements_affected, aml_monitoring)
VALUES
    (1005, 175, 'HIGH', 'HIGH', 60, 55, 'ENHANCED_MONITORING');







INSERT INTO summary
(req_id, type, published, effective_date, deadline, severity, confidence, summary)
VALUES
    (1001, 'REGULATION', '2026-06-15', '2026-08-01', '2026-07-25',
     'HIGH', 0.95, 'New regulatory requirements impacting cross-border payment processing and settlement operations.');

INSERT INTO summary
(req_id, type, published, effective_date, deadline, severity, confidence, summary)
VALUES
    (1002, 'DIRECTIVE', '2026-06-20', '2026-09-01', '2026-08-15',
     'MEDIUM', 0.89, 'Updated compliance requirements for customer due diligence and transaction monitoring processes.');

INSERT INTO summary
(req_id, type, published, effective_date, deadline, severity, confidence, summary)
VALUES
    (1003, 'REGULATION', '2026-06-25', '2026-07-15', '2026-07-30',
     'CRITICAL', 0.98, 'Urgent regulatory changes affecting AML monitoring, trade finance activities, and international payment systems.');

INSERT INTO summary
(req_id, type, published, effective_date, deadline, severity, confidence, summary)
VALUES
    (1004, 'GUIDELINE', '2026-07-01', '2026-10-01', '2026-09-15',
     'LOW', 0.85, 'Revised operational guidelines for improving internal compliance reviews and regulatory reporting procedures.');

INSERT INTO summary
(req_id, type, published, effective_date, deadline, severity, confidence, summary)
VALUES
    (1005, 'DIRECTIVE', '2026-07-05', '2026-08-15', '2026-08-05',
     'HIGH', 0.92, 'New requirements for financial institutions covering transaction screening, payment controls, and regulatory compliance monitoring.');





