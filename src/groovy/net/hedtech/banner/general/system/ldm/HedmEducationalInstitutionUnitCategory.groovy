/*******************************************************************************
 Copyright 2016-2016 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system.ldm


enum HedmEducationalInstitutionUnitCategory {

    FACULTY([v1: 'Faculty']),
    UNIVERSITY([v1: 'University']),
    COLLEGE([v1: 'College', v6: 'college',v7: 'college']),
    SCHOOL([v1: 'School', v6: 'school',v7: 'school']),
    DIVISION([v1: 'Division', v6: 'division',v7: 'division']),
    DEPARTMENT([v1: 'Department', v6: 'department',v7: 'department']),
    INSTITUTE([v6: 'institute',v7: 'institute']),
    FACILITY([v6: 'facility',v7: 'facility']),
    OFFICE([v6: 'office',v7: 'office'])

    private final Map<String, String> versionToEnumMap


    HedmEducationalInstitutionUnitCategory(Map<String, String> versionToEnumMap) {
        this.versionToEnumMap = versionToEnumMap
    }


    public Map<String, String> getVersionToEnumMap() {
        return versionToEnumMap
    }

    /**
     * This is useful in "create" and "update" operations to validate the input string.
     *
     * @param value
     * @param version
     * @return
     */
    public static HedmEducationalInstitutionUnitCategory getByString(String value, String version) {
        if (value) {
            Iterator itr = HedmEducationalInstitutionUnitCategory.values().iterator()
            while (itr.hasNext()) {
                HedmEducationalInstitutionUnitCategory hedmEducationalInstitutionUnitType = itr.next()
                if (hedmEducationalInstitutionUnitType.versionToEnumMap.containsKey(version) && hedmEducationalInstitutionUnitType.versionToEnumMap[version].equals(value)) {
                    return hedmEducationalInstitutionUnitType
                }
            }
        }
        return null
    }

}
