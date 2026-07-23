package com.regintel.ai.regulationintelligence.agent;

import com.regintel.ai.regulation.entity.Regulation;

final class RegulationIntelligencePromptBuilder {

    private RegulationIntelligencePromptBuilder() {
    }

    static String systemPrompt() {
        return """
                You are a regulatory intelligence analyst. Analyze the provided regulatory document and \
                return ONLY valid JSON matching this exact schema (no markdown, no explanation):

                {
                  "documentType": "string",
                  "regulatoryAuthority": "string",
                  "publicationDate": "YYYY-MM-DD or null",
                  "effectiveDate": "YYYY-MM-DD or null",
                  "severity": "CRITICAL|HIGH|MEDIUM|LOW",
                  "executiveSummary": "string (2-4 sentences)",
                  "keyChanges": [
                    {
                      "changeId": "CHG-001",
                      "summary": "string",
                      "changeType": "NEW|MODIFIED|REMOVED",
                      "sourceReference": {
                        "pageNumber": 1,
                        "section": "Article/Section reference",
                        "supportingText": "quoted or paraphrased text from document",
                        "confidenceScore": 0.85
                      }
                    }
                  ],
                  "newRequirements": [
                    {
                      "requirementId": "REQ-001",
                      "title": "string",
                      "description": "string",
                      "sourceReference": {
                        "pageNumber": 1,
                        "section": "Article/Section reference",
                        "supportingText": "quoted or paraphrased text from document",
                        "confidenceScore": 0.85
                      }
                    }
                  ],
                  "removedRequirements": [],
                  "modifiedRequirements": [],
                  "deadlines": [
                    {
                      "description": "string",
                      "dueDate": "YYYY-MM-DD or null",
                      "affectedArea": "string"
                    }
                  ],
                  "jurisdictions": ["string"],
                  "industries": ["string"],
                  "sanctionsInformation": "string or null",
                  "businessDomains": ["string"]
                }

                Rules:
                - Extract real requirements from the document text; do not invent unrelated rules.
                - severity must be one of: CRITICAL, HIGH, MEDIUM, LOW.
                - changeType must be one of: NEW, MODIFIED, REMOVED.
                - confidenceScore must be between 0.0 and 1.0.
                - Include at least one newRequirement and one keyChange when the document has substantive content.
                - Use empty arrays when no removed/modified requirements exist.
                - Return JSON only.
                """;
    }

    static String userPrompt(Regulation regulation) {
        String effectiveDate = regulation.getEffectiveDate() != null
                ? regulation.getEffectiveDate().toString()
                : "unknown";
        String content = regulation.getRawContent() != null ? regulation.getRawContent() : "";

        // Truncate very long documents to stay within free-tier context limits
        int maxChars = 80_000;
        if (content.length() > maxChars) {
            content = content.substring(0, maxChars) + "\n\n[Document truncated for analysis]";
        }

        return """
                Analyze this regulatory document and produce structured intelligence JSON.

                Metadata:
                - Title: %s
                - Source/Authority: %s
                - Jurisdiction: %s
                - Document Type: %s
                - Effective Date: %s

                Document Text:
                %s
                """.formatted(
                regulation.getTitle(),
                regulation.getSource(),
                regulation.getJurisdiction(),
                regulation.getDocumentType(),
                effectiveDate,
                content);
    }
}
