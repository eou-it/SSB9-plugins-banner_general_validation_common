/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system

import net.hedtech.banner.service.ServiceBase

/**
 * A transactional service supporting persistence of the Academic honor model.
 * */
class AcademicHonorService extends ServiceBase{

    boolean transactional = true


    /**
     * get count of Academic Honors
     * @return count
     */
    Integer  countRecord() {
        return AcademicHonorView.countRecord()
    }


    /**
     * get count of Academic honors data based on filter data
     * @param typeValue
     * @return count
     */
    Integer  countRecordWithFilter(typeValue) {
        return AcademicHonorView.countRecordWithFilter(typeValue)
    }

    /**
     * fetch Academic honors data
     * @param typeValue
     * @param params
     * @return
     */
    List<AcademicHonorView>  fetchByType(typeValue,params) {
        return AcademicHonorView.fetchByType(typeValue, params)

    }

    /**
     * fetch Academic honors list
     * @param params
     * @return List
     * */
    List<AcademicHonorView> fetchAll(params){
        return AcademicHonorView.fetchAll(params)
    }


    /**
     * fetch Academic honors data
     * @param params
     * @return List
     * */
    AcademicHonorView fetchByGuid(guid){
        return AcademicHonorView.fetchByGuid(guid)
    }

}


