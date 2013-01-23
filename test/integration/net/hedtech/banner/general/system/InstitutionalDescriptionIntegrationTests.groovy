/*********************************************************************************
 Copyright 2009-2011 SunGard Higher Education. All Rights Reserved.
 This copyrighted software contains confidential and proprietary information of 
 SunGard Higher Education and its subsidiaries. Any use of this software is limited 
 solely to SunGard Higher Education licensees, and is further subject to the terms 
 and conditions of one or more written license agreements between SunGard Higher 
 Education and the licensee in question. SunGard is either a registered trademark or
 trademark of SunGard Data Systems in the U.S.A. and/or other regions and/or countries.
 Banner and Luminis are either registered trademarks or trademarks of SunGard Higher 
 Education in the U.S.A. and/or other regions and/or countries.
 **********************************************************************************/
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

    /**
     * Please put all the custom tests in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(coursegeneralinformation_custom_test_methods) ENABLED START*/
    /*PROTECTED REGION END*/


}
