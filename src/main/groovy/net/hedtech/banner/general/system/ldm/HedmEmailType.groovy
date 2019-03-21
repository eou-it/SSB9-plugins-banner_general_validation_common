/*********************************************************************************
 Copyright 2016-2017 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.system.ldm

enum HedmEmailType {

    PERSONAL([v3: 'Personal', v6: 'personal']),
    INSTITUTION([v3: 'Institution']),
    WORK([v3: 'Work']),
    PREFERRED([v3: 'Preferred']),
    BUSINESS([v6: 'business']),
    SCHOOL([v6: 'school']),
    PARENT([v6: 'parent']),
    FAMILY([v6: 'family']),
    SALES(v6: 'sales'),
    SUPPORT([v6: 'support']),
    GENERAL([v6: 'general']),
    BILLING([v6: 'billing']),
    LEGAL([v6: 'legal']),
    HR([v6: 'hr']),
    MEDIA([v6: 'media']),
    MATCHINGGIFTS([v6: 'matchingGifts']),
    OTHER([v6: 'other'])

    final Map<String, String> versionToEnumMap


    HedmEmailType(Map<String, String> versionToEnumMap) {
        this.versionToEnumMap = versionToEnumMap
    }


    public static HedmEmailType getByDataModelValue(String value, String version) {
        if (value) {
            Iterator itr = HedmEmailType.values().iterator()
            while (itr.hasNext()) {
                HedmEmailType hedmEmailType = itr.next()
                if (hedmEmailType.versionToEnumMap.containsKey(version) && hedmEmailType.versionToEnumMap[version].equals(value)) {
                    return hedmEmailType
                }
            }
        }
        return null
    }

}
