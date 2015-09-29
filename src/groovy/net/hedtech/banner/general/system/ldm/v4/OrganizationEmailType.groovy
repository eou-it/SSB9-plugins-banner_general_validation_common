/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system.ldm.v4

/**
 * <p> Enum for OrganizationEmail </p>
 * */
public enum OrganizationEmailType {

    SALES('sales'),
    SUPPORT('support'),
    GENERAL('general'),
    BILLING('billing'),
    LEGAL('legal'),
    HR('hr'),
    MEDIA('media'),
    OTHERS('other'),
    MATCHINGGIFTS('matchingGifts')

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