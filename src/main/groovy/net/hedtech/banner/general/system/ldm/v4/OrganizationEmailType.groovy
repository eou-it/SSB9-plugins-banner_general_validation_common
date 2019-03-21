/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm.v4

import net.hedtech.banner.general.common.GeneralValidationCommonConstants

/**
 * <p> Enum for OrganizationEmail </p>
 * */
public enum OrganizationEmailType {

    SALES(GeneralValidationCommonConstants.SALES),
    SUPPORT(GeneralValidationCommonConstants.SUPPORT),
    GENERAL(GeneralValidationCommonConstants.GENERAL),
    BILLING(GeneralValidationCommonConstants.BILLING),
    LEGAL(GeneralValidationCommonConstants.LEGAL),
    HR(GeneralValidationCommonConstants.HR),
    MEDIA(GeneralValidationCommonConstants.MEDIA),
    OTHERS(GeneralValidationCommonConstants.OTHERS),
    MATCHINGGIFTS(GeneralValidationCommonConstants.MATCHINGGIFTS)

    def value

    OrganizationEmailType(String value) {
        this.value = value
    }


    @Override
    public String toString() {
        return "OrganizationEmailType{" +
                "value=" + value +
                '}';
    }
}