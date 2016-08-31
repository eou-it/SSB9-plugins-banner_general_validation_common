/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.service.ServiceBase

// NOTE:
// This service is injected with create, update, and delete methods that may throw runtime exceptions (listed below).
// These exceptions must be caught and handled by the controller using this service.
//
// update and delete may throw net.hedtech.banner.exceptions.NotFoundException if the entity cannot be found in the database
// update and delete may throw org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException a runtime exception if an optimistic lock failure occurs
// create, update, and delete may throw grails.validation.ValidationException a runtime exception when there is a validation failure


class EmailTypeService extends ServiceBase {
    boolean transactional = true

    def fetchByCodeAndWebDisplayable(code) {
        def emailType = EmailType.fetchByCodeAndWebDisplayable(code)[0]
        if(emailType){
            return emailType
        }
        else{
            throw new ApplicationException(EmailType, "@@r1:invalidEmailType@@")
        }
    }

    def fetchEmailTypeList(int max = 10, int offset = 0, String searchString = '') {
        def criteria = EmailType.createCriteria()
        def emailTypeList = criteria.list(max: max, offset: offset, sort: 'description', order: 'asc') {
            and {
                eq("displayWebIndicator", true)
                ilike("description", "%${searchString}%")
            }
        }

        emailTypeList
    }
}
