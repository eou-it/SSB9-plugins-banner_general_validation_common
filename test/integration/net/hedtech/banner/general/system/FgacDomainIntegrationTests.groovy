/*********************************************************************************
 Copyright 2009-2011 SunGard Higher Education. All Rights Reserved.
 This copyrighted software contains confidential and proprietary information of 
 SunGard Higher Education and its subsidiaries. Any use of this software is limited 
 solely to SunGard Higher Education licensees, and is further subject to the terms 
 and conditions of one or more written license agreements between SunGard Higher 
 Education and the licensee in question. SunGard is either a registered trademark or
 trademark of SunGard Data Systems in the U.S.A. and/or other regions and/or countries.
 Banner and Luminis are either registered trademarks or trademarks of SunGard Higher 
 Education in the U.S.A. and/or other regions and/or countries.
 **********************************************************************************/

package net.hedtech.banner.general.system

import net.hedtech.banner.testing.BaseIntegrationTestCase
import groovy.sql.Sql
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

class FgacDomainIntegrationTests extends BaseIntegrationTestCase {

    def fgacDomainService


    protected void setUp() {
        formContext = ['GTVFDMN'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateFgacDomain() {
        def fgacDomain = newFgacDomain()
        save fgacDomain
        //Test if the generated entity now has an id assigned
        assertNotNull fgacDomain.id
    }


    void testUpdateFgacDomain() {
        def fgacDomain = newFgacDomain()
        save fgacDomain

        assertNotNull fgacDomain.id
        assertEquals 0L, fgacDomain.version
        assertEquals "TTTTT", fgacDomain.code
        assertEquals "TTTTT", fgacDomain.description

        //Update the entity
        def testDate = new Date()
        fgacDomain.code = "UUUUU"
        fgacDomain.description = "UUUUU"
        fgacDomain.lastModified = testDate
        fgacDomain.lastModifiedBy = "test"
        fgacDomain.dataOrigin = "Banner"
        save fgacDomain

        fgacDomain = FgacDomain.get(fgacDomain.id)
        assertEquals 1L, fgacDomain?.version
        assertEquals "UUUUU", fgacDomain.code
        assertEquals "UUUUU", fgacDomain.description
    }


    void testOptimisticLock() {
        def fgacDomain = newFgacDomain()
        save fgacDomain

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update GTVFDMN set GTVFDMN_VERSION = 999 where GTVFDMN_SURROGATE_ID = ?", [fgacDomain.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        fgacDomain.code = "UUUUU"
        fgacDomain.description = "UUUUU"
        fgacDomain.lastModified = new Date()
        fgacDomain.lastModifiedBy = "test"
        fgacDomain.dataOrigin = "Banner"
        shouldFail(HibernateOptimisticLockingFailureException) {
            fgacDomain.save(flush:true, failOnError:true)
        }
    }


    void testDeleteFgacDomain() {
        def fgacDomain = newFgacDomain()
        save fgacDomain
        def id = fgacDomain.id
        assertNotNull id
        fgacDomain.delete()
        assertNull FgacDomain.get(id)
    }


    void testValidation() {
        def fgacDomain = newFgacDomain()
        assertTrue "FgacDomain could not be validated as expected due to ${fgacDomain.errors}", fgacDomain.validate()
    }


    void testNullValidationFailure() {
        def fgacDomain = new FgacDomain()
        assertFalse "FgacDomain should have failed validation", fgacDomain.validate()
        assertErrorsFor fgacDomain, 'nullable',
                [
                        'code',
                        'description'
                ]
    }




    private def newFgacDomain() {
        def fgacDomain = new FgacDomain(
                code: "TTTTT",
                description: "TTTTT",
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return fgacDomain
    }

    /**
     * Please put all the custom tests in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(fgacdomain_custom_integration_test_methods) ENABLED START*/
    /*PROTECTED REGION END*/
}
