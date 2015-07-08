/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm.v4

import net.hedtech.banner.general.system.PhoneTypeView
import net.hedtech.banner.general.system.ldm.v1.Metadata

/**
 * Decorator for "phone-types" API
 */
class PhoneType {
    @Delegate
    private final  PhoneTypeView phoneTypeView
    Metadata metadata
    Map<String,String> type
    List<Map<String, String>> title
    List<Map<String, String>> descriptionList=null //as of now it does not have any DB mapping, so that it has to be set to null

    PhoneType(PhoneTypeView phoneTypeView,Metadata metadata,String entityType,String phoneType){
        this.metadata=metadata
        this.phoneTypeView=phoneTypeView
        if(phoneTypeView.description){
            title = []
            this.title <<["en": phoneTypeView.description]
        }
        this.type=[entityType:entityType,phoneType:phoneType]
    }


    @Override
    public String toString() {
        return "PhoneType{" +
                "phoneTypeView=" + phoneTypeView +
                ", metadata=" + metadata +
                ", type=" + type +
                ", title=" + title +
                ", descriptionList=" + descriptionList +
                '}';
    }
}
