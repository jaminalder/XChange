package arbitragebot;

import org.junit.Test;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.bitfinex.v1.BitfinexExchange;
import org.knowm.xchange.bittrex.BittrexExchange;
import org.knowm.xchange.cexio.CexIOExchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.hitbtc.v2.HitbtcExchange;
import org.knowm.xchange.kraken.KrakenExchange;
import org.knowm.xchange.poloniex.PoloniexExchange;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class CurrencyPairsOnlineTest {

    List<CurrencyPair> currencyPairs = Arrays.asList(
            CurrencyPair.ZEC_BTC,
            CurrencyPair.ETH_BTC,
            CurrencyPair.XRP_BTC,
            CurrencyPair.DASH_BTC
            //CurrencyPair.BCH_BTC
    );

    @Test
    public void testCurrencyPairsForPoloniex() throws IOException, InterruptedException {
        Exchange exchange = ExchangeFactory.INSTANCE.createExchange(PoloniexExchange.class.getName());
        checkPairsOnExchange(exchange, currencyPairs);
    }

    @Test
    public void testCurrencyPairsForCex() throws IOException, InterruptedException {
        Exchange exchange = ExchangeFactory.INSTANCE.createExchange(CexIOExchange.class.getName());
        checkPairsOnExchange(exchange, currencyPairs);
    }

    @Test
    public void testCurrencyPairsForKraken() throws IOException, InterruptedException {
        Exchange exchange = ExchangeFactory.INSTANCE.createExchange(KrakenExchange.class.getName());
        checkPairsOnExchange(exchange, currencyPairs);
    }

    @Test
    public void testCurrencyPairsForBitfinex() throws IOException, InterruptedException {
        Exchange exchange = ExchangeFactory.INSTANCE.createExchange(BitfinexExchange.class.getName());
        checkPairsOnExchange(exchange, currencyPairs);
    }

    @Test
    public void testCurrencyPairsForBittrex() throws IOException, InterruptedException {
        Exchange exchange = ExchangeFactory.INSTANCE.createExchange(BittrexExchange.class.getName());
        checkPairsOnExchange(exchange, currencyPairs);
    }

    @Test
    public void testCurrencyPairsForHitbtc() throws IOException, InterruptedException {
        Exchange exchange = ExchangeFactory.INSTANCE.createExchange(HitbtcExchange.class.getName());
        checkPairsOnExchange(exchange, currencyPairs);
    }

    private void checkPairsOnExchange(Exchange exchange, List<CurrencyPair> pairs) throws IOException, InterruptedException {
        for (CurrencyPair currencyPair : pairs) {
            OrderBook orderBook = exchange.getMarketDataService().getOrderBook(currencyPair);
            System.out.println(orderBook);
            assertTrue("no bids for " + currencyPair + " on " + exchange, orderBook.getBids().size() > 5);
            assertTrue("no asks for " + currencyPair + " on " + exchange,orderBook.getAsks().size() > 5);
            Thread.sleep(1200);
        }
    }
}
