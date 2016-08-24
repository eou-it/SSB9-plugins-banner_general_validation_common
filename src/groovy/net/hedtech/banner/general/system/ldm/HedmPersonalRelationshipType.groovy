/*********************************************************************************
 Copyright 2016-2016 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.system.ldm

enum HedmPersonalRelationshipType {
    PARENT([v7: 'parent']),
    STEPPARENT([v7: 'stepParent']),
    MOTHER([v7: 'mother']),
    STEPMOTHER([v7: 'stepMother']),
    FATHER([v7: 'father']),
    STEPFATHER([v7: 'stepFather']),
    CHILD([v7: 'child']),
    STEPCHILD([v7: 'stepChild']),
    DAUGHTER([v7: 'daughter']),
    STEPDAUGHTER([v7: 'stepDaughter']),
    SON([v7: 'son']),
    STEPSON([v7: 'stepSon']),
    SIBLING(v7: 'sibling'),
    STEPSIBLING([v7: 'stepSibling']),
    BROTHER([v7: 'brother']),
    STEPBROTHER([v7: 'stepBrother']),
    SISTER([v7: 'sister']),
    STEPSISTER([v7: 'stepSister']),
    SPOUSE([v7: 'spouse']),
    WIFE([v7: 'wife']),
    PARTNER([v7: 'partner']),
    GRANDPARENT([v7: 'grandParent']),
    GRANDMOTHER([v7: 'grandMother']),
    GRANDFATHER([v7: 'grandFather']),
    GRANDCHILD([v7: 'grandChild']),
    GRANDDAUGHTER([v7: 'grandDaughter']),
    GRANDSON([v7: 'grandSon']),
    PARENTINLAW([v7: 'parentInLaw']),
    MOTHERINLAW([v7: 'motherInLaw']),
    FATHERINLAW([v7: 'fatherInLaw']),
    CHILDINLAW([v7: 'childInLaw']),
    DAUGHTERINLAW([v7: 'daughterInLaw']),
    SONINLAW([v7: 'sonInLaw']),
    SIBLINGINLAW([v7: 'siblingInLaw']),
    SISTERINLAW([v7: 'sisterInLaw']),
    BROTHERINLAW([v7: 'brotherInLaw']),
    SIBLINGOFPARENT([v7: 'siblingOfParent']),
    AUNT([v7: 'aunt']),
    UNCLE([v7: 'uncle']),
    CHILDOFSIBLING([v7: 'childOfSibling']),
    NIECE([v7: 'niece']),
    NEPHEW([v7: 'nephew']),
    COUSIN([v7: 'cousin']),
    FRIEND([v7: 'friend']),
    RELATIVE([v7: 'relative']),
    COWORKER([v7: 'coworker']),
    NEIGHBOR([v7: 'neighbor']),
    CLASSMATE([v7: 'classmate']),
    CAREGIVER([v7: 'caregiver']),
    OTHER([v7: 'other']),
    HUSBAND([v7: 'husband'])



    private final Map<String, String> versionToEnumMap


    HedmPersonalRelationshipType(Map<String, String> versionToEnumMap) {
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
    public static HedmPersonalRelationshipType getByString(String value, String version) {
        if (value) {
            Iterator itr = HedmPersonalRelationshipType.values().iterator()
            while (itr.hasNext()) {
                HedmPersonalRelationshipType hedmPersonalRelationshipType = itr.next()
                if (hedmPersonalRelationshipType.versionToEnumMap.containsKey(version) && hedmPersonalRelationshipType.versionToEnumMap[version].equals(value)) {
                    return hedmPersonalRelationshipType
                }
            }
        }
        return null
    }
}
