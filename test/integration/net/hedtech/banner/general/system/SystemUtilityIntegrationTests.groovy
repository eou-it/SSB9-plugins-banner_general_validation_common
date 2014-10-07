/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system
import org.junit.Before
import org.junit.Test
import org.junit.After


import net.hedtech.banner.testing.BaseIntegrationTestCase

class SystemUtilityIntegrationTests extends BaseIntegrationTestCase {


    @Before
    public void setUp() {
        formContext = ['SSASECT'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    @After
    public void tearDown() {
        super.tearDown()
    }


    @Test
    void testIsDegreeWorksInstalledTrue() {
        def sdaCrosswalkConversions = SdaCrosswalkConversion.fetchAllByInternalAndInternalGroup("PREREQCHK",
                "DEGREEWORKS")
        sdaCrosswalkConversions.each {
            it.delete()
        }

        sdaCrosswalkConversions = SdaCrosswalkConversion.fetchAllByInternalAndInternalGroup("PREREQCHK",
                "DEGREEWORKS")
        assertEquals sdaCrosswalkConversions.size(), 0

        def sdaCrosswalkConversion = new SdaCrosswalkConversion(
                external: "999999",
                internalGroup: "DEGREEWORKS",
                internal: "PREREQCHK",
                description: "Degree works",
                systemRequestIndicator: "Y",
                internalSequenceNumber: 1,
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        save sdaCrosswalkConversion
        assertNotNull sdaCrosswalkConversion.id
        sdaCrosswalkConversion = new SdaCrosswalkConversion(
                external: "199510",
                internalGroup: "DEGREEWORKS",
                internal: "PREREQCHK",
                description: "Degree works",
                systemRequestIndicator: "Y",
                internalSequenceNumber: 2,
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        save sdaCrosswalkConversion
        assertNotNull sdaCrosswalkConversion.id

        assertNotNull Term.findByCode("200520")
        assertTrue SystemUtility.isDegreeWorksInstalled("200520")

        assertNotNull Term.findByCode("200510")
        assertTrue SystemUtility.isDegreeWorksInstalled("200510")

        assertNotNull Term.findByCode("199410")
        assertFalse SystemUtility.isDegreeWorksInstalled("199410")
    }


    @Test
    void testIsDegreeWorksInstalledInvalidTerm() {
        def sdaCrosswalkConversions = SdaCrosswalkConversion.fetchAllByInternalAndInternalGroup("PREREQCHK",
                "DEGREEWORKS")

        sdaCrosswalkConversions.each {
            it.delete()
        }
        sdaCrosswalkConversions = SdaCrosswalkConversion.fetchAllByInternalAndInternalGroup("PREREQCHK",
                "DEGREEWORKS")
        assertEquals sdaCrosswalkConversions.size(), 0
        def sdaCrosswalkConversion = new SdaCrosswalkConversion(
                external: "999999",
                internalGroup: "DEGREEWORKS",
                internal: "PREREQCHK",
                description: "Degree works",
                systemRequestIndicator: "Y",
                internalSequenceNumber: 1,
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        save sdaCrosswalkConversion
        assertNotNull sdaCrosswalkConversion.id
        sdaCrosswalkConversion = new SdaCrosswalkConversion(
                external: "199510",
                internalGroup: "DEGREEWORKS",
                internal: "PREREQCHK",
                description: "Degree works",
                systemRequestIndicator: "Y",
                internalSequenceNumber: 2,
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        save sdaCrosswalkConversion
        assertNotNull sdaCrosswalkConversion.id

        assertNull Term.findByCode("199499")
        assertFalse SystemUtility.isDegreeWorksInstalled("199499")
    }


    @Test
    void testIsDegreeWorksInstalledFalse() {
        def sdaCrosswalkConversions = SdaCrosswalkConversion.fetchAllByInternalAndInternalGroup("PREREQCHK",
                "DEGREEWORKS")

        sdaCrosswalkConversions.each {
            it.delete()
        }
        sdaCrosswalkConversions = SdaCrosswalkConversion.fetchAllByInternalAndInternalGroup("PREREQCHK",
                "DEGREEWORKS")
        assertEquals sdaCrosswalkConversions.size(), 0
        def sdaCrosswalkConversion = new SdaCrosswalkConversion(
                external: "999999",
                internalGroup: "DEGREEWORKS",
                internal: "PREREQCHK",
                description: "Degree works",
                systemRequestIndicator: "Y",
                internalSequenceNumber: 1,
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        save sdaCrosswalkConversion
        assertNotNull sdaCrosswalkConversion.id
        sdaCrosswalkConversion = SdaCrosswalkConversion.fetchAllByInternalAndInternalGroup("PREREQCHK",
                "DEGREEWORKS")
        assertNotNull sdaCrosswalkConversion
        assertEquals sdaCrosswalkConversion.size(), 1

        assertFalse SystemUtility.isDegreeWorksInstalled("200510")
    }


    @Test
    void testIsDegreeWorksInstalledNoSda() {
        def sdaCrosswalkConversion = SdaCrosswalkConversion.fetchAllByInternalAndInternalGroup("PREREQCHK",
                "DEGREEWORKS")
        assertNotNull sdaCrosswalkConversion
        sdaCrosswalkConversion.each {
            it.delete()
        }
        sdaCrosswalkConversion = SdaCrosswalkConversion.fetchAllByInternalAndInternalGroup("PREREQCHK",
                "DEGREEWORKS")
        assertEquals sdaCrosswalkConversion.size(), 0

        sdaCrosswalkConversion = SdaCrosswalkConversion.fetchAllByInternalAndInternalGroup("PREREQCHK",
                "DEGREEWORKS")
        assertEquals sdaCrosswalkConversion.size(), 0

        assertNotNull Term.findByCode("200510")
        assertFalse SystemUtility.isDegreeWorksInstalled("200510")

    }

}
