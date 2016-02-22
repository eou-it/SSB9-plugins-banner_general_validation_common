/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

package net.hedtech.banner.general.system.ldm.v4

import net.hedtech.banner.general.common.GeneralValidationCommonConstants

/**
 * <p>Enum for PersonEmail Type </p>
 */
public enum PersonEmailType {

    PERSONAL(GeneralValidationCommonConstants.PERSONAL),
    BUSINESS(GeneralValidationCommonConstants.BUSINESS),
    SCHOOL(GeneralValidationCommonConstants.SCHOOL),
    OTHERS(GeneralValidationCommonConstants.OTHERS),
    PARENT(GeneralValidationCommonConstants.PARENT),
    FAMILY(GeneralValidationCommonConstants.FAMILY)

    def value

    PersonEmailType(String value) {
        this.value = value
    }

    @Override
    public String toString() {
        return "PersonEmailType{" +
                "value=" + value +
                '}';
    }
}