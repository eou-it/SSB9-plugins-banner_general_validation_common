
/*******************************************************************************
 © 2010 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 *******************************************************************************/
package com.sungardhe.banner.general.system

import com.sungardhe.banner.testing.BaseIntegrationTestCase
import groovy.sql.Sql

import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException
import com.sungardhe.banner.general.system.DegreeLevel

class DegreeLevelIntegrationTests extends BaseIntegrationTestCase {

	def degreeLevelService
	
	protected void setUp() {
		formContext = ['STVDLEV'] // Since we are not testing a controller, we need to explicitly set this
		super.setUp()
	}

	protected void tearDown() {
		super.tearDown()
	}

	void testCreateDegreeLevel() {
		def degreeLevel = newDegreeLevel()
		save degreeLevel
		//Test if the generated entity now has an id assigned		
        assertNotNull degreeLevel.id
	}

	void testUpdateDegreeLevel() {
		def degreeLevel = newDegreeLevel()
		save degreeLevel
       
        assertNotNull degreeLevel.id
        groovy.util.GroovyTestCase.assertEquals( 0L, degreeLevel.version )
        assertEquals("TT", degreeLevel.code )
        assertEquals("TTTTT", degreeLevel.description )
        assertEquals(2, degreeLevel.numericValue )
        

		//Update the entity
		degreeLevel.code="UU"
		degreeLevel.description="UUUUU"
		degreeLevel.numericValue=1
		degreeLevel.lastModified= new Date()
		degreeLevel.lastModifiedBy="test"
		degreeLevel.dataOrigin= "Banner" 

        save degreeLevel
        
        degreeLevel = DegreeLevel.get( degreeLevel.id )
        groovy.util.GroovyTestCase.assertEquals new Long( 1 ), degreeLevel?.version
        assertEquals ("UU", degreeLevel.code)
		assertEquals ("UUUUU", degreeLevel.description)
		assertEquals (1, degreeLevel.numericValue)
		
	}

    void testOptimisticLock() { 
		def degreeLevel = newDegreeLevel()
		save degreeLevel
        
        def sql
        try {
            sql = new Sql( sessionFactory.getCurrentSession().connection() )
            sql.executeUpdate( "update STVDLEV set STVDLEV_VERSION = 999 where STVDLEV_SURROGATE_ID = ?", [ degreeLevel.id ] )
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
		//Try to update the entity
		degreeLevel.code="UU"
		degreeLevel.description="UUUUU"
		degreeLevel.numericValue=2
		degreeLevel.lastModified= new Date()
		degreeLevel.lastModifiedBy="test"
		degreeLevel.dataOrigin= "Banner" 
        shouldFail( HibernateOptimisticLockingFailureException ) {
            degreeLevel.save( flush: true )
        }
    }
	
	void testDeleteDegreeLevel() {
		def degreeLevel = newDegreeLevel()
		save degreeLevel
		def id = degreeLevel.id
		assertNotNull id
		degreeLevel.delete()
		assertNull DegreeLevel.get( id )
	}
	
    void testValidation() {
       def degreeLevel = newDegreeLevel()
       assertTrue "DegreeLevel could not be validated as expected due to ${degreeLevel.errors}", degreeLevel.validate()
    }

    void testNullValidationFailure() {
        def degreeLevel = new DegreeLevel()
        assertFalse "DegreeLevel should have failed validation", degreeLevel.validate()
        assertNoErrorsFor( degreeLevel,  [ 'numericValue' ] )
        assertErrorsFor( degreeLevel, 'nullable', [ 'code', 'description'  ] )
    }
    
    void testMaxSizeValidationFailures() {
        def degreeLevel = new DegreeLevel( 
        code:'XXXX',
        description:'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX' ) 
		assertFalse "DegreeLevel should have failed validation", degreeLevel.validate()
		assertErrorsFor( degreeLevel, 'maxSize', [ 'code', 'description' ] )    
    }
    
	void testValidationMessages() {
	    def degreeLevel = newDegreeLevel()
	    
	    degreeLevel.code = null
	    assertFalse degreeLevel.validate()
	    assertLocalizedError( degreeLevel, 'nullable', /.*Field.*code.*of class.*DegreeLevel.*cannot be null.*/, 'code' )
	    
	}


    private def newDegreeLevel() {
        new DegreeLevel(code: "TT", description: "TTTTT", numericValue: 2 , lastModified: new Date(),
			lastModifiedBy: "test", dataOrigin: "Banner" ) 
    }
    
    /**
     * Please put all the custom tests in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(degreelevel_custom_integration_test_methods) ENABLED START*/
    /*PROTECTED REGION END*/
}
