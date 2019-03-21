package net.hedtech.banner.general.system
import org.junit.Before
import org.junit.Test
import org.junit.After

import net.hedtech.banner.testing.BaseIntegrationTestCase

class MealRateCodeServiceIntegrationTests extends BaseIntegrationTestCase {

    def mealRateCodeService


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
    void testCreateMealRateCode() {
        def mealRateCode = newMealRateCode()
        mealRateCode = mealRateCodeService.create([domainModel: mealRateCode])
        assertNotNull mealRateCode
    }


    @Test
    void testUpdateMealRateCode() {
        def mealRateCode = newMealRateCode()
        mealRateCode = mealRateCodeService.create([domainModel: mealRateCode])

        MealRateCode mealRateCodeUpdate = MealRateCode.findWhere(code: "TT")
        assertNotNull mealRateCodeUpdate

        mealRateCodeUpdate.description = "ZZ"
        mealRateCodeUpdate = mealRateCodeService.update([domainModel: mealRateCodeUpdate])
        assertEquals "ZZ", mealRateCodeUpdate.description
    }


    @Test
    void testDeleteMealRateCode() {
        def mealRateCode = newMealRateCode()
        mealRateCode = mealRateCodeService.create([domainModel: mealRateCode])
        assertNotNull mealRateCode
        def id = mealRateCode.id
        def deleteMe = MealRateCode.get(id)
        mealRateCodeService.delete([domainModel: deleteMe])
        assertNull MealRateCode.get(id)

    }


    @Test
    void testList() {
        def mealRateCode = mealRateCodeService.list()
        assertTrue mealRateCode.size() > 0
    }


    private def newMealRateCode() {
        def mealRateCode = new MealRateCode(
                code: "TT",
                description: "TT",
                monthlyIndicator: false,
                dailyIndicator: false,
                termIndicator: true,
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return mealRateCode
    }
}
