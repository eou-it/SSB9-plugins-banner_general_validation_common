/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.service.ServiceBase

/**
 * A transactional service supporting persistence of the TelephoneTypeService model.
 **/
class TelephoneTypeService extends ServiceBase {
    boolean transactional = true

    /**
     * fetching TelephoneType details based on code
     * @param code
     * @return
     */
      TelephoneType fetchByCode(String code){
        return TelephoneType.fetchByCode(code)
    }

    /**
     * Fetch TelephoneType details for updatable UI select list (read a Select2 widget).
     * @param max
     * @param offset
     * @param searchString
     * @return List of TelephoneType objects
     */
    def fetchUpdateableTelephoneTypeList(int max = 10, int offset = 0, String searchString = '') {
        def criteria = TelephoneType.createCriteria()
        def telephoneTypeList = criteria.list(max: max, offset: offset, sort: 'description', order: 'asc') {
            and {
                ilike("description", "%${searchString}%")
            }
        }

        telephoneTypeList
    }
}
