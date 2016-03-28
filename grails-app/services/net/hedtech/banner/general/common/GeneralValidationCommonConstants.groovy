/*********************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/

package net.hedtech.banner.general.common

/**
 * Util class for General validation module where in  will be
 * This class will holds all common variables used across the banner-general-validation-common plugin.
 */

class GeneralValidationCommonConstants {

    private GeneralValidationCommonConstants() {} // prevents instantiation

    static final String NIL_GUID = "00000000-0000-0000-0000-000000000000"

    /** Common constants */
    final static String TYPE = 'type'
    final static String CODE = 'code'
    final static String ERROR_MSG_EXISTS_MESSAGE = 'exists.message'
    final static String ERROR_MSG_CODE_REQUIRED = 'code.required.message'
    final static String ERROR_MSG_DESCRIPTION_REQUIRED = 'description.required.message'
    final static String ERROR_MSG_CODE_EXISTS = 'code.exists.message'
    final static String DEFAULT_ORDER_TYPE = 'ASC'
    final static String DEFAULT_SORT_FIELD_ABBREVIATION = 'abbreviation'
    final static String DEFAULT_SORT_FIELD_CODE = 'code'
    final static String VERSION_V1 = 'v1'
    final static String VERSION_V2 = 'v2'
    final static String VERSION_V3 = 'v3'
    final static String VERSION_V4 = 'v4'
    final static String NON_HISPANIC = 'nonHispanic'
    final static String HISPANIC = 'hispanic'
    final static String ABBREVIATION = 'abbreviation'
    final static String TITLE = 'title'
    final static String RACE = 'race'
    final static String DESCRIPTION = 'description'

    /** Common constants for AcademicDiscipline*/
    final static String ACADEMIC_DISCIPLINE = 'academicDiscipline'

    //Ethnicity constants
    static final String ETHNICITY_LDM_NAME = 'ethnicities'
    static final String ETHNICITIES_US = 'ethnicities-us'
    static final String ETHNICITY = 'ethnicity'

    final static String ERROR_MSG_OPERATION_NOT_SUPPORTED = 'operation not supported'

    /** Common constants for Location Type*/
    final static String LOCATION_TYPE = 'locationType'
    final static String ORGANIZATION = 'organization'
    final static String PERSON = 'person'

    /** Common constants for EmailType*/
    final static String PERSONAL = 'personal'
    final static String BUSINESS = 'business'
    final static String SCHOOL = 'school'
    final static String OTHERS = 'other'
    final static String PARENT = 'parent'
    final static String FAMILY = 'family'
    final static String SALES = 'sales'
    final static String SUPPORT = 'support'
    final static String GENERAL = 'general'
    final static String BILLING = 'billing'
    final static String LEGAL = 'legal'
    final static String HR = 'hr'
    final static String MEDIA = 'media'
    final static String MATCHINGGIFTS = 'matchingGifts'
    final static String EMAIL_TYPE = 'emailType'

    //common constants for  MaritalStatus
    static final String PROCESS_CODE = 'HEDM'
    static final String MARITAL_STATUS_LDM_NAME = 'marital-status'
    static final String MARITAL_STATUS_PARENT_CATEGORY = "MARITALSTATUS.PARENTCATEGORY"
    static final String MARITAL_STATUS_MARTIAL_CATEGORY = "MARITALSTATUS.MARITALCATEGORY"
    static final String MARITAL_STATUS = 'maritalStatus'

    //common constants for academic credentials
    final static String ACADEMIC_CREDENTIAL = 'academicCredential'
    static final String ACADEMIC_CREDENTIAL_LDM_NAME = 'academic-credentials'
    final static String HEDM_CREDENTIAL_CATEGORY = 'HEDM_CREDENTIAL_CATEGORY'
    final static String HEDM_CREDENTIAL_DESCRIPTION = 'HEDM_CREDENTIAL_DESCRIPTION'
    final static String ERROR_MSG_TYPE_NOT_EXISTS = 'invalid.type.message'

    //common constants for academic level
    static final String ACADEMIC_LEVEL_LDM_NAME = 'academic-levels'
    final static String ACADEMIC_LEVEL = 'academicLevel'

    //common constants for Races
    static final String RACE_LDM_NAME = 'races'
    static final String RACE_PARENT_CATEGORY = "RACE.PARENTCATEGORY"
    static final String RACE_RACIAL_CATEGORY = "RACE.RACIALCATEGORY"
    static final String SETTING_NAME = 'settingName'
    static final String PROCESS_CODE_NAME = 'processCode'
    static final String TRANSLATION_VALUE = 'translationValueList'

    //common constants for Organization
    static final String COLLEGE = 'college'
    static final String DEPARTMENT = 'department'
    static final String DIVISION = 'division'
    static final String FACULTY = 'faculty'
    static final String UNIVERSITY = 'university'

    //common constants for restriction types
    static final String RESTRICTION_TYPE_LDM_NAME = 'restriction-types'
    static final String PERSONS_LDM_NAME = "persons"
    static final String PERSONS_ENDPOINT = "persons"

    //common constants for  citizenship statuses
    static final String CITIZENSHIP_STATUS = 'citizenshipStatus'
    static final String CITIZENSHIP_STATUSES_LDM_NAME = 'citizenship-statuses'
    static final String CITIZENSHIP_STATUSES_CATEGORY_CITIZEN = 'citizen'
    static final String CITIZENSHIP_STATUSES_CATEGORY_NON_CITIZEN = 'nonCitizen'

    //common constants for visa-types
    static final String IMMIGRANT = 'immigrant'
    static final String NON_IMMIGRANT = 'nonImmigrant'

}
