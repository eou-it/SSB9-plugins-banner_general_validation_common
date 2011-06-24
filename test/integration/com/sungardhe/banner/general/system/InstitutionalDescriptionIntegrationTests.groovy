/** *****************************************************************************
 © 2011 SunGard Higher Education.  All Rights Reserved.
 CONFIDENTIAL BUSINESS INFORMATION
 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 ****************************************************************************** */
package com.sungardhe.banner.general.system

import com.sungardhe.banner.testing.BaseIntegrationTestCase

/**
 * Integration test for the course grading mode model.
 * */
class InstitutionalDescriptionIntegrationTests extends BaseIntegrationTestCase {

    def institutionalDescriptionService


    protected void setUp() {
        formContext = ['GUAINIT'] // Since we are not testing a controller, we need to explicitly set this
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

    /**
     * Please put all the custom tests in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(coursegeneralinformation_custom_test_methods) ENABLED START*/
    /*PROTECTED REGION END*/


}