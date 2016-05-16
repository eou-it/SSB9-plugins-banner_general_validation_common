/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system

import net.hedtech.banner.service.ServiceBase

/**
 * A transactional service supporting persistence of the Name Type model.
 * Providing restriction on create , update and delete on Name Type model.
 * */
class NameTypeService extends ServiceBase {
    boolean transactional = true

    /**
     * fetch list of Name types
     * @param params
     * @return
     */
    List fetchAll(Map params) {
        return NameType.fetchAll(params)
    }

    /**
     * fetch Namme type data bases on guid
     * @param guid
     * @return
     */
    def fetchByGuid(String guid) {
        return NameType.fetchByGuid(guid)
    }

}
