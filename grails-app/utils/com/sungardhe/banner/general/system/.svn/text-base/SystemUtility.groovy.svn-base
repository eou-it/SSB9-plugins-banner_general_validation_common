/** *****************************************************************************
 © 2011 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 ****************************************************************************** */
package com.sungardhe.banner.general.system

/**
 * This is a helper class that is used to help common validation and other processing for
 * General System
 *
 */

class SystemUtility {

    /**
     * Call to find out if degree works is installed so the catalog / schedule can show the registration prerequisite rules from degree works
     * @param term
     * @return boolean
     */

    public static Boolean isDegreeWorksInstalled(String term) {
        def sdaCrossWalkConversions = SdaCrosswalkConversion.fetchAllByInternalAndInternalGroup("PREREQCHK",
                "DEGREEWORKS")

        if (sdaCrossWalkConversions) {
            def sdaCrossWalkTerm = sdaCrossWalkConversions.find { it.external != "999999" }
            if (sdaCrossWalkTerm) {
                if (Term.findByCode(sdaCrossWalkTerm.external)) {
                    if (term >= sdaCrossWalkTerm.external) {
                        return true
                    }
                }
                else {
                    return false
                }

            }
            else {
                return false
            }
        }
        else {
            return false

        }
        return false
    }
}
