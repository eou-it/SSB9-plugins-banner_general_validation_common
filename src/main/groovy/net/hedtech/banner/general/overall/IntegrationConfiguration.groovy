/*******************************************************************************
 Copyright 2017 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.overall

import org.apache.commons.lang3.StringUtils
import org.hibernate.CacheMode
import org.hibernate.annotations.CacheConcurrencyStrategy

import javax.persistence.*

/**
 * Integration configuration rules table
 */
@Cacheable(true)
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "ldmEnumeration")
@Entity
@Table(name = "GV_GORICCR")
@NamedQueries(value = [
        @NamedQuery(name = "IntegrationConfiguration.fetchAllByProcessCode",
                query = """FROM IntegrationConfiguration a
                              WHERE a.processCode = :processCode"""),
        @NamedQuery(name = "IntegrationConfiguration.fetchAllByProcessCodeAndSettingNameAndTranslationValue",
                query = """FROM IntegrationConfiguration a
                              WHERE a.processCode = :processCode
                              and a.settingName = :settingName
                              and a.translationValue = :translationValue"""),
        @NamedQuery(name = "IntegrationConfiguration.fetchAllByProcessCodeAndSettingNameAndValue",
                query = """FROM IntegrationConfiguration a
                              WHERE a.processCode = :processCode
                              and a.settingName = :settingName
                              and a.value = :value"""),
        @NamedQuery(name = "IntegrationConfiguration.fetchAllByProcessCodeAndSettingNameAndValues",
                query = """FROM IntegrationConfiguration a
                                      WHERE a.processCode = :processCode
                                      and a.settingName = :settingName
                                      and a.value in (:values)"""),
        @NamedQuery(name = "IntegrationConfiguration.fetchByProcessCodeAndSettingName",
                query = """FROM IntegrationConfiguration a
                                              WHERE a.processCode = :processCode
                                              and a.settingName = :settingName"""),
        @NamedQuery(name = "IntegrationConfiguration.fetchAllByProcessCodeAndSettingName",
                query = """FROM IntegrationConfiguration a
                                     WHERE a.processCode = :processCode
                                     and a.settingName = :settingName""")
])
class IntegrationConfiguration implements Serializable {

    static final String LDM_CACHE_REGION_NAME = "ldmEnumeration"

