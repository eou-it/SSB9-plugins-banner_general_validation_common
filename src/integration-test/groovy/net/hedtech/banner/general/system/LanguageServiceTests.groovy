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
class LanguageServiceTests extends BaseIntegrationTestCase {

    def languageService


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
    void testCreateLanguage() {
        def language = new Language(code: "T", description: "TT")
        language = languageService.create([domainModel: language])
        assertNotNull language
    }


    @Test
    void testUpdateLanguage() {
        def language = new Language(code: "T", description: "TT")
        language = languageService.create([domainModel: language])

        Language languageUpdate = Language.findWhere(code: "T")
        assertNotNull languageUpdate

        languageUpdate.description = "ZZ"
        languageUpdate = languageService.update([domainModel: languageUpdate])
        assertEquals "ZZ", languageUpdate.description
    }


    @Test
    void testDeleteLanguage() {
        def language = new Language(code: "T", description: "TT")
        language = languageService.create([domainModel: language])
        assertNotNull language
        def id = language.id
        def deleteMe = Language.get(id)
        languageService.delete([domainModel: deleteMe])
        assertNull Language.get(id)

    }


    @Test
    void testList() {
        def language = languageService.list()
        assertTrue language.size() > 0
    }

}
