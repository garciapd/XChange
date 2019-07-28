package org.knowm.xchange.bitfinex.v1.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

import org.knowm.xchange.bitfinex.common.dto.BitfinexException;
import org.knowm.xchange.bitfinex.v1.dto.account.BitfinexCurrencyLabels;

@Path("v2")
@Produces(MediaType.APPLICATION_JSON)
public interface BitfinexConfig {

    @GET
    @Path("conf/pub:map:currency:label")
    BitfinexCurrencyLabels getCurrencyLabels()
            throws IOException, BitfinexException;
}