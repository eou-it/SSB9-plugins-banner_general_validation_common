/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm.v4

import net.hedtech.banner.general.system.PhoneTypeView

/**
 * Decorator for "phone-types" API
 */
class PhoneType {
    @Delegate
    private final  PhoneTypeView phoneTypeView
    Map<String,String> type

    PhoneType(PhoneTypeView phoneTypeView,String entityType,String phoneType){
        this.phoneTypeView=phoneTypeView
        if (entityType =='organization'){
            this.type = ["organization" : ["phoneType": phoneType]]
        }
        if (entityType =='person'){
            this.type = ["person" : ["phoneType": phoneType]]
        }
    }


    @Override
    public String toString() {
        return "PhoneType{" +
                "phoneTypeView=" + phoneTypeView +
                ", type=" + type +
                '}';
    }
}
