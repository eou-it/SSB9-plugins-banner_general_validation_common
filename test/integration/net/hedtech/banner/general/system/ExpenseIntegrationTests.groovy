/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import grails.validation.ValidationException
import groovy.sql.Sql
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

class ExpenseIntegrationTests extends BaseIntegrationTestCase {

    //Test data for creating new domain instance
    //Valid test data (For success tests)

    def i_success_code = "TT"
    def i_success_description = "i_success_description"
    //Invalid test data (For failure tests)

    def i_failure_code = "TT"
    def i_failure_description = null

    //Test data for creating updating domain instance
    //Valid test data (For success tests)

    def u_success_code = "TT"
    def u_success_description = "u_success_description"
    //Valid test data (For failure tests)

    def u_failure_code = "TT"
    def u_failure_description = null


    protected void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateValidExpense() {
        def expense = newValidForCreateExpense()
        expense.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull expense.id
    }


    void testCreateInvalidExpense() {
        def expense = newInvalidForCreateExpense()
        shouldFail(ValidationException) {
            expense.save(failOnError: true, flush: true)
        }
    }


    void testUpdateValidExpense() {
        def expense = newValidForCreateExpense()
        expense.save(failOnError: true, flush: true)
        assertNotNull expense.id
        assertEquals 0L, expense.version
        assertEquals i_success_code, expense.code
        assertEquals i_success_description, expense.description

        //Update the entity
        expense.description = u_success_description
        expense.save(failOnError: true, flush: true)
        //Assert for sucessful update
        expense = Expense.get(expense.id)
        assertEquals 1L, expense?.version
        assertEquals u_success_description, expense.description
    }


    void testUpdateInvalidExpense() {
        def expense = newValidForCreateExpense()
        expense.save(failOnError: true, flush: true)
        assertNotNull expense.id
        assertEquals 0L, expense.version
        assertEquals i_success_code, expense.code
        assertEquals i_success_description, expense.description

        //Update the entity with invalid values
        expense.description = u_failure_description
        shouldFail(ValidationException) {
            expense.save(failOnError: true, flush: true)
        }
    }


    void testOptimisticLock() {
        def expense = newValidForCreateExpense()
        expense.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update GTVEXPN set GTVEXPN_VERSION = 999 where GTVEXPN_SURROGATE_ID = ?", [expense.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        expense.description = u_success_description
        shouldFail(HibernateOptimisticLockingFailureException) {
            expense.save(failOnError: true, flush: true)
        }
    }


    void testDeleteExpense() {
        def expense = newValidForCreateExpense()
        expense.save(failOnError: true, flush: true)
        def id = expense.id
        assertNotNull id
        expense.delete()
        assertNull Expense.get(id)
    }


    void testValidation() {
        def expense = newInvalidForCreateExpense()
        assertFalse "Expense could not be validated as expected due to ${expense.errors}", expense.validate()
    }


    void testNullValidationFailure() {
        def expense = new Expense()
        assertFalse "Expense should have failed validation", expense.validate()
        assertErrorsFor expense, 'nullable',
                [
                        'code',
                        'description'
                ]
    }



    private def newValidForCreateExpense() {
        def expense = new Expense(
                code: i_success_code,
                description: i_success_description,
        )
        return expense
    }


    private def newInvalidForCreateExpense() {
        def expense = new Expense(
                code: i_failure_code,
                description: i_failure_description,
        )
        return expense
    }


}
