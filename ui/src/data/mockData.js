export const recentUploads = [
  { id: 1, name: 'OFAC SDN Update July 2026', type: 'Sanctions', date: '22 Jul 2026' },
  { id: 2, name: 'RBI KYC Circular', type: 'Compliance', date: '18 Jul 2026' },
  { id: 3, name: 'FATF AML Guidance', type: 'AML', date: '15 Jul 2026' },
  { id: 4, name: 'ECB Reporting Update', type: 'Reporting', date: '10 Jul 2026' },
];

export const documentSummary = {
  type: 'OFAC Sanctions Update',
  published: '22 July 2026',
  effective: 'Immediate',
  severity: 'Critical',
  confidence: 98,
  summary:
    'This update introduces 42 newly sanctioned entities, expands restrictions on the energy sector, and requires immediate updates to transaction screening rules.',
};

export const enterpriseImpact = {
  applications: 12,
  microservices: 27,
  apis: 31,
  databases: 8,
  businessTeams: 6,
  repositories: 19,
  dependencies: 134,
};

export const customerImpact = {
  customerMatches: 128,
  pendingPayments: 186,
  tradeFinanceDeals: 41,
  corporateAccounts: 22,
  swiftMessages: 94,
  historicalTransactions: 38241,
};

export const transactionTrend = [
  { date: 'Jul 16', count: 420 },
  { date: 'Jul 17', count: 580 },
  { date: 'Jul 18', count: 720 },
  { date: 'Jul 19', count: 890 },
  { date: 'Jul 20', count: 1100 },
  { date: 'Jul 21', count: 1350 },
  { date: 'Jul 22', count: 1680 },
];

export const businessImpact = {
  revenueAtRisk: '$145K/day',
  settlementDelay: 'High',
  operationalReviews: 620,
  manualInvestigationHours: 190,
  businessCriticality: 'Critical',
  paymentDisruption: '18%',
  impactScore: 87,
};

export const engineeringSummary = {
  storyPoints: 23,
  teams: 3,
  estimatedDays: 4,
};

export const heatmapData = {
  rows: ['Compliance', 'Payments', 'Trade Finance', 'Operations', 'Technology', 'AML'],
  columns: ['Low', 'Medium', 'High', 'Critical'],
  values: [
    [0, 1, 2, 3],
    [0, 1, 3, 2],
    [1, 2, 3, 1],
    [2, 1, 1, 0],
    [1, 2, 2, 1],
    [0, 2, 3, 2],
  ],
  overallScore: 94,
  overallLevel: 'Critical',
};

export const recommendedActions = [
  { id: 1, title: 'Generate Jira Stories', icon: 'jira', description: 'Auto-create epics and stories' },
  { id: 2, title: 'Generate Executive Report', icon: 'report', description: 'Board-ready impact summary' },
  { id: 3, title: 'Draft Compliance Email', icon: 'email', description: 'Stakeholder notification draft' },
  { id: 4, title: 'Create PowerPoint', icon: 'ppt', description: 'Executive presentation deck' },
  { id: 5, title: 'Export Impact Analysis', icon: 'export', description: 'Full analysis export (PDF/CSV)' },
];

export const aiRecommendations = [
  { priority: 1, text: 'Refresh sanctions watchlist immediately.', confidence: 99 },
  { priority: 2, text: 'Pause transactions involving newly sanctioned entities.', confidence: 97 },
  { priority: 3, text: 'Run retrospective screening for the last 30 days.', confidence: 95 },
  { priority: 4, text: 'Notify Trade Finance Team.', confidence: 92 },
  { priority: 5, text: 'Deploy updated screening rules within 24 hours.', confidence: 88 },
];

export const engineeringTeams = [
  'Payments Team',
  'Trade Finance Team',
  'Compliance Tech',
  'Platform Team',
];

export const timelinePhases = [
  { phase: 'Today', status: 'current' },
  { phase: 'Development', status: 'upcoming' },
  { phase: 'Testing', status: 'upcoming' },
  { phase: 'UAT', status: 'upcoming' },
  { phase: 'Production', status: 'upcoming' },
];

