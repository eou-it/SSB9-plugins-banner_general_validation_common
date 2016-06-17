/*********************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.utility

import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import net.hedtech.banner.general.utility.DateConvertHelperService

/**
 * <p>Integration test cases for Date convert helper service.</p>
 */
class DateConvertHelperServiceIntegrationTests extends BaseIntegrationTestCase{

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
    void testConvertDateIntoUTCFormat(){
        String expectedDate = DateConvertHelperService.convertDateIntoUTCFormat(new Date())
        assertNotNull expectedDate
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"))
        calendar.setTime(Date.parse(GeneralValidationCommonConstants.UTC_DATE_FORMAT, expectedDate));
        assertEquals TimeZone.getTimeZone("UTC").getID(), calendar.getTimeZone().getID()
        assertEquals calendar.getTimeInMillis(), DateConvertHelperService.convertUTCStringToServerDate(expectedDate).getTime()
    }

    @Test
    void testConvertUTCStringToServerDate(){
        Date sampleDate = new Date()
        String expectedDate = DateConvertHelperService.convertDateIntoUTCFormat(sampleDate)
        assertNotNull expectedDate
        Date serverDate = DateConvertHelperService.convertUTCStringToServerDate(expectedDate)
        String actualDate = DateConvertHelperService.convertDateIntoUTCFormat(serverDate)
        assertEquals expectedDate,actualDate
    }

    @Test
    void testConvertUTCStringToServerDateWithTime(){
        Date sampleDate = new Date()
        String expectedDate = DateConvertHelperService.convertDateIntoUTCFormat(sampleDate,'103925')
        assertNotNull expectedDate
        Date serverDate = DateConvertHelperService.convertUTCStringToServerDate(expectedDate)
        String actualDate = DateConvertHelperService.convertDateIntoUTCFormat(serverDate,'103925')
        assertEquals expectedDate,actualDate
    }
}
