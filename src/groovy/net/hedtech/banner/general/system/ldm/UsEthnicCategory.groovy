/*********************************************************************************
 Copyright 2017 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.system.ldm

/**
 * Ethnic Categories of United States of America
 *
 */
enum UsEthnicCategory {


    HISPANIC([v1: "Hispanic", v4: "hispanic"], "2"),
    NON_HISPANIC([v1: "Non-Hispanic", v4: "nonHispanic"], "1")

    final Map<String, String> versionToEnumMap
    final String bannerValue


    UsEthnicCategory(Map<String, String> versionToEnumMap, String bannerValue) {
        this.versionToEnumMap = versionToEnumMap
        this.bannerValue = bannerValue
    }


    public static UsEthnicCategory getByDataModelValue(String value, String version) {
        if (value) {
            Iterator itr = UsEthnicCategory.values().iterator()
            while (itr.hasNext()) {
                UsEthnicCategory usEthnicCategory = itr.next()
                if (usEthnicCategory.versionToEnumMap.containsKey(version) && usEthnicCategory.versionToEnumMap[version].equals(value)) {
                    return usEthnicCategory
                }
            }
        }
        return null
    }


    public static UsEthnicCategory getByBannerValue(String value) {
        if (value) {
            Iterator itr = UsEthnicCategory.values().iterator()
            while (itr.hasNext()) {
                UsEthnicCategory usEthnicCategory = itr.next()
                if (usEthnicCategory.bannerValue == value) {
                    return usEthnicCategory
                }
            }
        }
        return null
    }

}
