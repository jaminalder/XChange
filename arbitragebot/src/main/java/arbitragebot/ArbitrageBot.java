package arbitragebot;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.bittrex.service.BittrexAccountServiceRaw;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.Balance;
import org.knowm.xchange.service.account.AccountService;

import java.io.IOException;
import java.util.Map;

public class ArbitrageBot {

    public static void main(String[] args) throws IOException {
        Exchange bittrexExchange = BittrexKeys.getExchange();
        Exchange krakenExchange = KrakenKeys.getExchange();

        AccountService bittrexAccountService = bittrexExchange.getAccountService();
        AccountService krakenAccountService = krakenExchange.getAccountService();

        generic(bittrexAccountService);
        generic(krakenAccountService);
    }

    private static void generic(AccountService accountService) throws IOException {

        System.out.println("----------GENERIC---------");

        Map<Currency, Balance> balances = accountService.getAccountInfo().getWallet().getBalances();
        System.out.println(balances);

    }
}
