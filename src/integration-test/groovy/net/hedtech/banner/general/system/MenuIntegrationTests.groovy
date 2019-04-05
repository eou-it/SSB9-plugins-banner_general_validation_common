/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system
import org.junit.Before
import org.junit.Test
import org.junit.After

import grails.validation.ValidationException
import groovy.sql.Sql
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.springframework.orm.hibernate5.HibernateOptimisticLockingFailureException
import static groovy.test.GroovyAssert.*
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback

@Integration
@Rollback
class MenuIntegrationTests extends BaseIntegrationTestCase {

    //Test data for creating new domain instance
    //Valid test data (For success tests)

    def i_success_code = "TTTTT"
    def i_success_description = "TTTTT"
    //Invalid test data (For failure tests)

    def i_failure_code = "TTTTT"
    def i_failure_description = "TTTTT"

    //Test data for creating updating domain instance
    //Valid test data (For success tests)

    def u_success_code = "TTTTT"
    def u_success_description = "WWWWW"
    //Valid test data (For failure tests)

    def u_failure_code = "TTTTT"
    def u_failure_description = "TTTTT"


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
    void testCreateValidMenu() {
        def menu = newValidForCreateMenu()
        menu.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull menu.id
    }


    @Test
    void testCreateInvalidMenu() {
        def menu = newInvalidForCreateMenu()
        menu.code = null
        shouldFail(ValidationException) {
            menu.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testUpdateValidMenu() {
        def menu = newValidForCreateMenu()
        menu.save(failOnError: true, flush: true)
        assertNotNull menu.id
        assertEquals 0L, menu.version
        assertEquals i_success_code, menu.code
        assertEquals i_success_description, menu.description

        //Update the entity
        menu.description = u_success_description
        menu.save(failOnError: true, flush: true)
        //Assert for sucessful update
        menu = Menu.get(menu.id)
        assertEquals 1L, menu?.version
        assertEquals u_success_description, menu.description
    }


    @Test
    void testUpdateInvalidMenu() {
        def menu = newValidForCreateMenu()
        menu.save(failOnError: true, flush: true)
        assertNotNull menu.id
        assertEquals 0L, menu.version
        assertEquals i_success_code, menu.code
        assertEquals i_success_description, menu.description

        //Update the entity with invalid values
        menu.description = u_failure_description
        menu.code = null
        shouldFail(ValidationException) {
            menu.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testOptimisticLock() {
        def menu = newValidForCreateMenu()
        menu.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update GTVMENU set GTVMENU_VERSION = 999 where GTVMENU_SURROGATE_ID = ?", [menu.id])
        } finally {
//            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        menu.description = u_success_description
        shouldFail(HibernateOptimisticLockingFailureException) {
            menu.save(failOnError: true, flush: true)
        }
    }


    @Test
    void testDeleteMenu() {
        def menu = newValidForCreateMenu()
        menu.save(failOnError: true, flush: true)
        def id = menu.id
        assertNotNull id
        menu.delete()
        assertNull Menu.get(id)
    }


    @Test
    void testValidation() {
        def menu = newInvalidForCreateMenu()
        menu.code = null
        assertFalse "Menu could not be validated as expected due to ${menu.errors}", menu.validate()
    }


    @Test
    void testNullValidationFailure() {
        def menu = new Menu()
        assertFalse "Menu should have failed validation", menu.validate()
        assertErrorsFor menu, 'nullable',
                [
                        'code',
                        'description'
                ]
    }


    private def newValidForCreateMenu() {
        def menu = new Menu(
                code: i_success_code,
                description: i_success_description,
        )
        return menu
    }


    private def newInvalidForCreateMenu() {
        def menu = new Menu(
                code: i_failure_code,
                description: i_failure_description,
        )
        return menu
    }


}
