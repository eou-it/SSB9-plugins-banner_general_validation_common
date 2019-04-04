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
class FgacDomainIntegrationTests extends BaseIntegrationTestCase {

    def fgacDomainService


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
    void testCreateFgacDomain() {
        def fgacDomain = newFgacDomain()
        save fgacDomain
        //Test if the generated entity now has an id assigned
        assertNotNull fgacDomain.id
    }


    @Test
    void testUpdateFgacDomain() {
        def fgacDomain = newFgacDomain()
        save fgacDomain

        assertNotNull fgacDomain.id
        assertEquals 0L, fgacDomain.version
        assertEquals "TTTTT", fgacDomain.code
        assertEquals "TTTTT", fgacDomain.description

        //Update the entity
        def testDate = new Date()
        fgacDomain.code = "UUUUU"
        fgacDomain.description = "UUUUU"
        fgacDomain.lastModified = testDate
        fgacDomain.lastModifiedBy = "test"
        fgacDomain.dataOrigin = "Banner"
        save fgacDomain

        fgacDomain = FgacDomain.get(fgacDomain.id)
        assertEquals 1L, fgacDomain?.version
        assertEquals "UUUUU", fgacDomain.code
        assertEquals "UUUUU", fgacDomain.description
    }


    @Test
    void testOptimisticLock() {
        def fgacDomain = newFgacDomain()
        save fgacDomain

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update GTVFDMN set GTVFDMN_VERSION = 999 where GTVFDMN_SURROGATE_ID = ?", [fgacDomain.id])
        } finally {
//            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        fgacDomain.code = "UUUUU"
        fgacDomain.description = "UUUUU"
        fgacDomain.lastModified = new Date()
        fgacDomain.lastModifiedBy = "test"
        fgacDomain.dataOrigin = "Banner"
        shouldFail(HibernateOptimisticLockingFailureException) {
            fgacDomain.save(flush: true, failOnError: true)
        }
    }


    @Test
    void testDeleteFgacDomain() {
        def fgacDomain = newFgacDomain()
        save fgacDomain
        def id = fgacDomain.id
        assertNotNull id
        fgacDomain.delete()
        assertNull FgacDomain.get(id)
    }


    @Test
    void testValidation() {
        def fgacDomain = newFgacDomain()
        assertTrue "FgacDomain could not be validated as expected due to ${fgacDomain.errors}", fgacDomain.validate()
    }


    @Test
    void testNullValidationFailure() {
        def fgacDomain = new FgacDomain()
        assertFalse "FgacDomain should have failed validation", fgacDomain.validate()
        assertErrorsFor fgacDomain, 'nullable',
                [
                        'code',
                        'description'
                ]
    }




    private def newFgacDomain() {
        def fgacDomain = new FgacDomain(
                code: "TTTTT",
                description: "TTTTT",
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return fgacDomain
    }


}
