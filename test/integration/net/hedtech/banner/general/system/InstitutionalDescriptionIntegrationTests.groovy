/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import net.hedtech.banner.testing.BaseIntegrationTestCase

/**
 * Integration test for the course grading mode model.
 * */
class InstitutionalDescriptionIntegrationTests extends BaseIntegrationTestCase {

    def institutionalDescriptionService


    protected void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    void testFindByKey() {
        def institutionalDescription = InstitutionalDescription.fetchByKey()

        assertNotNull institutionalDescription
        assertTrue institutionalDescription.studentInstalled
    }


    void testServiceFindByKey() {
        def institutionalDescription = institutionalDescriptionService.findByKey()

        assertNotNull institutionalDescription
        assertTrue institutionalDescription.studentInstalled
    }


}
