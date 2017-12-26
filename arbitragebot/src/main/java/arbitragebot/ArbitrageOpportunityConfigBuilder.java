package arbitragebot;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;

public final class ArbitrageOpportunityConfigBuilder {
    private Exchange buyExchange;
    private Exchange sellExchange;
    private CurrencyPair currencyPair;
    private double amount;
    private double threshold;

    private ArbitrageOpportunityConfigBuilder() {
    }

    public static ArbitrageOpportunityConfigBuilder anArbitrageOpportunityConfig() {
        return new ArbitrageOpportunityConfigBuilder();
    }

    public ArbitrageOpportunityConfigBuilder withBuyExchange(Exchange buyExchange) {
        this.buyExchange = buyExchange;
        return this;
    }

    public ArbitrageOpportunityConfigBuilder withSellExchange(Exchange sellExchange) {
        this.sellExchange = sellExchange;
        return this;
    }

    public ArbitrageOpportunityConfigBuilder withCurrencyPair(CurrencyPair currencyPair) {
        this.currencyPair = currencyPair;
        return this;
    }

    public ArbitrageOpportunityConfigBuilder withAmount(double amount) {
        this.amount = amount;
        return this;
    }

    public ArbitrageOpportunityConfigBuilder withThreshold(double threshold) {
        this.threshold = threshold;
        return this;
    }

    public ArbitrageOpportunityConfigBuilder but() {
        return anArbitrageOpportunityConfig().withBuyExchange(buyExchange).withSellExchange(sellExchange).withCurrencyPair(currencyPair).withAmount(amount).withThreshold(threshold);
    }

    public ArbitrageOpportunityConfig build() {
        return new ArbitrageOpportunityConfig(buyExchange, sellExchange, currencyPair, amount, threshold);
    }
}
