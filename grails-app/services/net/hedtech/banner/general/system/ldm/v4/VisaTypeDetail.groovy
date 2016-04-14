package net.hedtech.banner.general.system.ldm.v4

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import net.hedtech.banner.general.system.VisaType

/**
 * Decorator for HEDM schema "visa-types"
 */
@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true, includeFields = true)
class VisaTypeDetail {

    @Delegate
    private final VisaType visaType
    String category
    String guid

    VisaTypeDetail(VisaType visaType,String category, String guid) {
        this.visaType = visaType
        this.category = category
        this.guid = guid
    }

}


