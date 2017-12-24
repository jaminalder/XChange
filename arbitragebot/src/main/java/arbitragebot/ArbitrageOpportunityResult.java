package arbitragebot;

public class ArbitrageOpportunityResult {
    private ArbitrageOpportunityConfig config;
    private boolean isOpportunity;
    private double percentPriceDiff;

    public ArbitrageOpportunityResult(ArbitrageOpportunityConfig config, boolean isOpportunity, double percentPriceDiff) {

        this.config = config;
        this.isOpportunity = isOpportunity;
        this.percentPriceDiff = percentPriceDiff;
    }

    public boolean isOpportunity() {
        return isOpportunity;
    }

    public double getPercentPriceDiff() {
        return percentPriceDiff;
    }

    public ArbitrageOpportunityConfig getConfig() {
        return config;
    }
}
