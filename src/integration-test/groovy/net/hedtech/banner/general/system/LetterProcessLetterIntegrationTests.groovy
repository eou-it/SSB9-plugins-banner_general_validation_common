/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system
import org.junit.Before
import org.junit.Test
import org.junit.After

import org.springframework.orm.hibernate5.HibernateOptimisticLockingFailureException as OptimisticLock

import net.hedtech.banner.testing.BaseIntegrationTestCase
import groovy.sql.Sql
import static groovy.test.GroovyAssert.*
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback

@Integration
@Rollback
class LetterProcessLetterIntegrationTests extends BaseIntegrationTestCase {

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
    void testCreateLetterProcessLetter() {
        def letterProcessLetter = createLetterProcessLetter()
        save letterProcessLetter
        //Test if the generated entity now has an id assigned
        assertNotNull letterProcessLetter.id
    }


    @Test
    void testUpdateLetterProcessLetter() {
        def letterProcessLetter = createLetterProcessLetter()
        save letterProcessLetter

        assertNotNull letterProcessLetter.id
        assertEquals 0L, letterProcessLetter.version
        assertEquals "TTTTT", letterProcessLetter.code
        assertTrue letterProcessLetter.duplIndicator
        assertEquals "TTTTT", letterProcessLetter.description
        assertEquals "TTTTT", letterProcessLetter.printCommand
        assertEquals "TTTTT", letterProcessLetter.letterAlt

        //Update the entity
        def testDate = new Date()
        letterProcessLetter.code = "UUUUU"
        letterProcessLetter.duplIndicator = false
        letterProcessLetter.description = "UUUUU"
        letterProcessLetter.printCommand = "UUUUU"
        letterProcessLetter.letterAlt = "UUUUU"
        letterProcessLetter.lastModified = testDate
        letterProcessLetter.lastModifiedBy = "test"
        letterProcessLetter.dataOrigin = "Banner"
        save letterProcessLetter

        letterProcessLetter = LetterProcessLetter.get(letterProcessLetter.id)
        assertEquals 1L, letterProcessLetter?.version
        assertEquals "UUUUU", letterProcessLetter.code
        assertEquals false, letterProcessLetter.duplIndicator
        assertEquals "UUUUU", letterProcessLetter.description
        assertEquals "UUUUU", letterProcessLetter.printCommand
        assertEquals "UUUUU", letterProcessLetter.letterAlt
    }


    @Test
    void testOptimisticLock() {
        def letterProcessLetter = createLetterProcessLetter()
        save letterProcessLetter

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update GTVLETR set GTVLETR_VERSION = 999 where GTVLETR_SURROGATE_ID = ?", [letterProcessLetter.id])
        } finally {
//            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        letterProcessLetter.code = "UUUUU"
        letterProcessLetter.duplIndicator = false
        letterProcessLetter.description = "UUUUU"
        letterProcessLetter.printCommand = "UUUUU"
        letterProcessLetter.letterAlt = "UUUUU"
        letterProcessLetter.lastModified = new Date()
        letterProcessLetter.lastModifiedBy = "test"
        letterProcessLetter.dataOrigin = "Banner"
        shouldFail(OptimisticLock) {
            letterProcessLetter.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testDeleteLetterProcessLetter() {
        def letterProcessLetter = createLetterProcessLetter()
        save letterProcessLetter
        def id = letterProcessLetter.id
        assertNotNull id
        letterProcessLetter.delete()
        assertNull LetterProcessLetter.get(id)
    }


    @Test
    void testValidation() {
        def letterProcessLetter = createLetterProcessLetter()
        assertTrue "LetterProcessLetter could not be validated as expected due to ${letterProcessLetter.errors}", letterProcessLetter.validate()
    }


    @Test
    void testNullValidationFailure() {
        def letterProcessLetter = new LetterProcessLetter()
        assertFalse "LetterProcessLetter should have failed validation", letterProcessLetter.validate()
        assertErrorsFor letterProcessLetter, 'nullable',
                [
                        'code',
                        'duplIndicator',

                ]
        assertNoErrorsFor letterProcessLetter,
                [
                        'description',
                        'printCommand',
                        'letterAlt',

                ]
    }


    @Test
    void testMaxSizeValidationFailures() {
        def letterProcessLetter = new LetterProcessLetter(
                description: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX',
                printCommand: 'XXXXXXXXXXXX',
                letterAlt: 'XXXXXXXXXXXXXXXXX')
        assertFalse "LetterProcessLetter should have failed validation", letterProcessLetter.validate()
        assertErrorsFor letterProcessLetter, 'maxSize', ['description', 'printCommand', 'letterAlt']
    }


    private def createLetterProcessLetter() {
        def letterProcessLetter = new LetterProcessLetter(
                code: "TTTTT",
                duplIndicator: true,
                description: "TTTTT",
                printCommand: "TTTTT",
                letterAlt: "TTTTT",
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return letterProcessLetter
    }


}
