/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

package net.hedtech.banner.general.system.ldm

/**
 * Section Status Enum
 */
public enum SectionDurationUnitName {

    DAYS([v1:'Days', v4:'days']),
    WEEKS([v1:'Weeks', v4:'weeks']),
    MONTHS([v1:'Months', v4:'months']),
    YEARS([v1:'Years', v4:'years'])

    private final Map<String, String> versionToEnumMap


    SectionDurationUnitName(Map<String, String> versionToEnumMap) {
        this.versionToEnumMap = versionToEnumMap
    }


    public Map<String, String> getVersionToEnumMap() {
        return versionToEnumMap
    }


    public static SectionDurationUnitName getByString(String value, String version) {
        if (value) {
            Iterator itr = SectionDurationUnitName.values().iterator()
            while (itr.hasNext()) {
                SectionDurationUnitName status = itr.next()
                if (status.versionToEnumMap.containsKey(version) && status.versionToEnumMap[version].equalsIgnoreCase(value)) {
                    return status
                }
            }
        }
        return null
    }

}