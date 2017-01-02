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
    final static String VERSION_V5 = 'v5'
    final static String VERSION_V6 = 'v6'
    final static String VERSION_V7 = 'v7'
    final static String VERSION_V8 = 'v8'
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
    static final String RACE_RACIAL_CATEGORY_V6 = "RACE.RACIALCATEGORY.V6"
    static final String SETTING_NAME = 'settingName'
    static final String PROCESS_CODE_NAME = 'processCode'
    static final String TRANSLATION_VALUE = 'translationValueList'
    static final String COUNTRY_CODE = 'USA'

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

    //common constants for visa-types
    static final String IMMIGRANT = 'immigrant'
    static final String NON_IMMIGRANT = 'nonImmigrant'
    static final String VISA_TYPES_LDM_NAME = 'visa-types'

    //common constants for geographic-areas
    static final String GEOGRAPHIC_REGION_LDM_NAME = 'geographic-regions'
    static final String GEOGRAPHIC_DIVISION_LDM_NAME = 'geographic-divisions'
    static final String GEOGRAPHIC_AREA_LDM_NAME = 'geographic-areas'
    static final String GEOGRAPHIC_DIVISION_KEY = 'gDivisonLdmName'
    static final String GEOGRAPHIC_REGION_KEY = 'gRegionLdmName'
    static final String GEOGRAPHIC_AREA_KEY = 'gAreaLdmName'
    static final String GEOGRAPHIC_AREA_GUID_KEY = 'guid'

    static final String UTC_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssXXX"
    static final String UTC_TIME_ZONE = "UTC"
    //Religions constants
    static final String DATETIME_WITHOUT_TIMEZONE = "yyyy-MM-dd HH:mm:ss"
    static final String DATE_WITHOUT_TIMEZONE = "yyyy-MM-dd"

    static final String GEOGRAPHIC_REGION_TYPE = 'Geographic Region'
    static final String GEOGRAPHIC_DIVISION_TYPE = 'Geographic Division'

    static final String PERSON_NAME_TYPES_LDM_NAME = 'person-name-types'
    static final String PERSON_NAME_TYPE_SETTING = 'PERSON.NAMES.NAMETYPE'

    //common constants for academic-honors
    public static final def ACADEMIC_HONOR_SEARCH_FIELD = 'type'
    public static final String LDM_NAME_INSTITUTIONAL = 'institutional-honors'
    public static final String LDM_NAME_DEPARTMENTAL = 'departmental-honors'
    public static final Integer MAX_UPPER_LIMIT = 500
    final static String ACADEMIC_HONOR = 'academicHonor'
    //Religions constants
    static final String RELIGION_LDM_NAME = 'religions'
    static final String RELIGION = 'religion'
    final static String ERROR_MSG_TITLE_REQUIRED = 'title.required.message'

    //Person Hold Types Constants
    static final String PERSON_HOLD_TYPES = 'personHoldTypes'
    static final String PERSON_HOLD_TYPES_ACADEMIC = "academic"
    static final String PERSON_HOLD_TYPES_FINANCE = "financial"

    //Person Hold Constants
    public static final def PERSON_HOLD_SEARCH_FIELD = 'person'
    static final String PERSON_HOLS_LDM_NAME = 'person-holds'
    static final String PERSON_HOLDS_KEY = 'gPersonHolds'
    static final String PERSONS_KEY = 'gPerson'
    static final String PERSON_HOLDS_TYPE_KEY = 'gPersonHoldsType'

    //Ethnicities V6
    static final String ETHNICITIES = 'ethnicities'

    //Email Type V6 constants
    public static final String EMAIL_TYPE_SETTING_NAME_V6 = 'EMAILS.EMAILTYPE'
    public static final String EAMIL_TYPE_LDM_NAME = 'email-types'
    public static final String EMAIL_TYPE_SETTING_NAME_V3 = 'PERSON.EMAILS.EMAILTYPE'

    //Phone Type V6 constants
    public static final String PHONE_TYPE_SETTING_NAME_V6 = 'PHONES.PHONETYPE'
    public static final String PHONE_TYPE_SETTING_NAME_V3 = 'PERSON.PHONES.PHONETYPE'
    public static final String PHONE_TYPE_LDM_NAME = 'phone-types'
    public static final String PHONE_ENTITY_TYPE = 'phoneType'
    // Interest constants
    public static final String INTEREST_LDM_NAME = 'interest-codes'

    //Address Type V6 constants
    public static final String ADDRESS_TYPE_SETTING_NAME_V6 = 'ADDRESSES.ADDRESSTYPE'
    public static final String ADDRESS_TYPE_SETTING_NAME_V3 = 'PERSON.ADDRESSES.ADDRESSTYPE'
    public static final String ADDRESS_TYPE_LDM_NAME = 'address-types'
    public static final String ADDRESS_TYPE = 'addressType'

    static final String NON_PERSONS_LDM_NAME = 'non-persons'

    static final String COMMITTEE_FUNCTION_LDM_NAME = 'committee-function'
    static final String ACTIVITY_TYPE_LDM_NAME = 'activity-type'

    //Section V4 Status Settings Name
    public static final String SECTION_STATUS_SETTING_NAME_V4 = 'SECTIONDETAIL.STATUS.V4'
    public static final String SECTION_STATUS_SETTING_NAME_V1 = 'SECTIONDETAIL.STATUS'
    public static final String SECTION_DURATION_UNIT_SETTING_NAME_V1 = 'SECTIONDETAIL.DURATION.UNIT'
    public static final String SECTION_DURATION_UNIT_SETTING_NAME_V4 = 'SECTIONDETAIL.DURATION.UNIT.V4'

    public final static String COLLEGE_LDM_NAME = "colleges"
    public final static String DEPARTMENT_LDM_NAME = "departments"
    public final static String DIVISION_LDM_NAME = "divisions"

    //Relationship type V7 constants
    public static final String RELATIONSHIP_TYPE_SETTING_NAME_V7 = 'PERSONAL.RELATIONSHIP.TYPES'

    //Section Registration Status constants
    public static final String COURSE_REGISTRATION_STATUSES_LDM_NAME = 'course-registration-statuses'

    static final String INSTRUCTIONAL_METHOD_LDM_NAME = 'instructional-methods'

    public static final String STUDENT_ACADEMICLOAD_CODE = "STUDENT.ACADEMICLOAD.CODE"

}



