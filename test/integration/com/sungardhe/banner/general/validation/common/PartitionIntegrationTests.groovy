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

import org.codehaus.groovy.grails.commons.ConfigurationHolder


class PartitionIntegrationTests extends BaseIntegrationTestCase {
    
    protected void setUp() {
        formContext = [ 'GTVPARS' ] // Since we are not testing a controller, we need to explicitly set this
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
        assertNull Partition.get( id )
    }
    
    void testUpdate() {
        def pCode = savePartitionCode()
        pCode.description = "Updated by Dan"
        save pCode
        def updatedpCode = Partition.get( pCode.id )
        assertEquals new Long ( 1 ), updatedpCode.version
        assertEquals "Updated by Dan", updatedpCode.description
    }
    
    void testFindAllByCode() {
        def pCode = savePartitionCode()
        def pCodes = Partition.findAllByCode( "1111")
        assertTrue pCodes.size > 0
    }
    
    void testFindAllByDescription() {
    	def pCode = savePartitionCode()
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
        def partition = new Partition(code: "1111", description: "unit-test", schedulerNumber: 1,
                lastModifiedBy: "test", lastModified: new Date(), campusCode: "any", dataOrigin: "Horizon")
    }
    
    private Partition savePartitionCode() {
    	save newPartition()
    }
}
