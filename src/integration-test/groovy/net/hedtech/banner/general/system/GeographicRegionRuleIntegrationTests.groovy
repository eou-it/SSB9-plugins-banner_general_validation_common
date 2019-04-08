/** *******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system

import groovy.sql.Sql
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.orm.hibernate5.HibernateOptimisticLockingFailureException
import static groovy.test.GroovyAssert.*
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback

@Integration
@Rollback
/**
 * Integration test cases for GeographicRegionRule Domain
 */
class GeographicRegionRuleIntegrationTests extends BaseIntegrationTestCase {

    def insertContent
    def updateContent


    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
        initializeTestDataForReferences()
    }


    @After
    public void tearDown() {
        super.tearDown()
    }

    void initializeTestDataForReferences() {
        insertContent = [region:  GeographicRegion.findByCode('BALT/WASH'), division: GeographicDivision.findByCode('ALUM'), regionType: 'i_test', startTypeRange: '12', endTypeRange: '34']
        updateContent = [region: GeographicRegion.findByCode('BALT/WASH'),  division: GeographicDivision.findByCode('ALUM'), regionType: 'u_test', startTypeRange: '21', endTypeRange: '57']
    }

    @Test
    void testCreate() {
        GeographicRegionRule geographicRegionRule = newGeographicRegionRule(insertContent)
        geographicRegionRule = save geographicRegionRule
        assertNotNull geographicRegionRule
        assertNotNull geographicRegionRule.id
        assertNotNull geographicRegionRule.version
        assertEquals 0L, geographicRegionRule.version
        assertNotNull geographicRegionRule.lastModified
        assertNotNull geographicRegionRule.lastModifiedBy
        assertNotNull geographicRegionRule.dataOrigin
        assertEquals geographicRegionRule.region, insertContent.region
        assertEquals geographicRegionRule.division, insertContent.division
        assertEquals geographicRegionRule.regionType, insertContent.regionType
        assertEquals geographicRegionRule.startTypeRange, insertContent.startTypeRange
        assertEquals geographicRegionRule.endTypeRange, insertContent.endTypeRange
    }


    @Test
    void testUpdate() {
        GeographicRegionRule geographicRegionRule = newGeographicRegionRule(insertContent)
        geographicRegionRule = save geographicRegionRule
        assertNotNull geographicRegionRule
        assertNotNull geographicRegionRule.id
        assertNotNull geographicRegionRule.version
        assertEquals 0L, geographicRegionRule.version
        assertNotNull geographicRegionRule.lastModified
        assertNotNull geographicRegionRule.lastModifiedBy
        assertEquals geographicRegionRule.region, insertContent.region
        assertEquals geographicRegionRule.division, insertContent.division
        assertEquals geographicRegionRule.regionType, insertContent.regionType
        assertEquals geographicRegionRule.startTypeRange, insertContent.startTypeRange
        assertEquals geographicRegionRule.endTypeRange, insertContent.endTypeRange
        geographicRegionRule.region = updateContent.region
        geographicRegionRule.division = updateContent.division
        geographicRegionRule.regionType = updateContent.regionType
        geographicRegionRule.startTypeRange = updateContent.startTypeRange
        geographicRegionRule.endTypeRange = updateContent.endTypeRange
        geographicRegionRule = save geographicRegionRule
        assertEquals 1L, geographicRegionRule.version
        assertEquals geographicRegionRule.region, updateContent.region
        assertEquals geographicRegionRule.division, updateContent.division
        assertEquals geographicRegionRule.regionType, updateContent.regionType
        assertEquals geographicRegionRule.startTypeRange, updateContent.startTypeRange
        assertEquals geographicRegionRule.endTypeRange, updateContent.endTypeRange
    }

    @Test
    void testDelete() {
        GeographicRegionRule geographicRegionRule = newGeographicRegionRule(insertContent)
        geographicRegionRule = save geographicRegionRule
        def id = geographicRegionRule.id
        assertNotNull id
        geographicRegionRule.delete()
        assertNull GeographicRegionRule.get(id)
    }

    @Test
    void testNullValidationFailure() {
        def geographicRegionRule = new GeographicRegionRule()
        assertFalse "GeographicRegionRule should have failed validation", geographicRegionRule.validate()
        assertErrorsFor(geographicRegionRule, 'nullable', ['region', 'division', 'regionType', 'startTypeRange', 'endTypeRange'])
        assertNoErrorsFor(geographicRegionRule, ['dataOrigin'])
    }


    @Test
    void testOptimisticLock() {
        GeographicRegionRule geographicRegionRule = newGeographicRegionRule(insertContent)
        geographicRegionRule = save geographicRegionRule

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update SORGEOR set SORGEOR_VERSION = 999 where SORGEOR_SURROGATE_ID = ?", [geographicRegionRule.id])
        } finally {
//            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        geographicRegionRule.dataOrigin = 'update'
        shouldFail(HibernateOptimisticLockingFailureException) {
            geographicRegionRule.save(flush: true, failOnError: true)
        }
    }

    @Test
    void testCount() {
        save newGeographicRegionRule(insertContent)
        def expectedCount
        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            expectedCount = sql.firstRow("SELECT count(DISTINCT SORGEOR_GEOR_CODE||SORGEOR_GEOD_CODE) as gaCount from SORGEOR").gaCount.toInteger()
        } finally {
//            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        def actualCount = GeographicRegionRule.countAll().toInteger()
        assertEquals expectedCount, actualCount
    }

    @Test
    void testFindAll() {
        def geographicRegionRule = save newGeographicRegionRule(insertContent)
        assertNotNull geographicRegionRule
        def geographicaAreas = GeographicRegionRule.fetchAll([max: 500, offset: 0])
        assertFalse geographicaAreas.isEmpty()
        assertEquals geographicaAreas.size(), GeographicRegionRule.countAll()
    }

    @Test
    void testFindByGuid() {
        GeographicRegionRule geographicRegionRule = save newGeographicRegionRule(insertContent)
        assertNotNull geographicRegionRule
        def guid = GlobalUniqueIdentifier.fetchByDomainKeyAndLdmName(geographicRegionRule.region.code + '-^' + geographicRegionRule.division.code, GeneralValidationCommonConstants.GEOGRAPHIC_AREA_LDM_NAME).guid
        assertNotNull guid
        def geographicArea = GeographicRegionRule.fetchByGuid(guid)
        assertEquals geographicArea.getAt(0), geographicRegionRule.region.code + '-' + geographicRegionRule.division.code
        GeographicRegion geographicRegion = GeographicRegion.findByCode(geographicRegionRule.region.code)
        assertNotNull geographicRegion
        assertEquals geographicArea.getAt(4),GlobalUniqueIdentifier.fetchByDomainKeyAndLdmName(geographicRegion.code,GeneralValidationCommonConstants.GEOGRAPHIC_REGION_LDM_NAME).guid
        GeographicDivision geographicDivision = GeographicDivision.findByCode(geographicRegionRule.division.code)
        assertNotNull geographicDivision
        assertEquals geographicArea.getAt(3),GlobalUniqueIdentifier.fetchByDomainKeyAndLdmName(geographicDivision.code,GeneralValidationCommonConstants.GEOGRAPHIC_DIVISION_LDM_NAME).guid
        assertEquals geographicArea.getAt(1),geographicRegion.description+ '-' +geographicDivision.description
    }

    private GeographicRegionRule newGeographicRegionRule(Map content) {
        new GeographicRegionRule(content)
    }

}
