/** *******************************************************************************
 Copyright 2015-2016 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm.v6

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import net.hedtech.banner.general.common.GeneralValidationCommonConstants

/**
 *  decorator for v6 ethnicity
 */
@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true, includeFields = true)
class EthnicityDecorator {

    String id
    String title
    String category

    EthnicityDecorator(String id, String title,String category) {
        this.id = id
        this.title = title
        this.category = category
}

    List<ReportingDecorator> getReporting(){
        return  [new ReportingDecorator(GeneralValidationCommonConstants.ETHNICITIES, category)]

    }

    Map getEthnicGroup(){
        return [id:id]
    }

}
