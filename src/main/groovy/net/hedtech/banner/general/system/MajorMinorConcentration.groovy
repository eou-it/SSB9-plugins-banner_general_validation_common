/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import org.apache.commons.lang.StringUtils
import org.hibernate.annotations.Type

import javax.persistence.*

/**
 * Import Related Entities if they are external to this package
 */
/**
 * Major, Minor, Concentration Validation Table
 */
@Entity
@Table(name = "STVMAJR")
@NamedQueries(value = [
@NamedQuery(name = "MajorMinorConcentration.fetchAllByCodeDescriptionAndValidMajor",
        query = """FROM MajorMinorConcentration a
           WHERE ( a.code  like :filter
           OR UPPER(a.description) like :filter )
           AND a.validMajorIndicator = 'Y'
           ORDER by a.code """),
@NamedQuery(name = "MajorMinorConcentration.fetchAllByCodeDescriptionAndValidMinor",
        query = """FROM MajorMinorConcentration a
           WHERE ( a.code  like :filter
           OR UPPER(a.description) like :filter )
           AND a.validMinorIndicator = 'Y'
           ORDER by a.code """),
@NamedQuery(name = "MajorMinorConcentration.fetchAllByCodeDescriptionAndValidConcentration",
        query = """FROM MajorMinorConcentration a
           WHERE ( a.code  like :filter
           OR UPPER(a.description) like :filter )
           AND a.validConcentratnIndicator = 'Y'
           ORDER by a.code """),
@NamedQuery(name = "MajorMinorConcentration.fetchByCodeAndValidMajor",
        query = """FROM MajorMinorConcentration a
           WHERE   a.code = :filter
           AND a.validMajorIndicator = 'Y'  """),
@NamedQuery(name = "MajorMinorConcentration.fetchByCodeAndValidMinor",
        query = """FROM MajorMinorConcentration a
           WHERE   a.code  = :filter
           AND a.validMinorIndicator = 'Y'  """),
@NamedQuery(name = "MajorMinorConcentration.fetchByCodeAndValidConcentration",
        query = """FROM MajorMinorConcentration a
           WHERE   a.code  = :filter
           AND a.validConcentratnIndicator = 'Y'  """),
@NamedQuery(name = "MajorMinorConcentration.fetchBySomeAttributeFromExistingMajors",
        query = """FROM MajorMinorConcentration a
           WHERE ( a.code  like :filter
           OR UPPER(a.description) like :filter )
           AND a.code in :majors
           ORDER by a.code """),
@NamedQuery(name = "MajorMinorConcentration.fetchByCodeFromExistingMajors",
        query = """FROM MajorMinorConcentration a
           WHERE   a.code  = :filter
           AND a.code in :majors """),
@NamedQuery(name = "MajorMinorConcentration.fetchByCode",
                query = """FROM  MajorMinorConcentration a WHERE a.code = :code """)
])
class MajorMinorConcentration implements Serializable {

    /**
     * Surrogate ID for STVMAJR
     */
    @Id
    @Column(name = "STVMAJR_SURROGATE_ID")
    @SequenceGenerator(name = "STVMAJR_SEQ_GEN", allocationSize = 1, sequenceName = "STVMAJR_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVMAJR_SEQ_GEN")
    Long id

    /**
     * This field identifies the major code referenced in the Catalog, Class Sched., Recruit., Admissions, Gen. Student, Registr., and Acad. Hist. Modules. Reqd. value: 00 - Major Not Declared.
     */
    @Column(name = "STVMAJR_CODE", nullable = false, unique = true, length = 4)
    String code

    /**
     * This field specifies the major area of study associated with the major code.
     */
    @Column(name = "STVMAJR_DESC", length = 30)
    String description

    /**
     * This field indicates whether the area of study is a valid major.
     */
    @Type(type = "yes_no")
    @Column(name = "STVMAJR_VALID_MAJOR_IND")
    Boolean validMajorIndicator

    /**
     * This field indicates whether the area of study is a valid minor.
     */
    @Type(type = "yes_no")
    @Column(name = "STVMAJR_VALID_MINOR_IND")
    Boolean validMinorIndicator

    /**
     * This field indicates whether the area of study is a valid concentration.
     */
    @Type(type = "yes_no")
    @Column(name = "STVMAJR_VALID_CONCENTRATN_IND")
    Boolean validConcentratnIndicator

