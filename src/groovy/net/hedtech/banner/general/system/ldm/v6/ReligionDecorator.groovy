/** *******************************************************************************
 Copyright 2016-2017 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm.v6

import net.hedtech.banner.general.system.Ethnicity
import net.hedtech.banner.general.system.ldm.v1.Metadata

/**
 *  decorator for religions resource
 */
class ReligionDecorator {

    String id;
    String code;
    String description;


    ReligionDecorator(String id, String code, String description) {
        this.id = id
        this.code = code
        this.description = description
    }


}
