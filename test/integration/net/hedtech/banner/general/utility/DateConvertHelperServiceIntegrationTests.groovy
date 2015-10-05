/*********************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.utility

import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * <p>Integration test cases for Date convert helper service.</p>
 */
class DateConvertHelperServiceIntegrationTests extends BaseIntegrationTestCase{

 def dateConvertHelperService

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
        def date = new Date()
        assertNotNull date

        def expectedDate = dateConvertHelperService.convertDateIntoUTCFormat(date)
        assertNotNull expectedDate

        def dbTimeZone = dateConvertHelperService.getDBTimeZone()
        assertNotNull dbTimeZone

        def actualDate = date?.format("yyyy-MM-dd'T'HH:mm:ss") + dbTimeZone[0][0]
        assertEquals actualDate , expectedDate

        expectedDate = dateConvertHelperService.convertDateIntoUTCFormat(date,dbTimeZone)
        assertNotNull expectedDate
        assertEquals actualDate , expectedDate
    }

    @Test
    void testConvertDateIntoUTCFormatWithTime(){
        def date = new Date()
        assertNotNull date
        def time = '1300'
        def dateTime = time ? time?.substring( 0, 2 ) + ':' + time?.substring( 2, 4 ) + ':' + '00' : null

        def expectedDate = dateConvertHelperService.convertDateIntoUTCFormat(date, null)
        assertNotNull expectedDate

        def dbTimeZone = dateConvertHelperService.getDBTimeZone()
        assertNotNull dbTimeZone

        def actualDate = date?.format("yyyy-MM-dd'T'HH:mm:ss") + dbTimeZone[0][0]
        assertEquals actualDate , expectedDate

        expectedDate = dateConvertHelperService.convertDateIntoUTCFormat(date, time)
        assertNotNull expectedDate

        actualDate = date?.format("yyyy-MM-dd'T'")+dateTime+ dbTimeZone[0][0]
        assertEquals actualDate , expectedDate

        expectedDate = dateConvertHelperService.convertDateIntoUTCFormat(date, time, dbTimeZone)
        assertEquals actualDate , expectedDate

    }

}
