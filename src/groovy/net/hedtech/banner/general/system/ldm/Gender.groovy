/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm


enum Gender {

    MALE([v1: "Male", v6: "male"], "M"),
    FEMALE([v1: "Female", v6: "female"], "F"),
    UNKNOWN([v1: "Unknown", v6: "unknown"], "N"),

    final Map<String, String> versionToEnumMap
    final String bannerValue


    Gender(Map<String, String> versionToEnumMap, String bannerValue) {
        this.versionToEnumMap = versionToEnumMap
        this.bannerValue = bannerValue
    }


    public static Gender getByDataModelValue(String value, String version) {
        if (value) {
            Iterator itr = Gender.values().iterator()
            while (itr.hasNext()) {
                Gender gender = itr.next()
                if (gender.versionToEnumMap.containsKey(version) && gender.versionToEnumMap[version].equals(value)) {
                    return gender
                }
            }
        }
        return null
    }


    public static Gender getByBannerValue(String value) {
        if (value) {
            Iterator itr = Gender.values().iterator()
            while (itr.hasNext()) {
                Gender gender = itr.next()
                if (gender.bannerValue == value) {
                    return gender
                }
            }
        }
        return null
    }

}

