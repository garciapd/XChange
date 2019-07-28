package org.knowm.xchange.bitfinex.v1.service;


import java.io.IOException;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.bitfinex.v1.BitfinexExchange;
import org.knowm.xchange.bitfinex.v1.dto.account.BitfinexCurrencyLabels;
import si.mazi.rescu.RestProxyFactory;

@Ignore
public class BitfinexConfigTest {

    @Test
    public void testLoadCurrencyLabels() throws IOException {

        ExchangeSpecification exchangeSpecification = new ExchangeSpecification(BitfinexExchange.class);
        Exchange exchange = ExchangeFactory.INSTANCE.createExchange(exchangeSpecification);

        BitfinexConfig bitfinexConfig = RestProxyFactory.createProxy(
                BitfinexConfig.class,
                exchange.getExchangeSpecification().getSslUri());

        BitfinexCurrencyLabels currencyLabels = bitfinexConfig.getCurrencyLabels();

        Assert.assertEquals("Bitcoin", currencyLabels.getLabel("BTC"));
        Assert.assertEquals("Ethereum", currencyLabels.getLabel("ETH"));
        Assert.assertEquals("TRON", currencyLabels.getLabel("TRX"));
    }
}
