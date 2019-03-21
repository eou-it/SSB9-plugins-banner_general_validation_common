/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system
import org.junit.Before
import org.junit.Test
import org.junit.After

import net.hedtech.banner.testing.BaseIntegrationTestCase
import groovy.sql.Sql
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

class LearnerFieldStudyIntegrationTests extends BaseIntegrationTestCase {

    //Test data for creating new domain instance
    //Valid test data (For success tests)

    def i_success_code = "TTTTT"
    def i_success_description = "TTTTT"
    def i_success_systemRequiredIndicator = true

    // No Invalid Scenario required


    @Before
    public void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }

    @After
    public void tearDown() {
        super.tearDown()
    }

    @Test
    void testCreateValidLearnerFieldStudy() {
        def learnerFieldStudy = newValidForCreateLearnerFieldStudy()
        save learnerFieldStudy
        //Test if the generated entity now has an id assigned
        assertNotNull learnerFieldStudy.id
    }

    @Test
    void testUpdateValidLearnerFieldStudy() {
        def learnerFieldStudy = newValidForCreateLearnerFieldStudy()
        save learnerFieldStudy
        assertNotNull learnerFieldStudy.id
        assertEquals 0L, learnerFieldStudy.version
        assertEquals i_success_code, learnerFieldStudy.code
        assertEquals i_success_description, learnerFieldStudy.description
        assertEquals i_success_systemRequiredIndicator, learnerFieldStudy.systemRequiredIndicator

        //Update the entity
        learnerFieldStudy.description = "Updated Description"
        learnerFieldStudy.systemRequiredIndicator = false
        save learnerFieldStudy
        //Asset for sucessful update
        learnerFieldStudy = LearnerFieldStudy.get(learnerFieldStudy.id)
        assertEquals 1L, learnerFieldStudy?.version
        assertEquals "Updated Description", learnerFieldStudy.description
        assertEquals false, learnerFieldStudy.systemRequiredIndicator
    }

    @Test
    void testOptimisticLock() {
        def learnerFieldStudy = newValidForCreateLearnerFieldStudy()
        save learnerFieldStudy

        def sql
        try {
            sql = new Sql(sessionFactory.getCurrentSession().connection())
            sql.executeUpdate("update GTVLFST set GTVLFST_VERSION = 999 where GTVLFST_SURROGATE_ID = ?", [learnerFieldStudy.id])
        } finally {
            sql?.close() // note that the test will close the connection, since it's our current session's connection
        }
        //Try to update the entity
        //Update the entity
        learnerFieldStudy.description = "updated description"
        learnerFieldStudy.systemRequiredIndicator = true
        shouldFail(HibernateOptimisticLockingFailureException) {
            learnerFieldStudy.save(flush: true, failOnError: true)
        }
    }

    @Test
    void testDeleteLearnerFieldStudy() {
        def learnerFieldStudy = newValidForCreateLearnerFieldStudy()
        save learnerFieldStudy
        def id = learnerFieldStudy.id
        assertNotNull id
        learnerFieldStudy.delete()
        assertNull LearnerFieldStudy.get(id)
    }

    @Test
    void testValidation() {
        def learnerFieldStudy = newValidForCreateLearnerFieldStudy()
        assertTrue "LearnerFieldStudy could not be validated as expected due to ${learnerFieldStudy.errors}", learnerFieldStudy.validate()
    }

    @Test
    void testNullValidationFailure() {
        def learnerFieldStudy = new LearnerFieldStudy()
        assertFalse "LearnerFieldStudy should have failed validation", learnerFieldStudy.validate()
        assertErrorsFor learnerFieldStudy, 'nullable',
                [
                        'code',
                        'description',
                        'systemRequiredIndicator'
                ]
    }



    private def newValidForCreateLearnerFieldStudy() {
        def learnerFieldStudy = new LearnerFieldStudy(
                code: i_success_code,
                description: i_success_description,
                systemRequiredIndicator: i_success_systemRequiredIndicator,
                lastModified: new Date(),
                lastModifiedBy: "test",
                dataOrigin: "Banner"
        )
        return learnerFieldStudy
    }

}
