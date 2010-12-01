/** *****************************************************************************
 © 2010 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 ****************************************************************************** */

package com.sungardhe.banner.general.validation.common

import com.sungardhe.banner.testing.BaseIntegrationTestCase
import com.sungardhe.banner.general.validation.common.NameType

import grails.test.GrailsUnitTestCase
import groovy.sql.Sql
import org.hibernate.annotations.OptimisticLock
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
            nameType.save(flush: true)
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


    void testValidationMessages() {
        def nameType = newNameType()
        nameType.code = null
        assertFalse nameType.validate()
        assertLocalizedError nameType, 'nullable', /.*Field.*code.*of class.*NameType.*cannot be null.*/, 'code'
        nameType.description = null
        assertFalse nameType.validate()
        assertLocalizedError nameType, 'nullable', /.*Field.*description.*of class.*NameType.*cannot be null.*/, 'description'
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
