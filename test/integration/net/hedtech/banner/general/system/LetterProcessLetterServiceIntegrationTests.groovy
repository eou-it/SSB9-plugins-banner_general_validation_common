/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import net.hedtech.banner.testing.BaseIntegrationTestCase

class LetterProcessLetterServiceIntegrationTests extends BaseIntegrationTestCase {

    def letterProcessLetterService

    protected void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }


    void testCreateLetterProcessLetter() {

        def letterProcessLetter = newLetterProcessLetter()

        letterProcessLetter = letterProcessLetterService.create(letterProcessLetter)
        assertNotNull "LetterProcessLetter ID is null in LetterProcessLetter Service Tests Create", letterProcessLetter.id
    }

    void testUpdate() {
        def letterProcessLetter = newLetterProcessLetter()
        letterProcessLetter = letterProcessLetterService.create(letterProcessLetter)

        // create new values for the fields
        def icode = "XXXXX"
        def iduplIndicator = false
        def idescription = "XXXXX"
        def iprintCommand = "XXXXX"
        def iletterAlt = "XXXXX"

        // change the values
        letterProcessLetter.code = icode
        letterProcessLetter.duplIndicator = iduplIndicator
        letterProcessLetter.description = idescription
        letterProcessLetter.printCommand = iprintCommand
        letterProcessLetter.letterAlt = iletterAlt
        letterProcessLetter = letterProcessLetterService.update(letterProcessLetter)

        // test the values
        assertEquals icode, letterProcessLetter.code
        assertEquals iduplIndicator, letterProcessLetter.duplIndicator
        assertEquals idescription, letterProcessLetter.description
        assertEquals iprintCommand, letterProcessLetter.printCommand
        assertEquals iletterAlt, letterProcessLetter.letterAlt
    }

    void testLetterProcessLetterDelete() {
        def letterProcessLetter = newLetterProcessLetter()
        letterProcessLetter = letterProcessLetterService.create(letterProcessLetter)

        def id = letterProcessLetter.id
        letterProcessLetterService.delete(id)

        assertNull "LetterProcessLetter should have been deleted", letterProcessLetter.get(id)
    }

    private def newLetterProcessLetter() {


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
