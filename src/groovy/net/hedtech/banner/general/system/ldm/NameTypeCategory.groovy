/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm

enum NameTypeCategory {

    PERSONAL([v3: "Primary", v6: "personal"]),
    BIRTH([v3: "Birth", v6: "birth"]),
    LEGAL([v6: "legal"]),

    private final Map<String, String> versionToEnumMap


    NameTypeCategory(Map<String, String> versionToEnumMap) {
        this.versionToEnumMap = versionToEnumMap
    }


    public Map<String, String> getVersionToEnumMap() {
        return versionToEnumMap
    }

    /**
     * Given "Primary" and "v3" returns corresponding enum PERSONAL.
     * This is useful in "create" and "update" operations to validate the input string.
     *
     * @param value
     * @param version
     * @return
     */
    public static NameTypeCategory getByString(String value, String version) {
        if (value) {
            Iterator itr = NameTypeCategory.values().iterator()
            while (itr.hasNext()) {
                NameTypeCategory nameTypeCategory = itr.next()
                if (nameTypeCategory.versionToEnumMap.containsKey(version) && nameTypeCategory.versionToEnumMap[version].equalsIgnoreCase(value)) {
                    return nameTypeCategory
                }
            }
        }
        return null
    }


    public static NameTypeCategory getByString(String value) {
        if (value) {
            Iterator itr = NameTypeCategory.values().iterator()
            while (itr.hasNext()) {
                NameTypeCategory nameTypeCategory = itr.next()
                if (nameTypeCategory.versionToEnumMap.values().contains(value)) {
                    return nameTypeCategory
                }
            }
        }
        return null
    }

}
