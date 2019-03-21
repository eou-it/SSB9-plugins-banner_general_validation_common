/** *****************************************************************************
 Copyright 2016-2016 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.Before
import org.junit.Test


class AddressTypeServiceIntegrationTests extends BaseIntegrationTestCase {

    AddressTypeService addressTypeService
    GlobalUniqueIdentifier i_success_globalUniqueIdentifier

    AddressType i_success_address_type
    Collection<String> i_success_address_type_codes = ['FI', 'BI', 'BU']


    @Before
    public void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
        initializeTestDataForReferences()
    }

    void initializeTestDataForReferences() {
        i_success_address_type = AddressType.findByCode("FI")
        i_success_globalUniqueIdentifier = GlobalUniqueIdentifier.fetchByDomainKeyAndLdmName('FI', 'address-types')
    }


    @Test
    void testFetchByCode() {
        AddressType o_success_address_type = addressTypeService.fetchByCode(i_success_address_type.code)
        assertNotNull o_success_address_type
        assertEquals i_success_address_type.code, o_success_address_type.code
        assertEquals i_success_address_type.description, o_success_address_type.description
        assertEquals i_success_address_type.telephoneType.code, o_success_address_type.telephoneType.code
        assertEquals i_success_address_type.systemRequiredIndicator, o_success_address_type.systemRequiredIndicator
    }


    @Test
    void testFetchAllWithGuidByCodeInList() {
        List o_success_entities = addressTypeService.fetchAllWithGuidByCodeInList(i_success_address_type_codes, 10, 0)
        assertNotNull o_success_entities
        assertTrue o_success_entities.size() > 0
        AddressType o_success_address_type = o_success_entities.find {
            it.addressType.code == i_success_address_type.code
        }.addressType
        assertNotNull o_success_address_type
        assertEquals i_success_address_type.code, o_success_address_type.code
        assertEquals i_success_address_type.description, o_success_address_type.description
        assertEquals i_success_address_type.telephoneType.code, o_success_address_type.telephoneType.code
        assertEquals i_success_address_type.systemRequiredIndicator, o_success_address_type.systemRequiredIndicator
        GlobalUniqueIdentifier o_success_globalUniqueIdentifier = o_success_entities.find {
            it.addressType.code == i_success_address_type.code
        }.globalUniqueIdentifier
        assertNotNull o_success_globalUniqueIdentifier
        assertEquals i_success_globalUniqueIdentifier.guid, o_success_globalUniqueIdentifier.guid
    }

    @Test
    void testFetchAllByCodeInList() {
        List<AddressType> o_success_address_types = addressTypeService.fetchAllByCodeInList(i_success_address_type_codes)
        assertNotNull o_success_address_types
        assertTrue o_success_address_types.size() > 0
        AddressType o_success_address_type = o_success_address_types.find { it.code == i_success_address_type.code }
        assertNotNull o_success_address_type
        assertEquals i_success_address_type.code, o_success_address_type.code
        assertEquals i_success_address_type.description, o_success_address_type.description
        assertEquals i_success_address_type.telephoneType.code, o_success_address_type.telephoneType.code
        assertEquals i_success_address_type.systemRequiredIndicator, o_success_address_type.systemRequiredIndicator
    }

    @Test
    void testFetchAllWithGuid() {
        List o_success_entities = addressTypeService.fetchAllWithGuid(10, 0)
        assertNotNull o_success_entities
        assertTrue o_success_entities.size() > 0
        AddressType o_success_address_type = o_success_entities[0][0]
        assertNotNull o_success_address_type
        assertNotNull o_success_address_type.code
        GlobalUniqueIdentifier o_success_globalUniqueIdentifier = o_success_entities[0][1]
        assertNotNull o_success_globalUniqueIdentifier
        assertNotNull o_success_globalUniqueIdentifier.guid
    }

}
