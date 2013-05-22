/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import grails.validation.ValidationException
import groovy.sql.Sql
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

import java.text.SimpleDateFormat

class InstitutionalHonorIntegrationTests extends BaseIntegrationTestCase {

    protected void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateValidInstitutionalHonor() {
        def institutionalHonor = newValidForCreateInstitutionalHonor()
        institutionalHonor.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull institutionalHonor.id
    }


    void testCreateInvalidInstitutionalHonor() {
        def institutionalHonor = newInvalidForCreateInstitutionalHonor()
        shouldFail(ValidationException) {
            institutionalHonor.save(failOnError: true, flush: true)
        }
    }


    void testUpdateValidInstitutionalHonor() {
        def institutionalHonor = newValidForCreateInstitutionalHonor()
        institutionalHonor.save(failOnError: true, flush: true)
        assertNotNull institutionalHonor.id
        assertEquals 0L, institutionalHonor.version
        assertEquals "TTTTTT", institutionalHonor.code
        assertEquals "123456789012345678901234567890", institutionalHonor.description
        assertEquals "Y", institutionalHonor.transcPrintIndicator
        assertEquals "Y", institutionalHonor.commencePrintIndicator
        assertEquals "TTT", institutionalHonor.electronicDataInterchangeEquivalent

        //Update the entity
        institutionalHonor.description = "UPDATE789012345678901234567890"
        institutionalHonor.transcPrintIndicator = null
        institutionalHonor.commencePrintIndicator = null
        institutionalHonor.electronicDataInterchangeEquivalent = "UPD"
        institutionalHonor.save(failOnError: true, flush: true)

        //Assert for sucessful update
        institutionalHonor = InstitutionalHonor.get(institutionalHonor.id)
        assertEquals 1L, institutionalHonor?.version
        assertEquals "UPDATE789012345678901234567890", institutionalHonor.description
        assertNull institutionalHonor.transcPrintIndicator
        assertNull institutionalHonor.commencePrintIndicator
        assertEquals "UPD", institutionalHonor.electronicDataInterchangeEquivalent
    }


    void testUpdateInvalidInstitutionalHonor() {
        def institutionalHonor = newValidForCreateInstitutionalHonor()
        institutionalHonor.save(failOnError: true, flush: true)
        assertNotNull institutionalHonor.id
        assertEquals 0L, institutionalHonor.version
        assertEquals "TTTTTT", institutionalHonor.code
        assertEquals "123456789012345678901234567890", institutionalHonor.description
        assertEquals "Y", institutionalHonor.transcPrintIndicator
        assertEquals "Y", institutionalHonor.commencePrintIndicator
        assertEquals "TTT", institutionalHonor.electronicDataInterchangeEquivalent

        //Update the entity with invalid values
        institutionalHonor.description = "123456789012345678901234567890FAIL"
        institutionalHonor.transcPrintIndicator = "Z"
        institutionalHonor.commencePrintIndicator = "Z"
        institutionalHonor.electronicDataInterchangeEquivalent = "FAIL"
        shouldFail(ValidationException) {
            institutionalHonor.save(failOnError: true, flush: true)
        }
    }


    void testDates() {
        def time = new SimpleDateFormat('HHmmss')
        def hour = new SimpleDateFormat('HH')
        def date = new SimpleDateFormat('yyyy-M-d')
        def today = new Date()

        def institutionalHonor = newValidForCreateInstitutionalHonor()
        institutionalHonor.save(flush: true, failOnError: true)
        institutionalHonor.refresh()
        assertNotNull "InstitutionalHonor should have been saved", institutionalHonor.id

        // test date values -
        assertEquals date.format(today), date.format(institutionalHonor.lastModified)
        assertEquals hour.format(today), hour.format(institutionalHonor.lastModified)
    }


    void testOptimisticLock() {
        def institutionalHonor = newValidForCreateInstitutionalHonor()
        institutionalHonor.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVHONR set STVHONR_VERSION = 999 where STVHONR_SURROGATE_ID = ?", [institutionalHonor.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        institutionalHonor.description = "UPDATE"
        shouldFail(HibernateOptimisticLockingFailureException) {
            institutionalHonor.save(failOnError: true, flush: true)
        }
    }


    void testDeleteInstitutionalHonor() {
        def institutionalHonor = newValidForCreateInstitutionalHonor()
        institutionalHonor.save(failOnError: true, flush: true)
        def id = institutionalHonor.id
        assertNotNull id
        institutionalHonor.delete()
        assertNull InstitutionalHonor.get(id)
    }


    void testValidation() {
        def institutionalHonor = newInvalidForCreateInstitutionalHonor()
        assertFalse "InstitutionalHonor could not be validated as expected due to ${institutionalHonor.errors}", institutionalHonor.validate()
    }


    void testNullValidationFailure() {
        def institutionalHonor = new InstitutionalHonor()
        assertFalse "InstitutionalHonor should have failed validation", institutionalHonor.validate()
        assertErrorsFor institutionalHonor, 'nullable',
                [
                        'code'
                ]
        assertNoErrorsFor institutionalHonor,
                [
                        'description',
                        'transcPrintIndicator',
                        'commencePrintIndicator',
                        'electronicDataInterchangeEquivalent'
                ]
    }


    void testMaxSizeValidationFailures() {
        def institutionalHonor = new InstitutionalHonor(
                description: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
                transcPrintIndicator: 'XXX',
                commencePrintIndicator: 'XXX',
                electronicDataInterchangeEquivalent: 'XXXXX')
        assertFalse "InstitutionalHonor should have failed validation", institutionalHonor.validate()
        assertErrorsFor institutionalHonor, 'maxSize', ['description', 'transcPrintIndicator', 'commencePrintIndicator', 'electronicDataInterchangeEquivalent']
    }


    private def newValidForCreateInstitutionalHonor() {
        def institutionalHonor = new InstitutionalHonor(
                code: "TTTTTT",
                description: "123456789012345678901234567890",
                transcPrintIndicator: "Y",
                commencePrintIndicator: "Y",
                electronicDataInterchangeEquivalent: "TTT",
        )
        return institutionalHonor
    }


    private def newInvalidForCreateInstitutionalHonor() {
        def institutionalHonor = new InstitutionalHonor(
                code: "FAILTT",
                description: "FAIL56789012345678901234567890",
                transcPrintIndicator: "Z",
                commencePrintIndicator: "Z",
                electronicDataInterchangeEquivalent: "FAIL",
        )
        return institutionalHonor
    }

}
