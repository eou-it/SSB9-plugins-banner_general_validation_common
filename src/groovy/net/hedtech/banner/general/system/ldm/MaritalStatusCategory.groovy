/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm


enum MaritalStatusCategory {

    SINGLE([v1: "Single", v6: "single"]),
    MARRIED([v1: "Married", v6: "married"]),
    DIVORCED([v1: "Divorced", v6: "divorced"]),
    WIDOWED([v1: "Widowed", v6: "widowed"]),
    SEPARATED([v1: "Separated", v6: "separated"])

    final Map<String, String> versionToEnumMap


    MaritalStatusCategory(Map<String, String> versionToEnumMap) {
        this.versionToEnumMap = versionToEnumMap
    }


    public static MaritalStatusCategory getByDataModelValue(String value, String version) {
        if (value) {
            Iterator itr = MaritalStatusCategory.values().iterator()
            while (itr.hasNext()) {
                MaritalStatusCategory maritalStatusCategory = itr.next()
                if (maritalStatusCategory.versionToEnumMap.containsKey(version) && maritalStatusCategory.versionToEnumMap[version].equals(value)) {
                    return maritalStatusCategory
                }
            }
        }
        return null
    }

}
