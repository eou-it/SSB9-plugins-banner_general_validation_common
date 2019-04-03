/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.overall

import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.dao.InvalidDataAccessResourceUsageException
import static groovy.test.GroovyAssert.*
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback

@Integration
@Rollback
class UserRoleIntegrationTests extends BaseIntegrationTestCase {

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
    void testUpdateFailedUserRole() {
        Integer pidm = 50199
        UserRole userRole = UserRole.fetchByPidm(pidm)
        assertNotNull userRole
        userRole.facultyIndicator = "Y"
        shouldFail(InvalidDataAccessResourceUsageException) {
            userRole.save(flush: true, onError: true)
        }
    }

    @Test
    void testFetchByPidm() {
        Integer pidm = 50199
        UserRole userRole = UserRole.fetchByPidm(pidm)

        assertNotNull userRole.id
    }

}
