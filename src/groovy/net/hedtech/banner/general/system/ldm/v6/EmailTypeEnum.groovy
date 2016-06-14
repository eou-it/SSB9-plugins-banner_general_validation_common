/*********************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.system.ldm.v6

/**
 * Email Type Enum
 */
enum EmailTypeEnum {

    public static
    final List<String> EMAIL_TYPE = ['personal', 'business', 'school', 'parent', 'family', 'sales', 'support', 'general', 'billing', 'legal', 'hr', 'media', 'matchingGifts', 'other']

    EmailTypeEnum(String value) { this.value = value }

    private final String value

    public String getValue() { return value }
}