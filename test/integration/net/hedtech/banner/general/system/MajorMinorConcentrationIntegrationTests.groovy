/*********************************************************************************
 Copyright 2009-2011 SunGard Higher Education. All Rights Reserved.
 This copyrighted software contains confidential and proprietary information of 
 SunGard Higher Education and its subsidiaries. Any use of this software is limited 
 solely to SunGard Higher Education licensees, and is further subject to the terms 
 and conditions of one or more written license agreements between SunGard Higher 
 Education and the licensee in question. SunGard is either a registered trademark or
 trademark of SunGard Data Systems in the U.S.A. and/or other regions and/or countries.
 Banner and Luminis are either registered trademarks or trademarks of SunGard Higher 
 Education in the U.S.A. and/or other regions and/or countries.
 **********************************************************************************/
/**
 Banner Automator Version: 0.1.1
 Generated: Mon Jan 03 16:09:29 CST 2011 
 */
package net.hedtech.banner.general.system

import net.hedtech.banner.testing.BaseIntegrationTestCase
import groovy.sql.Sql
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

class MajorMinorConcentrationIntegrationTests extends BaseIntegrationTestCase {

    /*PROTECTED REGION ID(majorminorconcentration_domain_integration_test_data) ENABLED START*/
    //Test data for creating new domain instance
    //Valid test data (For success tests)
    def i_success_code = "TTTT"
    def i_success_description = "TTTTT"
    def i_success_validMajorIndicator = true
    def i_success_validMinorIndicator = true
    def i_success_validConcentratnIndicator = true
    def i_success_occupationIndicator = true
    def i_success_aidEligibilityIndicator = true
    def i_success_systemRequiredIndicator = true
    def i_success_voiceResponseMessageNumber = 1

    def i_success_studentExchangeVisitorInformationSystemEquivalent = "TTTTT"


