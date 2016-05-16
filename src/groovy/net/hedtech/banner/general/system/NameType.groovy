/** *****************************************************************************
 Copyright 2009-2016 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import net.hedtech.banner.general.common.GeneralValidationCommonConstants

import javax.persistence.*

/**
 * This table defines the Banner name type codes.
 */

@Entity
@Table(name = "GTVNTYP")
@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true, includeFields = true)
@NamedQueries(value = [
   @NamedQuery(name = "NameType.fetchByGuid", query = """select g.guid as guid, n.code as code, n.description as title,
                   (select translationValue from IntegrationConfiguration  where processCode = :processCode AND settingName = :settingName AND value =  n.code) as cateogry
                   FROM NameType n,GlobalUniqueIdentifier g where g.ldmName = :ldmName AND g.domainKey = n.code and g.guid = :guid"""),
   @NamedQuery(name = "NameType.fetchAll", query = """select g.guid as guid, n.code as code, n.description as title,
                   (select translationValue from IntegrationConfiguration  where processCode = :processCode AND settingName = :settingName AND value =  n.code) as cateogry
                   FROM NameType n,GlobalUniqueIdentifier g where g.ldmName = :ldmName AND g.domainKey = n.code"""),
])
class NameType implements Serializable {

    /**
     * Surrogate ID for GTVNTYP
     */
    @Id
    @Column(name = "GTVNTYP_SURROGATE_ID")
    @SequenceGenerator(name = "GTVNTYP_SEQ_GEN", allocationSize = 1, sequenceName = "GTVNTYP_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GTVNTYP_SEQ_GEN")
    Long id

    /**
     * NAME TYPE CODE: The code associated with a type of name.
     */
    @Column(name = "GTVNTYP_CODE", nullable = false, length = 4)
    String code

    /**
     * DESCRIPTION: The description of the name type code.
     */
    @Column(name = "GTVNTYP_DESC", nullable = false, length = 30)
    String description

    /**
     * ACTIVITY DATE: The date that this record was created or last updated.
     */
    @Column(name = "GTVNTYP_ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * Version column which is used as a optimistic lock token for GTVNTYP
     */
    @Version
    @Column(name = "GTVNTYP_VERSION", length = 19)
    Long version

    /**
     * Last Modified By column for GTVNTYP
     */
    @Column(name = "GTVNTYP_USER_ID", length = 30)
    String lastModifiedBy

    /**
     * Data Origin column for GTVNTYP
     */
    @Column(name = "GTVNTYP_DATA_ORIGIN", length = 30)
    String dataOrigin

    static constraints = {
        code(nullable: false, maxSize: 4)
        description(nullable: false, maxSize: 30)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }

    /**
     * fetch list of Name types
     * @param params
     * @return
     */
    static List fetchAll(Map params) {
        return NameType.withSession { session ->
            session.getNamedQuery('NameType.fetchAll')
                      .setString('settingName',GeneralValidationCommonConstants.PERSON_NAME_TYPE_SETTING)
                      .setString('processCode',GeneralValidationCommonConstants.PROCESS_CODE)
                      .setString('ldmName',GeneralValidationCommonConstants.PERSON_NAME_TYPES_LDM_NAME)
                     .setMaxResults(params?.max as Integer)
                     .setFirstResult((params?.offset ?: '0') as Integer)
                     .list()
        }
    }

    /**
     * fetch Namme type data bases on guid
     * @param guid
     * @return
     */
    static def fetchByGuid(String guid) {
        return NameType.withSession { session ->
            session.getNamedQuery('NameType.fetchByGuid')
                    .setString('settingName',GeneralValidationCommonConstants.PERSON_NAME_TYPE_SETTING)
                    .setString('processCode',GeneralValidationCommonConstants.PROCESS_CODE)
                    .setString('ldmName',GeneralValidationCommonConstants.PERSON_NAME_TYPES_LDM_NAME)
                    .setString('guid',guid)
                    .uniqueResult();
        }
    }

}
