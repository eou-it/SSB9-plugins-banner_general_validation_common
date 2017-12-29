/*********************************************************************************
 Copyright 2016-2017 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.system.ldm

enum HedmPhoneType {

    MOBILE([v3: 'Mobile', v6: 'mobile']),
    HOME([v3: 'Home', v6: 'home']),
    RESIDENCE([v3: 'Residence']),
    WORK([v3: 'Work']),
    SCHOOL([v6: 'school']),
    VACATION([v6: 'vacation']),
    BUSINESS([v6: 'business']),
    FAX([v6: 'fax']),
    PAGER([v6: 'pager']),
    TDD([v6: 'tdd']),
    PARENT([v6: 'parent']),
    FAMILY([v6: 'family']),
    MAIN([v6: 'main']),
    BRANCH([v6: 'branch']),
    REGION([v6: 'region']),
    SUPPORT([v6: 'support']),
    BILLING([v6: 'billing']),
    MATCHINGGIFTS([v6: 'matchingGifts']),
    OTHER([v6: 'other'])


    final Map<String, String> versionToEnumMap


    HedmPhoneType(Map<String, String> versionToEnumMap) {
        this.versionToEnumMap = versionToEnumMap
    }


    public static HedmPhoneType getByDataModelValue(String value, String version) {
        if (value) {
            Iterator itr = HedmPhoneType.values().iterator()
            while (itr.hasNext()) {
                HedmPhoneType phoneType = itr.next()
                if (phoneType.versionToEnumMap.containsKey(version) && phoneType.versionToEnumMap[version].equals(value)) {
                    return phoneType
                }
            }
        }
        return null
    }

}
