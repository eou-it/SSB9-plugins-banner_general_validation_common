/*********************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.system.ldm

enum HedmAddressType {

    RESIDENCE([v3: 'Residence']),
    WORK([v3: 'Work']),
    HOME([v3: 'Home', v6: 'home']),
    PREFERRED([v3: 'Mailing', v6: 'mailing']),
    SCHOOL([v6: 'school']),
    VACATION([v6: 'vacation']),
    BILLING([v6: 'billing']),
    SHIPPING([v6: 'shipping']),
    BUSINESS([v6: 'business']),
    PARENT([v6: 'parent']),
    FAMILY([v6: 'family']),
    POBOX(v6: 'pobox'),
    MAIN([v6: 'main']),
    BRANCH([v6: 'branch']),
    REGION([v6: 'region']),
    SUPPORT([v6: 'support']),
    MATCHINGGIFTS([v6: 'matchingGifts']),
    OTHER([v6: 'other'])

    private final Map<String, String> versionToEnumMap


    HedmAddressType(Map<String, String> versionToEnumMap) {
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
    public static HedmAddressType getByString(String value, String version) {
        if (value) {
            Iterator itr = HedmAddressType.values().iterator()
            while (itr.hasNext()) {
                HedmAddressType hedmAddressType = itr.next()
                if (hedmAddressType.versionToEnumMap.containsKey(version) && hedmAddressType.versionToEnumMap[version].equals(value)) {
                    return hedmAddressType
                }
            }
        }
        return null
    }

}