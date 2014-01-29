/*********************************************************************************
 Copyright 2010-2014 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */

package net.hedtech.banner.general.utility

import net.hedtech.banner.testing.BaseIntegrationTestCase

class InformationTextPersonaIntegrationTests extends BaseIntegrationTestCase {


    protected void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    void testInformationTextPersonasWithoutFilter() {
        InformationTextPersonaListService.webTailorRoleList = [
                [
                        code: "ALUMNI",
                        description: "Alumni",
                        lastModified: "19-OCT-98",
                        lastModifiedBy: ""
                ],
                [
                        code: "EMPLOYEE",
                        description: "Employee",
                        lastModified: "19-OCT-98",
                        lastModifiedBy: ""
                ],
                [
                        code: "FACULTY",
                        description: "Faculty",
                        lastModified: "19-OCT-98",
                        lastModifiedBy: ""
                ]]
        def personaList = InformationTextPersonaListService.fetchPersonas()
        int personaCount = personaList.list.size()
        assertEquals(4, personaCount)
    }

    void testInformationTextPersonasWithFilter() {
        InformationTextPersonaListService.webTailorRoleList = [
                [
                        code: "ALUMNI",
                        description: "Alumni",
                        lastModified: "19-OCT-98",
                        lastModifiedBy: ""
                ],
                [
                        code: "EMPLOYEE",
                        description: "Employee",
                        lastModified: "19-OCT-98",
                        lastModifiedBy: ""
                ],
                [
                        code: "FACULTY",
                        description: "Faculty",
                        lastModified: "19-OCT-98",
                        lastModifiedBy: ""
                ]]
        def personaList = InformationTextPersonaListService.fetchPersonas('U')
        int personaCount = personaList.list.size()
        assertEquals(3, personaCount)
    }

    void testInformationTextPersonasWithExactFilterMatch() {
        InformationTextPersonaListService.webTailorRoleList = [
                [
                        code: "ALUMNI",
                        description: "Alumni",
                        lastModified: "19-OCT-98",
                        lastModifiedBy: ""
                ],
                [
                        code: "EMPLOYEE",
                        description: "Employee",
                        lastModified: "19-OCT-98",
                        lastModifiedBy: ""
                ],
                [
                        code: "FACULTY",
                        description: "Faculty",
                        lastModified: "19-OCT-98",
                        lastModifiedBy: ""
                ]]
        def personaList = InformationTextPersonaListService.fetchPersonas('ALUMNI')
        String persona = personaList.list[0].code
        assertEquals('ALUMNI', persona)
    }

    void testValidInformationTextPersonaWithFilter() {
        InformationTextPersonaListService.webTailorRoleList = [
                [
                        code: "ALUMNI",
                        description: "Alumni",
                        lastModified: "19-OCT-98",
                        lastModifiedBy: ""
                ],
                [
                        code: "EMPLOYEE",
                        description: "Employee",
                        lastModified: "19-OCT-98",
                        lastModifiedBy: ""
                ],
                [
                        code: "FACULTY",
                        description: "Faculty",
                        lastModified: "19-OCT-98",
                        lastModifiedBy: ""
                ]]
        def personaList = InformationTextPersonaListService.fetchValidInformationTextPersona('U')
        String persona = personaList.code
        assertEquals("ALUMNI", persona)
    }
}


