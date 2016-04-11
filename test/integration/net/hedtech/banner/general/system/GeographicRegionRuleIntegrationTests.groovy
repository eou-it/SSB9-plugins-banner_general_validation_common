/** *******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system

import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

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
        insertContent = [regionCode: 'BALT/WASH', divisionCode: 'ALUMRC1']
        updateContent = [regionCode: 'BALT/WASH', divisionCode: 'ALUMRC1']
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
        assertEquals geographicRegionRule.regionCode,insertContent.regionCode
        assertEquals geographicRegionRule.divisionCode,insertContent.divisionCode
    }

    private GeographicRegionRule newGeographicRegionRule(Map content) {
        new GeographicRegionRule(
                regionCode: content.regionCode,
                divisionCode: content.divisionCode,
                regionType: 'test',
                startTypeRange: '12',
                endTypeRange: '34'
        )
    }

}
