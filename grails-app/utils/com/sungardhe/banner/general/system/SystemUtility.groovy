/*********************************************************************************
 Copyright 2009-2011 SunGard Higher Education. All Rights Reserved.
 This copyrighted software contains confidential and proprietary information of 
 SunGard Higher Education and its subsidiaries. Any use of this software is limited 
 solely to SunGard Higher Education licensees, and is further subject to the terms 
 and conditions of one or more written license agreements between SunGard Higher 
 Education and the licensee in question. SunGard is either a registered trademark or
 trademark of SunGard Data Systems in the U.S.A. and/or other regions and/or countries.
 Banner and Luminis are either registered trademarks or trademarks of SunGard Higher 
 Education in the U.S.A. and/or other regions and/or countries.
 **********************************************************************************/
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
