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
class PhoneTypeServiceIntegrationTests extends BaseIntegrationTestCase {

    def phoneTypeService

  private String query = """select count(*) as cnt from goriccr g , STVTELE s  where GORICCR_SQPR_CODE = 'HEDM'  AND GORICCR_ICSN_CODE = 'PHONES.PHONETYPE' AND s.STVTELE_CODE = g.GORICCR_VALUE AND
    g.GORICCR_TRANSLATION_VALUE in ('mobile','home','school','vacation','business','fax','pager','tdd','parent','family','main','branch','region','support','billing','matchingGifts','other')"""


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
        List<PhoneType> phoneTypeList= phoneTypeService.fetchAll(params)
        assertFalse phoneTypeList.isEmpty()

        phoneTypeList.each{
            PhoneType phoneType =  phoneTypeService.fetchByGuid(it.id)
            assertNotNull phoneType
            assertEquals it.phoneType, phoneType.phoneType
            assertEquals it.code, phoneType.code
            assertEquals it.id, phoneType.id
            assertEquals it.value,phoneType.value
            assertEquals it.description,phoneType.description
        }
    }

    @Test
    void testList(){
        List<PhoneType> phoneTypeList= phoneTypeService.fetchAll([max:'500',offset: '0'])
        assertFalse phoneTypeList.isEmpty()
        List actualTypes= net.hedtech.banner.general.system.PhoneType.list(max:'500')
        assertFalse actualTypes.isEmpty()
        assertTrue phoneTypeList.code.containsAll(actualTypes.code)
        assertEquals phoneTypeList.size() , actualTypes.size()

    }

    @Test
    void testCount(){
        def count = phoneTypeService.fetchCountAll()
        assertNotNull count
        Sql sql = new Sql(sessionFactory.getCurrentSession().connection())
        def result = sql.firstRow(query)
        Long expectCount = result.cnt
        assertNotNull expectCount
        assertEquals expectCount,count
    }


}

