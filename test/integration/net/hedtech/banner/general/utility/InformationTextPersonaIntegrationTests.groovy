/*********************************************************************************
 Copyright 2010-2013 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */

package net.hedtech.banner.general.utility

import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder

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
        def personaList = InformationTextPersona.fetchInformationTextPersonas()
        int personaCount = personaList.list.size()
        assertEquals(3, personaCount)
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
        def personaList = InformationTextPersona.fetchInformationTextPersonas('U')
        int personaCount = personaList.list.size()
        assertEquals(2, personaCount)
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
        def personaList = InformationTextPersona.fetchInformationTextPersonas('ALUMNI')
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
        def personaList = InformationTextPersona.fetchValidInformationTextPersona('U')
        String persona = personaList.code
        assertEquals("ALUMNI", persona)
    }
}

