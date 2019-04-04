/** *****************************************************************************
 Copyright 2009-2019 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import static groovy.test.GroovyAssert.*
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback

@Integration
@Rollback
class CampusServiceTests extends BaseIntegrationTestCase {

    def campusService


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
    void testCreateCampus() {
        def campus = new Campus(code: "TT", description: "TT", lastModified: new Date(), dataOrigin: "banner", lastModifiedBy: "banner")
        campus = campusService.create([domainModel: campus])
        assertNotNull campus
    }


    @Test
    void testUpdateCampus() {
        def campus = new Campus(code: "TT", description: "TT", lastModified: new Date(), dataOrigin: "banner", lastModifiedBy: "banner")
        campus = campusService.create([domainModel: campus])

        Campus campusUpdate = Campus.findWhere(code: "TT")
        assertNotNull campusUpdate

        campusUpdate.description = "ZZ"
        campusUpdate = campusService.update([domainModel: campusUpdate])
        assertEquals "ZZ", campusUpdate.description
    }


    @Test
    void testDeleteCampus() {
        def campus = new Campus(code: "TT", description: "TT", lastModified: new Date(), dataOrigin: "banner", lastModifiedBy: "banner")
        campus = campusService.create([domainModel: campus])
        assertNotNull campus
        def id = campus.id
        def deleteMe = Campus.get(id)
        campusService.delete([domainModel: deleteMe])
        assertNull Campus.get(id)
    }


    @Test
    void testList() {
        def campus = campusService.list()
        assertTrue campus.size() > 0
    }


    @Test
    void testGetCodeToTimeZoneIDMap() {
        def list = campusService.list()
        def map = campusService.getCodeToTimeZoneIDMap()
        assertEquals list.size(), map.size()
        assertTrue map.keySet().containsAll(list.collect { it.code })
    }

}
