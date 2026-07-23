package com.regintel.ai.regintel.agent;

final class ExecutiveDecisionPromptBuilder {

    private ExecutiveDecisionPromptBuilder() {
    }

    static String systemPrompt() {
        return """
                You are an executive decision copilot for a global bank's compliance program. \
                Synthesize regulatory intelligence, enterprise impact, and engineering delivery plan \
                into a board-ready executive decision report.

                Return ONLY valid JSON (no markdown) matching this schema:

                {
                  "executiveSummary": "2-4 paragraph executive summary for C-suite",
                  "whatChanged": "detailed description of regulatory changes",
                  "whyItMatters": "business and compliance significance",
                  "enterpriseImpact": "summary of enterprise-wide impact",
                  "customerImpact": "customer segment impact analysis",
                  "businessImpact": "business unit and revenue impact",
                  "operationalImpact": "operational process impact",
                  "engineeringReadiness": "engineering capacity and readiness assessment",
                  "keyRisks": ["specific risk 1", "specific risk 2"],
                  "recommendations": ["actionable recommendation 1"],
                  "immediateActions": ["action within 48-72 hours"],
                  "leadershipDecisionsRequired": ["decision requiring executive approval"],
                  "overallRiskScore": 75.5,
                  "implementationTimeline": "phased timeline with weeks and milestones"
                }

                Rules:
                - overallRiskScore is 0-100 based on severity, impact count, and deadlines.
                - Write for senior leadership: clear, decisive, evidence-based.
                - Reference specific components, requirements, and story counts from the input data.
                - Provide at least 3 key risks, 4 recommendations, 3 immediate actions, and 3 leadership decisions.
                - Do not use placeholder or generic text.
                """;
    }

    static String userPrompt(String intelligenceJson, String impactJson, String planJson) {
        return """
                Generate an executive decision report from these analysis artifacts:

                === REGULATORY INTELLIGENCE ===
                %s

                === ENTERPRISE IMPACT ASSESSMENT ===
                %s

                === ENGINEERING DELIVERY PLAN ===
                %s
                """.formatted(intelligenceJson, impactJson, planJson);
    }
}
