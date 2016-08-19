/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm


enum MaritalStatusCategory {

    SINGLE([v1: "Single", v4: "single"]),
    MARRIED([v1: "Married", v4: "married"]),
    DIVORCED([v1: "Divorced", v4: "divorced"]),
    WIDOWED([v1: "Widowed", v4: "widowed"]),
    SEPARATED([v1: "Separated", v4: "separated"])

    private final Map<String, String> versionToEnumMap


    MaritalStatusCategory(Map<String, String> versionToEnumMap) {
        this.versionToEnumMap = versionToEnumMap
    }


    public Map<String, String> getVersionToEnumMap() {
        return versionToEnumMap
    }


    public static MaritalStatusCategory getByString(String value, String version) {
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
