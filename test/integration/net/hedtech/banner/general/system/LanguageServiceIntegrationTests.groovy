/** *****************************************************************************
 Copyright 2019 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.hibernate.Session
import org.junit.After
import org.junit.Before
import org.junit.Test

class LanguageServiceIntegrationTests extends BaseIntegrationTestCase {

    def languageService

    final String LANG_ISO_CODE_ENG = 'eng'
    final String LANG_ISO_CODE_RUS = 'rus'
    final String LANG_ISO_CODE_AAR = 'aar'

    final String LANG_CODE_ENG = 'ENG'
    final String LANG_CODE_RUS = 'RUS'
    final String LANG_CODE_ARA = 'ARA'


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
        def language = newValidForCreateLanguageWithIsoCode()
        language = languageService.create([domainModel: language])
        assertNotNull language
        assertNotNull language.id
    }


    @Test
    void testUpdateLanguage() {
        def language = newValidForCreateLanguageWithIsoCode()
        language = languageService.create([domainModel: language])
        assertNotNull language
        assertNotNull language.id

        Language lang = Language.findWhere(code: "TTT")
        assertNotNull lang

        lang.description = "ZZ"
        lang.isoCode = LANG_ISO_CODE_AAR
        Language languageUpdated = languageService.update([domainModel: lang])

        Language languageNew = Language.findWhere(code: "TTT")
        assertNotNull languageNew
        assertNotNull languageNew.id
        assertEquals "ZZ", languageNew.description
        assertNotNull languageNew.isoCode
        assertTrue LANG_ISO_CODE_AAR.equalsIgnoreCase(languageNew.isoCode)
    }


    @Test
    void testDeleteLanguage() {
        def language = newValidForCreateLanguageWithIsoCode()
        language = languageService.create([domainModel: language])
        assertNotNull language
        assertNotNull language.id

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


    @Test
    void testValidLanguageAndFetchByCode(){
        def language = newValidForCreateLanguageWithIsoCode()
        language = languageService.create([domainModel: language])
        assertNotNull language
        assertNotNull language.id

        def lang = languageService.fetchByCode('TTT')
        assertEquals 'TTT Desc', lang.description

    }

    @Test
    void testInvalidLanguage(){
        Language language = languageService.fetchByCode('DJD')
        assertNull language
    }


    @Test
    void test_fetchAllByCodeInList(){
        def languageCodes = [LANG_CODE_ENG, LANG_CODE_RUS, LANG_CODE_ARA]

        updateLanguageWithIsoCodeInList(languageCodes)

        def languages = languageService.fetchAllByCodeInList(languageCodes)
        assertNotNull languages
        assertTrue languages.size() > 0

        def language_eng = languages.find { language -> language.code == LANG_CODE_ENG}
        assertNotNull language_eng

        def language_rus = languages.find { language -> language.code == LANG_CODE_RUS}
        assertNotNull language_rus

        def language_ara = languages.find { language -> language.code == LANG_CODE_ARA}
        assertNotNull language_ara
    }

    @Test
    void test_fetchAllByIsoCode(){
        def languageCodes = [LANG_CODE_ENG, LANG_CODE_RUS, LANG_CODE_ARA]
        updateLanguageWithIsoCodeInList(languageCodes)


        def languages = languageService.fetchAllByIsoCode(LANG_ISO_CODE_ENG)
        assertNotNull languages
        assertTrue languages.size() > 0

        def language_eng = languages.find { language -> language.code == LANG_CODE_ENG}
        assertNotNull language_eng
        assertNotNull language_eng.isoCode
        assertTrue language_eng.isoCode.equalsIgnoreCase(LANG_ISO_CODE_ENG)
    }

    @Test
    void test_fetchIsoCodeToLanguageCodeMap() {
        def languageCodes = [LANG_CODE_ENG, LANG_CODE_RUS, LANG_CODE_ARA]

        updateLanguageWithIsoCodeInList(languageCodes)

        def languages = languageService.fetchAllByCodeInList(languageCodes)
        assertNotNull languages
        assertTrue languages.size() > 0

        Map languagesMap = languageService.getCodeToIsoCodeMap([LANG_CODE_ENG, LANG_CODE_RUS])
        assertNotNull  languagesMap
        assertTrue  languagesMap.size() == 2

        String langNewEng = languagesMap.get(LANG_CODE_ENG)
        assertNotNull langNewEng
        assertTrue LANG_ISO_CODE_ENG.equalsIgnoreCase(langNewEng)

        String langNewRus = languagesMap.get(LANG_CODE_RUS)
        assertNotNull langNewRus
        assertTrue LANG_ISO_CODE_RUS.equalsIgnoreCase(langNewRus)

    }

    private updateLanguageWithIsoCodeInList(Collection<String> languageCodes) {
        Session session = sessionFactory.getCurrentSession()

        if(languageCodes && languageCodes.contains(LANG_CODE_ENG)) {
            session.createSQLQuery( "UPDATE STVLANG set STVLANG_SCOD_CODE_ISO = '" + LANG_ISO_CODE_ENG + "' WHERE STVLANG_CODE ='" + LANG_CODE_ENG + "' ").executeUpdate()
        }

        if(languageCodes && languageCodes.contains(LANG_CODE_RUS)) {
            session.createSQLQuery( "UPDATE STVLANG set STVLANG_SCOD_CODE_ISO = '" + LANG_ISO_CODE_RUS + "' WHERE STVLANG_CODE ='" + LANG_CODE_RUS + "' ").executeUpdate()
        }

        if(languageCodes && languageCodes.contains(LANG_CODE_ARA)) {
            session.createSQLQuery( "UPDATE STVLANG set STVLANG_SCOD_CODE_ISO = '" + LANG_ISO_CODE_AAR + "' WHERE STVLANG_CODE ='" + LANG_CODE_ARA + "' ").executeUpdate()
        }
    }

    private def newValidForCreateLanguageWithIsoCode() {
        def language = new Language(
                code: "TTT",
                description: "TTT Desc",
                isoCode: "TTT"
        )
        return language
    }
}
