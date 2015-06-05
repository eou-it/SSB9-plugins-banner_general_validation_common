package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.general.system.MajorMinorConcentration
import net.hedtech.banner.general.system.ldm.v4.AcademicDisciplineType
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Created by invthannee on 6/4/2015.
 */
class AcademicDisciplineCompositeServiceIntegrationTests extends BaseIntegrationTestCase {

    def academicDisciplineCompositeService
    def i_fail_academicDiscipline
    def i_sucess_academicDiscipline_major
    def i_sucess_academicDiscipline_minor
    def i_sucess_academicDiscipline_concentration
    def i_sucess_academicDiscipline_guid


    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
        initiializeDataReferences()
    }

    @After
    public void tearDown() {
        super.tearDown()
    }


    private void initiializeDataReferences() {
        i_fail_academicDiscipline = MajorMinorConcentration.findByCode('AUSO')
        i_sucess_academicDiscipline_minor = MajorMinorConcentration.findAllByValidMinorIndicator(Boolean.TRUE)
        i_sucess_academicDiscipline_major = MajorMinorConcentration.findAllByValidMajorIndicator(Boolean.TRUE)
        i_sucess_academicDiscipline_concentration = MajorMinorConcentration.findAllByValidConcentratnIndicator(Boolean.TRUE)
        i_sucess_academicDiscipline_guid = GlobalUniqueIdentifier.findAllByLdmName('academic-disciplines')
    }

    @Test
    void testListWithoutPaginationParams() {
        List academicDisciplines = academicDisciplineCompositeService.list([:])
        assertNotNull academicDisciplines
        assertFalse academicDisciplines.isEmpty()
        assertFalse academicDisciplines.code.contains(i_fail_academicDiscipline.code)
        assertTrue academicDisciplines.code.contains(i_sucess_academicDiscipline_minor.find { it.code == '101' }.code)
        assertTrue academicDisciplines.code.contains(i_sucess_academicDiscipline_major.find { it.code == '101' }.code)
        assertTrue academicDisciplines.code.contains(i_sucess_academicDiscipline_concentration.find {
            it.code == '101'
        }.code)
        assertTrue academicDisciplines.guid.contains(i_sucess_academicDiscipline_guid.find {
            it.domainKey == '101'
        }.guid)
    }

    @Test
    void testListWithPagination() {
        def paginationParams = [max: '20', offset: '0']
        List academicDisciplines = academicDisciplineCompositeService.list(paginationParams)
        assertNotNull academicDisciplines
        assertFalse academicDisciplines.isEmpty()
        assertFalse academicDisciplines.code.contains(i_fail_academicDiscipline.code)
        assertTrue academicDisciplines.code.contains(i_sucess_academicDiscipline_minor.find { it.code == '101' }.code)
        assertTrue academicDisciplines.code.contains(i_sucess_academicDiscipline_major.find { it.code == '101' }.code)
        assertTrue academicDisciplines.code.contains(i_sucess_academicDiscipline_concentration.find {
            it.code == '101'
        }.code)
        assertTrue academicDisciplines.guid.contains(i_sucess_academicDiscipline_guid.find {
            it.domainKey == '101'
        }.guid)
        assertFalse academicDisciplines.code.contains(i_sucess_academicDiscipline_minor.find { it.code == '107' }.code)
        assertFalse academicDisciplines.code.contains(i_sucess_academicDiscipline_major.find { it.code == '107' }.code)
        assertFalse academicDisciplines.code.contains(i_sucess_academicDiscipline_concentration.find {
            it.code == '107'
        }.code)
        assertFalse academicDisciplines.guid.contains(i_sucess_academicDiscipline_guid.find {
            it.domainKey == '107'
        }.guid)
    }

    @Test
    void testCount() {
        assertEquals i_sucess_academicDiscipline_minor.size +
                i_sucess_academicDiscipline_major.size +
                i_sucess_academicDiscipline_concentration.size, academicDisciplineCompositeService.count([:])
    }

    @Test
    void testCountByType() {
        assertEquals i_sucess_academicDiscipline_minor.size, academicDisciplineCompositeService.count([type: AcademicDisciplineType.MINOR.value])
        assertEquals i_sucess_academicDiscipline_major.size, academicDisciplineCompositeService.count([type: AcademicDisciplineType.MAJOR.value])
        assertEquals i_sucess_academicDiscipline_concentration.size, academicDisciplineCompositeService.count([type: AcademicDisciplineType.CONCENTRATION.value])
    }

    @Test
    void testListWithoutPaginationParamsByMinorType() {
        List academicDisciplines = academicDisciplineCompositeService.list([type: AcademicDisciplineType.MINOR.value])
        assertNotNull academicDisciplines
        assertFalse academicDisciplines.isEmpty()
        assertFalse academicDisciplines.code.contains(i_fail_academicDiscipline.code)
        assertTrue academicDisciplines.code.contains(i_sucess_academicDiscipline_minor.find { it.code == '101' }?.code)
        assertTrue academicDisciplines.code.contains(i_sucess_academicDiscipline_major.find { it.code == '101' }?.code)
        assertTrue academicDisciplines.code.contains(i_sucess_academicDiscipline_concentration.find {
            it.code == '101'
        }?.code)
        assertTrue academicDisciplines.guid.contains(i_sucess_academicDiscipline_guid.find {
            it.domainKey == '101'
        }?.guid)
        assertFalse academicDisciplines.code.contains(i_sucess_academicDiscipline_major.find {
            it.code == 'BADN'
        }?.code)
        assertFalse academicDisciplines.code.contains(i_sucess_academicDiscipline_concentration.find {
            it.code == 'BADN'
        }?.code)
        assertFalse academicDisciplines.guid.contains(i_sucess_academicDiscipline_guid.find {
            it.domainKey == 'BADN'
        }?.guid)
    }

    @Test
    void testListWithoutPaginationParamsByMajorType() {
        List academicDisciplines = academicDisciplineCompositeService.list([type: AcademicDisciplineType.MAJOR.value])
        assertNotNull academicDisciplines
        assertFalse academicDisciplines.isEmpty()
        assertFalse academicDisciplines.code.contains(i_fail_academicDiscipline.code)
        assertTrue academicDisciplines.code.contains(i_sucess_academicDiscipline_minor.find { it.code == '101' }?.code)
        assertTrue academicDisciplines.code.contains(i_sucess_academicDiscipline_major.find { it.code == '101' }?.code)
        assertTrue academicDisciplines.code.contains(i_sucess_academicDiscipline_concentration.find {
            it.code == '101'
        }?.code)
        assertTrue academicDisciplines.guid.contains(i_sucess_academicDiscipline_guid.find {
            it.domainKey == '101'
        }?.guid)
        assertFalse academicDisciplines.code.contains(i_sucess_academicDiscipline_minor.find {
            it.code == 'ECOM'
        }?.code)
        assertFalse academicDisciplines.code.contains(i_sucess_academicDiscipline_concentration.find {
            it.code == 'ECOM'
        }?.code)
        assertFalse academicDisciplines.guid.contains(i_sucess_academicDiscipline_guid.find {
            it.domainKey == 'ECOM'
        }?.guid)
    }

    @Test
    void testListWithoutPaginationParamsByConcentrationType() {
        List academicDisciplines = academicDisciplineCompositeService.list([type: AcademicDisciplineType.CONCENTRATION.value])
        assertNotNull academicDisciplines
        assertFalse academicDisciplines.isEmpty()
        assertFalse academicDisciplines.code.contains(i_fail_academicDiscipline.code)
        assertTrue academicDisciplines.code.contains(i_sucess_academicDiscipline_minor.find { it.code == '101' }?.code)
        assertTrue academicDisciplines.code.contains(i_sucess_academicDiscipline_major.find { it.code == '101' }?.code)
        assertTrue academicDisciplines.code.contains(i_sucess_academicDiscipline_concentration.find {
            it.code == '101'
        }?.code)
        assertTrue academicDisciplines.guid.contains(i_sucess_academicDiscipline_guid.find {
            it.domainKey == '101'
        }?.guid)
        assertFalse academicDisciplines.code.contains(i_sucess_academicDiscipline_major.find {
            it.code == 'ELIT'
        }?.code)
        assertFalse academicDisciplines.code.contains(i_sucess_academicDiscipline_minor.find {
            it.code == 'ELIT'
        }?.code)
        assertFalse academicDisciplines.guid.contains(i_sucess_academicDiscipline_guid.find {
            it.domainKey == 'ELIT'
        }?.guid)
    }

    @Test
    void testListWithInvalidType() {
        List academicDisciplines = academicDisciplineCompositeService.list([type: "MAJORS"])
        assertNotNull academicDisciplines
        assertTrue academicDisciplines.isEmpty()
    }

    @Test
    void testListWithPaginationByMinorType() {
        def params = [max: '20', offset: '0', type: AcademicDisciplineType.MINOR.value]
        List academicDisciplines = academicDisciplineCompositeService.list(params)
        assertNotNull academicDisciplines
        assertFalse academicDisciplines.isEmpty()
        assertFalse academicDisciplines.code.contains(i_fail_academicDiscipline.code)
        assertTrue academicDisciplines.code.contains(i_sucess_academicDiscipline_minor.find { it.code == '101' }?.code)
        assertTrue academicDisciplines.code.contains(i_sucess_academicDiscipline_major.find { it.code == '101' }?.code)
        assertTrue academicDisciplines.code.contains(i_sucess_academicDiscipline_concentration.find {
            it.code == '101'
        }?.code)
        assertTrue academicDisciplines.guid.contains(i_sucess_academicDiscipline_guid.find {
            it.domainKey == '101'
        }?.guid)
        assertFalse academicDisciplines.code.contains(i_sucess_academicDiscipline_major.find {
            it.code == 'BADN'
        }?.code)
        assertFalse academicDisciplines.code.contains(i_sucess_academicDiscipline_concentration.find {
            it.code == 'BADN'
        }?.code)
        assertFalse academicDisciplines.guid.contains(i_sucess_academicDiscipline_guid.find {
            it.domainKey == 'BADN'
        }?.guid)
        assertFalse academicDisciplines.guid.contains(i_sucess_academicDiscipline_minor.find { it.code == '208' }?.code)
    }

    @Test
    void testListWithPaginationByConcentrationType() {
        def params = [max: '20', offset: '0', type: AcademicDisciplineType.CONCENTRATION.value]
        List academicDisciplines = academicDisciplineCompositeService.list(params)
        assertNotNull academicDisciplines
        assertFalse academicDisciplines.isEmpty()
        assertFalse academicDisciplines.code.contains(i_fail_academicDiscipline.code)
        assertTrue academicDisciplines.code.contains(i_sucess_academicDiscipline_minor.find { it.code == '101' }?.code)
        assertTrue academicDisciplines.code.contains(i_sucess_academicDiscipline_major.find { it.code == '101' }?.code)
        assertTrue academicDisciplines.code.contains(i_sucess_academicDiscipline_concentration.find {
            it.code == '101'
        }?.code)
        assertTrue academicDisciplines.guid.contains(i_sucess_academicDiscipline_guid.find {
            it.domainKey == '101'
        }?.guid)
        assertFalse academicDisciplines.code.contains(i_sucess_academicDiscipline_minor.find {
            it.code == 'ECOM'
        }?.code)
        assertFalse academicDisciplines.code.contains(i_sucess_academicDiscipline_concentration.find {
            it.code == 'ECOM'
        }?.code)
        assertFalse academicDisciplines.guid.contains(i_sucess_academicDiscipline_guid.find {
            it.domainKey == 'ECOM'
        }?.guid)
        assertFalse academicDisciplines.guid.contains(i_sucess_academicDiscipline_concentration.find {
            it.code == '209'
        }?.code)
    }

    @Test
    void testListWithPaginationByMajorType() {
        def params = [max: '20', offset: '0', type: AcademicDisciplineType.MAJOR.value]
        List academicDisciplines = academicDisciplineCompositeService.list(params)
        assertNotNull academicDisciplines
        assertFalse academicDisciplines.isEmpty()
        assertFalse academicDisciplines.code.contains(i_fail_academicDiscipline.code)
        assertTrue academicDisciplines.code.contains(i_sucess_academicDiscipline_minor.find { it.code == '101' }?.code)
        assertTrue academicDisciplines.code.contains(i_sucess_academicDiscipline_major.find { it.code == '101' }?.code)
        assertTrue academicDisciplines.code.contains(i_sucess_academicDiscipline_concentration.find {
            it.code == '101'
        }?.code)
        assertTrue academicDisciplines.guid.contains(i_sucess_academicDiscipline_guid.find {
            it.domainKey == '101'
        }?.guid)
        assertFalse academicDisciplines.code.contains(i_sucess_academicDiscipline_major.find {
            it.code == 'ELIT'
        }?.code)
        assertFalse academicDisciplines.code.contains(i_sucess_academicDiscipline_minor.find {
            it.code == 'ELIT'
        }?.code)
        assertFalse academicDisciplines.guid.contains(i_sucess_academicDiscipline_guid.find {
            it.domainKey == 'ELIT'
        }?.guid)
        assertFalse academicDisciplines.guid.contains(i_sucess_academicDiscipline_major.find { it.code == '208' }?.code)
    }

    @Test
    void testGet() {
        def paginationParams = [max: '1', offset: '0']
        List academicDisciplines = academicDisciplineCompositeService.list(paginationParams)
        assertNotNull academicDisciplines
        assertFalse academicDisciplines.isEmpty()
        assertNotNull academicDisciplines[0].guid
        def academicDiscipline = academicDisciplineCompositeService.get(academicDisciplines[0].guid)
        assertNotNull academicDiscipline
        assertEquals academicDisciplines[0].guid, academicDiscipline[0].guid
        assertEquals academicDisciplines[0].code, academicDiscipline[0].code
    }

    @Test
    void testGetWithInvalidGuid() {
        def paginationParams = [max: '1', offset: '0']
        List academicDisciplines = academicDisciplineCompositeService.list(paginationParams)
        assertNotNull academicDisciplines
        assertFalse academicDisciplines.isEmpty()
        assertNotNull academicDisciplines[0].guid
        try {
            def academicDiscipline = academicDisciplineCompositeService.get(academicDisciplines[0].guid + '2')
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }

    @Test
    void testGetWithNullGuid() {
        try {
            def academicDiscipline = academicDisciplineCompositeService.get(null)
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }

    @Test
    void testGetWithEmptyGuid() {
        try {
            def academicDiscipline = academicDisciplineCompositeService.get("")
        } catch (ApplicationException ae) {
            assertApplicationException ae, "NotFoundException"
        }
    }
}
