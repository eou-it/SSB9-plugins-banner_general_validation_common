/** *****************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
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
 * A transactional service supporting persistence of the Academic Year model.
 * */
class AcademicCredentialService extends ServiceBase {

    boolean transactional = true

    /**
     * fetching AcademicCredential data based on guid
     * @param guid
     * @return AcademicCredential
     */
    AcademicCredential fetchByGuid(String guid){
        return AcademicCredential.fetchByGuid(guid)
    }

    /**
     * get count of Academic Credential data based on filter data
     * @param filterData
     * @return
     */
    def  countAll(filterData) {
        return AcademicCredential.countAll(filterData)
    }

    /**
     * fetch Academic Credential data based on filter data
     * @param filterData
     * @param pagingAndSortParams
     * @return
     */
    def  fetchSearch(filterData, pagingAndSortParams) {
        return AcademicCredential.fetchSearch(filterData, pagingAndSortParams)

    }
}
