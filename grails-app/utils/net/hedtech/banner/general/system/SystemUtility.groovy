/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

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
