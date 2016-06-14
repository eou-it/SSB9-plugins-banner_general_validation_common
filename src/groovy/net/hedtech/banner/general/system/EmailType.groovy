/** *****************************************************************************
 Copyright 2009-2016 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.system.ldm.v6.EmailTypeEnum
import org.hibernate.annotations.Type

import javax.persistence.*

/**
 * The GTVEMAL table contains the valid e-mail type codes.
 */

@Entity
@Table(name = "GTVEMAL")
@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true, includeFields = true)
@NamedQueries(value = [
        @NamedQuery(name = "EmailType.fetchAll",
                query = """from EmailType a , GlobalUniqueIdentifier b, IntegrationConfiguration c where a.code = b.domainKey and b.ldmName = :ldmName
                            and c.processCode = :processCode and c.settingName = :settingName and c.value = a.code and c.translationValue in (:translationValue)"""),
        @NamedQuery(name = "EmailType.fetchByGuid",
                query = """from EmailType a , GlobalUniqueIdentifier b, IntegrationConfiguration c where a.code = b.domainKey and b.ldmName = :ldmName and b.guid = :guid
                            and c.processCode = :processCode and c.settingName = :settingName and c.value = a.code and c.translationValue in (:translationValue)"""),
        @NamedQuery(name = "EmailType.countAll",
                query = """select count(*) from EmailType a , GlobalUniqueIdentifier b, IntegrationConfiguration c where a.code = b.domainKey and b.ldmName = :ldmName
                            and c.processCode = :processCode and c.settingName = :settingName and c.value = a.code and c.translationValue in (:translationValue)""")

])
class EmailType implements Serializable {

    /**
     * Surrogate ID for GTVEMAL
     */
    @Id
    @Column(name = "GTVEMAL_SURROGATE_ID")
    @SequenceGenerator(name = "GTVEMAL_SEQ_GEN", allocationSize = 1, sequenceName = "GTVEMAL_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GTVEMAL_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for GTVEMAL
     */
    @Version
    @Column(name = "GTVEMAL_VERSION", nullable = false, precision = 19)
    Long version

    /**
     * A code identifying the type of e-mail address.
     */
    @Column(name = "GTVEMAL_CODE", nullable = false, unique = true, length = 4)
    String code

    /**
     * The description of the e-mail address.
     */
    @Column(name = "GTVEMAL_DESC", length = 60)
    String description

    /**
     * Display on Web Indicator, Y indicates this type of email should display
     */
    @Type(type = "yes_no")
    @Column(name = "GTVEMAL_DISP_WEB_IND", nullable = false, length = 1)
    Boolean displayWebIndicator

    /**
     * URL Indicator, Y indicates that this email record is actually a URL.
     */
    @Type(type = "yes_no")
    @Column(name = "GTVEMAL_URL_IND", nullable = false, length = 1)
    Boolean urlIndicator

    /**
     * The date on which the row was added or modified.
     */
    @Column(name = "GTVEMAL_ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * Last modified by column for GTVEMAL
     */
    @Column(name = "GTVEMAL_USER_ID", length = 30)
    String lastModifiedBy

    /**
     * Data origin column for GTVEMAL
     */
    @Column(name = "GTVEMAL_DATA_ORIGIN", length = 30)
    String dataOrigin


    static constraints = {
        code(nullable: false, maxSize: 4)
        description(nullable: true, maxSize: 60)
        displayWebIndicator(nullable: false)
        urlIndicator(nullable: false)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }

    //Read Only fields that should be protected against update
    public static readonlyProperties = ['code']

    /**
     * Return Count value of Email Type which are mapped to goriccr
     * @return Long
     */
    public static Long countAll() {
        return EmailType.withSession { session ->
            session.getNamedQuery('EmailType.countAll')
                    .setString('processCode', GeneralValidationCommonConstants.PROCESS_CODE)
                    .setString('settingName', GeneralValidationCommonConstants.EMAIL_TYPE_SETTING_NAME)
                    .setParameterList('translationValue', EmailTypeEnum.EMAIL_TYPE)
                    .setString('ldmName', GeneralValidationCommonConstants.EAMIL_TYPE_LDM_NAME)
                    .uniqueResult()
        }
    }

    /**
     * fetching EmailType data
     * @param Map
     * @return List
     */
    public static List fetchAll(Map params) {
        return EmailType.withSession { session ->
            session.getNamedQuery('EmailType.fetchAll')
                    .setString('processCode', GeneralValidationCommonConstants.PROCESS_CODE)
                    .setString('settingName', GeneralValidationCommonConstants.EMAIL_TYPE_SETTING_NAME)
                    .setParameterList('translationValue', EmailTypeEnum.EMAIL_TYPE)
                    .setString('ldmName', GeneralValidationCommonConstants.EAMIL_TYPE_LDM_NAME)
                    .setMaxResults(params.max as Integer).setFirstResult((params?.offset ?: '0') as Integer)
                    .list()
        }
    }

    /**
     * fetching EmailType data
     * @param Map
     * @return List
     */
    public static List fetchAll() {
        return EmailType.withSession { session ->
            session.getNamedQuery('EmailType.fetchAll')
                    .setString('processCode', GeneralValidationCommonConstants.PROCESS_CODE)
                    .setString('settingName', GeneralValidationCommonConstants.EMAIL_TYPE_SETTING_NAME)
                    .setParameterList('translationValue', EmailTypeEnum.EMAIL_TYPE)
                    .setString('ldmName', GeneralValidationCommonConstants.EAMIL_TYPE_LDM_NAME)
                    .list()
        }
    }

    /**
     * Return Email Type which are mapped to goriccr
     * @return Long
     */
    public static List fetchByGuid(String guid) {
        return EmailType.withSession { session ->
            session.getNamedQuery('EmailType.fetchByGuid')
                    .setString('processCode', GeneralValidationCommonConstants.PROCESS_CODE)
                    .setString('settingName', GeneralValidationCommonConstants.EMAIL_TYPE_SETTING_NAME)
                    .setParameterList('translationValue', EmailTypeEnum.EMAIL_TYPE)
                    .setString('ldmName', GeneralValidationCommonConstants.EAMIL_TYPE_LDM_NAME)
                    .setString('guid',guid)
                    .uniqueResult()
        }
    }


}
