/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm.v4

import net.hedtech.banner.general.system.PhoneType

/**
 * Decorator for "phone-types" API
 */
class PhoneTypeDecorator {
    @Delegate
    private final PhoneType phoneType
    Map<String,String> type

    PhoneTypeDecorator(PhoneType phoneType,Map type){
        this.phoneType = phoneType
        this.type = type
    }


    @Override
    public String toString() {
        return """PhoneTypeDecorator{ +
                phoneTypeView = $phoneType ,
                type = $type
                }"""
    }
}
