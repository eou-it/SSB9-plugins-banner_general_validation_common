/*********************************************************************************
 Copyright 2017 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.utility

import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * <p>Integration test cases for Date convert helper service.</p>
 */
class DateConvertHelperServiceIntegrationTests extends BaseIntegrationTestCase {

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
    void testConvertDateIntoUTCFormat() {
        String expectedDate = DateConvertHelperService.convertDateIntoUTCFormat(new Date())
        assertNotNull expectedDate
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Date.parse(GeneralValidationCommonConstants.UTC_DATE_FORMAT_WITHOUT_TIMEZONE, expectedDate));
        assertEquals calendar.getTimeInMillis(), DateConvertHelperService.convertUTCStringToServerDate(expectedDate).getTime()
    }


    @Test
    void testConvertUTCStringToServerDate() {
        Date sampleDate = new Date()
        String expectedDate = DateConvertHelperService.convertDateIntoUTCFormat(sampleDate)
        assertNotNull expectedDate
        Date serverDate = DateConvertHelperService.convertUTCStringToServerDate(expectedDate)
        String actualDate = DateConvertHelperService.convertDateIntoUTCFormat(serverDate)
        assertEquals expectedDate, actualDate
    }


    @Test
    void testConvertUTCStringToServerDateWithTime() {
        Date sampleDate = new Date()
        String expectedDate = DateConvertHelperService.convertDateIntoUTCFormat(sampleDate, '103925')
        assertNotNull expectedDate
        Date serverDate = DateConvertHelperService.convertUTCStringToServerDate(expectedDate)
        String actualDate = DateConvertHelperService.convertDateIntoUTCFormat(serverDate, '103925')
        assertEquals expectedDate, actualDate
    }


    @Test
    void testConvertString2Date() {
        Date date = DateConvertHelperService.convertString2Date("1970-01-01T00:00:00+00:00", GeneralValidationCommonConstants.PATTERN_DATE_TIME_ISO8601TIMEZONE)
        assertNotNull date
        date = DateConvertHelperService.convertString2Date("1969-12-31T23:59:00+00:00", GeneralValidationCommonConstants.PATTERN_DATE_TIME_ISO8601TIMEZONE)
        assertNull date
        date = DateConvertHelperService.convertString2Date("1969-12-31T23:59:00+00:00", GeneralValidationCommonConstants.PATTERN_DATE_TIME_ISO8601TIMEZONE, true)
        assertNotNull date
        date = DateConvertHelperService.convertString2Date("1970-01-15", GeneralValidationCommonConstants.DATE_WITHOUT_TIMEZONE)
        assertNotNull date
        date = DateConvertHelperService.convertString2Date("1969-12-15", GeneralValidationCommonConstants.DATE_WITHOUT_TIMEZONE)
        assertNull date
        date = DateConvertHelperService.convertString2Date("1969-12-15", GeneralValidationCommonConstants.DATE_WITHOUT_TIMEZONE, true)
        assertNotNull date
    }

}
