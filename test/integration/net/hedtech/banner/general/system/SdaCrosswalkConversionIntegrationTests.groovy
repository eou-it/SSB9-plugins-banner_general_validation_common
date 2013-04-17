/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import net.hedtech.banner.testing.BaseIntegrationTestCase
import groovy.sql.Sql
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

class SdaCrosswalkConversionIntegrationTests extends BaseIntegrationTestCase {

    //Test data for creating new domain instance
    //Valid test data (For success tests)

    def i_success_external = "TTTTT"
    def i_success_internal = "TTTTT"
    def i_success_reportingDate = new Date()

    def i_success_translation = "TTTTT"
    def i_success_internalSequenceNumber = 1

    def i_success_internalGroup = "TTTTT"
    def i_success_description = "TTTTT"
    def i_success_systemRequestIndicator = "Y"

    //Invalid test data (For failure tests)

    def i_failure_external = "TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT"
    def i_failure_internal = "TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT"
    def i_failure_reportingDate = new Date()

    def i_failure_translation = "TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT"
    def i_failure_internalSequenceNumber = null

    def i_failure_internalGroup = "TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT"
    def i_failure_description = "TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT"
    def i_failure_systemRequestIndicator = "X"

    //Test data for creating updating domain instance
    //Valid test data (For success tests)

    def u_success_external = "TTTTT"
    def u_success_internal = "TTTTT"
    def u_success_reportingDate = new Date()

    def u_success_translation = "TTTTT"
    def u_success_internalSequenceNumber = 1

    def u_success_internalGroup = "TTTTT"
    def u_success_description = "TTTTT"
    def u_success_systemRequestIndicator = "Y"

    //Valid test data (For failure tests)

    def u_failure_external = "TTTTTTTT"
    def u_failure_internal = "TTTTTTTT"
    def u_failure_reportingDate = new Date()

    def u_failure_translation = "TTTTTTTT"
    def u_failure_internalSequenceNumber = 1

    def u_failure_internalGroup = "TTTTTTTT"
    def u_failure_description = "TTTTT"
    def u_failure_systemRequestIndicator = "L"


    protected void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateValidSdaCrosswalkConversion() {
        def sdaCrosswalkConversion = newValidForCreateSdaCrosswalkConversion()
        save sdaCrosswalkConversion
        //Test if the generated entity now has an id assigned
        assertNotNull sdaCrosswalkConversion.id
    }


    void testCreateInvalidSdaCrosswalkConversion() {
        def sdaCrosswalkConversion = newInvalidForCreateSdaCrosswalkConversion()
        assertFalse sdaCrosswalkConversion.validate()
    }


    void testUpdateValidSdaCrosswalkConversion() {
        def sdaCrosswalkConversion = newValidForCreateSdaCrosswalkConversion()
        save sdaCrosswalkConversion
        assertNotNull sdaCrosswalkConversion.id
        assertEquals 0L, sdaCrosswalkConversion.version
        assertEquals i_success_external, sdaCrosswalkConversion.external
        assertEquals i_success_internal, sdaCrosswalkConversion.internal
        assertEquals i_success_reportingDate, sdaCrosswalkConversion.reportingDate
        assertEquals i_success_translation, sdaCrosswalkConversion.translation
        assertEquals i_success_internalSequenceNumber, sdaCrosswalkConversion.internalSequenceNumber
        assertEquals i_success_internalGroup, sdaCrosswalkConversion.internalGroup
        assertEquals i_success_description, sdaCrosswalkConversion.description
        assertEquals i_success_systemRequestIndicator, sdaCrosswalkConversion.systemRequestIndicator

        //Update the entity
        sdaCrosswalkConversion.external = "UUUUU"

        save sdaCrosswalkConversion
        //Asset for successful update
        sdaCrosswalkConversion = SdaCrosswalkConversion.get(sdaCrosswalkConversion.id)
        assertEquals 1L, sdaCrosswalkConversion?.version
        assertEquals sdaCrosswalkConversion.external, "UUUUU"
    }


    void testUpdateInvalidSdaCrosswalkConversion() {
        def sdaCrosswalkConversion = newValidForCreateSdaCrosswalkConversion()
        save sdaCrosswalkConversion
        assertNotNull sdaCrosswalkConversion.id
        assertEquals 0L, sdaCrosswalkConversion.version
        assertEquals i_success_external, sdaCrosswalkConversion.external
        assertEquals i_success_internal, sdaCrosswalkConversion.internal
        assertEquals i_success_reportingDate, sdaCrosswalkConversion.reportingDate
        assertEquals i_success_translation, sdaCrosswalkConversion.translation
        assertEquals i_success_internalSequenceNumber, sdaCrosswalkConversion.internalSequenceNumber
        assertEquals i_success_internalGroup, sdaCrosswalkConversion.internalGroup
        assertEquals i_success_description, sdaCrosswalkConversion.description
        assertEquals i_success_systemRequestIndicator, sdaCrosswalkConversion.systemRequestIndicator

        //Update the entity with invalid values
        sdaCrosswalkConversion.external = u_failure_external
        sdaCrosswalkConversion.internal = u_failure_internal
        sdaCrosswalkConversion.reportingDate = u_failure_reportingDate
        sdaCrosswalkConversion.translation = u_failure_translation
        sdaCrosswalkConversion.internalSequenceNumber = u_failure_internalSequenceNumber
        sdaCrosswalkConversion.internalGroup = u_failure_internalGroup
        sdaCrosswalkConversion.description = u_failure_description
        sdaCrosswalkConversion.systemRequestIndicator = u_failure_systemRequestIndicator
        assertFalse sdaCrosswalkConversion.validate()
    }


