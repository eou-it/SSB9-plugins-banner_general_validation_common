/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm

/**
 * Higher Ed Data Model racialCategory enums
 * The IPEDS racial category for the United States of America
 */
enum UsRacialCategory {

    ASIAN([v1: "Asian", v4: "asian", v6: "asian"]),
    WHITE([v1: "White", v4: "white", v6: "white"]),
    AMERICAN_INDIAN([v1: "American Indian", v4: "americanIndian"]),
    ALASKA_NATIVE([v1: "Alaska Native", v4: "alaskaNative"]),
    AMERICAN_INDIAN_OR_ALASKA_NATIVE([v6: "americanIndianOrAlaskaNative"]),
    BLACK([v1: "Black", v4: "black"]),
    AFRICAN_AMERICAN([v1: "African American", v4: "africanAmerican"]),
    BLACK_OR_AFRICAN_AMERICAN([v6: "blackOrAfricanAmerican"]),
    NATIVE_HAWAIIAN([v1: "Native Hawaiian", v4: "nativeHawaiian"]),
    OTHER_PACIFIC_ISLANDER([v1: "Other Pacific Islander", v4: "otherPacificIslander"]),
    HAWAIIAN_OR_PACIFIC_ISLANDER([v6: "hawaiianOrPacificIslander"])

    private final Map<String, String> versionToEnumMap


    UsRacialCategory(Map<String, String> versionToEnumMap) {
        this.versionToEnumMap = versionToEnumMap
    }


    public Map<String, String> getVersionToEnumMap() {
        return versionToEnumMap
    }

    /**
     * Given "Other Pacific Islander" and "v1" returns corresponding enum OTHER_PACIFIC_ISLANDER.
     * This is useful in "create" and "update" operations to validate the input string.
     *
     * @param value
     * @param version
     * @return
     */
    public static UsRacialCategory getByString(String value, String version) {
        if (value) {
            Iterator itr = UsRacialCategory.values().iterator()
            while (itr.hasNext()) {
                UsRacialCategory usRacialCategory = itr.next()
                if (usRacialCategory.versionToEnumMap.containsKey(version) && usRacialCategory.versionToEnumMap[version].equals(value)) {
                    return usRacialCategory
                }
            }
        }
        return null
    }

}
