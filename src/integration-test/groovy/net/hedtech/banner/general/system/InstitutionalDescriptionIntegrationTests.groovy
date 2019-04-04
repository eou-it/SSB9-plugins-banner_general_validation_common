/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system
import org.junit.Before
import org.junit.Test
import org.junit.After

import net.hedtech.banner.testing.BaseIntegrationTestCase
import static groovy.test.GroovyAssert.*
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback

@Integration
@Rollback
/**
 * Integration test for the course grading mode model.
 * */
class InstitutionalDescriptionIntegrationTests extends BaseIntegrationTestCase {

    def institutionalDescriptionService


    @Before
    public void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    @Test
    void testFindByKey() {
        def institutionalDescription = InstitutionalDescription.fetchByKey()

        assertNotNull institutionalDescription
        assertTrue institutionalDescription.studentInstalled
    }


    @Test
    void testServiceFindByKey() {
        def institutionalDescription = institutionalDescriptionService.findByKey()

        assertNotNull institutionalDescription
        assertTrue institutionalDescription.studentInstalled
    }


}
