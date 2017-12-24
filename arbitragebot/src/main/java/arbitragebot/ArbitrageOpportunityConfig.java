package arbitragebot;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;

public class ArbitrageOpportunityConfig {

    private Exchange buyExchange;
    private Exchange sellExchange;
    private CurrencyPair currencyPair;
    private double amount;
    private double threshold;

    public ArbitrageOpportunityConfig(Exchange buyExchange, Exchange sellExchange, CurrencyPair currencyPair, double amount, double threshold) {
        this.buyExchange = buyExchange;
        this.sellExchange = sellExchange;
        this.currencyPair = currencyPair;
        this.amount = amount;
        this.threshold = threshold;
    }

    public Exchange getBuyExchange() {
        return buyExchange;
    }

    public Exchange getSellExchange() {
        return sellExchange;
    }

    public CurrencyPair getCurrencyPair() {
        return currencyPair;
    }

    public double getAmount() {
        return amount;
    }

    public double getThreshold() {
        return threshold;
    }
}
