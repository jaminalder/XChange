package arbitragebot;

import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

public class ArbitrageOpportunityChecker {

    private static final Logger log = LoggerFactory.getLogger(ArbitrageOpportunityChecker.class);

    private final ArbitrageOpportunityConfig config;
    private final MarketDataService buyService;
    private final MarketDataService sellService;

    public ArbitrageOpportunityChecker(ArbitrageOpportunityConfig config) {
        this.config = config;
        buyService = config.getBuyExchange().getMarketDataService();
        sellService = config.getSellExchange().getMarketDataService();
    }

    public ArbitrageOpportunityResult check() {
        try {
            List<LimitOrder> asks = buyService.getOrderBook(config.getCurrencyPair()).getAsks();
            List<LimitOrder> bids = sellService.getOrderBook(config.getCurrencyPair()).getBids();
            double askPrice = priceForAmount(asks, config.getAmount());
            double bidPrice = priceForAmount(bids, config.getAmount());
            double percentChange = percentChange(askPrice, bidPrice);
            return new ArbitrageOpportunityResult(config, percentChange > config.getThreshold(), percentChange, askPrice, bidPrice);
        } catch (Exception e) {
            log.warn("error while arbitrage check for " + config, e);
            return new ArbitrageOpportunityResult(config, false, 0.0, 0.0, 0.0);
        }
    }

    private double priceForAmount(List<LimitOrder> bidsOrAsks, double amount) throws Exception {
        Collections.sort(bidsOrAsks);
        double amountSum = 0.0;
        for (LimitOrder bidOrAsk : bidsOrAsks) {
            amountSum += bidOrAsk.getOriginalAmount().doubleValue();
            if((amountSum * bidOrAsk.getLimitPrice().doubleValue()) > amount){
                return bidOrAsk.getLimitPrice().doubleValue();
            }
        }
        throw new Exception("order book too small. available amount: " + amountSum + ", looked for: " + amount);
    }

    private double percentChange(double base, double changed) {
        return 100 * ((changed - base) / base);
    }
}
