/*******************************************************************************
 Copyright 2015-2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm.v4

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * Decorator for "phone-types" API
 */
@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true, includeFields = true)
class PhoneTypeDecorator {

    String code
    String description
    String id
    String phoneType

    PhoneTypeDecorator(String code, String description, String guid, String hedmPhoneType) {
        this.code = code
        this.description = description
        this.id = guid
        this.phoneType = hedmPhoneType
    }

    Map getDetail() {
        return [id: this.id]
    }

}
