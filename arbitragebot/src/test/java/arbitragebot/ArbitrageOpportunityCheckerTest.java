package arbitragebot;

import org.junit.Test;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.service.marketdata.MarketDataService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ArbitrageOpportunityCheckerTest {

    @Test
    public void testOpportunityConfig() throws IOException {
        ArbitrageOpportunityConfig config = createConfig();

        LimitOrder ask = new LimitOrder.Builder(Order.OrderType.ASK, config.getCurrencyPair())
                .originalAmount(BigDecimal.valueOf(config.getAmount() + 0.5))
                .limitPrice(BigDecimal.valueOf(0.1))
                .build();

        LimitOrder bid = new LimitOrder.Builder(Order.OrderType.ASK, config.getCurrencyPair())
                .originalAmount(BigDecimal.valueOf(config.getAmount() + 0.5))
                .limitPrice(BigDecimal.valueOf(0.08))
                .build();

        mockOrderBook(config.getBuyExchange(), config.getCurrencyPair(), Arrays.asList(ask), Arrays.asList());
        mockOrderBook(config.getSellExchange(), config.getCurrencyPair(), Arrays.asList(), Arrays.asList(bid));

        ArbitrageOpportunityChecker checker = new ArbitrageOpportunityChecker(config);

        ArbitrageOpportunityResult result = checker.check();

        assertEquals(config.getBuyExchange(), result.getConfig().getBuyExchange());
        assertEquals(config.getSellExchange(), result.getConfig().getSellExchange());
        assertEquals(config.getCurrencyPair(), result.getConfig().getCurrencyPair());
        assertTrue(config.getAmount() == result.getConfig().getAmount());
        assertTrue(config.getThreshold() == result.getConfig().getThreshold());

    }

    @Test
    public void testNoOpportunity() throws IOException {

        ArbitrageOpportunityConfig config = createConfig();

        LimitOrder ask = new LimitOrder.Builder(Order.OrderType.ASK, config.getCurrencyPair())
                .originalAmount(BigDecimal.valueOf(config.getAmount() + 0.5))
                .limitPrice(BigDecimal.valueOf(0.1))
                .build();

        LimitOrder bid = new LimitOrder.Builder(Order.OrderType.ASK, config.getCurrencyPair())
                .originalAmount(BigDecimal.valueOf(config.getAmount() + 0.5))
                .limitPrice(BigDecimal.valueOf(0.09))
                .build();

        mockOrderBook(config.getBuyExchange(), config.getCurrencyPair(), Arrays.asList(ask), Arrays.asList());
        mockOrderBook(config.getSellExchange(), config.getCurrencyPair(), Arrays.asList(), Arrays.asList(bid));

        ArbitrageOpportunityChecker checker = new ArbitrageOpportunityChecker(config);

        ArbitrageOpportunityResult result = checker.check();

        assertFalse(result.isOpportunity());
        assertEquals(-10.0, result.getPercentPriceDiff(), 0.0001);
    }

    @Test
    public void testOpportunity() throws IOException {
        ArbitrageOpportunityConfig config = createConfig();

        LimitOrder ask = new LimitOrder.Builder(Order.OrderType.ASK, config.getCurrencyPair())
                .originalAmount(BigDecimal.valueOf(config.getAmount() + 0.5))
                .limitPrice(BigDecimal.valueOf(0.1))
                .build();

        LimitOrder bid = new LimitOrder.Builder(Order.OrderType.ASK, config.getCurrencyPair())
                .originalAmount(BigDecimal.valueOf(config.getAmount() + 0.5))
                .limitPrice(BigDecimal.valueOf(0.11))
                .build();

        mockOrderBook(config.getBuyExchange(), config.getCurrencyPair(), Arrays.asList(ask), Arrays.asList());
        mockOrderBook(config.getSellExchange(), config.getCurrencyPair(), Arrays.asList(), Arrays.asList(bid));

        ArbitrageOpportunityChecker checker = new ArbitrageOpportunityChecker(config);

        ArbitrageOpportunityResult result = checker.check();

        assertTrue(result.isOpportunity());
        assertEquals(10.0, result.getPercentPriceDiff(), 0.0001);
    }

    private ArbitrageOpportunityConfig createConfig() {
        CurrencyPair currencyPair = CurrencyPair.ZEC_BTC;
        Exchange buyExchange = mock(Exchange.class);
        Exchange sellExchange = mock(Exchange.class);
        double amount = 0.1; // amount to trade
        double threshold = 1.5; // percent change

        return new ArbitrageOpportunityConfig(buyExchange, sellExchange, currencyPair, amount, threshold);
    }

    private void mockOrderBook(Exchange mockedExchange, CurrencyPair currencyPair, List<LimitOrder> asks, List<LimitOrder> bids) throws IOException {
        OrderBook orderBook = new OrderBook(new Date(), asks, bids);
        MarketDataService marketDataService = mock(MarketDataService.class);
        when(marketDataService.getOrderBook(currencyPair)).thenReturn(orderBook);
        when(mockedExchange.getMarketDataService()).thenReturn(marketDataService);
    }

}
