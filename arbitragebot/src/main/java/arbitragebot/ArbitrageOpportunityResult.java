package arbitragebot;

public class ArbitrageOpportunityResult {
    private final ArbitrageOpportunityConfig config;
    private final boolean isOpportunity;
    private final double percentPriceDiff;
    private final double buyPrice;
    private final double sellPrice;


    public ArbitrageOpportunityResult(ArbitrageOpportunityConfig config, boolean isOpportunity, double percentPriceDiff, double buyPrice, double sellPrice) {

        this.config = config;
        this.isOpportunity = isOpportunity;
        this.percentPriceDiff = percentPriceDiff;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
    }

    public boolean isOpportunity() {
        return isOpportunity;
    }

    public double getPercentPriceDiff() {
        return percentPriceDiff;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public ArbitrageOpportunityConfig getConfig() {
        return config;
    }

    @Override
    public String toString() {
        return "ArbitrageOpportunityResult{" +
                "config=" + config +
                ", isOpportunity=" + isOpportunity +
                ", percentPriceDiff=" + percentPriceDiff +
                ", buyPrice=" + buyPrice +
                ", sellPrice=" + sellPrice +
                '}';
    }
}
