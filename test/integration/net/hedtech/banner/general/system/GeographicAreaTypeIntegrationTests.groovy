/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Integration test cases for Geographic area view domain.
 */
class GeographicAreaTypeIntegrationTests extends BaseIntegrationTestCase {

    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
    }


    @After
    public void tearDown() {
        super.tearDown()
    }

    @Test
    void testCountAll(){

    }

    @Test
    void testFetchByGuid(){

    }

    @Test
    void testFetchAll(){

    }

    @Test
    void testFetchAllWithPagination(){

    }


}

