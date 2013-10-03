package net.hedtech.banner.general.system

import net.hedtech.banner.testing.BaseIntegrationTestCase

class LanguageServiceTests extends BaseIntegrationTestCase {

    def languageService


    protected void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateLanguage() {
        def language = new Language(code: "T", description: "TT", nonResIndicator: "T",
                                    voiceResponseMsgNumber: 1, statscanCde2: 1, sevisEquiv: "T")
        language = languageService.create([domainModel: language])
        assertNotNull language
    }


    void testUpdateLanguage() {
        def language = new Language(code: "T", description: "TT", nonResIndicator: "T",
                                    voiceResponseMsgNumber: 1, statscanCde2: 1, sevisEquiv: "T")
        language = languageService.create([domainModel: language])

        Language languageUpdate = Language.findWhere(code: "T")
        assertNotNull languageUpdate

        languageUpdate.description = "ZZ"
        languageUpdate = languageService.update([domainModel: language])
        assertEquals "ZZ", languageUpdate.description
    }


    void testDeleteLanguage() {
        def language = new Language(code: "T", description: "TT", nonResIndicator: "T",
                                    voiceResponseMsgNumber: 1, statscanCde2: 1, sevisEquiv: "T")
        language = languageService.create([domainModel: language])
        assertNotNull language
        def id = language.id
        def deleteMe = Language.get(id)
        languageService.delete([domainModel: deleteMe])
        assertNull Language.get(id)

    }


    void testList() {
        def language = languageService.list()
        assertTrue language.size() > 0
    }

}
