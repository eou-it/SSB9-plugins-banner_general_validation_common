/*******************************************************************************
 Copyright 2016-2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

package net.hedtech.banner.general.system.ldm.v6

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * Decorator for "address-types" API
 */
@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true, includeFields = true)
class AddressTypeDecorator {

    String code
    String description
    String id
    String addressType

    AddressTypeDecorator(String code, String description, String guid, String addressType) {
        this.code = code
        this.description = description
        this.id = guid
        this.addressType = addressType
    }

    Map getDetail() {
        return [id: this.id]
    }
}
