/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import net.hedtech.banner.general.common.GeneralValidationCommonConstants

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.NamedQueries
import javax.persistence.NamedQuery
import javax.persistence.SequenceGenerator
import javax.persistence.Table
import javax.persistence.Temporal
import javax.persistence.TemporalType
import javax.persistence.Version


/**
 * Geographic Region Rules Table.
 */


@Entity
@Table(name="SORGEOR")
@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true, includeFields = true)
@NamedQueries(value = [
        @NamedQuery(name = "GeographicRegionRule.fetchAllGeographicRegionArea",
                   query = """SELECT distinct CONCAT(s2.code,'-',s1.code) AS CODE,CONCAT(s2.description,'-',s1.description) AS TITLE, g.guid AS GAGUID,
                             (SELECT g1.guid from GlobalUniqueIdentifier g1 where g1.ldmName= :gDivisonLdmName AND s1.code = g1.domainKey) AS GDGUID,
                             (SELECT g2.guid from GlobalUniqueIdentifier g2 where g2.ldmName= :gRegionLdmName AND s2.code = g2.domainKey) AS GRGUID FROM GeographicRegionRule s ,GeographicDivision s1,GeographicRegion s2,GlobalUniqueIdentifier g
                              where s.divisionCode = s1.code and s.regionCode = s2.code and g.ldmName = :gAreaLdmName and g.domainKey = concat(s.regionCode, '-^' , s.divisionCode)
                               """),
        @NamedQuery(name = "GeographicRegionRule.countAllGeographicRegionArea",
                query = """select  COUNT(s) FROM GeographicRegionRule s
                           where EXISTS(select s1.regionCode,s1.divisionCode from GeographicRegionRule s1 group by s1.regionCode,s1.divisionCode  having min(s1.id) = s.id)
                        """),
        @NamedQuery(name = "GeographicRegionRule.fetchByGeographicRegionAreaGuid",
                query = """SELECT distinct CONCAT(s2.code,'-',s1.code) AS CODE,CONCAT(s2.description,'-',s1.description) AS TITLE, g.guid AS GAGUID,
                             (SELECT g1.guid from GlobalUniqueIdentifier g1 where g1.ldmName= :gDivisonLdmName AND s1.code = g1.domainKey) AS GDGUID,
                             (SELECT g2.guid from GlobalUniqueIdentifier g2 where g2.ldmName= :gRegionLdmName AND s2.code = g2.domainKey) AS GRGUID FROM GeographicRegionRule s ,GeographicDivision s1,GeographicRegion s2,GlobalUniqueIdentifier g
                              where s.divisionCode = s1.code and s.regionCode = s2.code and g.ldmName = :gAreaLdmName and g.domainKey = concat(s.regionCode, '-^' , s.divisionCode)
                              and g.guid = :guid
                               """)
])
class GeographicRegionRule implements Serializable{

    /**
     * Surrogate ID for SORGEOR
      */
    @Id
    @Column(name="SORGEOR_SURROGATE_ID")
    @SequenceGenerator(name = "SORGEOR_SEQ_GEN", allocationSize = 1, sequenceName = "SORGEOR_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SORGEOR_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for SORGEOR
     */
    @Version
    @Column(name = "SORGEOR_VERSION")
    Long version

    /**
     * The geographic region code.
     */
    @Column(name = "SORGEOR_GEOR_CODE")
    String regionCode

    /**
     * The geographic region division for the region code.
     */
    @Column(name = "SORGEOR_GEOD_CODE")
    String divisionCode

    /**
     * The geographic region type corresponding to the ranges specified.
     */
    @Column(name = "SORGEOR_TYPE")
    String regionType

    /**
     * The starting range for the type of geographic region.
     */
    @Column(name = "SORGEOR_START_RANGE")
    String startTypeRange

    /**
     * The ending range for the type of geographic region.
     */
    @Column(name = "SORGEOR_END_RANGE")
    String endTypeRange


    /**
     * The date the record was created or last updated
     */
    @Column(name = "SORGEOR_ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * The user who created or last updated the record
     */
    @Column(name = "SORGEOR_USER_ID")
    String lastModifiedBy

    /**
     * Data Origin column
     */
    @Column(name = "SORGEOR_DATA_ORIGIN")
    String dataOrigin


    /**
     * Constraints
     */
    static constraints = {
        regionCode(nullable: false, maxSize: 10)
        divisionCode(nullable: false, maxSize: 10)
        regionType(nullable : false,maxSize:10)
        startTypeRange(nullable: false,maxSize: 20)
        endTypeRange(nullable: false,maxSize: 20)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }


   static List fetchAll(Map params) {
       return GeographicRegionRule.withSession { session ->
            session.getNamedQuery('GeographicRegionRule.fetchAllGeographicRegionArea').
                    setString(GeneralValidationCommonConstants.GEOGRAPHIC_AREA_KEY,GeneralValidationCommonConstants.GEOGRAPHIC_AREA_LDM_NAME)
                    .setString(GeneralValidationCommonConstants.GEOGRAPHIC_DIVISION_KEY,GeneralValidationCommonConstants.GEOGRAPHIC_DIVISION_LDM_NAME)
                    .setString(GeneralValidationCommonConstants.GEOGRAPHIC_REGION_KEY,GeneralValidationCommonConstants.GEOGRAPHIC_REGION_LDM_NAME)
                    .setMaxResults(params?.max as Integer)
                    .setFirstResult((params?.offset ?: '0') as Integer)
                    .list();
        }
    }

    static def countAll() {
     return   GeographicRegionRule.withSession { session ->
            session.getNamedQuery('GeographicRegionRule.countAllGeographicRegionArea').uniqueResult();
        }
    }

    static def fetchByGuid(String guid) {
        return GeographicRegionRule.withSession { session ->
            session.getNamedQuery('GeographicRegionRule.fetchByGeographicRegionAreaGuid').
                     setString(GeneralValidationCommonConstants.GEOGRAPHIC_AREA_KEY,GeneralValidationCommonConstants.GEOGRAPHIC_AREA_LDM_NAME)
                    .setString(GeneralValidationCommonConstants.GEOGRAPHIC_DIVISION_KEY,GeneralValidationCommonConstants.GEOGRAPHIC_DIVISION_LDM_NAME)
                    .setString(GeneralValidationCommonConstants.GEOGRAPHIC_REGION_KEY,GeneralValidationCommonConstants.GEOGRAPHIC_REGION_LDM_NAME)
                    .setString(GeneralValidationCommonConstants.GEOGRAPHIC_AREA_GUID_KEY,guid)
                    .list().getAt(0);
        }
    }

    /**
     * Protect the field against the updates
     */
    public static readonlyProperties = ['regionCode','divisionCode']
}
