/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system
import org.junit.Before
import org.junit.Test
import org.junit.After

import net.hedtech.banner.testing.BaseIntegrationTestCase
import static groovy.test.GroovyAssert.*
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback

@Integration
@Rollback
class PartitionIntegrationTests extends BaseIntegrationTestCase {

    @Before
    public void setUp() {
        formContext = ['GUAGMNU'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    @Test
    void testCreate() {
        def partition = newPartition()
        partition.save()
        assertNotNull partition.id
    }


    @Test
    void testList() {
        def pCodes = Partition.list()
        assertTrue pCodes.size() > 0
    }


    @Test
    void testDelete() {
        def pCode = savePartitionCode()
        def id = pCode.id
        pCode.delete()
        assertNull Partition.get(id)
    }


    @Test
    void testUpdate() {
        def pCode = savePartitionCode()
        pCode.description = "Updated by Dan"
        save pCode
        def updatedpCode = Partition.get(pCode.id)
        assertEquals new Long(1), updatedpCode.version
        assertEquals "Updated by Dan", updatedpCode.description
    }


    @Test
    void testFindAllByCode() {
        savePartitionCode()
        def pCodes = Partition.findAllByCode("1111")
        assertTrue pCodes.size() > 0
    }


    @Test
    void testFindAllByDescription() {
        savePartitionCode()
        def pCodes = Partition.findAllByDescription("unit-test")
        assertTrue pCodes.size() > 0
    }


    @Test
    void testValidationFail() {
        def partition = new Partition(code: "exceeds_length", description: "unit-test", schedulerNumber: 1,
                lastModifiedBy: "test", lastModified: new Date(), dataOrigin: "Horizon")
        assertFalse "Partition information should have failed validation since code exceeds constraint length", partition.validate()
    }


    @Test
    void testValidationSuccess() {
        def partition = new Partition(code: "1111", description: "unit-test", schedulerNumber: 1,
                lastModifiedBy: "test", lastModified: new Date(), dataOrigin: "Horizon")
        assertTrue "Partition validation failed due to ${partition.errors}", partition.validate()
    }


    private Partition newPartition() {
        new Partition(code: "1111", description: "unit-test", schedulerNumber: 1,
                lastModifiedBy: "test", lastModified: new Date(), dataOrigin: "Horizon")
    }


    private Partition savePartitionCode() {
        save newPartition()
    }
}
