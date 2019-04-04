/** *****************************************************************************
 Copyright 2009-2016 Ellucian Company L.P. and its affiliates.
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
 * Integration test for the Department model.
 * */

class DepartmentServiceTests extends BaseIntegrationTestCase {

    def departmentService


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
    void testCreateDepartment() {
        def department = new Department(code: "TT", description: "TT", systemRequiredIndicator: "N", voiceResponseMessageNumber: 1,
                lastModified: new Date(), lastModifiedBy: "test", dataOrigin: "Banner")
        department = departmentService.create(department)
        assertNotNull "Department ID is null in Department Create Service Test", department.id
        assertNotNull "Department Code is null in Department Create Service Test", department.code
    }

    @Test
    void testUpdateDepartment() {
        def department = new Department(code: "TT", description: "TT", systemRequiredIndicator: "N", voiceResponseMessageNumber: 1,
                lastModified: new Date(), lastModifiedBy: "test", dataOrigin: "Banner")
        department = departmentService.create(department)

        Department departmentUpdate = Department.findWhere(code: "TT")
        assertNotNull "Department ID is null in Department Update Service Test", departmentUpdate.id

        departmentUpdate.description = "ZZ"
        departmentUpdate = departmentService.update(departmentUpdate)
        assertEquals "ZZ", departmentUpdate.description
    }

    @Test
    void testDeleteDepartment() {
        def department = new Department(code: "TT", description: "TT", systemRequiredIndicator: "N", voiceResponseMessageNumber: 1,
                lastModified: new Date(), lastModifiedBy: "test", dataOrigin: "Banner")
        department = departmentService.create(department)
        assertNotNull "Department ID is null in Department Delete Service Test", department.id

        Department departmentDelete = Department.findWhere(code: "TT")
        assertNotNull "Department ID is null in Department Delete Service Test", departmentDelete.id

        departmentService.delete(departmentDelete.id)
        assertNull "Department should have been deleted in Department Delete Service Test", department.get(departmentDelete.id)
    }

    @Test
    void testFetchByCode() {
        Department department = new Department(code: "TT", description: "TT", systemRequiredIndicator: "N", voiceResponseMessageNumber: 1,
                lastModified: new Date(), lastModifiedBy: "test", dataOrigin: "Banner")

        department = departmentService.create(department)
        assertNotNull department
        assertNotNull department.id

        Department expectDepartment = departmentService.fetchByCode(department.code)
        assertNotNull expectDepartment

        assertEquals department, expectDepartment
    }

    @Test
    void testFetchAllWithGuidByCodeInList() {
        Department department = new Department(code: "TT", description: "TT", systemRequiredIndicator: "N", voiceResponseMessageNumber: 1,
                lastModified: new Date(), lastModifiedBy: "test", dataOrigin: "Banner")

        department = departmentService.create(department)
        assertNotNull department
        assertNotNull department.id

        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.fetchByLdmNameAndDomainId(GeneralValidationCommonConstants.DEPARTMENT_LDM_NAME, department.id)
        assertNotNull globalUniqueIdentifier

        List entities = departmentService.fetchAllWithGuidByCodeInList([department.code])
        assertFalse entities.isEmpty()
        assertEquals entities.size(), 1

        Map entitiesMap = entities[0]
        assertFalse entitiesMap.isEmpty()

        assertEquals department, entitiesMap.department
        assertEquals globalUniqueIdentifier, entitiesMap.globalUniqueIdentifier
    }

    @Test
    void testFetchAllWithGuidByCodeInListWithPagination() {
        params.sort = 'id'
        params.order = 'asc'
        List<Department> departments = departmentService.list(params)
        assertNotNull departments
        assertFalse departments.isEmpty()

        Department department = departments[0]
        assertNotNull department.id

        GlobalUniqueIdentifier globalUniqueIdentifier = GlobalUniqueIdentifier.fetchByLdmNameAndDomainId(GeneralValidationCommonConstants.DEPARTMENT_LDM_NAME, department.id)
        assertNotNull globalUniqueIdentifier

        List entities = departmentService.fetchAllWithGuidByCodeInList(departments.code, 1, 0)
        assertFalse entities.isEmpty()
        assertEquals entities.size(), 1

        Map entitiesMap = entities[0]
        assertFalse entitiesMap.isEmpty()

        assertEquals department, entitiesMap.department
        assertEquals globalUniqueIdentifier, entitiesMap.globalUniqueIdentifier
    }
}

