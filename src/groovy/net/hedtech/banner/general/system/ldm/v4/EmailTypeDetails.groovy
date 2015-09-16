/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

package net.hedtech.banner.general.system.ldm.v4

import net.hedtech.banner.general.system.EmailTypesView

/**
 * <p> Decorator for EmailTypeCompositeSevice</p>
 */
class EmailTypeDetails {

    def types = [:]

    @Delegate
    EmailTypesView emailTypesView

    EmailTypeDetails(types ,EmailTypesView emailTypesView) {
        this.types = types
        this.emailTypesView=emailTypesView
    }


    @Override
    public String toString() {
        return "EmailTypeDetails{" +
                ", types=" + types +
                '}';
    }


}
