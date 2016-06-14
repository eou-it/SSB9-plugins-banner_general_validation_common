/*******************************************************************************
 Copyright 2015-2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.NotFoundException
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
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

    /**
     * Return Count value of Email Type which are mapped to goriccr
     * @return Long
     */
    public Long countAll() {
        return EmailType.countAll()
    }

    /**
     * fetching EmailType data
     * @param Map
     * @return List
     */
    public List fetchAll(Map params) {
        if (params) {
            return EmailType.fetchAll(params)
        } else {
            return EmailType.fetchAll()
        }

    }

    /**
     * fetching EmailType data
     * @param Map
     * @return List
     */
    public List fetchAll() {
        return EmailType.fetchAll()
    }

    /**
     * Return Email Type which are mapped to goriccr
     * @return Long
     */
    public  List fetchByGuid(String guid) {
        if(!guid){
            throw new ApplicationException(GeneralValidationCommonConstants.EMAIL_TYPE, new NotFoundException())
        }
        return EmailType.fetchByGuid(guid)
    }

}
