/*******************************************************************************
 Copyright 2015-2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system

import groovy.sql.Sql
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Service Integration Test cases for PhoneType, which is Read Only view.
 */
class LocationTypeReadOnlyServiceIntegrationTests extends BaseIntegrationTestCase {

    def locationTypeReadOnlyService

  private String query = """select count(*) as cnt from goriccr g , STVATYP s  where GORICCR_SQPR_CODE = 'HEDM'  AND GORICCR_ICSN_CODE = 'ADDRESSES.ADDRESSTYPE' AND s.STVATYP_CODE = g.GORICCR_VALUE AND
    g.GORICCR_TRANSLATION_VALUE in ('home','school','vacation','billing','shipping','mailing','business','parent','family','pobox','main','branch','region','support','matchingGifts','other')"""


    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
    }


    @After
    public void tearDown() {
        super.tearDown()
    }

    @Test
    void testGet() {
        def params = [max: '1', offset: '0']
        List<LocationTypeReadOnly> addressTypeList= locationTypeReadOnlyService.fetchAll(params)
        assertFalse addressTypeList.isEmpty()

        addressTypeList.each{
            LocationTypeReadOnly lcoationType =  locationTypeReadOnlyService.fetchByGuid(it.id)
            assertNotNull lcoationType
            assertEquals it.locationType, lcoationType.locationType
            assertEquals it.code, lcoationType.code
            assertEquals it.id, lcoationType.id
            assertEquals it.value,lcoationType.value
            assertEquals it.description,lcoationType.description
        }
    }

    @Test
    void testList(){
        List<LocationTypeReadOnly> locationTypeList= locationTypeReadOnlyService.fetchAll([max:'500',offset: '0'])
        assertFalse locationTypeList.isEmpty()
        List actualTypes= net.hedtech.banner.general.system.LocationTypeReadOnly.list(max:'500')
        assertFalse actualTypes.isEmpty()
        assertTrue locationTypeList.code.containsAll(actualTypes.code)
        assertEquals locationTypeList.size() , actualTypes.size()

    }

    @Test
    void testCount(){
        def count = locationTypeReadOnlyService.fetchCountAll()
        assertNotNull count
        Sql sql = new Sql(sessionFactory.getCurrentSession().connection())
        def result = sql.firstRow(query)
        Long expectCount = result.cnt
        assertNotNull expectCount
        assertEquals expectCount,count
    }


}

