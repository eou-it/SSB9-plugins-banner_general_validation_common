/** *****************************************************************************
 Copyright 2020 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import net.hedtech.banner.testing.BaseIntegrationTestCase
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import org.junit.After
import org.junit.Before
import org.junit.Test

@Integration
@Rollback
class CommentTypeValidationIntegrationTests extends BaseIntegrationTestCase{

    def commentTypeValidation

    @Before
    public  void setUp() {
        formContext = ['GUAGMNU']
        commentTypeValidation = new CommentTypeValidation()
        super.setUp()
    }

    @After
    public  void tearDown() {
        super.tearDown()
    }

    /*
    * This test will fetch the comment types from the CommentTypeValidation
    * */
    @Test
    void testFetchCommentTypes() {

        String code = '100'

        def result = commentTypeValidation.fetchByCode(code)
        assertNotNull(result)

        assertEquals('100', result.code)
        assertEquals('General Comment', result.description)
    }

    /*
    * This test will return null as the param sent is invalid
    * */

    @Test
    void testFetchCommentTypes_WhenInvalidParametersAreSent() {
        String code = 'ABC'
        def result = commentTypeValidation.fetchByCode(code)
        assertNull(result)
    }

    /*
    * This test will return null as there are no params sent to the service
    * */

    @Test
    void testFetchCommentTypes_WhenParametersAreNotSent() {
        String code
        def result = commentTypeValidation.fetchByCode(code)
        assertNull(result)
    }

}


