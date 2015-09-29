/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

package net.hedtech.banner.general.system.ldm.v4

/**
 * <p>Enum for PersonEmail Type </p>
 */
public enum PersonEmailType {

    PERSONAL('personal'),
    BUSINESS('business'),
    SCHOOL('school'),
    OTHERS('other'),
    PARENT('parent'),
    FAMILY('family')

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