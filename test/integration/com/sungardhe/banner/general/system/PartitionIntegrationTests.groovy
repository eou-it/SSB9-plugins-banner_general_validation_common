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

class PartitionIntegrationTests extends BaseIntegrationTestCase {

    protected void setUp() {
        formContext = ['GTVPARS'] // Since we are not testing a controller, we need to explicitly set this
        super.setUp()
    }


    void testCreate() {
        def partition = newPartition()
        partition.save()
        assertNotNull partition.id
    }


    void testList() {
        def pCodes = Partition.list()
        assertTrue pCodes.size() > 0
    }


    void testDelete() {
        def pCode = savePartitionCode()
        def id = pCode.id
        pCode.delete()
        assertNull Partition.get(id)
    }


    void testUpdate() {
        def pCode = savePartitionCode()
        pCode.description = "Updated by Dan"
        save pCode
        def updatedpCode = Partition.get(pCode.id)
        assertEquals new Long(1), updatedpCode.version
        assertEquals "Updated by Dan", updatedpCode.description
    }


    void testFindAllByCode() {
        savePartitionCode()
        def pCodes = Partition.findAllByCode("1111")
        assertTrue pCodes.size > 0
    }


    void testFindAllByDescription() {
        savePartitionCode()
        def pCodes = Partition.findAllByDescription("unit-test")
        assertTrue pCodes.size > 0
    }


    void testValidationFail() {
        def partition = new Partition(code: "exceeds_length", description: "unit-test", schedulerNumber: 1,
                lastModifiedBy: "test", lastModified: new Date(), campusCode: "any", dataOrigin: "Horizon")
        assertFalse "Partition information should have failed validation since code exceeds constraint length", partition.validate()
    }


    void testValidationSuccess() {
        def partition = new Partition(code: "1111", description: "unit-test", schedulerNumber: 1,
                lastModifiedBy: "test", lastModified: new Date(), campusCode: "any", dataOrigin: "Horizon")
        assertTrue "Partition validation failed due to ${partition.errors}", partition.validate()
    }


    private Partition newPartition() {
        new Partition(code: "1111", description: "unit-test", schedulerNumber: 1,
                lastModifiedBy: "test", lastModified: new Date(), campusCode: "any", dataOrigin: "Horizon")
    }


    private Partition savePartitionCode() {
        save newPartition()
    }
}
