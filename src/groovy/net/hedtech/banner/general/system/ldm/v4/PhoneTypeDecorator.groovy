/*******************************************************************************
 Copyright 2015-2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm.v4

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import net.hedtech.banner.general.system.PhoneType

/**
 * Decorator for "phone-types" API
 */
@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true, includeFields = true)
class PhoneTypeDecorator {
    @Delegate
    private final PhoneType phoneType

    PhoneTypeDecorator(PhoneType phoneType){
        this.phoneType = phoneType
    }

}