    protected void setUp() {
        formContext = ['STVMAJR'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateValidMajorMinorConcentration() {
        def majorMinorConcentration = newValidForCreateMajorMinorConcentration()
        save majorMinorConcentration
        //Test if the generated entity now has an id assigned
        assertNotNull majorMinorConcentration.id
    }


    void testUpdateValidMajorMinorConcentration() {
        def majorMinorConcentration = newValidForCreateMajorMinorConcentration()
        save majorMinorConcentration
        assertNotNull majorMinorConcentration.id
        assertEquals 0L, majorMinorConcentration.version
        assertEquals i_success_code, majorMinorConcentration.code
        assertEquals i_success_description, majorMinorConcentration.description
        assertEquals i_success_validMajorIndicator, majorMinorConcentration.validMajorIndicator
        assertEquals i_success_validMinorIndicator, majorMinorConcentration.validMinorIndicator
        assertEquals i_success_validConcentratnIndicator, majorMinorConcentration.validConcentratnIndicator
        assertEquals i_success_occupationIndicator, majorMinorConcentration.occupationIndicator
        assertEquals i_success_aidEligibilityIndicator, majorMinorConcentration.aidEligibilityIndicator
        assertEquals i_success_systemRequiredIndicator, majorMinorConcentration.systemRequiredIndicator
        assertEquals i_success_voiceResponseMessageNumber, majorMinorConcentration.voiceResponseMsgNumber
        assertEquals i_success_studentExchangeVisitorInformationSystemEquivalent, majorMinorConcentration.sevisEquiv

        //Update the entity
        majorMinorConcentration.description = "Updating Description"
        majorMinorConcentration.validMajorIndicator = false
        majorMinorConcentration.validMinorIndicator = false
        majorMinorConcentration.validConcentratnIndicator = false
        majorMinorConcentration.occupationIndicator = false
        majorMinorConcentration.aidEligibilityIndicator = false
        majorMinorConcentration.systemRequiredIndicator = false
        majorMinorConcentration.voiceResponseMsgNumber = 3
        majorMinorConcentration.sevisEquiv = "UUUUUU"

        majorMinorConcentration.cipcCode = CIPCode.findWhere(code: "420101")
        save majorMinorConcentration
        //Asset for sucessful update
        majorMinorConcentration = MajorMinorConcentration.get(majorMinorConcentration.id)
        assertEquals 1L, majorMinorConcentration?.version
        assertEquals "Updating Description", majorMinorConcentration.description
        assertEquals false, majorMinorConcentration.validMajorIndicator
        assertEquals false, majorMinorConcentration.validMinorIndicator
        assertEquals false, majorMinorConcentration.validConcentratnIndicator
        assertEquals false, majorMinorConcentration.occupationIndicator
        assertEquals false, majorMinorConcentration.aidEligibilityIndicator
        assertEquals false, majorMinorConcentration.systemRequiredIndicator
        assertEquals 3, majorMinorConcentration.voiceResponseMsgNumber
        assertEquals "UUUUUU", majorMinorConcentration.sevisEquiv

    }


    void testOptimisticLock() {
        def majorMinorConcentration = newValidForCreateMajorMinorConcentration()
        save majorMinorConcentration

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVMAJR set STVMAJR_VERSION = 999 where STVMAJR_SURROGATE_ID = ?", [majorMinorConcentration.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        majorMinorConcentration.description = "Update Description"
        shouldFail(HibernateOptimisticLockingFailureException) {
            majorMinorConcentration.save(flush:true, failOnError:true)
        }
    }


    void testDeleteMajorMinorConcentration() {
        def majorMinorConcentration = newValidForCreateMajorMinorConcentration()
        save majorMinorConcentration
        def id = majorMinorConcentration.id
        assertNotNull id
        majorMinorConcentration.delete()
        assertNull MajorMinorConcentration.get(id)
    }


    void testValidation() {
        def majorMinorConcentration = newValidForCreateMajorMinorConcentration()
        assertTrue "MajorMinorConcentration could not be validated as expected due to ${majorMinorConcentration.errors}", majorMinorConcentration.validate()
    }


    void testNullValidationFailure() {
        def majorMinorConcentration = new MajorMinorConcentration()
        assertFalse "MajorMinorConcentration should have failed validation", majorMinorConcentration.validate()
        assertErrorsFor majorMinorConcentration, 'nullable',
                        [
                        'code'
                        ]
        assertNoErrorsFor majorMinorConcentration,
                          [
                          'description',
                          'validMajorIndicator',
                          'validMinorIndicator',
                          'validConcentratnIndicator',
                          'occupationIndicator',
                          'aidEligibilityIndicator',
                          'systemRequiredIndicator',
                          'voiceResponseMessageNumber',
                          'sevisEquiv',
                          'cipcCode'
                          ]
    }


    void testMaxSizeValidationFailures() {
        def majorMinorConcentration = new MajorMinorConcentration(
                description: 'Update Description, this one will violate the maxSize constraint',
                sevisEquiv: 'This will give maxSize constraint violation')
        assertFalse "MajorMinorConcentration should have failed validation", majorMinorConcentration.validate()
        assertErrorsFor majorMinorConcentration, 'maxSize', ['description', 'sevisEquiv']
    }



    void testFetchBySomeAttribute() {
        // test no filter, no parms
        def majorMinorConcentrations = MajorMinorConcentration.fetchBySomeAttribute()
        assertEquals majorMinorConcentrations.list.size(), MajorMinorConcentration.findAll().size()
    }


    void testFetchBySomeAttributeWithFilter() {
        // test with filter, no parms
        def majorMinorConcentrations = MajorMinorConcentration.fetchBySomeAttribute("MATH")
        assertEquals majorMinorConcentrations.list.size()   ,
                     MajorMinorConcentration.findAllByCodeIlikeOrDescriptionIlike("%MATH%", "%MATH%").size()
    }


    void testFetchBySomeAttributeFieldOfStudy() {
        // test no filter, with parms
        def majorMinorConcentrations = MajorMinorConcentration.fetchBySomeAttribute([fieldOfStudy: "MAJOR"])
        assertEquals majorMinorConcentrations.list.size(), MajorMinorConcentration.findAllByValidMajorIndicator(true).size()

        majorMinorConcentrations = MajorMinorConcentration.fetchBySomeAttribute([fieldOfStudy: "MINOR"])
        assertEquals majorMinorConcentrations.list.size(), MajorMinorConcentration.findAllByValidMinorIndicator(true).size()

        majorMinorConcentrations = MajorMinorConcentration.fetchBySomeAttribute([fieldOfStudy: "CONCENTRATION"])
        assertEquals majorMinorConcentrations.list.size(), MajorMinorConcentration.findAllByValidConcentratnIndicator(true).size()
    }


    void testFetchBySomeAttributeFieldOfStudyWithFilter() {
        // test no filter, with parms

        def majorMinorConcentrations = MajorMinorConcentration.fetchBySomeAttribute("MIS", [fieldOfStudy: "MAJOR"])
        def majors = MajorMinorConcentration.findAllByValidMajorIndicator(true)
        def filteredMajors = majors.findAll { it.code =~ "MIS" || it.description =~ "MIS"}
        assertEquals majorMinorConcentrations.list.size(), filteredMajors.size()

        majorMinorConcentrations = MajorMinorConcentration.fetchBySomeAttribute("MIS", [fieldOfStudy: "MINOR"])
        majors = MajorMinorConcentration.findAllByValidMinorIndicator(true)
        filteredMajors = majors.findAll { it.code =~ "MIS" || it.description =~ "MIS"}
        assertEquals majorMinorConcentrations.list.size(), filteredMajors.size()

        majorMinorConcentrations = MajorMinorConcentration.fetchBySomeAttribute("MIS", [fieldOfStudy: "CONCENTRATION"])
        majors = MajorMinorConcentration.findAllByValidConcentratnIndicator(true)
        filteredMajors = majors.findAll { it.code =~ "MIS" || it.description =~ "MIS"}
        assertEquals majorMinorConcentrations.list.size(), filteredMajors.size()

        majorMinorConcentrations = MajorMinorConcentration.fetchBySomeAttribute("MIS", [fieldOfStudy: "INTERNSHIP"])
        majors = MajorMinorConcentration.findAllByCodeIlikeOrDescriptionIlike("%MIS%", "%MIS%")
        assertEquals majorMinorConcentrations.list.size(), majors.size()

        majorMinorConcentrations = MajorMinorConcentration.fetchBySomeAttribute("MIS", [fieldOfStudy: ""])
        majors = MajorMinorConcentration.findAllByCodeIlikeOrDescriptionIlike("%MIS%", "%MIS%")
        assertEquals majorMinorConcentrations.list.size(), majors.size()

    }


    void testFetchValidMajor() {
        // test no filter, with parms

        def majorMinorConcentration = MajorMinorConcentration.fetchValidMajor("MIS", [fieldOfStudy: "MAJOR"])
        assertNotNull majorMinorConcentration

        majorMinorConcentration = MajorMinorConcentration.fetchValidMajor("MIS", [fieldOfStudy: "MINOR"])
        assertNotNull majorMinorConcentration

        majorMinorConcentration = MajorMinorConcentration.fetchValidMajor("MIS", [fieldOfStudy: "CONCENTRATION"])
        assertNotNull majorMinorConcentration

        majorMinorConcentration = MajorMinorConcentration.fetchValidMajor("MIS", [fieldOfStudy: "INTERNSHIP"])
        assertNotNull majorMinorConcentration

        majorMinorConcentration = MajorMinorConcentration.fetchValidMajor("MIS", [fieldOfStudy: ""])
        assertNotNull majorMinorConcentration

    }


    void testFetchInValidMajor() {
        // test no filter, with parms
        assertNull MajorMinorConcentration.findByCode("M")

        def majorMinorConcentration = MajorMinorConcentration.fetchValidMajor("M", [fieldOfStudy: "MAJOR"])
        assertNull majorMinorConcentration

        def bilMajor = MajorMinorConcentration.findByCode("BIL")
        assertNull bilMajor.validMajorIndicator
        majorMinorConcentration = MajorMinorConcentration.fetchValidMajor("BIL", [fieldOfStudy: "MAJOR"])
        assertNull majorMinorConcentration


        majorMinorConcentration = MajorMinorConcentration.fetchValidMajor("M", [fieldOfStudy: "MINOR"])
        assertNull majorMinorConcentration

        assertNull bilMajor.validMinorIndicator
        majorMinorConcentration = MajorMinorConcentration.fetchValidMajor("BIL", [fieldOfStudy: "MINOR"])
        assertNull majorMinorConcentration

        def marbMajor = MajorMinorConcentration.findByCode("MARB")
        assertNull marbMajor.validConcentratnIndicator
        majorMinorConcentration = MajorMinorConcentration.fetchValidMajor("M", [fieldOfStudy: "CONCENTRATION"])
        assertNull majorMinorConcentration

        majorMinorConcentration = MajorMinorConcentration.fetchValidMajor("MARB", [fieldOfStudy: "CONCENTRATION"])
        assertNull majorMinorConcentration

        majorMinorConcentration = MajorMinorConcentration.fetchValidMajor("M", [fieldOfStudy: ""])
        assertNull majorMinorConcentration

    }


    private def newValidForCreateMajorMinorConcentration() {
        def majorMinorConcentration = new MajorMinorConcentration(
                code: i_success_code,
                description: i_success_description,
                validMajorIndicator: i_success_validMajorIndicator,
                validMinorIndicator: i_success_validMinorIndicator,
                validConcentratnIndicator: i_success_validConcentratnIndicator,
                occupationIndicator: i_success_occupationIndicator,
                aidEligibilityIndicator: i_success_aidEligibilityIndicator,
                systemRequiredIndicator: i_success_systemRequiredIndicator,
                voiceResponseMsgNumber: i_success_voiceResponseMessageNumber,
                sevisEquiv: i_success_studentExchangeVisitorInformationSystemEquivalent,
                cipcCode: CIPCode.findWhere(code: "TT"),
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return majorMinorConcentration
    }

}