/** *****************************************************************************
 Copyright 2009-2016 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import groovy.sql.Sql
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.overall.ldm.GlobalUniqueIdentifier
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException

class NameTypeIntegrationTests extends BaseIntegrationTestCase {

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
    void testCreateNameType() {
        def nameType = newNameType()
        save nameType
        //Test if the generated entity now has an id assigned
        assertNotNull nameType.id
    }


    @Test
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


    @Test
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
            nameType.save(flush: true, failOnError: true)
        }
    }


    @Test
    void testDeleteNameType() {
        def nameType = newNameType()
        save nameType
        def id = nameType.id
        assertNotNull id
        nameType.delete()
        assertNull NameType.get(id)
    }


    @Test
    void testValidation() {
        def nameType = newNameType()
        assertTrue "NameType could not be validated as expected due to ${nameType.errors}", nameType.validate()
    }


    @Test
    void testNullValidationFailure() {
        def nameType = new NameType()
        assertFalse "NameType should have failed validation", nameType.validate()
        assertErrorsFor nameType, 'nullable',
                [
                        'code',
                        'description'
                ]
    }


    @Test
    void testFetchAll() {
        def nameList = NameType.fetchAll([max: '500', offset: '0'])
        assertFalse nameList.isEmpty()
        assertEquals NameType.list([:]).size(), nameList.size()
    }


    @Test
    void testFetchByGuid() {
        NameType nameType = newNameType()
        save nameType
        assertNotNull nameType.id
        def guid = GlobalUniqueIdentifier.fetchByLdmNameAndDomainId(GeneralValidationCommonConstants.PERSON_NAME_TYPES_LDM_NAME, nameType.id)?.guid
        assertNotNull guid
        def list = NameType.fetchByGuid(guid)
        assertEquals guid, list.getAt(0)
        assertEquals nameType.code, list.getAt(1)
        assertEquals nameType.description, list.getAt(2)
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

}
