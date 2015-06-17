/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.system.ldm.v4.AcademicHonor
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * <p>This is the integration testcases for Academic Honor</p>
 * @author Sitakant
 */
class AcademicHonorCompositeServiceInitegrationTests extends BaseIntegrationTestCase {

    def academicHonorCompositeService

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
    public void testListAcademicHonors(){
       List list = academicHonorCompositeService.list(params)
       assertTrue list.size()>0

    }

    @Test
    public void testAwardHonors(){
        params.type='award'
        List list = academicHonorCompositeService.list(params)
        assertTrue list.size()>0
        AcademicHonor academicHonor = list.get(0)
        assertEquals academicHonor.type,'award'
    }

    @Test
    public void testDistinctionHonors(){
        params.type='distinction'
        List list = academicHonorCompositeService.list(params)
        assertTrue list.size()>0
        AcademicHonor academicHonor = list.get(0)
        assertEquals academicHonor.type,'distinction'
    }

    @Test
    public void testSortOrder(){
        params.order='DESC'
        params.sort='title'
        List list = academicHonorCompositeService.list(params)
        assertTrue list.size()>0
    }

    @Test
    public void testOffsetCriteria(){
        List list = academicHonorCompositeService.list(params)
        assertTrue list.size()>0

        params.offset=2
        List list2 = academicHonorCompositeService.list(params)
        assertTrue list.size()>0

        assertTrue list2.size()<list.size()
    }

    @Test
    public void testShow(){
        List list = academicHonorCompositeService.list(params)
        assertNotNull list
        assertTrue list.size()>0
        AcademicHonor academicHonor = list.get(0)
        assertNotNull academicHonor

        AcademicHonor academicHonor1 = academicHonorCompositeService.get(academicHonor.guid)
        assertNotNull academicHonor1
        assertEquals academicHonor.descriptions,academicHonor1.descriptions
        assertEquals academicHonor.guid,academicHonor1.guid
        assertEquals academicHonor.type,academicHonor1.type
    }


    @Test
    public void testInvalidShow(){
        List list = academicHonorCompositeService.list(params)
        assertNotNull list
        assertTrue list.size()>0
        AcademicHonor academicHonor = list.get(0)
        assertNotNull academicHonor

        try {
            academicHonorCompositeService.get(academicHonor.guid.substring(0,academicHonor.guid.length()-2))
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }

    @Test
    public void testDifferentLDMGuid(){
        String siteGuid = GlobalUniqueIdentifier.findByLdmNameAndDomainKey('campuses', 'M')?.guid
        assertNotNull siteGuid
        try {
            academicHonorCompositeService.get(siteGuid)
        } catch (ApplicationException ae) {
            assertApplicationException ae, "invalid.guid"
        }
    }
}
