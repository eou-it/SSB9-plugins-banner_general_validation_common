/*********************************************************************************
 Copyright 2016-2016 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm

import grails.test.GrailsUnitTestCase
import org.junit.Test


class UsRacialCategoryTests extends GrailsUnitTestCase {

    @Test
    void testGetByString() {
        String value = "Other Pacific Islander"
        String version = "v1"
        UsRacialCategory usRacialCategory = UsRacialCategory.getByString(value, version)
        assertNotNull usRacialCategory
        assertEquals value, usRacialCategory.versionToEnumMap[version]
        assertEquals UsRacialCategory.OTHER_PACIFIC_ISLANDER, usRacialCategory

        value = "hawaiianOrPacificIslander"
        version = "v6"
        usRacialCategory = UsRacialCategory.getByString(value, version)
        assertNotNull usRacialCategory
        assertEquals value, usRacialCategory.versionToEnumMap[version]
        assertEquals UsRacialCategory.HAWAIIAN_OR_PACIFIC_ISLANDER, usRacialCategory


        value = "blackOrAfricanAmerican"
        version = "v4"
        usRacialCategory = UsRacialCategory.getByString(value, version)
        assertNull usRacialCategory
    }

}
