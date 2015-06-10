package net.hedtech.banner.general.system.ldm

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
}
