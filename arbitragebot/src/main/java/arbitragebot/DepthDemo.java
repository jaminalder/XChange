package arbitragebot;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.cexio.CexIOExchange;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.poloniex.PoloniexExchange;
import org.knowm.xchange.service.marketdata.MarketDataService;

import java.io.IOException;

/**
 * Author: brox Since: 2/6/14
 */

public class DepthDemo {

  public static void main(String[] args) throws IOException {

    CurrencyPair currencyPair = CurrencyPair.ZEC_BTC;
    // Use the factory to get Cex.IO exchange API using default settings
    Exchange cexExchange = ExchangeFactory.INSTANCE.createExchange(CexIOExchange.class.getName());
    Exchange poloniexExchange = ExchangeFactory.INSTANCE.createExchange(PoloniexExchange.class.getName());

    // Interested in the public market data feed (no authentication)
    MarketDataService marketDataService = poloniexExchange.getMarketDataService();

    // Get the latest order book data for GHs/BTC
    OrderBook orderBook = marketDataService.getOrderBook(currencyPair);

    System.out.println("Current Order Book size for GHS/BTC: " + (orderBook.getAsks().size() + orderBook.getBids().size()));
    System.out.println("First Ask: " + orderBook.getAsks().get(0).toString());
    System.out.println("First Bid: " + orderBook.getBids().get(0).toString());
    System.out.println("Timestamp: " + orderBook.getTimeStamp().toString());
    // System.out.println(orderBook.toString());
  }

}
