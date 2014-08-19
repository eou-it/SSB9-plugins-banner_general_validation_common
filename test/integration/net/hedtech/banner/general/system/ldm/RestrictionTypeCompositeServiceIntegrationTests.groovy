/** *******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm


import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.system.HoldType
import net.hedtech.banner.general.system.ldm.v1.RestrictionType
import net.hedtech.banner.testing.BaseIntegrationTestCase


class RestrictionTypeCompositeServiceIntegrationTests extends BaseIntegrationTestCase{

    HoldType i_success_holdType
    def restrictionTypeCompositeService


    void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
        initiializeDataReferences()
    }


    private void initiializeDataReferences() {
        newHoldType()
        i_success_holdType = HoldType.findByCode('TT')
    }


    void testListWithoutPaginationParams() {
        List restrictionTypes = restrictionTypeCompositeService.list([:])
        assertNotNull restrictionTypes
        assertFalse restrictionTypes.isEmpty()
        assertTrue restrictionTypes.size() > 0
    }


    void testList() {
        def paginationParams = [max: '2', offset: '0']
        List restrictionTypes = restrictionTypeCompositeService.list(paginationParams)
        assertNotNull restrictionTypes
        assertFalse restrictionTypes.isEmpty()
        assertTrue restrictionTypes.size() == 2
    }


    void testCount() {
        assertNotNull i_success_holdType
        assertTrue restrictionTypeCompositeService.count() > 0
    }


    void testGetInvalidGuid() {
        try {
            restrictionTypeCompositeService.get('Invalid-guid')
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }


    void testGetNullGuid() {
        try {
            restrictionTypeCompositeService.get(null)
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }


    void testGet() {
        def paginationParams = [max: '1', offset: '0']
        def restrictionTypes = restrictionTypeCompositeService.list(paginationParams)
        assertNotNull restrictionTypes
        assertFalse restrictionTypes.isEmpty()

        assertNotNull restrictionTypes[0].guid
        def restrictionType = restrictionTypeCompositeService.get(restrictionTypes[0].guid)
        assertNotNull restrictionType
        assertNotNull restrictionType.code
        assertEquals restrictionTypes[0].code, restrictionType.code
        assertNotNull restrictionType.guid
        assertEquals restrictionTypes[0].guid, restrictionType.guid
        assertNotNull restrictionType.guid
        assertEquals restrictionTypes[0].description, restrictionType.description
        assertNotNull restrictionType.metadata
        assertEquals restrictionTypes[0].metadata.dataOrigin, restrictionType.metadata.dataOrigin
        assertEquals restrictionTypes[0], restrictionType
    }


    void testFetchByHoldTypeIdInvalid() {
        try {
            restrictionTypeCompositeService.fetchByHoldTypeId(null)
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }


    void testFetchByHoldTypeId() {
        RestrictionType restrictionType = restrictionTypeCompositeService.fetchByHoldTypeId(i_success_holdType.id)
        assertNotNull restrictionType
        assertEquals i_success_holdType.id, restrictionType.id
        assertEquals i_success_holdType.code, restrictionType.code
        assertEquals i_success_holdType.description, restrictionType.description
        assertEquals i_success_holdType.dataOrigin, restrictionType.metadata.dataOrigin
    }


    void testFetchByHoldTypeInvalid() {
        assertNull restrictionTypeCompositeService.fetchByHoldTypeCode(null)
        assertNull restrictionTypeCompositeService.fetchByHoldTypeCode('Q')
    }


    void testFetchByHoldTypeCode() {
        RestrictionType restrictionType = restrictionTypeCompositeService.fetchByHoldTypeCode(i_success_holdType.code)
        assertNotNull restrictionType
        assertEquals i_success_holdType.id, restrictionType.id
        assertEquals i_success_holdType.code, restrictionType.code
        assertEquals i_success_holdType.description, restrictionType.description
        assertEquals i_success_holdType.dataOrigin, restrictionType.metadata.dataOrigin
    }


    private def newHoldType() {

        def holdType = new HoldType(
                code: "TT",
                registrationHoldIndicator: true,
                transcriptHoldIndicator: true,
                graduationHoldIndicator: true,
                gradeHoldIndicator: true,
                description: "TTTTT",
                accountsReceivableHoldIndicator: true,
                enrollmentVerificationHoldIndicator: true,
                voiceResponseMessageNumber: 1,
                displayWebIndicator: true,
                applicationHoldIndicator: true,
                complianceHoldIndicator: true,
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        holdType.save(failOnError: true, flush: true)
    }

}
