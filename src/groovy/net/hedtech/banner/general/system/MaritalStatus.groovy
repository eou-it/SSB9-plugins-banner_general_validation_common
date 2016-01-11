/*******************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system

import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import net.hedtech.banner.general.system.ldm.v4.MaritalStatusMaritalCategory

import javax.persistence.*

/**
 * Marital Status Validation Table
 */
@Entity
@Table(name = "STVMRTL")
@NamedQueries(value = [
        @NamedQuery(name = "MaritalStatus.fetchByCode",query = """FROM MaritalStatus a WHERE a.code = :code"""),
        @NamedQuery(name = "MaritalStatus.fetchMartialStatusDetailsCount",query = """select count(*) from MaritalStatus r,IntegrationConfiguration i where r.code = i.value and i.settingName = :settingName and i.processCode = :processCode and i.translationValue in (:translationValueList)""")

])

class MaritalStatus implements Serializable {

    /**
     * Surrogate ID for STVMRTL
     */
    @Id
    @Column(name = "STVMRTL_SURROGATE_ID")
    @SequenceGenerator(name = "STVMRTL_SEQ_GEN", allocationSize = 1, sequenceName = "STVMRTL_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVMRTL_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for STVMRTL
     */
    @Version
    @Column(name = "STVMRTL_VERSION")
    Long version

    /**
     * This field identifies the marital status code referenced on the General Person Form (SPAPERS).
     */
    @Column(name = "STVMRTL_CODE")
    String code

    /**
     * This field specifies the marital status associated with the marital status    code.
     */
    @Column(name = "STVMRTL_DESC")
    String description

    /**
     * This field indicates the marital status code equivalent for financial aid purposes.
     */
    @Column(name = "STVMRTL_FA_CONV_CODE")
    String financeConversion

    /**
     * EDI Marital Status Code
     */
    @Column(name = "STVMRTL_EDI_EQUIV")
    String electronicDataInterchangeEquivalent

    /**
     * This field identifies the most recent date a record was created or updated.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "STVMRTL_ACTIVITY_DATE")
    Date lastModified

    /**
     * Last modified by column for STVMRTL
     */
    @Column(name = "STVMRTL_USER_ID")
    String lastModifiedBy

    /**
     * Data origin column for STVMRTL
     */
    @Column(name = "STVMRTL_DATA_ORIGIN")
    String dataOrigin



    public String toString() {
        """MaritalStatus[
					id=$id, 
					version=$version, 
					code=$code, 
					description=$description, 
					financeConversion=$financeConversion, 
					electronicDataInterchangeEquivalent=$electronicDataInterchangeEquivalent, 
					lastModified=$lastModified, 
					lastModifiedBy=$lastModifiedBy, 
					dataOrigin=$dataOrigin]"""
    }


    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof MaritalStatus)) return false
        MaritalStatus that = (MaritalStatus) o
        if (id != that.id) return false
        if (version != that.version) return false
        if (code != that.code) return false
        if (description != that.description) return false
        if (financeConversion != that.financeConversion) return false
        if (electronicDataInterchangeEquivalent != that.electronicDataInterchangeEquivalent) return false
        if (lastModified != that.lastModified) return false
        if (lastModifiedBy != that.lastModifiedBy) return false
        if (dataOrigin != that.dataOrigin) return false
        return true
    }


    int hashCode() {
        int result
        result = (id != null ? id.hashCode() : 0)
        result = 31 * result + (version != null ? version.hashCode() : 0)
        result = 31 * result + (code != null ? code.hashCode() : 0)
        result = 31 * result + (description != null ? description.hashCode() : 0)
        result = 31 * result + (financeConversion != null ? financeConversion.hashCode() : 0)
        result = 31 * result + (electronicDataInterchangeEquivalent != null ? electronicDataInterchangeEquivalent.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        return result
    }


    static constraints = {
        code(nullable: false, maxSize: 1)
        description(nullable: true, maxSize: 30)
        financeConversion(nullable: false, maxSize: 1)
        electronicDataInterchangeEquivalent(nullable: true, maxSize: 1)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }

    //Read Only fields that should be protected against update
    public static readonlyProperties = ['code']

    /**
     *fetch marital status details which are mapped on goriccr settings
     * @param content
     * @param count
     */
    def static fetchMartialStatusDetails(def content) {
        def query = "from MaritalStatus r,IntegrationConfiguration i where r.code = i.value and i.settingName = :settingName and i.processCode = :processCode and i.translationValue in (:translationValueList)"
        MaritalStatus.withSession { session ->
            if (content?.sort) {
                def order = content.order ?: 'asc'
                query += " order by LOWER(r.$content.sort) $order"
            }
            def maritalQuery = session.createQuery(query).
                    setString(GeneralValidationCommonConstants.SETTING_NAME, GeneralValidationCommonConstants.MARITAL_STATUS_MARTIAL_CATEGORY).
                    setString(GeneralValidationCommonConstants.PROCESS_CODE_NAME, GeneralValidationCommonConstants.PROCESS_CODE).
                    setParameterList(GeneralValidationCommonConstants.TRANSLATION_VALUE, MaritalStatusMaritalCategory.MARITAL_STATUS_MARTIAL_CATEGORY).
                    setMaxResults(content?.max as Integer).setFirstResult((content?.offset ?: '0') as Integer)

            return maritalQuery.list()
        }
    }

    /**
     *fetch marital status details count which are mapped on goriccr settings
     * @param content
     * @param count
     */
    def static fetchMartialStatusDetailsCount() {
       def count = MaritalStatus.withSession { session ->
                   session.getNamedQuery('MaritalStatus.fetchMartialStatusDetailsCount').
                    setString(GeneralValidationCommonConstants.SETTING_NAME, GeneralValidationCommonConstants.MARITAL_STATUS_MARTIAL_CATEGORY).
                    setString(GeneralValidationCommonConstants.PROCESS_CODE_NAME, GeneralValidationCommonConstants.PROCESS_CODE).
                    setParameterList(GeneralValidationCommonConstants.TRANSLATION_VALUE, MaritalStatusMaritalCategory.MARITAL_STATUS_MARTIAL_CATEGORY).uniqueResult()

        }
        return count
    }


    /**
     * fetching MaritalStatus details based on code
     * @param code
     * @return
     */
    public static MaritalStatus fetchByCode(String code){
        MaritalStatus maritalStatus = MaritalStatus.withSession{ session ->
            session.getNamedQuery('MaritalStatus.fetchByCode').setString('code',code).uniqueResult()
        }
        return maritalStatus

    }

}
