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
 * Service Integration Test cases for email type, which is Read Only view.
 */
class EmailTypeReadOnlyServiceIntegrationTests extends BaseIntegrationTestCase {

    def emailTypeReadOnlyService

  private String query = """select count(*) as cnt from goriccr g , GTVEMAL s  where GORICCR_SQPR_CODE = 'HEDM'  AND GORICCR_ICSN_CODE = 'EMAILS.EMAILTYPE' AND s.GTVEMAL_CODE = g.GORICCR_VALUE AND
    g.GORICCR_TRANSLATION_VALUE in ('personal','business','school','parent','family','sales','support','general','billing','legal','hr','media','matchingGifts','other')"""


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
        List<EmailTypeReadOnly> emailTypeList= emailTypeReadOnlyService.fetchAll(params)
        assertFalse emailTypeList.isEmpty()

        emailTypeList.each{
            EmailTypeReadOnly emailType =  emailTypeReadOnlyService.fetchByGuid(it.id)
            assertNotNull emailType
            assertEquals it.emailType, emailType.emailType
            assertEquals it.code, emailType.code
            assertEquals it.id, emailType.id
            assertEquals it.value,emailType.value
            assertEquals it.description,emailType.description
        }
    }

    @Test
    void testList(){
        List<EmailTypeReadOnly> emailTypeList= emailTypeReadOnlyService.fetchAll([max:'500',offset: '0'])
        assertFalse emailTypeList.isEmpty()
        List actualTypes= net.hedtech.banner.general.system.EmailTypeReadOnly.list(max:'500')
        assertFalse actualTypes.isEmpty()
        assertTrue emailTypeList.code.containsAll(actualTypes.code)
        assertEquals emailTypeList.size() , actualTypes.size()

    }

    @Test
    void testCount(){
        def count = emailTypeReadOnlyService.fetchCountAll()
        assertNotNull count
        Sql sql = new Sql(sessionFactory.getCurrentSession().connection())
        def result = sql.firstRow(query)
        Long expectCount = result.cnt
        assertNotNull expectCount
        assertEquals expectCount,count
    }


}

