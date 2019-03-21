/** *****************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import net.hedtech.banner.service.ServiceBase
import grails.gorm.transactions.Transactional
/**
 * A transactional service supporting persistence of the DurationUnit domain.
 */
@Transactional
class DurationUnitService extends ServiceBase {


    List<DurationUnit> fetchAllByDurationUnitCodes( List<String> durationUnitCodes ) {
        List<DurationUnit> durationUnits = []
        durationUnits = DurationUnit.fetchAllByDurationUnitCodes( durationUnitCodes )
        return durationUnits
    }
}
