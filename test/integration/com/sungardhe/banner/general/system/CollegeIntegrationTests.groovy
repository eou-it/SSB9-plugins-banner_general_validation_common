/** *****************************************************************************

 © 2010 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 ****************************************************************************** */
package com.sungardhe.banner.general.system

import com.sungardhe.banner.testing.BaseIntegrationTestCase

import com.sungardhe.banner.testing.BaseIntegrationTestCase
import groovy.sql.Sql
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

/**
 * Integration test for the college model.
 **/
class CollegeIntegrationTests extends BaseIntegrationTestCase {

	def collegeService
	
	
	protected void setUp() {
        formContext = [ 'STVCOLL' ] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


	void testCreateCollege() {
		def entity = newCollege()
		save entity		
	
        assertNotNull entity.id
	}
	

	void testUpdateCollege() {
		def entity = newCollege()
		save entity
       
        assertNotNull entity.id
        assertEquals( 0L, entity.version )
        assertEquals( 'TT', entity.description )
        
        entity.description = "Updated TT"
        save entity
        
        entity = College.get( entity.id )
        assertEquals new Long( 1 ), entity?.version
        assertEquals "Updated TT", entity?.description       
	}
	
	
    void testOptimisticLock() { 
		def entity = newCollege()
		save entity
        
        def sql
        try {
            sql = new Sql( sessionFactory.getCurrentSession().connection() )
            sql.executeUpdate( "update STVCOLL set STVCOLL_VERSION = 999 where STVCOLL_SURROGATE_ID = ?", [ entity.id ] )
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        entity.description = "This better fail"
        shouldFail( HibernateOptimisticLockingFailureException ) {
            entity.save( flush: true )
        }
    }
	

	void testDeleteCollege() {
		def college = newCollege()
		save college
		
		def id = college.id
		assertNotNull id
		college.delete()
		assertNull College.get( id )
	}
	
	
    void testValidation() {
        def college = newCollege()
        assertTrue "College could not be validated as expected due to ${college.errors}", college.validate()
    }
    
    
    void testNullValidationFailure() {
        def college = new College()
        assertFalse "College should have failed validation", college.validate()
        assertErrorsFor( college, 'nullable', [ 'code', 'description' ] )
        assertNoErrorsFor( college, [ 'addressStreetLine1', 'addressStreetLine2', 'addressStreetLine3', 'addressStreetLine4',
                           'addressCity', 'addressState', 'addressCountry', 'addressZipCode', 'systemRequiredIndicator', 'voiceResponseMessageNumber',
                           'statisticsCanadianInstitution', 'districtDivision', 'houseNumber' ] )
    }
    
    
    void testMaxSizeValidationFailures() {
        def college = new College( code: "TTXX", description: "TTXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", 
                                   addressStreetLine1: "TTXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
                                   addressStreetLine2: "TTXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
                                   addressStreetLine3: "TTXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
                                   addressCity: "TTXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
		                           addressState: "TTX", addressCountry: "TTXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", addressZipCode: "TTXXXXXXXXXXXXXXXXX",
		                           systemRequiredIndicator: "N", statisticsCanadianInstitution: "TTXXXXX",
		                           districtDivision: "TTXX", houseNumber: "TTXXXXXXXXX",
		                           addressStreetLine4: "TTXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX" )
		                           
		assertFalse "College should have failed validation", college.validate()
		assertErrorsFor( college, 'maxSize', [ 'code', 'description', 'addressStreetLine1', 'addressStreetLine2', 'addressStreetLine3',
                                               'addressStreetLine4', 'addressCity', 'addressState', 'addressCountry', 'addressZipCode',
                                               'statisticsCanadianInstitution', 'districtDivision', 'houseNumber' ] )
    }
	
	
	void testValidationMessages() {
	    def college = newCollege()
	    college.code = null
	    assertFalse college.validate()

	    assertLocalizedError( college, 'nullable', /.*Field.*code.*of class.*College.*cannot be null.*/, 'code' ) 
	}


    private def newCollege() {
        new College( code: "TT", description: "TT", addressStreetLine1: "TT", addressStreetLine2: "TT", addressStreetLine3: "TT", addressCity: "TT",
		             addressState: "TT", addressCountry: "TT", addressZipCode: "TT", systemRequiredIndicator: "N", voiceResponseMessageNumber: 1, statisticsCanadianInstitution: "TT",
		             districtDivision: "TT", houseNumber: "TT", addressStreetLine4: "TT",
		             lastModified: new Date(), lastModifiedBy: 'horizon', dataOrigin: 'horizon' ) // audit trail normally set in service 
    }
}
