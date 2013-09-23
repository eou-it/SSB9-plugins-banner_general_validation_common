/** *****************************************************************************
 © 2013 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 ****************************************************************************** */
package net.hedtech.banner.general.utility

/**
 * SourceIndicators.
 *
 * Date: 9/5/13
 * Time: 4:32 PM
 */
public enum SourceIndicators {
       BASELINE("B"){

        },
        LOCAL("L"){

        };
    private String indicatorCode = null

            private SourceIndicators(String indicatorCode) {
                this.indicatorCode = indicatorCode
            }

            public String getCode () {
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
