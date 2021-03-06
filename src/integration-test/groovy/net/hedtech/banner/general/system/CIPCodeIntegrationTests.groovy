/** *****************************************************************************
 Copyright 2009-2014 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system
import org.junit.Before
import org.junit.Test
import org.junit.After

import net.hedtech.banner.testing.BaseIntegrationTestCase
import groovy.sql.Sql
import org.springframework.orm.hibernate5.HibernateOptimisticLockingFailureException
import static groovy.test.GroovyAssert.*
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback

@Integration
@Rollback
/**
 * Integration test for the CIP code model.
 * */
class CIPCodeIntegrationTests extends BaseIntegrationTestCase {

    def cipCodeService


    @Before
    public void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    @Test
    void testCreateCIPCode() {
        def cipCode = newCIPCode()
        save cipCode
        assertNotNull cipCode.id
    }


    @Test
    void testUpdateCIPCode() {
        def cipCode = newCIPCode()
        save cipCode

        def id = cipCode.id
        def version = cipCode.version
        assertNotNull id
        assertEquals 0L, version

        cipCode.description = "updated"
        save cipCode
        cipCode = CIPCode.get(id)

        assertNotNull "found must not be null", cipCode
        assertEquals "updated", cipCode.description
        assertEquals 1, cipCode.version
    }

    @Test
    void testOptimisticLock() {
        def cipCode = newCIPCode()
        save cipCode

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVCIPC set STVCIPC_VERSION = 999 where STVCIPC_SURROGATE_ID = ?", [cipCode.id])
        } finally {
//            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }

        //Try to update the entity
        cipCode.code = "UU"
        cipCode.description = "UUUU"
        cipCode.lastModified = new Date()
        cipCode.lastModifiedBy = "test"
        cipCode.dataOrigin = "Banner"
        shouldFail(HibernateOptimisticLockingFailureException) {
            cipCode.save(flush: true)
        }
    }

    @Test
    void testDeleteCIPCode() {
        def cipCode = newCIPCode()
        save cipCode

        def id = cipCode.id
        assertNotNull id
        cipCode.delete()
        assertNull CIPCode.get(id)
    }

    @Test
    void testValidation() {
        def cipCode = newCIPCode()
        //should not pass validation since none of the required values are provided
        assertTrue "CIP Code could not be validated as expected due to ${cipCode.errors}", cipCode.validate()
    }

    @Test
    void testNullValidationFailure() {
        def cipCode = new CIPCode()
        assertFalse "CIP Code should have failed validation", cipCode.validate()
        assertErrorsFor cipCode, 'nullable', ['code', 'description', 'publicationYear']
    }

    @Test
    void testMaxSizeValidationFailures() {
        def cipCode = new CIPCode(
                code: 'XXXXXXXXXX',
                description: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX')
        assertFalse "CIPCode should have failed validation", cipCode.validate()
        assertErrorsFor cipCode, 'maxSize', ['code', 'description']
    }


    private def newCIPCode() {
        new CIPCode(code: "TT", description: "TT", cipcAIndicator: true, cipcBIndicator: true, cipcCIndicator: true, sp04Program: "TT", publicationYear: 2010,
                lastModified: new Date(), lastModifiedBy: "test", dataOrigin: "Banner")
    }

}
