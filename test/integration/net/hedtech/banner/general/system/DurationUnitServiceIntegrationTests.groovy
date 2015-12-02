/** *****************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

class DurationUnitServiceIntegrationTests extends BaseIntegrationTestCase {


    DurationUnitService durationUnitService

    //Test data for creating new domain instance
    //Valid test data (For success tests)
    def i_success_code = "TTTT"
    def i_success_description = "TTTTT"
    def i_success_numberOfDays = 1
    def i_success_voiceResponseMessageNumber = 1


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
    void testCreate() {
        DurationUnit durationUnit = newValidForCreateDurationUnit()
        durationUnit = durationUnitService.create( [domainModel: durationUnit] )
        assertNotNull durationUnit
    }


    @Test
    void testUpdate() {
        DurationUnit durationUnit = newValidForCreateDurationUnit()
        durationUnit = durationUnitService.create( [domainModel: durationUnit] )
        assertNotNull durationUnit

        String newDesc = "NewDesc"
        durationUnit.description = newDesc
        durationUnitService.update( durationUnit )

        DurationUnit durationUnitModified = DurationUnit.findWhere( code: i_success_code )
        assertNotNull durationUnitModified
        assertEquals durationUnitModified.description, newDesc
    }


    @Test
    void testDelete() {
        DurationUnit durationUnit = newValidForCreateDurationUnit()
        durationUnit = durationUnitService.create( [domainModel: durationUnit] )
        assertNotNull durationUnit

        DurationUnit deleteThis = DurationUnit.get( durationUnit.id )
        assertNotNull deleteThis
        def durationUnitDeleteId = deleteThis.id

        durationUnitService.delete( [domainModel: deleteThis] )
        assertNull DurationUnit.get( durationUnitDeleteId )
    }


    @Test
    void testFetchAllByDurationUnitCodes() {
        List<String> durationUnitCodes = ["DAY", "WEEK", "DAYS", "SEM", "HALF"]
        List<DurationUnit> durationUnits = durationUnitService.fetchAllByDurationUnitCodes( durationUnitCodes )
        assertEquals 5, durationUnits.size()

        assertNull durationUnits.find {it.code == 'MTHS'}

        assertNotNull durationUnits.find {it.code == durationUnitCodes[0]}
        assertNotNull durationUnits.find {it.code == durationUnitCodes[1]}
        assertNotNull durationUnits.find {it.code == durationUnitCodes[2]}
        assertNotNull durationUnits.find {it.code == durationUnitCodes[3]}
        assertNotNull durationUnits.find {it.code == durationUnitCodes[4]}
    }


    private def newValidForCreateDurationUnit() {
        def durationUnit = new DurationUnit(
                code: i_success_code,
                description: i_success_description,
                numberOfDays: i_success_numberOfDays,
                vrMsgNo: i_success_voiceResponseMessageNumber,
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return durationUnit
    }
}
