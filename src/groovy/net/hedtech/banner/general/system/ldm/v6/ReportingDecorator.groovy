/** *******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 ********************************************************************************* */
package net.hedtech.banner.general.system.ldm.v6

import net.hedtech.banner.general.common.GeneralValidationCommonConstants

class ReportingDecorator {

    String entityType
    String category

    ReportingDecorator(String entityType, String category) {
        this.entityType = entityType
        this.category = category
    }

    Map<String,String> getCountry() {
        if (entityType?.equalsIgnoreCase('races')) {
            return [code: GeneralValidationCommonConstants.COUNTRY_CODE, racialCategory: category]
        } else {
            return [code: GeneralValidationCommonConstants.COUNTRY_CODE, ethnicCategory: category]
        }
    }
    }
