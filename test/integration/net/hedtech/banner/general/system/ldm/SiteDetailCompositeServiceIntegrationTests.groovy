/** *******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.general.system.Campus
import net.hedtech.banner.general.system.ldm.v1.SiteDetail
import net.hedtech.banner.testing.BaseIntegrationTestCase

class SiteDetailCompositeServiceIntegrationTests extends BaseIntegrationTestCase {

    Campus campus
    def siteDetailCompositeService


    void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
        initiializeDataReferences()
    }


    private void initiializeDataReferences() {
        campus = Campus.findByCode('A')
    }

    /**
     * Test case for List method
     */
    void testList() {
        def paginationParams = [max: '20', offset: '0']
        List siteList = siteDetailCompositeService.list(paginationParams)
        assertNotNull siteList
        assertFalse siteList.isEmpty()
        assertTrue siteList.code.contains(campus.code)
    }

    /**
     * Testcase for count method
     */
    void testCount() {
        assertTrue siteDetailCompositeService.count() > 0
    }

    /**
     * Testcase for show method
     */
    void testGet() {
        def paginationParams = [max: '20', offset: '0']
        List siteList = siteDetailCompositeService.list(paginationParams)
        assertNotNull siteList
        assertTrue siteList.size() > 0
        assertNotNull siteList[0].guid
        def site = siteDetailCompositeService.get(siteList[0].guid)
        assertNotNull site
        assertEquals siteList[0].code, site.code
    }

    /**
     * Testcase for fetchByCampusId method
     */
    void testFetchByCampusId() {
        def siteDetail = siteDetailCompositeService.fetchByCampusId(campus.id)
        assertNotNull siteDetail
        assertEquals campus.id, siteDetail.id
        assertEquals campus.code, siteDetail.code
        assertEquals campus.description, siteDetail.description

    }

    /**
     * Testcase for fetchByCampusCode
     */
    void testFetchFetchByCampusCode() {
        SiteDetail site = siteDetailCompositeService.fetchByCampusCode(campus.code)
        assertNotNull site
        assertEquals campus.id, site.id
        assertEquals campus.code, site.code
        assertEquals campus.description, site.description
    }

}
