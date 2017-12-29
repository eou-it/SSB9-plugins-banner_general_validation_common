/*******************************************************************************
 Copyright 2017 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm


enum CitizenshipStatusCategory {

    CITIZEN([v6: "citizen"]),
    NON_CITIZEN([v6: "nonCitizen"])

    final Map<String, String> versionToEnumMap


    CitizenshipStatusCategory(Map<String, String> versionToEnumMap) {
        this.versionToEnumMap = versionToEnumMap
    }


    public static CitizenshipStatusCategory getByDataModelValue(String value, String version) {
        if (value) {
            Iterator itr = CitizenshipStatusCategory.values().iterator()
            while (itr.hasNext()) {
                CitizenshipStatusCategory citizenshipStatusCategory = itr.next()
                if (citizenshipStatusCategory.versionToEnumMap.containsKey(version) && citizenshipStatusCategory.versionToEnumMap[version].equals(value)) {
                    return citizenshipStatusCategory
                }
            }
        }
        return null
    }


    public static CitizenshipStatusCategory getByCitizenIndicator(Boolean citizenIndicator) {
        CitizenshipStatusCategory citizenshipStatusCategory
        if (citizenIndicator) {
            citizenshipStatusCategory = CitizenshipStatusCategory.CITIZEN
        } else {
            citizenshipStatusCategory = CitizenshipStatusCategory.NON_CITIZEN
        }
        return citizenshipStatusCategory
    }

}
