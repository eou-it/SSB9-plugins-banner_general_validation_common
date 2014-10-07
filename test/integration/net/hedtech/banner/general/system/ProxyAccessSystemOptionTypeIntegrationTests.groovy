/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system
import org.junit.Before
import org.junit.Test
import org.junit.After

import groovy.sql.Sql
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

/**
 * Integration tests for ProxyAccessSystemOptionType Tests
 */
class ProxyAccessSystemOptionTypeIntegrationTests extends BaseIntegrationTestCase {

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
    void testCreateProxyAccessSystemOptionType() {
        def proxyAccessSystemOptionType = newProxyAccessSystemOptionType()
        save proxyAccessSystemOptionType
        //Test if the generated entity now has an id assigned
        assertNotNull proxyAccessSystemOptionType.id
    }

    @Test
    void testUpdateProxyAccessSystemOptionType() {
        def proxyAccessSystemOptionType = newProxyAccessSystemOptionType()
        save proxyAccessSystemOptionType

        assertNotNull proxyAccessSystemOptionType.id
        assertEquals 0L, proxyAccessSystemOptionType.version
        assertEquals "ACCESS_WINDOW_DAYS_NEW", proxyAccessSystemOptionType.code
        assertEquals "Number of days that a proxy is allowed access", proxyAccessSystemOptionType.description
        assertEquals "R", proxyAccessSystemOptionType.sytemLevelCode

        //Update the entity
        def testDate = new Date()
        proxyAccessSystemOptionType.code = "ACCESS_WINDOW_DAYS_NEW"
        proxyAccessSystemOptionType.description = "Number of days that a proxy is allowed access"
        proxyAccessSystemOptionType.sytemLevelCode = "R"
        proxyAccessSystemOptionType.lastModified = testDate
        proxyAccessSystemOptionType.lastModifiedBy = "test"
        proxyAccessSystemOptionType.dataOrigin = "Banner"
        save proxyAccessSystemOptionType

        proxyAccessSystemOptionType = ProxyAccessSystemOptionType.get(proxyAccessSystemOptionType.id)
        assertEquals 1L, proxyAccessSystemOptionType?.version
        assertEquals "ACCESS_WINDOW_DAYS_NEW", proxyAccessSystemOptionType.code
        assertEquals "Number of days that a proxy is allowed access", proxyAccessSystemOptionType.description
        assertEquals "R", proxyAccessSystemOptionType.sytemLevelCode
    }

    @Test
    void testOptimisticLock() {
        def proxyAccessSystemOptionType = newProxyAccessSystemOptionType()
        save proxyAccessSystemOptionType

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update GTVOTYP set GTVOTYP_VERSION = 999 where GTVOTYP_SURROGATE_ID = ?", [proxyAccessSystemOptionType.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        proxyAccessSystemOptionType.code = "ACCESS_WINDOW_DAYS"
        proxyAccessSystemOptionType.description = "Number of days that a proxy is allowed access"
        proxyAccessSystemOptionType.sytemLevelCode = "R"
        proxyAccessSystemOptionType.lastModified = new Date()
        proxyAccessSystemOptionType.lastModifiedBy = "test"
        proxyAccessSystemOptionType.dataOrigin = "Banner"
        shouldFail(HibernateOptimisticLockingFailureException) {
            proxyAccessSystemOptionType.save(flush: true, failOnError: true)
        }
    }

    @Test
    void testDeleteProxyAccessSystemOptionType() {
        def proxyAccessSystemOptionType = newProxyAccessSystemOptionType()
        save proxyAccessSystemOptionType
        def id = proxyAccessSystemOptionType.id
        assertNotNull id
        proxyAccessSystemOptionType.delete()
        assertNull ProxyAccessSystemOptionType.get(id)
    }

    @Test
    void testValidation() {
        def proxyAccessSystemOptionType = newProxyAccessSystemOptionType()
        assertTrue "ProxyAccessSystemOptionType could not be validated as expected due to ${proxyAccessSystemOptionType.errors}", proxyAccessSystemOptionType.validate()
    }

    @Test
    void testNullValidationFailure() {
        def proxyAccessSystemOptionType = new ProxyAccessSystemOptionType()
        assertFalse "ProxyAccessSystemOptionType should have failed validation", proxyAccessSystemOptionType.validate()
        assertErrorsFor proxyAccessSystemOptionType, 'nullable', ['code', 'description', 'sytemLevelCode']
        assertNoErrorsFor proxyAccessSystemOptionType, []
    }

    @Test
    void testMaxSizeValidationFailures() {
        def proxyAccessSystemOptionType = new ProxyAccessSystemOptionType()
        assertFalse "ProxyAccessSystemOptionType should have failed validation", proxyAccessSystemOptionType.validate()
        assertErrorsFor proxyAccessSystemOptionType, 'maxSize', []
    }



    private def newProxyAccessSystemOptionType() {
        def proxyAccessSystemOptionType = new ProxyAccessSystemOptionType(
                code: "ACCESS_WINDOW_DAYS_NEW",
                systemCode: ProxyAccessSystem.findByCode("PROXY"),
                description: "Number of days that a proxy is allowed access",
                proxyDataType: "VARCHAR2",
                sytemLevelCode: "R",
                proxyOptdefault: "365",
                systemReqInd: "Y",
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return proxyAccessSystemOptionType
    }

}
