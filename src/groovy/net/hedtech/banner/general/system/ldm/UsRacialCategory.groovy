/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm

/**
 * The IPEDS racial category for the United States of America
 */
enum UsRacialCategory {

    ASIAN("Asian", "asian", "asian"),
    WHITE("White", "white", "white"),
    AMERICAN_INDIAN_OR_ALASKA_NATIVE(null, null, "americanIndianOrAlaskaNative"),
    AMERICAN_INDIAN("American Indian", "americanIndian", null),
    ALASKA_NATIVE("Alaska Native", "alaskaNative", null),
    BLACK_OR_AFRICAN_AMERICAN(null, null, "blackOrAfricanAmerican"),
    BLACK("Black", "black", null),
    AFRICAN_AMERICAN("African American", "africanAmerican", null),
    HAWAIIAN_OR_PACIFIC_ISLANDER(null, null, "hawaiianOrPacificIslander"),
    NATIVE_HAWAIIAN("Native Hawaiian", "nativeHawaiian", null),
    OTHER_PACIFIC_ISLANDER("Other Pacific Islander", "otherPacificIslander", null)


    private final String v1Enum
    private final String v4Enum
    private final String v6Enum


    UsRacialCategory(String v1Enum, String v4Enum, String v6Enum) {
        this.v1Enum = v1Enum
        this.v4Enum = v4Enum
        this.v6Enum = v6Enum
    }


    public String getV1() {
        return v1Enum
    }


    public String getV4() {
        return v4Enum
    }


    public String getV6() {
        return v6Enum
    }

    /**
     * Given a string like "Other Pacific Islander" returns corresponding enum OTHER_PACIFIC_ISLANDER.
     * This is useful in "create" and "update" operations to validate the input string.
     *
     * @param value
     * @return
     */
    public static UsRacialCategory getByString(String value) {
        if (value) {
            Iterator itr = UsRacialCategory.values().iterator()
            while (itr.hasNext()) {
                UsRacialCategory usRacialCategory = itr.next()
                def vals = []
                if (usRacialCategory.v1Enum) {
                    vals << usRacialCategory.v1Enum
                }
                if (usRacialCategory.v4Enum) {
                    vals << usRacialCategory.v4Enum
                }
                if (usRacialCategory.v6Enum) {
                    vals << usRacialCategory.v6Enum
                }
                if (vals.contains(value)) {
                    return usRacialCategory
                }
            }
        }
        return null
    }

}
