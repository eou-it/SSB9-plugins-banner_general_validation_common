/** *****************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import static groovy.test.GroovyAssert.*
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback

@Integration
@Rollback
/**
 * Integration test cases for Name type service.
 */
class NameTypeServiceIntegrationTests extends BaseIntegrationTestCase {

    def nameTypeService


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
    void testFetchAllWithGuidByCodeInList() {
        NameType nameType = NameType.findByCode("BRTH")
        assertNotNull nameType
        List nameTypeWithGuidList=  nameTypeService.fetchAllWithGuidByCodeInList([nameType.code],1,0)
        assertFalse nameTypeWithGuidList.isEmpty()
        assertEquals 1, nameTypeWithGuidList.size()
        nameTypeWithGuidList.each {
            NameType actualNameType = it.getAt(0)
            assertNotNull actualNameType
            def guid = GlobalUniqueIdentifier.fetchByLdmNameAndDomainId(GeneralValidationCommonConstants.PERSON_NAME_TYPES_LDM_NAME, actualNameType.id)?.guid
            assertNotNull guid
            GlobalUniqueIdentifier globalUniqueIdentifier = it.getAt(1)
            assertNotNull globalUniqueIdentifier
            assertEquals guid, globalUniqueIdentifier.guid
            assertEquals actualNameType, nameType
        }
    }


    @Test
    void testFetchAllByCodeInList() {
        NameType nameType = NameType.findByCode("BRTH")
        assertNotNull nameType
        List<NameType> nameTypeList =  nameTypeService.fetchAllByCodeInList([nameType.code])
        assertFalse nameTypeList.isEmpty()
        assertEquals 1, nameTypeList.size()
        nameTypeList.each {
            assertEquals it, nameType
        }
    }

    @Test
    void testFetchAllWithGuid(){
        List nameTypeWithGuidList=  nameTypeService.fetchAllWithGuid(1,0)
        assertFalse nameTypeWithGuidList.isEmpty()
        assertEquals 1, nameTypeWithGuidList.size()
        nameTypeWithGuidList.each {
            NameType actualNameType = it.getAt(0)
            assertNotNull actualNameType
            def guid = GlobalUniqueIdentifier.fetchByLdmNameAndDomainId(GeneralValidationCommonConstants.PERSON_NAME_TYPES_LDM_NAME, actualNameType.id)?.guid
            assertNotNull guid
            GlobalUniqueIdentifier globalUniqueIdentifier = it.getAt(1)
            assertNotNull globalUniqueIdentifier
            assertEquals guid, globalUniqueIdentifier.guid
        }
    }

    private def newNameType() {
        def nameType = new NameType(
                code: "TTTT",
                description: "TTTTT",
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return nameType
    }

}
