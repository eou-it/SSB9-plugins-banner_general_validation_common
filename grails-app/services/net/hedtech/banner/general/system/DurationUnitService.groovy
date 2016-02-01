/** *****************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import net.hedtech.banner.service.ServiceBase

/**
 * A transactional service supporting persistence of the DurationUnit domain.
 */
class DurationUnitService extends ServiceBase {

    boolean transactional = true


    List<DurationUnit> fetchAllByDurationUnitCodes( List<String> durationUnitCodes ) {
        List<DurationUnit> durationUnits = []
        durationUnits = DurationUnit.fetchAllByDurationUnitCodes( durationUnitCodes )
        return durationUnits
    }
}
