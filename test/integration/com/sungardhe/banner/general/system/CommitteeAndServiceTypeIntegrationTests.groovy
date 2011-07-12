/** *****************************************************************************
 © 2011 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 ****************************************************************************** */
package com.sungardhe.banner.general.system

import com.sungardhe.banner.testing.BaseIntegrationTestCase
import groovy.sql.Sql
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

class CommitteeAndServiceTypeIntegrationTests extends BaseIntegrationTestCase {

    def committeeAndServiceTypeService


    protected void setUp() {
        formContext = ['STVCOMT'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    protected void tearDown() {
        super.tearDown()
    }


    void testCreateCommitteeAndServiceType() {
        def committeeAndServiceType = newCommitteeAndServiceType()
        save committeeAndServiceType
        //Test if the generated entity now has an id assigned
        assertNotNull committeeAndServiceType.id
    }


    void testUpdateCommitteeAndServiceType() {
        def committeeAndServiceType = newCommitteeAndServiceType()
        save committeeAndServiceType

        assertNotNull committeeAndServiceType.id
        groovy.util.GroovyTestCase.assertEquals 0L, committeeAndServiceType.version
        assertEquals "TTTTT", committeeAndServiceType.code
        assertEquals "TTTTT", committeeAndServiceType.description
        assertEquals "T", committeeAndServiceType.transactionPrint

        //Update the entity
        committeeAndServiceType.code = "UUUUU"
        committeeAndServiceType.description = "UUUUU"
        committeeAndServiceType.transactionPrint = "U"
        committeeAndServiceType.lastModified = new Date()
        committeeAndServiceType.lastModifiedBy = "test"
        committeeAndServiceType.dataOrigin = "Banner"
        save committeeAndServiceType

        committeeAndServiceType = CommitteeAndServiceType.get(committeeAndServiceType.id)
        groovy.util.GroovyTestCase.assertEquals 1L, committeeAndServiceType?.version
        assertEquals "UUUUU", committeeAndServiceType.code
        assertEquals "UUUUU", committeeAndServiceType.description
        assertEquals "U", committeeAndServiceType.transactionPrint

    }


    void testOptimisticLock() {
        def committeeAndServiceType = newCommitteeAndServiceType()
        save committeeAndServiceType

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update STVCOMT set STVCOMT_VERSION = 999 where STVCOMT_SURROGATE_ID = ?", [committeeAndServiceType.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        committeeAndServiceType.code = "UUUUU"
        committeeAndServiceType.description = "UUUUU"
        committeeAndServiceType.transactionPrint = "U"
        committeeAndServiceType.lastModified = new Date()
        committeeAndServiceType.lastModifiedBy = "test"
        committeeAndServiceType.dataOrigin = "Banner"
        shouldFail(HibernateOptimisticLockingFailureException) {
            committeeAndServiceType.save(flush:true, failOnError:true)
        }
    }


    void testDeleteCommitteeAndServiceType() {
        def committeeAndServiceType = newCommitteeAndServiceType()
        save committeeAndServiceType
        def id = committeeAndServiceType.id
        assertNotNull id
        committeeAndServiceType.delete()
        assertNull CommitteeAndServiceType.get(id)
    }


    void testValidation() {
        def committeeAndServiceType = newCommitteeAndServiceType()
        assertTrue "CommitteeAndServiceType could not be validated as expected due to ${committeeAndServiceType.errors}", committeeAndServiceType.validate()
    }


    void testNullValidationFailure() {
        def committeeAndServiceType = new CommitteeAndServiceType()
        assertFalse "CommitteeAndServiceType should have failed validation", committeeAndServiceType.validate()
        assertErrorsFor committeeAndServiceType, 'nullable', ['code', 'description']
    }


    void testMaxSizeValidationFailures() {
        def committeeAndServiceType = new CommitteeAndServiceType(
                code: 'XXXXXXXX',
                description: 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX')
        assertFalse "CommitteeAndServiceType should have failed validation", committeeAndServiceType.validate()
        assertErrorsFor committeeAndServiceType, 'maxSize', ['code', 'description']
    }


    void testValidationMessages() {
        def committeeAndServiceType = newCommitteeAndServiceType()

        committeeAndServiceType.code = null
        assertFalse committeeAndServiceType.validate()
        assertLocalizedError committeeAndServiceType, 'nullable', /.*Field.*code.*of class.*CommitteeAndServiceType.*cannot be null.*/, 'code'

    }


    private def newCommitteeAndServiceType() {
        new CommitteeAndServiceType(code: "TTTTT", description: "TTTTT", transactionPrint: "T", lastModified: new Date(),
                lastModifiedBy: "test", dataOrigin: "Banner")
    }

    /**
     * Please put all the custom tests in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(committeeandservicetype_custom_integration_test_methods) ENABLED START*/
    /*PROTECTED REGION END*/
}
