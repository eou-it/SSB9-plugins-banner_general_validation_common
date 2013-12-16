package net.hedtech.banner.general.system

import net.hedtech.banner.testing.BaseIntegrationTestCase

class MealRateCodeServiceIntegrationTests extends BaseIntegrationTestCase {

    def mealRateCodeService


    protected void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateMealRateCode() {
        def mealRateCode = newMealRateCode()
        mealRateCode = mealRateCodeService.create([domainModel: mealRateCode])
        assertNotNull mealRateCode
    }


    void testUpdateMealRateCode() {
        def mealRateCode = newMealRateCode()
        mealRateCode = mealRateCodeService.create([domainModel: mealRateCode])

        MealRateCode mealRateCodeUpdate = MealRateCode.findWhere(code: "TT")
        assertNotNull mealRateCodeUpdate

        mealRateCodeUpdate.description = "ZZ"
        mealRateCodeUpdate = mealRateCodeService.update([domainModel: mealRateCodeUpdate])
        assertEquals "ZZ", mealRateCodeUpdate.description
    }


    void testDeleteMealRateCode() {
        def mealRateCode = newMealRateCode()
        mealRateCode = mealRateCodeService.create([domainModel: mealRateCode])
        assertNotNull mealRateCode
        def id = mealRateCode.id
        def deleteMe = MealRateCode.get(id)
        mealRateCodeService.delete([domainModel: deleteMe])
        assertNull MealRateCode.get(id)

    }


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
