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

class AwardCategoryIntegrationTests extends BaseIntegrationTestCase {

    def awardCategoryService


    protected void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateAwardCategory() {
        def awardCategory = newAwardCategory()
        save awardCategory
        //Test if the generated entity now has an id assigned
        assertNotNull awardCategory.id
    }


    void testUpdateAwardCategory() {
        def awardCategory = newAwardCategory()
        save awardCategory

        assertNotNull awardCategory.id
        groovy.util.GroovyTestCase.assertEquals(0L, awardCategory.version)
        assertEquals("TT", awardCategory.code)
        assertEquals("TTTTT", awardCategory.description)
        assertEquals("T", awardCategory.systemRequiredIndicator)

        //Update the entity
        awardCategory.code = "UU"
        awardCategory.description = "UUUUU"
        awardCategory.systemRequiredIndicator = "U"
        awardCategory.lastModified = new Date()
        awardCategory.lastModifiedBy = "test"
        awardCategory.dataOrigin = "Banner"

        save awardCategory

        awardCategory = AwardCategory.get(awardCategory.id)
        groovy.util.GroovyTestCase.assertEquals new Long(1), awardCategory?.version
        assertEquals("UU", awardCategory.code)
        assertEquals("UUUUU", awardCategory.description)
        assertEquals("U", awardCategory.systemRequiredIndicator)

    }


    void testOptimisticLock() {
        def awardCategory = newAwardCategory()
        save awardCategory

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVACAT set STVACAT_VERSION = 999 where STVACAT_SURROGATE_ID = ?", [awardCategory.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        awardCategory.code = "UU"
        awardCategory.description = "UUUUU"
        awardCategory.systemRequiredIndicator = "U"
        awardCategory.lastModified = new Date()
        awardCategory.lastModifiedBy = "test"
        awardCategory.dataOrigin = "Banner"
        shouldFail(HibernateOptimisticLockingFailureException) {
            awardCategory.save(flush:true, failOnError:true)
        }
    }


    void testDeleteAwardCategory() {
        def awardCategory = newAwardCategory()
        save awardCategory
        def id = awardCategory.id
        assertNotNull id
        awardCategory.delete()
        assertNull AwardCategory.get(id)
    }


    void testValidation() {
        def awardCategory = newAwardCategory()
        assertTrue "AwardCategory could not be validated as expected due to ${awardCategory.errors}", awardCategory.validate()
    }


    void testNullValidationFailure() {
        def awardCategory = new AwardCategory()
        assertFalse "AwardCategory should have failed validation", awardCategory.validate()
        assertErrorsFor(awardCategory, 'nullable', ['code', 'description'])
        assertNoErrorsFor(awardCategory, ['systemRequiredIndicator'])
    }


    void testMaxSizeValidationFailures() {
        def awardCategory = new AwardCategory(
                code: 'XXXX',
                description: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX')
        assertFalse "AwardCategory should have failed validation", awardCategory.validate()
        assertErrorsFor(awardCategory, 'maxSize', ['code', 'description'])
    }





    private def newAwardCategory() {
        new AwardCategory(code: "TT", description: "TTTTT", systemRequiredIndicator: "T", lastModified: new Date(),
                lastModifiedBy: "test", dataOrigin: "Banner")
    }

    /**
     * Please put all the custom tests in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(awardcategory_custom_integration_test_methods) ENABLED START*/
    /*PROTECTED REGION END*/
}
