/*******************************************************************************
 Copyright 2020 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system

import grails.gorm.transactions.Transactional
import net.hedtech.banner.service.ServiceBase

/*
* This service will fetch the comment details from the CommentTypeValidation
*/
@Transactional
class CommentTypeValidationService extends ServiceBase {

    /*
    * Fetch the Comment Type details using the STVCMTT_CODE
    * */

    def fetchCommentTypes(String code) {
        def commentDetails = CommentTypeValidation.fetchByCode(code)
        commentDetails
    }

}
