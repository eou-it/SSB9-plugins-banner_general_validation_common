/** *******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system


import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

import static groovy.test.GroovyAssert.*
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback

@Integration
@Rollback
class EthnicityServiceIntegrationTests extends BaseIntegrationTestCase{

    def ethnicityService

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
    void testCreateEthnicity() {
        def ethnicity = newEthnicity()
        ethnicity = ethnicityService.create([domainModel:ethnicity])
        assertNotNull ethnicity
        assertNotNull ethnicity.id
    }

    @Test
    void testUpdateEthnicity() {
        def ethnicity = newEthnicity()
        ethnicity = ethnicityService.create([domainModel:ethnicity])

        Ethnicity ethnicityUpdate = Ethnicity.findWhere(code: "TT")
        assertNotNull ethnicityUpdate

        ethnicityUpdate.description = "ZZ"
        ethnicityUpdate = ethnicityService.update([domainModel:ethnicityUpdate])
        assertEquals "ZZ", ethnicityUpdate.description
    }

    @Test
    void testDeleteEthnicity() {
        def ethnicity = newEthnicity()
        ethnicity = ethnicityService.create([domainModel:ethnicity])
        assertNotNull ethnicity

        def id = ethnicity.id
        def deleteMe = Ethnicity.get(id)
        ethnicityService.delete([domainModel:ethnicity])
        assertNull Ethnicity.get(id)

    }

    @Test
    void testList(){
        def ethnicity = ethnicityService.list()
        assertTrue ethnicity.size() > 0
    }

    private def newEthnicity() {
        def race = Race.findByRace("IND")
        def ipedsEthnicity = IpedsEthnicity.findByCode("2")

        def ethnicity = new Ethnicity(
                code: "TT",
                description: "TTTTTTTTTT",
                ethnicCode: "T",
                electronicDataInterchangeEquivalent: "T",
                lmsEquivalent: "T",
                ethnic: "1",
                race: race,
                ipedsEthnicity: ipedsEthnicity,
        )
        return ethnicity
    }
}
