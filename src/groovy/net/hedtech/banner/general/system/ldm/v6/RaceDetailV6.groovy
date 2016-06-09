/*********************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.system.ldm.v6

import groovy.transform.InheritConstructors
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.system.Race
import net.hedtech.banner.general.system.ldm.v1.Metadata
import net.hedtech.banner.general.system.ldm.v1.RaceDetail

/**
 * LDM decorator for races resource (/base/domain/races/races.json-schemaV6)
 */
@InheritConstructors
class RaceDetailV6 extends RaceDetail {


      List<ReportingDecorator> getReporting(){
        if(parentCategory) {
            return  [new ReportingDecorator(GeneralValidationCommonConstants.RACE_LDM_NAME, parentCategory)]
        }
    }


    Map getRaces(){
        return [id:guid]
    }
}