    /**
     * Surrogate ID for GORICCR
     */
    @Id
    @Column(name = "GORICCR_SURROGATE_ID")
    @SequenceGenerator(name = "GORICCR_SEQ_GEN", allocationSize = 1, sequenceName = "GORICCR_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GORICCR_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for GORICCR
     */
    @Version
    @Column(name = "GORICCR_VERSION")
    Long version

    /**
     * ACTIVITY DATE: Most current date the record was created or changed.
     */
    @Column(name = "GORICCR_ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * USER ID: Oracle user ID of the user who changed the record.
     */
    @Column(name = "GORICCR_USER_ID")
    String lastModifiedBy

    /**
     * DATA ORIGIN: Source system that created or updated the row.
     */
    @Column(name = "GORICCR_DATA_ORIGIN")
    String dataOrigin

    /**
     * PROCESS CODE: The integration configuration setting process code.
     */
    @Column(name = "GORICCR_SQPR_CODE")
    String processCode

    /**
     * SETTING NAME: The integration configuration setting name.
     */
    @Column(name = "GORICCR_ICSN_CODE")
    String settingName

    /**
     * VALUE: Value of configuration setting.
     */
    @Column(name = "GORICCR_VALUE")
    String value

    /**
     * VALUE DESCRIPTION: Description of the value entered for the given setting.
     */
    @Column(name = "GORICCR_VALUE_DESC")
    String valueDescription

    /**
     * SEQUENCE NUMBER: Ordinal number used when order of configuration settings is important.
     */
    @Column(name = "GORICCR_SEQ_NO")
    Integer sequenceNumber

    /**
     * TRANSLATION VALUE: A value that is the technical equivalent of the value in the VALUE field.
     */
    @Column(name = "GORICCR_TRANSLATION_VALUE")
    String translationValue


    public String toString() {
        """IntegrationConfigurationRule[
					id=$id,
					version=$version,
					lastModified=$lastModified,
					lastModifiedBy=$lastModifiedBy,
					dataOrigin=$dataOrigin,
                    processCode=$processCode,
                    settingName=$settingName,
                    value=$value,
                    valueDescription=$valueDescription,
                    sequenceNumber=$sequenceNumber,
                    translationValue=$translationValue"""
    }


    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof IntegrationConfiguration)) return false
        IntegrationConfiguration that = (IntegrationConfiguration) o
        if (id != that.id) return false
        if (version != that.version) return false
        if (lastModified != that.lastModified) return false
        if (lastModifiedBy != that.lastModifiedBy) return false
        if (dataOrigin != that.dataOrigin) return false
        if (processCode != that.processCode) return false
        if (settingName != that.settingName) return false
        if (value != that.value) return false
        if (valueDescription != that.valueDescription) return false
        if (sequenceNumber != that.sequenceNumber) return false
        if (translationValue != that.translationValue) return false
        return true
    }


    int hashCode() {
        int result
        result = (id != null ? id.hashCode() : 0)
        result = 31 * result + (version != null ? version.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        result = 31 * result + (processCode != null ? processCode.hashCode() : 0)
        result = 31 * result + (settingName != null ? settingName.hashCode() : 0)
        result = 31 * result + (value != null ? value.hashCode() : 0)
        result = 31 * result + (valueDescription != null ? valueDescription.hashCode() : 0)
        result = 31 * result + (sequenceNumber != null ? sequenceNumber.hashCode() : 0)
        result = 31 * result + (translationValue != null ? translationValue.hashCode() : 0)
        return result
    }


    static constraints = {
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
        processCode(nullable: false, maxSize: 30)
        settingName(nullable: false, maxSize: 30)
        value(nullable: false, maxSize: 200)
        valueDescription(nullable: true, maxSize: 60)
        sequenceNumber(nullable: true)
        translationValue(nullable: true, maxSize: 200)
    }

    //Read Only fields that should be protected against update
    public static readonlyProperties = ['processCode', 'settingName']


    static List<IntegrationConfiguration> fetchAllByProcessCode(String processCode) {
        List<IntegrationConfiguration> integrationList = null
        if (!processCode) return integrationList
        integrationList = IntegrationConfiguration.withSession { session ->
            integrationList = session.getNamedQuery('IntegrationConfiguration.fetchAllByProcessCode').setCacheMode(CacheMode.GET)
                    .setString('processCode', processCode).setCacheable(true).setCacheRegion("ldmEnumeration").list()
        }
        return integrationList

    }


    static List<IntegrationConfiguration> fetchAllByProcessCodeAndSettingNameAndTranslationValue(String processCode, String settingName, String translationValue) {
        List<IntegrationConfiguration> integrationList = null
        if (!processCode) return integrationList
        integrationList = IntegrationConfiguration.withSession { session ->
            integrationList = session.getNamedQuery('IntegrationConfiguration.fetchAllByProcessCodeAndSettingNameAndTranslationValue').setCacheMode(CacheMode.GET)
                    .setString('processCode', processCode).setString('settingName', settingName).setString('translationValue', translationValue).setCacheable(true).setCacheRegion("ldmEnumeration").list()


        }
        return integrationList

    }

    /**
     * @deprecated Primary key of table prevents this ever returning a list. {@link #fetchByProcessCodeAndSettingNameAndValue(String processCode, String settingName, String value)}
     */
    @Deprecated
    static List<IntegrationConfiguration> fetchAllByProcessCodeAndSettingNameAndValue(String processCode, String settingName, String value) {
        List<IntegrationConfiguration> integrationList = null
        if (!processCode) return integrationList
        integrationList = IntegrationConfiguration.withSession { session ->
            integrationList = session.getNamedQuery('IntegrationConfiguration.fetchAllByProcessCodeAndSettingNameAndValue').setCacheMode(CacheMode.GET)
                    .setString('processCode', processCode).setString('settingName', settingName).setString('value', value).setCacheable(true).setCacheRegion("ldmEnumeration").list()


        }
        return integrationList

    }


    static IntegrationConfiguration fetchByProcessCodeAndSettingNameAndValue(String processCode, String settingName, String value) {
        List<IntegrationConfiguration> integrationList = null
        if (!processCode) return integrationList
        integrationList = IntegrationConfiguration.withSession { session ->
            integrationList = session.getNamedQuery('IntegrationConfiguration.fetchAllByProcessCodeAndSettingNameAndValue').setCacheMode(CacheMode.GET)
                    .setString('processCode', processCode).setString('settingName', settingName).setString('value', value).setCacheable(true).setCacheRegion("ldmEnumeration").list()
        }
        return integrationList?.size() > 0 ? integrationList?.get(0) : null

    }


    static List<IntegrationConfiguration> fetchByProcessCodeAndSettingNameAndValues(String processCode, String settingName, List<String> values) {
        List<IntegrationConfiguration> integrationList = null
        if (!processCode) return integrationList
        integrationList = IntegrationConfiguration.withSession { session ->

            String queryStr = """FROM IntegrationConfiguration a WHERE a.processCode = :processCode and a.settingName = :settingName and (a.value in (:values)"""
            //call to the method that builds the query dinamically for list of values that are bigger than 1000.
            //Implemented this method as a fix to the error that Hibernate throws:
            // ORA-01795: maximum number of expressions in a list is 1000 .
            def queryParams = getQueryParamsForValues(queryStr, values)
            queryParams.queryStr += """)"""
            integrationList = session.createQuery(queryParams.queryStr).setCacheMode(CacheMode.GET).setString('processCode', processCode)
                    .setString('settingName', settingName).setParameterList("values", queryParams.values).setCacheable(true).setCacheRegion("ldmEnumeration").list()
        }
        return integrationList
    }

    /**
     * Method that dynamically builds the queryString and the lists of values. Hibernate can handle only sets of 1000
     * expressions per parameter so if the list is bigger, this will handle it in batches of 1000.
     * @param queryStr
     * @param values
     * @return
     */
    private static def getQueryParamsForValues(String queryStr, List<String> values) {

        int count = 1
        if (values.size() > 1000) {
            List valuesTemp = values.subList(1000, values.size())
            values = values.subList(0, 1000)
            //if the values list had more than 1000 records set the rest of values.
            while (valuesTemp.size() > 0) {
                int toIndex = valuesTemp.size() > 1000 ? 1000 : valuesTemp.size()
                //setting the values directly into the queryString because using parameterList multiple times was
                // having some issues with thousands of records.
                queryStr += """ OR a.value in ('""" + StringUtils.join(valuesTemp.subList(0, toIndex), "','") + """')"""
                valuesTemp = valuesTemp.subList(toIndex, valuesTemp.size())
                count++
            }

        }
        return [queryStr: queryStr, values: values]


    }

    private static def getStringList(List values){

    }

    static IntegrationConfiguration fetchByProcessCodeAndSettingName(String processCode, String settingName) {
        IntegrationConfiguration integrationConfiguration = null
        if (processCode && settingName) {
            integrationConfiguration = IntegrationConfiguration.withSession { session ->
                session.getNamedQuery('IntegrationConfiguration.fetchByProcessCodeAndSettingName').setCacheMode(CacheMode.GET)
                        .setString('processCode', processCode).setString('settingName', settingName).setCacheable(true).setCacheRegion("ldmEnumeration").uniqueResult()
            }
        }
        return integrationConfiguration
    }


    static List<IntegrationConfiguration> fetchAllByProcessCodeAndSettingName(String processCode, String settingName) {
        List<IntegrationConfiguration> integrationList = null
        if (!processCode) return integrationList
        integrationList = IntegrationConfiguration.withSession { session ->
            integrationList = session.getNamedQuery('IntegrationConfiguration.fetchAllByProcessCodeAndSettingName').setCacheMode(CacheMode.GET)
                    .setString('processCode', processCode).setString('settingName', settingName).setCacheable(true).setCacheRegion("ldmEnumeration").list()
        }
        return integrationList
    }

}
