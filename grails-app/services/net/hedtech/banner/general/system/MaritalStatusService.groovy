/*********************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.system

import net.hedtech.banner.service.ServiceBase

// NOTE:
// This service is injected with create, update, and delete methods that may throw runtime exceptions (listed below).
// These exceptions must be caught and handled by the controller using this service.
//
// update and delete may throw net.hedtech.banner.exceptions.NotFoundException if the entity cannot be found in the database
// update and delete may throw org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException a runtime exception if an optimistic lock failure occurs
// create, update, and delete may throw grails.validation.ValidationException a runtime exception when there is a validation failure

/**
 * A transactional service supporting persistence of the Level model.
 *
 */
class MaritalStatusService extends ServiceBase {
    public static final MARITAL_STATUS_QUERY = "from MaritalStatus r,IntegrationConfiguration i where r.code = i.value and i.settingName = :settingName and i.processCode = :processCode and i.translationValue in (:translationValueList)"


    boolean transactional = true

    /**
     * fetch marital status details which are mapped on goriccr settings
     * @param content
     * @param count
     */
    def  fetchMartialStatusDetails(def content) {
      return  MaritalStatus.fetchMartialStatusDetails(content)
    }

    /**
     * fetch marital status total count which are mapped on goriccr settings
     * @param content
     * @param count
     */
    def  fetchMartialStatusDetailsCount() {
        return  MaritalStatus.fetchMartialStatusDetailsCount()
    }

    /**
     * fetching marital status details based on code
     * @param code
     * @return
     */
    MaritalStatus fetchByCode(String code){
        return MaritalStatus.fetchByCode(code)
    }

    def fetchMaritalStatusList(int max = 10, int offset = 0, String searchString = '') {
        def criteria = MaritalStatus.createCriteria()
        def maritalStatusList = criteria.list(max: max, offset: offset, sort: 'description', order: 'asc') {
            and {
                ilike("description", "%${searchString}%")
            }
        }

        maritalStatusList
    }

}
