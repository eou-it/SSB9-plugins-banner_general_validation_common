/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import groovy.sql.Sql
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

import java.text.SimpleDateFormat

class LanguageIntegrationTests extends BaseIntegrationTestCase {

    protected void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateValidLanguage() {
        def language = newValidForCreateLanguage()
        language.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull language.id
    }


    void testUpdateValidLanguage() {
        def language = newValidForCreateLanguage()
        language.save(failOnError: true, flush: true)
        assertNotNull language.id
        assertEquals 0L, language.version
        assertEquals "TTT", language.code
        assertEquals "TTTTTTTTTTTTTTTTTTTTTTTTTTTTTT", language.description

        //Update the entity
        language.description = "TTTTTTTTTTTTTTTTTTTTTTTTUPDATE"
        language.save(failOnError: true, flush: true)
        //Assert for sucessful update
        language = Language.get(language.id)
        assertEquals 1L, language?.version
        assertEquals "TTTTTTTTTTTTTTTTTTTTTTTTUPDATE", language.description
    }


    void testDates() {
        def time = new SimpleDateFormat('HHmmss')
        def hour = new SimpleDateFormat('HH')
        def date = new SimpleDateFormat('yyyy-M-d')
        def today = new Date()

        def language = newValidForCreateLanguage()

        language.save(flush: true, failOnError: true)
        language.refresh()
        assertNotNull "Language should have been saved", language.id

        // test date values -
        assertEquals date.format(today), date.format(language.lastModified)
        assertEquals hour.format(today), hour.format(language.lastModified)
    }


    void testOptimisticLock() {
        def language = newValidForCreateLanguage()
        language.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVLANG set STVLANG_VERSION = 999 where STVLANG_SURROGATE_ID = ?", [language.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        language.description = "TTTTTTTTTTTTTTTTTTTTTTTTUPDATE"
        shouldFail(HibernateOptimisticLockingFailureException) {
            language.save(failOnError: true, flush: true)
        }
    }


    void testDeleteLanguage() {
        def language = newValidForCreateLanguage()
        language.save(failOnError: true, flush: true)
        def id = language.id
        assertNotNull id
        language.delete()
        assertNull Language.get(id)
    }


    void testValidation() {
        def language = new Language()
        assertFalse "Language could not be validated as expected due to ${language.errors}", language.validate()
    }


    void testNullValidationFailure() {
        def language = new Language()
        assertFalse "Language should have failed validation", language.validate()
        assertErrorsFor language, 'nullable',
                [
                        'code',
                        'description'
                ]
    }


    private def newValidForCreateLanguage() {
        def language = new Language(
                code: "TTT",
                description: "TTTTTTTTTTTTTTTTTTTTTTTTTTTTTT",
        )
        return language
    }
}