    /**
     * This field identifies the most recent date a record was created or updated.
     */
    @Column(name = "STVMAJR_ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * Occupation specific indicator. Y indicates program has been designated as occupation specific
     */
    @Type(type = "yes_no")
    @Column(name = "STVMAJR_OCCUPATION_IND")
    Boolean occupationIndicator

    /**
     * A indicator which indicates the eligibility for aid
     */
    @Type(type = "yes_no")
    @Column(name = "STVMAJR_AID_ELIGIBILITY_IND")
    Boolean aidEligibilityIndicator

    /**
     * System Required Indicator
     */
    @Type(type = "yes_no")
    @Column(name = "STVMAJR_SYSTEM_REQ_IND")
    Boolean systemRequiredIndicator

    /**
     * The Voice Response message number assigned to the recorded message that describes the major code.
     */
    @Column(name = "STVMAJR_VR_MSG_NO", precision = 6)
    Integer voiceResponseMsgNumber

    /**
     * SEVIS EQUIVALENCY: SEVIS code for primary major
     */
    @Column(name = "STVMAJR_SEVIS_EQUIV", length = 6)
    String sevisEquiv

    /**
     * Version column which is used as a optimistic lock token for STVMAJR
     */
    @Version
    @Column(name = "STVMAJR_VERSION", nullable = false, precision = 19)
    Long version

    /**
     * Last Modified By column for STVMAJR
     */
    @Column(name = "STVMAJR_USER_ID", length = 30)
    String lastModifiedBy

    /**
     * Data Origin column for STVMAJR
     */
    @Column(name = "STVMAJR_DATA_ORIGIN", length = 30)
    String dataOrigin

    /**
     * Foreign Key : FK1_STVMAJR_INV_STVCIPC_CODE
     */
    @ManyToOne
    @JoinColumns([
    @JoinColumn(name = "STVMAJR_CIPC_CODE", referencedColumnName = "STVCIPC_CODE")
    ])
    CIPCode cipcCode


    public String toString() {
        """MajorMinorConcentration[
				  id=$id, code=$code,
				  description=$description,
				  validMajorIndicator=$validMajorIndicator,
				  validMinorIndicator=$validMinorIndicator,
				  validConcentratnIndicator=$validConcentratnIndicator,
				  lastModified=$lastModified,
				  occupationIndicator=$occupationIndicator,
				  aidEligibilityIndicator=$aidEligibilityIndicator,
				  systemRequiredIndicator=$systemRequiredIndicator,
				  voiceResponseMsgNumber=$voiceResponseMsgNumber,
				  sevisEquiv=$sevisEquiv, version=$version,
				  lastModifiedBy=$lastModifiedBy,
				  dataOrigin=$dataOrigin
				  cipcCode=$cipcCode]"""
    }


    static constraints = {
        code(nullable: false, maxSize: 4)
        description(nullable: true, maxSize: 30)
        validMajorIndicator(nullable: true)
        validMinorIndicator(nullable: true)
        validConcentratnIndicator(nullable: true)
        occupationIndicator(nullable: true)
        aidEligibilityIndicator(nullable: true)
        systemRequiredIndicator(nullable: true)
        voiceResponseMsgNumber(nullable: true, min: -999999, max: 999999)
        sevisEquiv(nullable: true, maxSize: 6)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
        cipcCode(nullable: true)
    }


    boolean equals(o) {
        if (this.is(o)) return true

        if (!(o instanceof MajorMinorConcentration)) return false

        MajorMinorConcentration that = (MajorMinorConcentration) o

        if (aidEligibilityIndicator != that.aidEligibilityIndicator) return false
        if (cipcCode != that.cipcCode) return false
        if (code != that.code) return false
        if (dataOrigin != that.dataOrigin) return false
        if (description != that.description) return false
        if (id != that.id) return false
        if (lastModified != that.lastModified) return false
        if (lastModifiedBy != that.lastModifiedBy) return false
        if (occupationIndicator != that.occupationIndicator) return false
        if (sevisEquiv != that.sevisEquiv) return false
        if (systemRequiredIndicator != that.systemRequiredIndicator) return false
        if (validConcentratnIndicator != that.validConcentratnIndicator) return false
        if (validMajorIndicator != that.validMajorIndicator) return false
        if (validMinorIndicator != that.validMinorIndicator) return false
        if (version != that.version) return false
        if (voiceResponseMsgNumber != that.voiceResponseMsgNumber) return false

        return true
    }


    int hashCode() {
        int result

        result = (id != null ? id.hashCode() : 0)
        result = 31 * result + (code != null ? code.hashCode() : 0)
        result = 31 * result + (description != null ? description.hashCode() : 0)
        result = 31 * result + (validMajorIndicator != null ? validMajorIndicator.hashCode() : 0)
        result = 31 * result + (validMinorIndicator != null ? validMinorIndicator.hashCode() : 0)
        result = 31 * result + (validConcentratnIndicator != null ? validConcentratnIndicator.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (occupationIndicator != null ? occupationIndicator.hashCode() : 0)
        result = 31 * result + (aidEligibilityIndicator != null ? aidEligibilityIndicator.hashCode() : 0)
        result = 31 * result + (systemRequiredIndicator != null ? systemRequiredIndicator.hashCode() : 0)
        result = 31 * result + (voiceResponseMsgNumber != null ? voiceResponseMsgNumber.hashCode() : 0)
        result = 31 * result + (sevisEquiv != null ? sevisEquiv.hashCode() : 0)
        result = 31 * result + (version != null ? version.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        result = 31 * result + (cipcCode != null ? cipcCode.hashCode() : 0)
        return result
    }

    //Read Only fields that should be protected against update
    public static readonlyProperties = ['code']

    /**
     * bean methods for lookups
     * @return
     */
    public static Object fetchBySomeAttribute() {
        def returnObj = [list: MajorMinorConcentration.findAll([sort: "code"])]
        return returnObj
    }


    public static Object fetchBySomeAttribute(String filter) {

        def returnObj = [list: MajorMinorConcentration.findAllByCodeIlikeOrDescriptionIlike("%" + filter + "%", "%" + filter + "%", [sort: "code"])]
        return returnObj
    }


    public static Object fetchBySomeAttribute(String filter, Map params) {
        def returnObj = [list: MajorMinorConcentration.fetchBySomeAttributeFromQuery(filter, params.fieldOfStudy)]
        return returnObj
    }


    public static Object fetchBySomeAttribute(Map params) {
        def returnObj = [list: MajorMinorConcentration.fetchBySomeAttributeFromQuery("%", params.fieldOfStudy)]
        return returnObj
    }

    /**
     *   Method to select the data based on field of study type and the filter entered in serach
     * @param filter
     * @param fieldOfStudy
     * @return list of MajorMinorConcentration
     */
    public static List fetchBySomeAttributeFromQuery(String filter, String fieldOfStudy) {

        if (StringUtils.isBlank(filter)) {
            filter = "%"
        } else if (!(filter =~ /%/)) {
            filter = "%" + filter.toUpperCase() + "%"
        } else filter = filter.toUpperCase()

        def result
        switch (fieldOfStudy) {
            case "MAJOR":
                result = MajorMinorConcentration.withSession { session ->
                    session.getNamedQuery('MajorMinorConcentration.fetchAllByCodeDescriptionAndValidMajor').setString('filter', filter).list()
                }
                break

            case "MINOR":
                result = MajorMinorConcentration.withSession { session ->
                    session.getNamedQuery('MajorMinorConcentration.fetchAllByCodeDescriptionAndValidMinor').setString('filter', filter).list()
                }
                break


            case ["CONCENTRATION"]:
                result = MajorMinorConcentration.withSession { session ->
                    session.getNamedQuery('MajorMinorConcentration.fetchAllByCodeDescriptionAndValidConcentration').setString('filter', filter).list()
                }
                break

            default:
                result = MajorMinorConcentration.findAllByCodeIlikeOrDescriptionIlike("%" + filter + "%", "%" + filter + "%", [sort: "code"])
        }
        return result

    }

    /**
     * Method to validate the major entered on the lookup component 
     * @param filter
     * @param params
     * @return MajorMinorConcentration object
     */
    public static MajorMinorConcentration fetchValidMajor(String filter, params) {

        def result
        switch (params.fieldOfStudy) {
            case "MAJOR":
                result = MajorMinorConcentration.withSession { session ->
                    session.getNamedQuery('MajorMinorConcentration.fetchByCodeAndValidMajor').setString('filter', filter).list()[0]
                }
                break

            case "MINOR":
                result = MajorMinorConcentration.withSession { session ->
                    session.getNamedQuery('MajorMinorConcentration.fetchByCodeAndValidMinor').setString('filter', filter).list()[0]
                }
                break


            case ["CONCENTRATION"]:
                result = MajorMinorConcentration.withSession { session ->
                    session.getNamedQuery('MajorMinorConcentration.fetchByCodeAndValidConcentration').setString('filter', filter).list()[0]
                }
                break

            default:
                result = MajorMinorConcentration.findByCode(filter)
        }

        return result

    }


    public static MajorMinorConcentration fetchValidType(String filter, params) {

        def result
        switch (params.fieldOfStudy) {
            case params.majorType:
                result = MajorMinorConcentration.withSession { session ->
                    session.getNamedQuery('MajorMinorConcentration.fetchByCodeAndValidMajor').setString('filter', filter).uniqueResult()
                }
                break

            case params.minorType:
                result = MajorMinorConcentration.withSession { session ->
                    session.getNamedQuery('MajorMinorConcentration.fetchByCodeAndValidMinor').setString('filter', filter).uniqueResult()
                }
                break


            case params.concentrationType:
                result = MajorMinorConcentration.withSession { session ->
                    session.getNamedQuery('MajorMinorConcentration.fetchByCodeAndValidConcentration').setString('filter', filter).uniqueResult()
                }
                break

            default:
                result = MajorMinorConcentration.findByCode(filter)
        }

        return result
    }


    public static def fetchBySomeAttributeForType(String filter, params) {
        def filterText
        if (StringUtils.isBlank(filter)) {
            filterText = "%"
        } else if (!(filter =~ /%/)) {
            filterText = "%" + filter.toUpperCase() + "%"
        } else filterText = filter.toUpperCase()

        def result
        switch (params.fieldOfStudy) {
            case params.majorType:
                result = MajorMinorConcentration.withSession { session ->
                    session.getNamedQuery('MajorMinorConcentration.fetchAllByCodeDescriptionAndValidMajor').setString('filter', filterText).list()
                }
                break

            case params.minorType:
                result = MajorMinorConcentration.withSession { session ->
                    session.getNamedQuery('MajorMinorConcentration.fetchAllByCodeDescriptionAndValidMinor').setString('filter', filterText).list()
                }
                break


            case params.concentrationType:
                result = MajorMinorConcentration.withSession { session ->
                    session.getNamedQuery('MajorMinorConcentration.fetchAllByCodeDescriptionAndValidConcentration').setString('filter', filterText).list()
                }
                break

            default:
                result = MajorMinorConcentration.findAllByCodeIlikeOrDescriptionIlike("%" + filterText + "%", "%" + filterText + "%", [sort: "code"])
        }
        return [list: result]

    }


    public static def fetchBySomeAttributeForType(params) {
        return fetchBySomeAttributeForType("%", params)
    }

    // take in list of existing majors for student and product list and validate the one they select is valid as per the list
    public static def fetchBySomeAttributeFromExistingMajors(filter, params) {
        def filterText
        if (StringUtils.isBlank(filter)) {
            filterText = "%"
        } else if (!(filter =~ /%/)) {
            filterText = "%" + filter.toUpperCase() + "%"
        } else filterText = filter.toUpperCase()
        def result = []
        if (params.existingMajors?.size()) {
            result = MajorMinorConcentration.withSession { session ->
                session.getNamedQuery('MajorMinorConcentration.fetchBySomeAttributeFromExistingMajors').setString('filter', filterText)
                        .setParameterList('majors', params.existingMajors).list()
            }
        }
        return [list: result]
    }


    public static def fetchBySomeAttributeFromExistingMajors(params) {
        return fetchBySomeAttributeFromExistingMajors("%", params)
    }


    public static def fetchByCodeFromExistingMajors(filter, params) {

        def result = []
        if (params.existingMajors?.size()) {
            result = MajorMinorConcentration.withSession { session ->
                session.getNamedQuery('MajorMinorConcentration.fetchByCodeFromExistingMajors').setString('filter', filter)
                        .setParameterList('majors', params.existingMajors).list()
            }
        }
        if (result.size() == 1) return result[0]
        else return null
    }

    /**
     * fetch MajorMinorConcentration based on code value
     * @param code
     * @return majorMinorConcentration
     */
    public static MajorMinorConcentration fetchByCode(String code) {
        MajorMinorConcentration majorMinorConcentration = MajorMinorConcentration.withSession { session ->
            session.getNamedQuery('MajorMinorConcentration.fetchByCode').setString(GeneralValidationCommonConstants.CODE, code).uniqueResult()
        }

        return majorMinorConcentration
    }

}
