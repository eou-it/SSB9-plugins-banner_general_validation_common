/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm


enum CitizenshipStatusCategory {

    CITIZEN([v4: "citizen"], true),
    NON_CITIZEN([v4: "nonCitizen"], false)

    final Map<String, String> versionToEnumMap
    final Boolean bannerValue


    CitizenshipStatusCategory(Map<String, String> versionToEnumMap, Boolean bannerValue) {
        this.versionToEnumMap = versionToEnumMap
        this.bannerValue = bannerValue
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


    public static CitizenshipStatusCategory getByBannerValue(Boolean value) {
        if (value) {
            Iterator itr = CitizenshipStatusCategory.values().iterator()
            while (itr.hasNext()) {
                CitizenshipStatusCategory citizenshipStatusCategory = itr.next()
                if (citizenshipStatusCategory.bannerValue == value) {
                    return citizenshipStatusCategory
                }
            }
        }
        return null
    }

}