    void testOptimisticLock() {
        def sdaCrosswalkConversion = newValidForCreateSdaCrosswalkConversion()
        save sdaCrosswalkConversion

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update GTVSDAX set GTVSDAX_VERSION = 999 where GTVSDAX_SURROGATE_ID = ?", [sdaCrosswalkConversion.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        sdaCrosswalkConversion.external = "XXXX"
        shouldFail(HibernateOptimisticLockingFailureException) {
            sdaCrosswalkConversion.save(failOnError: true, flush: true)
        }
    }


    void testDeleteSdaCrosswalkConversion() {
        def sdaCrosswalkConversion = newValidForCreateSdaCrosswalkConversion()
        save sdaCrosswalkConversion
        def id = sdaCrosswalkConversion.id
        assertNotNull id
        sdaCrosswalkConversion.delete()
        assertNull SdaCrosswalkConversion.get(id)
    }


    void testValidation() {
        def sdaCrosswalkConversion = newValidForCreateSdaCrosswalkConversion()
        assertTrue "SdaCrosswalkConversion could not be validated as expected due to ${sdaCrosswalkConversion.errors}", sdaCrosswalkConversion.validate()
    }


    void testNullValidationFailure() {
        def sdaCrosswalkConversion = new SdaCrosswalkConversion()
        assertFalse "SdaCrosswalkConversion should have failed validation", sdaCrosswalkConversion.validate()
        assertErrorsFor sdaCrosswalkConversion, 'nullable',
                [
                        'external',
                        'internal',
                        'internalGroup',
                        'description'
                ]
        assertNoErrorsFor sdaCrosswalkConversion,
                [
                        'reportingDate',
                        'translation',
                        'internalSequenceNumber',
                        'systemRequestIndicator'
                ]
    }


    void testMaxSizeValidationFailures() {
        def sdaCrosswalkConversion = new SdaCrosswalkConversion(
                translation: 'XXXXXXXXXXXXXXXXX',
                systemRequestIndicator: 'XXX')
        assertFalse "SdaCrosswalkConversion should have failed validation", sdaCrosswalkConversion.validate()
        assertErrorsFor sdaCrosswalkConversion, 'maxSize', ['translation', 'systemRequestIndicator']
    }


    void testfetchAllByInternalAndExternalAndInternalGroup() {
        def sdax = newValidForCreateSdaCrosswalkConversion()
        save sdax

        def sdaxs = SdaCrosswalkConversion.fetchAllByInternalAndExternalAndInternalGroup(i_success_internal,
                i_success_external,
                i_success_internalGroup)
        assertNotNull sdaxs
        assertEquals sdaxs.size(), 1

    }


    void testfetchAllByInternalAndInternalGroup() {
        def sdax = newValidForCreateSdaCrosswalkConversion()
        save sdax

        def sdaxs = SdaCrosswalkConversion.fetchAllByInternalAndInternalGroup(i_success_internal,
                i_success_internalGroup)
        assertNotNull sdaxs
        assertEquals sdaxs.size(), 1

    }


    void testfetchAllByInternalAndLessExternalAndInternalGroup() {
        def sdax = newValidForCreateSdaCrosswalkConversion()
        save sdax

        def sdaxs = SdaCrosswalkConversion.fetchAllByInternalAndLessExternalAndInternalGroup(i_success_internal,
                "UUUUU",
                i_success_internalGroup)
        assertNotNull sdaxs
        assertEquals sdaxs.size(), 1
    }


    private def newValidForCreateSdaCrosswalkConversion() {
        def sdaCrosswalkConversion = new SdaCrosswalkConversion(
                external: i_success_external,
                internal: i_success_internal,
                reportingDate: i_success_reportingDate,
                translation: i_success_translation,
                internalSequenceNumber: i_success_internalSequenceNumber,
                internalGroup: i_success_internalGroup,
                description: i_success_description,
                systemRequestIndicator: i_success_systemRequestIndicator,
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return sdaCrosswalkConversion
    }


    private def newInvalidForCreateSdaCrosswalkConversion() {
        def sdaCrosswalkConversion = new SdaCrosswalkConversion(
                external: i_failure_external,
                internal: i_failure_internal,
                reportingDate: i_failure_reportingDate,
                translation: i_failure_translation,
                internalSequenceNumber: i_failure_internalSequenceNumber,
                internalGroup: i_failure_internalGroup,
                description: i_failure_description,
                systemRequestIndicator: i_failure_systemRequestIndicator,
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return sdaCrosswalkConversion
    }

}
