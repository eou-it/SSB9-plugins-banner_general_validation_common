/** *****************************************************************************
 � 2010 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 ****************************************************************************** */
package com.sungardhe.banner.general.system


import com.sungardhe.banner.testing.BaseIntegrationTestCase

class SystemUtilityIntegrationTests extends BaseIntegrationTestCase {


    protected void setUp() {
        formContext = ['SSASECT'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }
    /*PROTECTED REGION ID(systemUtility custom_integration_test_methods) ENABLED START*/


    void testIsDegreeWorksInstalledTrue() {
        def sdaCrosswalkConversions = SdaCrosswalkConversion.fetchAllByInternalAndInternalGroup("PREREQCHK",
                                                                                                "DEGREEWORKS")

        sdaCrosswalkConversions.each {
            it.delete()
        }

        sdaCrosswalkConversions = SdaCrosswalkConversion.fetchAllByInternalAndInternalGroup("PREREQCHK",
                                                                                                "DEGREEWORKS")
        assertEquals sdaCrosswalkConversions.size(),0

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


    void testIsDegreeWorksInstalledInvalidTerm() {

        def sdaCrosswalkConversions = SdaCrosswalkConversion.fetchAllByInternalAndInternalGroup("PREREQCHK",
                                                                                                "DEGREEWORKS")

        sdaCrosswalkConversions.each {
            it.delete()
        }
        sdaCrosswalkConversions = SdaCrosswalkConversion.fetchAllByInternalAndInternalGroup("PREREQCHK",
                                                                                                "DEGREEWORKS")
        assertEquals sdaCrosswalkConversions.size(),0
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


    void testIsDegreeWorksInstalledFalse() {
         def sdaCrosswalkConversions = SdaCrosswalkConversion.fetchAllByInternalAndInternalGroup("PREREQCHK",
                                                                                                "DEGREEWORKS")

        sdaCrosswalkConversions.each {
            it.delete()
        }
        sdaCrosswalkConversions = SdaCrosswalkConversion.fetchAllByInternalAndInternalGroup("PREREQCHK",
                                                                                                "DEGREEWORKS")
        assertEquals sdaCrosswalkConversions.size(),0
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


    void testIsDegreeWorksInstalledNoSda() {

        def sdaCrosswalkConversion = SdaCrosswalkConversion.fetchAllByInternalAndInternalGroup("PREREQCHK",
                                                                                               "DEGREEWORKS")
        assertNotNull sdaCrosswalkConversion
        sdaCrosswalkConversion.each {
            it.delete()
        }
        sdaCrosswalkConversion  = SdaCrosswalkConversion.fetchAllByInternalAndInternalGroup("PREREQCHK",
                                                                                                "DEGREEWORKS")
        assertEquals sdaCrosswalkConversion.size(),0

        sdaCrosswalkConversion = SdaCrosswalkConversion.fetchAllByInternalAndInternalGroup("PREREQCHK",
                                                                                           "DEGREEWORKS")
        assertEquals sdaCrosswalkConversion.size(), 0

        assertNotNull Term.findByCode("200510")
        assertFalse SystemUtility.isDegreeWorksInstalled("200510")

    }
    /*PROTECTED REGION END*/
}