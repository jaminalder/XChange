package arbitragebot;

import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.service.marketdata.MarketDataService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class ArbitrageOpportunityChecker {
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
            double percentChange = percentChange(asks.get(0).getLimitPrice(), bids.get(0).getLimitPrice());
            return new ArbitrageOpportunityResult(config, percentChange > config.getThreshold(), percentChange);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArbitrageOpportunityResult(config, false, 0.0);
        }
    }

    private double percentChange(BigDecimal base, BigDecimal changed) {
        return 100 * ((changed.doubleValue() - base.doubleValue()) / base.doubleValue());
    }
}
