/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system
import org.junit.Before
import org.junit.Test
import org.junit.After

import groovy.sql.Sql
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.springframework.orm.hibernate5.HibernateOptimisticLockingFailureException
import static groovy.test.GroovyAssert.*
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback

@Integration
@Rollback
class ProxyAccessSystemIntegrationTests extends BaseIntegrationTestCase {
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
    void testCreateProxyAccessSystem() {
        def proxyAccessSystem = newProxyAccessSystem()
        save proxyAccessSystem
        //Test if the generated entity now has an id assigned
        assertNotNull proxyAccessSystem.id
    }

    @Test
    void testUpdateProxyAccessSystem() {
        def proxyAccessSystem = newProxyAccessSystem()
        save proxyAccessSystem

        assertNotNull proxyAccessSystem.id
        assertEquals 0L, proxyAccessSystem.version
        assertEquals "PROXYNew", proxyAccessSystem.code
        assertEquals "Proxy Access", proxyAccessSystem.description
        assertEquals "Y", proxyAccessSystem.systemReqInd

        //Update the entity
        def testDate = new Date()
        proxyAccessSystem.code = "ACCESS_WINDOW_DAYS"
        proxyAccessSystem.description = "Number of days that a proxy is allowed access"
        proxyAccessSystem.systemReqInd = "R"
        proxyAccessSystem.lastModified = testDate
        proxyAccessSystem.lastModifiedBy = "test"
        proxyAccessSystem.dataOrigin = "Banner"
        save proxyAccessSystem

        proxyAccessSystem = ProxyAccessSystem.get(proxyAccessSystem.id)
        assertEquals 1L, proxyAccessSystem?.version
        assertEquals "ACCESS_WINDOW_DAYS", proxyAccessSystem.code
        assertEquals "Number of days that a proxy is allowed access", proxyAccessSystem.description
        assertEquals "R", proxyAccessSystem.systemReqInd
    }

    @Test
    void testOptimisticLock() {
        def proxyAccessSystem = newProxyAccessSystem()
        save proxyAccessSystem

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update GTVSYST set GTVSYST_VERSION = 999 where GTVSYST_SURROGATE_ID = ?", [proxyAccessSystem.id])
        } finally {
//            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        proxyAccessSystem.code = "PROXY"
        proxyAccessSystem.description = "Proxy Access"
        proxyAccessSystem.systemReqInd = "Y"
        proxyAccessSystem.lastModified = new Date()
        proxyAccessSystem.lastModifiedBy = "BASELINE"
        proxyAccessSystem.dataOrigin = "BASELINE"
        shouldFail(HibernateOptimisticLockingFailureException) {
            proxyAccessSystem.save(flush: true, failOnError: true)
        }
    }

    @Test
    void testDeleteProxyAccessSystem() {
        def proxyAccessSystem = newProxyAccessSystem()
        save proxyAccessSystem
        def id = proxyAccessSystem.id
        assertNotNull id
        proxyAccessSystem.delete()
        assertNull ProxyAccessSystem.get(id)
    }

    @Test
    void testValidation() {
        def proxyAccessSystem = newProxyAccessSystem()
        assertTrue "ProxyAccessSystem could not be validated as expected due to ${proxyAccessSystem.errors}", proxyAccessSystem.validate()
    }

    @Test
    void testNullValidationFailure() {
        def proxyAccessSystem = new ProxyAccessSystem()
        assertFalse "ProxyAccessSystem should have failed validation", proxyAccessSystem.validate()
        assertErrorsFor proxyAccessSystem, 'nullable', ['code']
        assertNoErrorsFor proxyAccessSystem, ['description', 'systemReqInd']
    }

    @Test
    void testMaxSizeValidationFailures() {
        def proxyAccessSystem = new ProxyAccessSystem()
        assertFalse "ProxyAccessSystem should have failed validation", proxyAccessSystem.validate()
        assertErrorsFor proxyAccessSystem, 'maxSize', []
    }

    private def newProxyAccessSystem() {
        new ProxyAccessSystem(code: "PROXYNew",
                description: "Proxy Access",
                systemReqInd: "Y",
                lastModified: new Date(),
                lastModifiedBy: "BASELINE")

    }

}