export const jiraStories = [
  {
    id: 1,
    title: 'Update sanctions screening engine',
    priority: 'Critical',
    owner: 'Payments Team',
    storyPoints: 8,
    dependencies: ['Watchlist API v2', 'Screening Rules DB'],
    acceptanceCriteria: [
      'Integrate 42 new SDN entities into screening engine',
      'Update fuzzy matching thresholds for energy sector',
      'Validate screening latency < 200ms per transaction',
    ],
    affectedApis: ['/api/v2/screen', '/api/v1/sanctions/check'],
    affectedMicroservices: ['screening-engine', 'sanctions-service', 'watchlist-manager'],
    estimatedDuration: '2 days',
  },
  {
    id: 2,
    title: 'Refresh watchlists',
    priority: 'Critical',
    owner: 'Compliance Tech',
    storyPoints: 5,
    dependencies: ['OFAC SDN Feed', 'Internal Watchlist DB'],
    acceptanceCriteria: [
      'Import latest OFAC SDN list (July 2026)',
      'Reconcile with internal watchlist entries',
      'Generate audit trail for all changes',
    ],
    affectedApis: ['/api/v1/watchlist/sync', '/api/v1/watchlist/audit'],
    affectedMicroservices: ['watchlist-manager', 'compliance-feed-processor'],
    estimatedDuration: '1 day',
  },
  {
    id: 3,
    title: 'Modify payment validation rules',
    priority: 'High',
    owner: 'Trade Finance Team',
    storyPoints: 5,
    dependencies: ['Screening Engine v2.4', 'Payment Gateway'],
    acceptanceCriteria: [
      'Block payments to newly sanctioned entities',
      'Add energy sector restriction flags',
      'Update SWIFT message validation rules',
    ],
    affectedApis: ['/api/v1/payments/validate', '/api/v2/swift/process'],
    affectedMicroservices: ['payment-validator', 'swift-processor', 'trade-finance-core'],
    estimatedDuration: '1.5 days',
  },
  {
    id: 4,
    title: 'Run regression testing',
    priority: 'High',
    owner: 'Platform Team',
    storyPoints: 3,
    dependencies: ['All screening updates deployed to QA'],
    acceptanceCriteria: [
      'Execute full regression suite (1,240 test cases)',
      'Validate screening accuracy > 99.5%',
      'Performance benchmarks within SLA',
    ],
    affectedApis: ['All payment and screening APIs'],
    affectedMicroservices: ['All affected services in QA environment'],
    estimatedDuration: '1 day',
  },
  {
    id: 5,
    title: 'Deploy production rules',
    priority: 'Critical',
    owner: 'Platform Team',
    storyPoints: 2,
    dependencies: ['QA sign-off', 'Change Advisory Board approval'],
    acceptanceCriteria: [
      'Blue-green deployment to production',
      'Smoke tests pass on production',
      'Rollback plan validated and documented',
    ],
    affectedApis: ['Production screening and payment APIs'],
    affectedMicroservices: ['screening-engine', 'payment-validator', 'watchlist-manager'],
    estimatedDuration: '0.5 days',
  },
];

export const engineeringRecommendations = {
  potentialRisks: [
    'Screening latency may increase during watchlist refresh (estimated +50ms)',
    'Legacy SWIFT integration requires manual validation for MT103 messages',
    'Cross-region deployment sync delay between US and EU data centers',
  ],
  rollbackStrategy: [
    'Maintain previous watchlist snapshot (July 21, 2026)',
    'Feature flag: ENABLE_NEW_SDN_RULES for instant disable',
    'Automated rollback triggers if screening error rate > 0.1%',
    'Blue-green deployment allows instant traffic switchback',
  ],
  deploymentOrder: [
    '1. Deploy watchlist refresh (Compliance Tech)',
    '2. Deploy screening engine updates (Payments Team)',
    '3. Deploy payment validation rules (Trade Finance)',
    '4. Enable feature flag in production (Platform Team)',
    '5. Monitor metrics for 2 hours post-deployment',
  ],
  testingChecklist: [
    'Unit tests: 100% pass rate on screening logic',
    'Integration tests: Payment flow end-to-end validation',
    'Performance tests: Screening latency < 200ms at P99',
    'Security tests: No data leakage in audit logs',
    'UAT sign-off from Compliance and Operations',
  ],
};

export const architectureNodes = [
  { id: 'regulation', label: 'OFAC Update', type: 'regulation', x: 50, y: 20 },
  { id: 'compliance', label: 'Compliance', type: 'process', x: 20, y: 50 },
  { id: 'payments', label: 'Payments', type: 'process', x: 50, y: 50 },
  { id: 'trade', label: 'Trade Finance', type: 'process', x: 80, y: 50 },
  { id: 'screening', label: 'Screening Engine', type: 'service', x: 15, y: 80 },
  { id: 'watchlist', label: 'Watchlist Mgr', type: 'service', x: 40, y: 80 },
  { id: 'payment-api', label: 'Payment API', type: 'api', x: 65, y: 80 },
  { id: 'swift-api', label: 'SWIFT API', type: 'api', x: 85, y: 80 },
  { id: 'sanctions-db', label: 'Sanctions DB', type: 'database', x: 25, y: 95 },
  { id: 'txn-db', label: 'Transaction DB', type: 'database', x: 55, y: 95 },
  { id: 'owner', label: 'Compliance Tech', type: 'owner', x: 80, y: 95 },
];

export const architectureEdges = [
  ['regulation', 'compliance'],
  ['regulation', 'payments'],
  ['regulation', 'trade'],
  ['compliance', 'screening'],
  ['compliance', 'watchlist'],
  ['payments', 'payment-api'],
  ['payments', 'screening'],
  ['trade', 'swift-api'],
  ['trade', 'screening'],
  ['screening', 'sanctions-db'],
  ['watchlist', 'sanctions-db'],
  ['payment-api', 'txn-db'],
  ['swift-api', 'txn-db'],
  ['screening', 'owner'],
];
