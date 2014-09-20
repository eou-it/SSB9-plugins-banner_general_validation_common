/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system
import org.junit.Before
import org.junit.Test
import org.junit.After

import groovy.sql.Sql
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

import java.text.SimpleDateFormat

class DocumentIntegrationTests extends BaseIntegrationTestCase {

	@Before
	public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
    }


	@After
	public void tearDown() {
        super.tearDown()
    }


	@Test
    void testCreateValidDocument() {
        def document = newValidForCreateDocument()
        document.save(failOnError: true, flush: true)
        //Test if the generated entity now has an id assigned
        assertNotNull document.id
    }


	@Test
    void testUpdateValidDocument() {
        def document = newValidForCreateDocument()
        document.save(failOnError: true, flush: true)
        assertNotNull document.id
        assertEquals 0L, document.version
        assertEquals "TTTTTT", document.code
        assertEquals "TTTTTTTTTTTTTTTTTTTTTTTTTTTTTT", document.description

        //Update the entity
        document.description = "TTTTTTTTTTTTTTTTTTTTTTTTUPDATE"
        document.save(failOnError: true, flush: true)
        //Assert for sucessful update
        document = Document.get(document.id)
        assertEquals 1L, document?.version
        assertEquals "TTTTTTTTTTTTTTTTTTTTTTTTUPDATE", document.description
    }


	@Test
    void testDates() {
        def hour = new SimpleDateFormat('HH')
        def date = new SimpleDateFormat('yyyy-M-d')
        def today = new Date()

        def document = newValidForCreateDocument()

        document.save(flush: true, failOnError: true)
        document.refresh()
        assertNotNull "Document should have been saved", document.id

        // test date values -
        assertEquals date.format(today), date.format(document.lastModified)
        assertEquals hour.format(today), hour.format(document.lastModified)
    }


	@Test
    void testOptimisticLock() {
        def document = newValidForCreateDocument()
        document.save(failOnError: true, flush: true)

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update GTVDOCM set GTVDOCM_VERSION = 999 where GTVDOCM_SURROGATE_ID = ?", [document.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        document.description = "TTTTTTTTTTTTTTTTTTTTTTTTUPDATE"
        shouldFail(HibernateOptimisticLockingFailureException) {
            document.save(failOnError: true, flush: true)
        }
    }


	@Test
    void testDeleteDocument() {
        def document = newValidForCreateDocument()
        document.save(failOnError: true, flush: true)
        def id = document.id
        assertNotNull id
        document.delete()
        assertNull Document.get(id)
    }


	@Test
    void testValidation() {
        def document = new Document()
        assertFalse "Document could not be validated as expected due to ${document.errors}", document.validate()
    }


	@Test
    void testNullValidationFailure() {
        def document = new Document()
        assertFalse "Document should have failed validation", document.validate()
        assertErrorsFor document, 'nullable',
                [
                        'code',
                        'description'
                ]
    }


    private def newValidForCreateDocument() {
        def document = new Document(
                code: "TTTTTT",
                description: "TTTTTTTTTTTTTTTTTTTTTTTTTTTTTT",
        )
        return document
    }
}
