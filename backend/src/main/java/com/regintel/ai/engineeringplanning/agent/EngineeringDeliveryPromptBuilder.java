package com.regintel.ai.engineeringplanning.agent;

final class EngineeringDeliveryPromptBuilder {

    private EngineeringDeliveryPromptBuilder() {
    }

    static String systemPrompt() {
        return """
                You are a senior engineering delivery planner. Given an enterprise impact assessment, \
                produce a detailed, executable engineering delivery plan with epics, features, user stories, \
                tasks, acceptance criteria, and deployment strategies.

                Return ONLY valid JSON (no markdown) matching this schema:

                {
                  "affectedApis": ["string"],
                  "affectedMicroservices": ["string"],
                  "epic": {
                    "epicId": "EPIC-001",
                    "title": "string",
                    "description": "string",
                    "priority": "CRITICAL|HIGH|MEDIUM|LOW",
                    "ownerTeam": "string",
                    "totalStoryPoints": 0,
                    "features": [
                      {
                        "featureId": "FEAT-001",
                        "title": "string",
                        "description": "string",
                        "priority": "CRITICAL|HIGH|MEDIUM|LOW",
                        "ownerTeam": "string",
                        "userStories": [
                          {
                            "storyId": "US-001",
                            "title": "string",
                            "description": "As a [role], I need [change] so that [regulatory compliance goal]",
                            "priority": "CRITICAL|HIGH|MEDIUM|LOW",
                            "storyPoints": 5,
                            "ownerTeam": "string",
                            "dependencies": ["US-000"],
                            "affectedComponents": ["component-name"],
                            "acceptanceCriteria": ["string"],
                            "testingChecklist": ["string"],
                            "tasks": [
                              {
                                "taskId": "US-001-T1",
                                "title": "string",
                                "description": "string",
                                "priority": "CRITICAL|HIGH|MEDIUM|LOW",
                                "ownerTeam": "string",
                                "status": "TODO"
                              }
                            ],
                            "status": "TODO"
                          }
                        ]
                      }
                    ]
                  },
                  "strategies": {
                    "testingStrategy": "detailed testing approach",
                    "deploymentStrategy": "phased deployment plan",
                    "rollbackStrategy": "rollback procedures",
                    "productionValidationChecklist": ["string"]
                  }
                }

                Rules:
                - Create user stories for every significant impact item.
                - Database changes must be sequenced before dependent API/microservice stories (use dependencies).
                - Story points: CRITICAL=13, HIGH=8, MEDIUM=5, LOW=3.
                - Each user story needs at least 3 tasks and 3 acceptance criteria.
                - totalStoryPoints must equal sum of all user story points.
                - Be specific to the regulation and impacted components; no generic filler.
                """;
    }

    static String userPrompt(String impactAssessmentJson) {
        return """
                Generate an engineering delivery plan for this enterprise impact assessment:

                %s
                """.formatted(impactAssessmentJson);
    }
}
