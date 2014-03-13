/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.utility

public enum SourceIndicators {
    BASELINE("B") {

    },
    LOCAL("L") {

    };
    private String indicatorCode = null

    private SourceIndicators(String indicatorCode) {
        this.indicatorCode = indicatorCode
    }

    public String getCode() {
        return indicatorCode;
    }

    public static SourceIndicators getSourceIndicator(String indicatorCode) {
        if ("B".equalsIgnoreCase(indicatorCode)) {
            return BASELINE
        } else if ("L".equalsIgnoreCase(indicatorCode)) {
            return LOCAL
        } else {
            throw new IllegalArgumentException("No  Source Indicator for the code $indicatorCode")
        }


    }


}
