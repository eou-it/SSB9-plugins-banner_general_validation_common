/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

package net.hedtech.banner.general.system.ldm.v4

import net.hedtech.banner.general.system.EmailTypesView
import net.hedtech.banner.general.system.ldm.v1.Metadata

/**
 * <p> Decorator for EmailTypeCompositeSevice</p>
 */
class EmailTypeDetails {

    Metadata metadata
    def types = [:]

    @Delegate
    EmailTypesView emailTypesView

    EmailTypeDetails(Metadata metadata, types ,EmailTypesView emailTypesView) {
        this.metadata = metadata
        this.types = types
        this.emailTypesView=emailTypesView
    }


    @Override
    public String toString() {
        return "EmailTypeDetails{" +
                "metadata=" + metadata +
                ", types=" + types +
                '}';
    }


}
