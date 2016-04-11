/** *******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm

import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before

/**
 * Integration test cases for Geographic Area Composite Service
 */
class GeographicAreaCompositeServiceIntegrationTests extends BaseIntegrationTestCase{

    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
    }

    @After
    public void tearDown() {
        super.tearDown()
    }


}
