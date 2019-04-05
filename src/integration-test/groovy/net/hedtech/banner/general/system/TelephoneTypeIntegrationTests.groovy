/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
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
class TelephoneTypeIntegrationTests extends BaseIntegrationTestCase {

    //Test data for creating new domain instance
    //Valid test data (For success tests)

    def i_success_code = "TTTT"
    def i_success_description = "TTTTT"
    //Invalid test data (For failure tests)

    def i_failure_code = null
    def i_failure_description = null

    //Test data for creating updating domain instance
    //Valid test data (For success tests)

    def u_success_code = "XXXX"
    def u_success_description = "XXXX"
    //Valid test data (For failure tests)

    def u_failure_code = "1234567890"
    def u_failure_description = "1234567890123456789012345678901234567890"


    @Before
    public void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    @After
    public void tearDown() {
        super.tearDown()
    }

    @Test
    void testCreateValidTelephoneType() {
        def telephoneType = newValidForCreateTelephoneType()
        save telephoneType
        //Test if the generated entity now has an id assigned
        assertNotNull telephoneType.id
    }


    @Test
    void testUpdateValidTelephoneType() {
        def telephoneType = newValidForCreateTelephoneType()
        save telephoneType
        assertNotNull telephoneType.id
        assertEquals 0L, telephoneType.version
        assertEquals i_success_code, telephoneType.code
        assertEquals i_success_description, telephoneType.description

        //Update the entity
        telephoneType.description = u_success_description
        save telephoneType
        //Asset for sucessful update
        telephoneType = TelephoneType.get(telephoneType.id)
        assertEquals 1L, telephoneType?.version
        assertEquals u_success_description, telephoneType.description
    }


    @Test
    void testOptimisticLock() {
        def telephoneType = newValidForCreateTelephoneType()
        save telephoneType

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVTELE set STVTELE_VERSION = 999 where STVTELE_SURROGATE_ID = ?", [telephoneType.id])
        } finally {
//            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        telephoneType.description = u_success_description
        shouldFail(HibernateOptimisticLockingFailureException) {
            telephoneType.save(failOnError: true, flush: true)
        }
    }

    @Test
    void testDeleteTelephoneType() {
        def telephoneType = newValidForCreateTelephoneType()
        save telephoneType
        def id = telephoneType.id
        assertNotNull id
        telephoneType.delete()
        assertNull TelephoneType.get(id)
    }


    @Test
    void testNullValidationFailure() {
        def telephoneType = new TelephoneType()
        assertFalse "TelephoneType should have failed validation", telephoneType.validate()
        assertErrorsFor telephoneType, 'nullable',
                [
                        'code',
                        'description'
                ]
    }


    private def newValidForCreateTelephoneType() {
        def telephoneType = new TelephoneType(
                code: i_success_code,
                description: i_success_description,
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return telephoneType
    }

    private def newInvalidForCreateTelephoneType() {
        def telephoneType = new TelephoneType(
                code: i_failure_code,
                description: i_failure_description,
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return telephoneType
    }

}
