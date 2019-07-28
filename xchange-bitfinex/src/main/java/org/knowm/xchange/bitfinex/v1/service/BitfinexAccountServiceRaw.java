package org.knowm.xchange.bitfinex.v1.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.bitfinex.common.dto.BitfinexException;
import org.knowm.xchange.bitfinex.v1.dto.account.BitfinexAccountFeesResponse;
import org.knowm.xchange.bitfinex.v1.dto.account.BitfinexBalanceHistoryRequest;
import org.knowm.xchange.bitfinex.v1.dto.account.BitfinexBalanceHistoryResponse;
import org.knowm.xchange.bitfinex.v1.dto.account.BitfinexBalancesRequest;
import org.knowm.xchange.bitfinex.v1.dto.account.BitfinexBalancesResponse;
import org.knowm.xchange.bitfinex.v1.dto.account.BitfinexCurrencyLabels;
import org.knowm.xchange.bitfinex.v1.dto.account.BitfinexDepositAddressRequest;
import org.knowm.xchange.bitfinex.v1.dto.account.BitfinexDepositAddressResponse;
import org.knowm.xchange.bitfinex.v1.dto.account.BitfinexDepositWithdrawalHistoryRequest;
import org.knowm.xchange.bitfinex.v1.dto.account.BitfinexDepositWithdrawalHistoryResponse;
import org.knowm.xchange.bitfinex.v1.dto.account.BitfinexMarginInfosRequest;
import org.knowm.xchange.bitfinex.v1.dto.account.BitfinexMarginInfosResponse;
import org.knowm.xchange.bitfinex.v1.dto.account.BitfinexTradingFeeResponse;
import org.knowm.xchange.bitfinex.v1.dto.account.BitfinexTradingFeesRequest;
import org.knowm.xchange.bitfinex.v1.dto.account.BitfinexWithdrawalRequest;
import org.knowm.xchange.bitfinex.v1.dto.account.BitfinexWithdrawalResponse;
import org.knowm.xchange.bitfinex.v1.dto.trade.BitfinexNonceOnlyRequest;
import org.knowm.xchange.exceptions.ExchangeException;
import si.mazi.rescu.RestProxyFactory;

public class BitfinexAccountServiceRaw extends BitfinexBaseService {

  /**
   * Constructor
   *
   * @param exchange
   */

  private BitfinexConfig bitfinexConfig;

  public BitfinexAccountServiceRaw(Exchange exchange) {
    super(exchange);

    this.bitfinexConfig = RestProxyFactory.createProxy(
            BitfinexConfig.class,
            exchange.getExchangeSpecification().getSslUri(),
            getClientConfig());
  }

  public BitfinexTradingFeeResponse[] getBitfinexDynamicTradingFees() throws IOException {
    try {
      BitfinexTradingFeeResponse[] response =
          bitfinex.tradingFees(
              apiKey,
              payloadCreator,
              signatureCreator,
              new BitfinexTradingFeesRequest(
                  String.valueOf(exchange.getNonceFactory().createValue())));
      return response;
    } catch (BitfinexException e) {
      throw new ExchangeException(e);
    }
  }

  public BitfinexBalancesResponse[] getBitfinexAccountInfo() throws IOException {

    BitfinexBalancesResponse[] balances =
        bitfinex.balances(
            apiKey,
            payloadCreator,
            signatureCreator,
            new BitfinexBalancesRequest(String.valueOf(exchange.getNonceFactory().createValue())));
    return balances;
  }

  public BitfinexMarginInfosResponse[] getBitfinexMarginInfos() throws IOException {

    BitfinexMarginInfosResponse[] marginInfos =
        bitfinex.marginInfos(
            apiKey,
            payloadCreator,
            signatureCreator,
            new BitfinexMarginInfosRequest(
                String.valueOf(exchange.getNonceFactory().createValue())));
    return marginInfos;
  }

  public BitfinexDepositWithdrawalHistoryResponse[] getDepositWithdrawalHistory(
      String currency, String method, Date since, Date until, Integer limit) throws IOException {
    BitfinexDepositWithdrawalHistoryRequest request =
        new BitfinexDepositWithdrawalHistoryRequest(
            String.valueOf(exchange.getNonceFactory().createValue()),
            currency,
            method,
            since,
            until,
            limit);
    return bitfinex.depositWithdrawalHistory(apiKey, payloadCreator, signatureCreator, request);
  }

  public String withdraw(
      String withdrawType, String walletSelected, BigDecimal amount, String address)
      throws IOException {
    return withdraw(withdrawType, walletSelected, amount, address, null);
  }

  public String withdraw(
      String withdrawType,
      String walletSelected,
      BigDecimal amount,
      String address,
      String tagOrPaymentId)
      throws IOException {

    BitfinexWithdrawalResponse[] withdrawResponse =
        bitfinex.withdraw(
            apiKey,
            payloadCreator,
            signatureCreator,
            new BitfinexWithdrawalRequest(
                String.valueOf(exchange.getNonceFactory().createValue()),
                withdrawType,
                walletSelected,
                amount,
                address,
                tagOrPaymentId));
    if ("error".equalsIgnoreCase(withdrawResponse[0].getStatus())) {
      throw new ExchangeException(withdrawResponse[0].getMessage());
    }
    return withdrawResponse[0].getWithdrawalId();
  }

  public BitfinexDepositAddressResponse requestDepositAddressRaw(String currency)
      throws IOException {

    BitfinexCurrencyLabels currencyLabels = bitfinexConfig.getCurrencyLabels();

    String type = currencyLabels.getLabel(currency);

    BitfinexDepositAddressResponse requestDepositAddressResponse =
        bitfinex.requestDeposit(
            apiKey,
            payloadCreator,
            signatureCreator,
            new BitfinexDepositAddressRequest(
                String.valueOf(exchange.getNonceFactory().createValue()), type, "exchange", 0));
    if (requestDepositAddressResponse != null) {
      return requestDepositAddressResponse;
    } else {
      return null;
    }
  }

  public BitfinexAccountFeesResponse getAccountFees() throws IOException {
    return bitfinex.accountFees(
        apiKey,
        payloadCreator,
        signatureCreator,
        new BitfinexNonceOnlyRequest(
            "/v1/account_fees", String.valueOf(exchange.getNonceFactory().createValue())));
  }

  public BitfinexBalanceHistoryResponse[] getBitfinexBalanceHistory(
      String currency, String wallet, Long since, Long until, int limit) throws IOException {
    return bitfinex.balanceHistory(
        apiKey,
        payloadCreator,
        signatureCreator,
        new BitfinexBalanceHistoryRequest(
            String.valueOf(exchange.getNonceFactory().createValue()),
            currency,
            since,
            until,
            limit,
            wallet));
  }
}
