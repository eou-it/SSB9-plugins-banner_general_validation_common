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
class CommentTypeValidationServiceIntegrationTests extends BaseIntegrationTestCase {

    def commentTypeValidationService

    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
    }

    @After
    public void tearDown() {
        super.tearDown()
    }

    /*
    * This test will fetch the comment types from the commentTypeValidationService
    * */

    @Test
    void testFetchCommentTypes() {
        String code = '100'
        def commentType = commentTypeValidationService.fetchCommentTypes(code)
        assertNotNull(commentType)
        assertEquals('100', commentType.code)
        assertEquals('General Comment', commentType.description)
    }

    /*
    * This test will return null as the param sent is invalid
    * */

    @Test
    void testFetchCommentTypes_WhenInvalidParametersAreSent() {
        String code = 'ABC'
        def commentType = commentTypeValidationService.fetchCommentTypes(code)
        assertNull(commentType)
    }

    /*
    * This test will return null as there are no params sent to the service
    * */

    @Test
    void testFetchCommentTypes_WhenParametersAreNotSent() {
        String code
        def commentType = commentTypeValidationService.fetchCommentTypes(code)
        assertNull(commentType)
    }
}
