/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.hibernate.annotations.Type

import javax.persistence.*

/**
 * Student Level Validation Table
 */
@Entity
@Table(name = "STVLEVL")
@ToString
@EqualsAndHashCode
@NamedQueries(value = [
        @NamedQuery(name = "Level.fetchAllWithGuidByCodeInList", query = """ FROM Level a,GlobalUniqueIdentifier g where g.ldmName = 'academic-levels' AND g.domainKey = a.code and a.code in :codes """),
        @NamedQuery(name = "Level.fetchByCode",query = """FROM Level a WHERE a.code = :code"""),
        @NamedQuery(name = "Level.fetchAllByCodeInList",query = """FROM Level a WHERE a.code in (:codes)"""),
])
class Level implements Serializable {

    @Id
    @Column(name = "STVLEVL_SURROGATE_ID")
    @SequenceGenerator(name = "STVLEVL_SEQ_GEN", allocationSize = 1, sequenceName = "STVLEVL_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVLEVL_SEQ_GEN")
    Long id

    /**
     * This field identifies the student level code referenced in the Catalog, Recruiting, Admissions, Gen Student, Registration, and Acad Hist Modules. Required value: 00 - Level Not Declared.
     */
    @Column(name = "STVLEVL_CODE", nullable = false, unique = true, length = 2)
    String code

    /**
     * This field specifies the student level (e.g. undergraduate, graduate, professional) associated with the student level code.
     */
    @Column(name = "STVLEVL_DESC", nullable = false, length = 30)
    String description

    /**
     * This field identifies the most recent date a record was created or updated.
     */
    @Column(name = "STVLEVL_ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * This field is not currently in use.
     */
    @Type(type = "yes_no")
    @Column(name = "STVLEVL_ACAD_IND")
    Boolean acadInd

    /**
     * Continuing Education Indicator.
     */
    @Type(type = "yes_no")
    @Column(name = "STVLEVL_CEU_IND", nullable = false)
    Boolean ceuInd

    /**
     * System Required Indicator
     */
    @Type(type = "yes_no")
    @Column(name = "STVLEVL_SYSTEM_REQ_IND")
    Boolean systemReqInd

    /**
     * The Voice Response message number assigned to the recorded message that describes the student level.
     */
    @Column(name = "STVLEVL_VR_MSG_NO", precision = 6)
    Integer vrMsgNo

    /**
     * EDI Level Code
     */
    @Column(name = "STVLEVL_EDI_EQUIV", length = 2)
    String ediEquiv

    /**
     * Column for storing last modified by for STVLEVL
     */
    @Column(name = "STVLEVL_USER_ID", length = 30)
    String lastModifiedBy

    /**
     * Optimistic Lock Token for STVLEVL
     */
    @Version
    @Column(name = "STVLEVL_VERSION", nullable = false, precision = 19)
    Long version

    /**
     * Column for storing data origin for STVLEVL
     */
    @Column(name = "STVLEVL_DATA_ORIGIN", length = 30)
    String dataOrigin

    static constraints = {
        code(nullable: false, maxSize: 2)
        description(nullable: false, maxSize: 30)
        acadInd(nullable: true)
        ceuInd(nullable: false)
        systemReqInd(nullable: true)
        vrMsgNo(nullable: true)
        ediEquiv(nullable: true, maxSize: 2)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }

    public static Object fetchLevelForCEU(String level) {
        def returnObj = Level.findByCode(level)
        return returnObj
    }


    public static Object fetchLevelForCEU(String level, params) {
        def ceu = Boolean.valueOf(params.ceu)
        def returnObj = Level.findByCodeAndCeuInd(level, ceu)
        return returnObj
    }

    //List of below methods is used for Lookup in Default Calendar rules component.
    /**
     * fetchAllByTerm fetches all the SectionCensusInformationBase object for the given term code.
     */
    public static Object fetchAllByCeuIndAndCodeLike(Map params) {
        def ceu = Boolean.valueOf(params.ceu)
        return [list: Level.findAllByCeuInd(ceu)]
    }

    /**
     * fetchAllByTerm fetches all the SectionCensusInformationBase object for the given term code and academicCalendarTypecode filter.
     */
    public static Object fetchAllByCeuIndAndCodeLike(String filter, Map params) {
        def ceu = Boolean.valueOf(params.ceu)
        return [list: Level.findAllByCeuIndAndCodeIlike(ceu, filter + "%")]
    }

    //Read Only fields that should be protected against update
    public static readonlyProperties = ['code']

    /**
     * fetching Level details based on code
     * @param code
     * @return
     */
    public static Level fetchByCode(String code){
        Level level = Level.withSession{ session ->
            session.getNamedQuery('Level.fetchByCode').setString('code',code).uniqueResult()
        }
        return level

    }

    /**
     * fetching List of Level details based on codes
     * returns empty list if no matching records are found else a list of matching Level details
     * @param codes
     * @return
     */
    public static List<Level> fetchAllByCodeInList(List<String> codes){
        List<Level> levelList = []
        if(codes){
            Level.withSession{ session ->
                levelList = session.getNamedQuery('Level.fetchAllByCodeInList').setParameterList('codes',codes).list()
            }
        }
        return levelList
    }
}
