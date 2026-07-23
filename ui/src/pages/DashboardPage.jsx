import { useNavigate } from 'react-router-dom';
import {
  Users,
  CreditCard,
  Briefcase,
  Building2,
  Send,
  History,
  DollarSign,
  Clock,
  ClipboardList,
  Search,
  AlertTriangle,
  Percent,
} from 'lucide-react';
import Header from '../components/layout/Header';
import Sidebar from '../components/layout/Sidebar';
import DocumentBanner from '../components/dashboard/DocumentBanner';
import Card from '../components/common/Card';
import KPICard from '../components/common/KPICard';
import MetricGrid from '../components/dashboard/MetricGrid';
import ArchitectureGraph from '../components/dashboard/ArchitectureGraph';
import TrendChart from '../components/dashboard/TrendChart';
import RiskMeter from '../components/dashboard/RiskMeter';
import RiskHeatmap from '../components/dashboard/RiskHeatmap';
import ActionCard from '../components/dashboard/ActionCard';
import CollapsibleCard from '../components/dashboard/CollapsibleCard';
import { Code2, Users as UsersIcon, Calendar } from '../components/dashboard/CollapsibleCard';
import {
  documentSummary,
  enterpriseImpact,
  customerImpact,
  transactionTrend,
  businessImpact,
  engineeringSummary,
  heatmapData,
  recommendedActions,
  aiRecommendations,
} from '../data/mockData';
import './DashboardPage.css';

export default function DashboardPage() {
  const navigate = useNavigate();

  return (
    <div className="dashboard-page">
      <Header />

      <div className="dashboard-page__layout">
        <main className="dashboard-page__main">
          <DocumentBanner document={documentSummary} />

          {/* Section 1: Enterprise Impact */}
          <section className="dashboard-section">
            <Card title="Enterprise Impact">
              <MetricGrid metrics={enterpriseImpact} />
              <ArchitectureGraph />
            </Card>
          </section>

          {/* Section 2: Customer & Transaction Impact */}
          <section className="dashboard-section">
            <Card title="Customer & Transaction Impact">
              <div className="grid-3">
                <KPICard label="Potential Customer Matches" value={customerImpact.customerMatches} icon={Users} />
                <KPICard label="Pending Payments" value={customerImpact.pendingPayments} icon={CreditCard} />
                <KPICard label="Trade Finance Deals" value={customerImpact.tradeFinanceDeals} icon={Briefcase} />
                <KPICard label="Corporate Accounts" value={customerImpact.corporateAccounts} icon={Building2} />
                <KPICard label="SWIFT Messages" value={customerImpact.swiftMessages} icon={Send} />
                <KPICard
                  label="Historical Transactions to Rescreen"
                  value={customerImpact.historicalTransactions.toLocaleString()}
                  icon={History}
                  variant="accent"
                />
              </div>
              <div className="dashboard-section__chart-label">Impacted Transactions Over Time</div>
              <TrendChart data={transactionTrend} />
            </Card>
          </section>

          {/* Section 3: Business Impact */}
          <section className="dashboard-section">
            <Card title="Business Impact">
              <div className="grid-3">
                <KPICard label="Revenue at Risk" value={businessImpact.revenueAtRisk} icon={DollarSign} variant="critical" size="large" />
                <KPICard label="Settlement Delay" value={businessImpact.settlementDelay} icon={Clock} />
                <KPICard label="Operational Reviews" value={businessImpact.operationalReviews} icon={ClipboardList} />
                <KPICard label="Manual Investigation Hours" value={businessImpact.manualInvestigationHours} icon={Search} />
                <KPICard label="Business Criticality" value={businessImpact.businessCriticality} icon={AlertTriangle} variant="critical" />
                <KPICard label="Payment Disruption" value={businessImpact.paymentDisruption} icon={Percent} />
              </div>
              <RiskMeter score={businessImpact.impactScore} />
            </Card>
          </section>

          {/* Section 4: Engineering Impact */}
          <section className="dashboard-section">
            <CollapsibleCard
              title="Engineering Impact"
              defaultExpanded={false}
              summary={[
                { icon: Code2, label: 'Story Points', value: engineeringSummary.storyPoints },
                { icon: UsersIcon, label: 'Teams', value: engineeringSummary.teams },
                { icon: Calendar, label: 'Estimated Completion', value: `${engineeringSummary.estimatedDays} Days` },
              ]}
              onNavigate={() => navigate('/engineering')}
            />
          </section>

          {/* Section 5: Risk Heatmap */}
          <section className="dashboard-section">
            <Card title="Risk Heatmap">
              <RiskHeatmap data={heatmapData} />
            </Card>
          </section>

          {/* Section 6: Recommended Actions */}
          <section className="dashboard-section">
            <Card title="Recommended Actions">
              <div className="dashboard-actions">
                {recommendedActions.map((action) => (
                  <ActionCard key={action.id} {...action} />
                ))}
              </div>
            </Card>
          </section>
        </main>

        <Sidebar recommendations={aiRecommendations} />
      </div>
    </div>
  );
}
