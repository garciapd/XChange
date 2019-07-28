package org.knowm.xchange.bitfinex.v1.dto.account;

import com.fasterxml.jackson.annotation.JsonCreator;

public class BitfinexCurrencyLabels {

    // The format of the response is [[["ABS","The Abyss"],["ADD","ADD"],["AGI","SingularityNET"]]]
    String[][][] labels;

    @JsonCreator
    public BitfinexCurrencyLabels(String[][][] labels) {
        this.labels = labels;
    }

    public String getLabel(String currency) {
        if (labels.length > 0 && labels[0].length > 0) {
            for (int i = 0; i < labels[0].length; i++) {
                if (labels[0][i].length > 1 && labels[0][i][0].equalsIgnoreCase(currency)) {
                    return labels[0][i][1];
                }
            }
        }

        return "unknown";
    }
}
