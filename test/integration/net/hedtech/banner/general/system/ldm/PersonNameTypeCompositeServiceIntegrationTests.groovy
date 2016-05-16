/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm

import groovy.sql.Sql
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.system.NameType
import net.hedtech.banner.general.system.ldm.v6.NameTypeCategory
import net.hedtech.banner.general.system.ldm.v6.NameTypeDecorator
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * <p>Integration test cases for person name type composite service.</p>
 */
class PersonNameTypeCompositeServiceIntegrationTests extends BaseIntegrationTestCase {

    def personNameTypeCompositeService

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
    void testListWithOutPagination() {
        List<NameTypeDecorator> personNameTypeList = personNameTypeCompositeService.list(params)
        assertFalse personNameTypeList.isEmpty()
        List<NameType> list = NameType.list(params)
        assertFalse list.isEmpty()
        assertEquals list.size(), personNameTypeList.size()
        assertTrue list.code.containsAll(personNameTypeList.code)
        assertTrue list.description.containsAll(personNameTypeList.title)
        assertTrue NameTypeCategory.values().value.containsAll(personNameTypeList.category)
        assertTrue GlobalUniqueIdentifier.fetchByLdmName(GeneralValidationCommonConstants.PERSON_NAME_TYPES_LDM_NAME).guid.containsAll(personNameTypeList.id)
    }

    @Test
    void testListWithPagination() {
        params.max = '4'
        params.offset = '2'
        List<NameTypeDecorator> personNameTypeList = personNameTypeCompositeService.list(params)
        assertFalse personNameTypeList.isEmpty()
        List<NameType> list = NameType.list(params)
        assertFalse list.isEmpty()
        assertEquals list.size(), personNameTypeList.size()
        assertTrue list.code.containsAll(personNameTypeList.code)
        assertTrue list.description.containsAll(personNameTypeList.title)
        assertTrue NameTypeCategory.values().value.containsAll(personNameTypeList.category)
        assertTrue GlobalUniqueIdentifier.fetchByLdmName(GeneralValidationCommonConstants.PERSON_NAME_TYPES_LDM_NAME).guid.containsAll(personNameTypeList.id)
    }

    @Test
    void testCount() {
        def expectedCount
        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            expectedCount = sql.firstRow("SELECT count(*) as gatCount from GTVNTYP").gatCount.toInteger()
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        def actualCount = personNameTypeCompositeService.count().toInteger()
        assertEquals expectedCount, actualCount
    }

    @Test
    void testGetValidGuid() {
        params.max = '1'
        params.offset = '0'
        List<NameTypeDecorator> personNameTypeList = personNameTypeCompositeService.list(params)
        assertFalse personNameTypeList.isEmpty()
        NameTypeDecorator personNameType = personNameTypeList.getAt(0)
        assertNotNull personNameType
        NameTypeDecorator nameTypeDecorator = personNameTypeCompositeService.get(personNameType.id)
        assertNotNull nameTypeDecorator
        assertEquals personNameType.id, nameTypeDecorator.id
        assertEquals personNameType.code, nameTypeDecorator.code
        assertEquals personNameType.title, nameTypeDecorator.title
        assertEquals personNameType.category, nameTypeDecorator.category

    }


    @Test
    void testGetInValidGuid() {
        try {
            personNameTypeCompositeService.get('invalid_guid')
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }

}