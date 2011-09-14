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

package com.sungardhe.banner.general.system

import com.sungardhe.banner.testing.BaseIntegrationTestCase
import groovy.sql.Sql
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

class NameTypeIntegrationTests extends BaseIntegrationTestCase {

    def nameTypeService


    protected void setUp() {
        formContext = ['GTVNTYP'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateNameType() {
        def nameType = newNameType()
        save nameType
        //Test if the generated entity now has an id assigned
        assertNotNull nameType.id
    }


    void testUpdateNameType() {
        def nameType = newNameType()
        save nameType

        assertNotNull nameType.id
        assertEquals 0L, nameType.version
        assertEquals "TTTT", nameType.code
        assertEquals "TTTTT", nameType.description

        //Update the entity
        def testDate = new Date()
        nameType.code = "UUUU"
        nameType.description = "UUUUU"
        nameType.lastModified = testDate
        nameType.lastModifiedBy = "test"
        nameType.dataOrigin = "Banner"
        save nameType

        nameType = NameType.get(nameType.id)
        assertEquals 1L, nameType?.version
        assertEquals "UUUU", nameType.code
        assertEquals "UUUUU", nameType.description
    }


    void testOptimisticLock() {
        def nameType = newNameType()
        save nameType

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update GTVNTYP set GTVNTYP_VERSION = 999 where GTVNTYP_SURROGATE_ID = ?", [nameType.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        nameType.code = "UUUU"
        nameType.description = "UUUUU"
        nameType.lastModified = new Date()
        nameType.lastModifiedBy = "test"
        nameType.dataOrigin = "Banner"
        shouldFail(HibernateOptimisticLockingFailureException) {
            nameType.save(flush:true, failOnError:true)
        }
    }


    void testDeleteNameType() {
        def nameType = newNameType()
        save nameType
        def id = nameType.id
        assertNotNull id
        nameType.delete()
        assertNull NameType.get(id)
    }


    void testValidation() {
        def nameType = newNameType()
        assertTrue "NameType could not be validated as expected due to ${nameType.errors}", nameType.validate()
    }


    void testNullValidationFailure() {
        def nameType = new NameType()
        assertFalse "NameType should have failed validation", nameType.validate()
        assertErrorsFor nameType, 'nullable',
                [
                        'code',
                        'description'
                ]
    }



    private def newNameType() {
        def nameType = new NameType(
                code: "TTTT",
                description: "TTTTT",
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return nameType
    }

    /**
     * Please put all the custom tests in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(nametype_custom_integration_test_methods) ENABLED START*/
    /*PROTECTED REGION END*/
}
