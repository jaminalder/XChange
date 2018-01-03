package arbitragebot;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.bitfinex.v1.BitfinexExchange;
import org.knowm.xchange.bittrex.BittrexExchange;
import org.knowm.xchange.cexio.CexIOExchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.hitbtc.v2.HitbtcExchange;
import org.knowm.xchange.kraken.KrakenExchange;
import org.knowm.xchange.poloniex.PoloniexExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArbitrageFinder {

    private static final Logger log = LoggerFactory.getLogger(ArbitrageFinder.class);

    private final Exchange cexExchange = ExchangeFactory.INSTANCE.createExchange(CexIOExchange.class.getName());
    private final Exchange poloniexExchange = ExchangeFactory.INSTANCE.createExchange(PoloniexExchange.class.getName());
    private final Exchange krakenExchange = ExchangeFactory.INSTANCE.createExchange(KrakenExchange.class.getName());
    private final Exchange bittrexExchange = ExchangeFactory.INSTANCE.createExchange(BittrexExchange.class.getName());
    private final Exchange bitfinexExchange = ExchangeFactory.INSTANCE.createExchange(BitfinexExchange.class.getName());
    private final Exchange hitbtcExchange = ExchangeFactory.INSTANCE.createExchange(HitbtcExchange.class.getName());

    private final List<CurrencyPair> currencyPairs = Arrays.asList(
            CurrencyPair.ZEC_BTC,
            CurrencyPair.ETH_BTC,
            CurrencyPair.XRP_BTC,
            CurrencyPair.DASH_BTC
            //CurrencyPair.BCH_BTC
    );

    private final ArbitrageOpportunityConfigBuilder defaultConfig = ArbitrageOpportunityConfigBuilder.anArbitrageOpportunityConfig()
            .withThreshold(1.5)
            .withAmount(0.1);

    public static void main(String[] args) throws InterruptedException {
        ArbitrageFinder arbitrageFinder = new ArbitrageFinder();
        arbitrageFinder.run();
    }

    private void run() throws InterruptedException {

        List<ArbitrageOpportunityChecker> checkers = createCheckers(cexExchange, krakenExchange, currencyPairs);
        checkers.addAll(createCheckers(krakenExchange, cexExchange, currencyPairs));
        checkers.addAll(createCheckers(poloniexExchange, krakenExchange, currencyPairs));
        checkers.addAll(createCheckers(poloniexExchange, bittrexExchange, currencyPairs));
        checkers.addAll(createCheckers(poloniexExchange, bitfinexExchange, currencyPairs));
        checkers.addAll(createCheckers(poloniexExchange, hitbtcExchange, currencyPairs));
        checkers.addAll(createCheckers(krakenExchange, bittrexExchange, currencyPairs));
        checkers.addAll(createCheckers(krakenExchange, bitfinexExchange, currencyPairs));
        checkers.addAll(createCheckers(krakenExchange, hitbtcExchange, currencyPairs));
        checkers.addAll(createCheckers(bittrexExchange, bitfinexExchange, currencyPairs));
        checkers.addAll(createCheckers(bittrexExchange, hitbtcExchange, currencyPairs));
        checkers.addAll(createCheckers(bitfinexExchange, hitbtcExchange, currencyPairs));

        while (true) {
            checkAndLog(checkers);
        }
    }

    private List<ArbitrageOpportunityChecker> createCheckers(Exchange exchange1, Exchange exchange2, List<CurrencyPair> pairs) {
        List<ArbitrageOpportunityChecker> checkers = new ArrayList<>();

        for (CurrencyPair pair : pairs) {

            checkers.add(new ArbitrageOpportunityChecker(
                    defaultConfig.but()
                            .withBuyExchange(exchange1)
                            .withSellExchange(exchange2)
                            .withCurrencyPair(pair).build()));

            checkers.add(new ArbitrageOpportunityChecker(
                    defaultConfig.but()
                            .withBuyExchange(exchange2)
                            .withSellExchange(exchange1)
                            .withCurrencyPair(pair).build()));
        }

        return checkers;
    }

    private void checkAndLog(List<ArbitrageOpportunityChecker> checkers) throws InterruptedException {
        for (ArbitrageOpportunityChecker checker : checkers) {

            ArbitrageOpportunityResult checkResult = checker.check();

            if (checkResult.isOpportunity()) {
                log.info("FOUND OPPORTUNITY: " + checkResult);
            } else {
                log.trace(checkResult.toString());
            }

            Thread.sleep(1200);
        }
    }

}
