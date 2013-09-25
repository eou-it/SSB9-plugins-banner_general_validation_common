/*******************************************************************************
Copyright 2013 Ellucian Company L.P. and its affiliates.
*******************************************************************************/
package net.hedtech.banner.general.system

import net.hedtech.banner.testing.BaseIntegrationTestCase
import net.hedtech.banner.exceptions.ApplicationException
import groovy.sql.Sql
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

import java.text.SimpleDateFormat


class InternationalSponsorIntegrationTests extends BaseIntegrationTestCase {

	protected void setUp() {
		formContext = ['GUAGMNU']
		super.setUp()
	}


	protected void tearDown() {
		super.tearDown()
	}


	void testCreateValidInternationalSponsor() {
		def internationalSponsor = newValidForCreateInternationalSponsor()
		internationalSponsor.save( failOnError: true, flush: true )
		//Test if the generated entity now has an id assigned
        assertNotNull internationalSponsor.id
	}

	void testUpdateValidInternationalSponsor() {
		def internationalSponsor = newValidForCreateInternationalSponsor()
		internationalSponsor.save( failOnError: true, flush: true )
        assertNotNull internationalSponsor.id
        assertEquals 0L, internationalSponsor.version
        assertEquals "TTT", internationalSponsor.code
        assertEquals "TTTTTTTTTTTTTTTTTTTTTTTTTTTTTT", internationalSponsor.description

		//Update the entity
		internationalSponsor.description = "TTTTTTTTTTTTTTTTTTTTTTTTUPDATE"
		internationalSponsor.save( failOnError: true, flush: true )
		//Assert for sucessful update
        internationalSponsor = InternationalSponsor.get( internationalSponsor.id )
        assertEquals 1L, internationalSponsor?.version
        assertEquals "TTTTTTTTTTTTTTTTTTTTTTTTUPDATE", internationalSponsor.description
	}

    void testDates() {
        def time = new SimpleDateFormat('HHmmss')
        def hour = new SimpleDateFormat('HH')
        def date = new SimpleDateFormat('yyyy-M-d')
        def today = new Date()

    	def internationalSponsor = newValidForCreateInternationalSponsor()

    	internationalSponsor.save(flush: true, failOnError: true)
    	internationalSponsor.refresh()
    	assertNotNull "InternationalSponsor should have been saved", internationalSponsor.id

    	// test date values -
    	assertEquals date.format(today), date.format(internationalSponsor.lastModified)
    	assertEquals hour.format(today), hour.format(internationalSponsor.lastModified)
    }

    void testOptimisticLock() {
		def internationalSponsor = newValidForCreateInternationalSponsor()
		internationalSponsor.save( failOnError: true, flush: true )

        def sql
        try {
            sql = new Sql( sessionFactory.getCurrentSession().connection() )
            sql.executeUpdate( "update STVSPON set STVSPON_VERSION = 999 where STVSPON_SURROGATE_ID = ?", [ internationalSponsor.id ] )
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
		//Try to update the entity
		//Update the entity
		internationalSponsor.description = "TTTTTTTTTTTTTTTTTTTTTTTTUPDATE"
        shouldFail( HibernateOptimisticLockingFailureException ) {
            internationalSponsor.save( failOnError: true, flush: true )
        }
    }

	void testDeleteInternationalSponsor() {
		def internationalSponsor = newValidForCreateInternationalSponsor()
		internationalSponsor.save( failOnError: true, flush: true )
		def id = internationalSponsor.id
		assertNotNull id
		internationalSponsor.delete()
		assertNull InternationalSponsor.get( id )
	}

    void testValidation() {
       def internationalSponsor = new InternationalSponsor()
       assertFalse "InternationalSponsor could not be validated as expected due to ${internationalSponsor.errors}", internationalSponsor.validate()
    }

    void testNullValidationFailure() {
        def internationalSponsor = new InternationalSponsor()
        assertFalse "InternationalSponsor should have failed validation", internationalSponsor.validate()
        assertErrorsFor internationalSponsor, 'nullable',
                                               [ 
                                                 'code'                                                 
                                               ]
        assertNoErrorsFor internationalSponsor,
        									   [ 
             									 'description'                                                 
											   ]
    }

    void testMaxSizeValidationFailures() {
        def internationalSponsor = new InternationalSponsor( 
        description:'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX' )
		assertFalse "InternationalSponsor should have failed validation", internationalSponsor.validate()
		assertErrorsFor internationalSponsor, 'maxSize', [ 'description' ]
    }
    
	private def newValidForCreateInternationalSponsor() {
		def internationalSponsor = new InternationalSponsor(
			code: "TTT",
			description: "TTTTTTTTTTTTTTTTTTTTTTTTTTTTTT",
		)
		return internationalSponsor
	}
}