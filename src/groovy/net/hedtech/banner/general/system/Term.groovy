/** *****************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import org.apache.commons.lang.StringUtils
import org.hibernate.annotations.Type
import javax.persistence.*

/**
 * Term Code Validation Table
 */
@Entity
@Table(name = "STVTERM")
@NamedQueries(value = [
@NamedQuery(name = "Term.fetchPreviousTerm",
query = """FROM Term a WHERE a.code = ( SELECT MAX(b.code) FROM Term b WHERE b.code < :term)"""),
@NamedQuery(name = "Term.fetchMaxTermWithStartDateLessThanGivenDate",
query = """FROM Term t WHERE t.code = ( SELECT MAX(tm.code) FROM Term tm WHERE tm.startDate <= :givenDate)"""),
@NamedQuery(name = "Term.fetchMaxTermWithHousingStartDateLessThanEqualDate",
query = """FROM Term t WHERE t.code = ( SELECT MAX(tm.code) FROM Term tm WHERE tm.housingStartDate <= :housingStartDate)"""),
@NamedQuery(name = "Term.fetchAllByTerm",
query = """SELECT a.code,a.description FROM Term a WHERE UPPER(a.code) LIKE UPPER(:filter) OR UPPER(a.description) LIKE UPPER(:filter)
           ORDER BY a.code desc""")
])
class Term implements Serializable {

    /**
     * Surrogate ID for STVTERM
     */
    @Id
    @Column(name = "STVTERM_SURROGATE_ID")
    @SequenceGenerator(name = "STVTERM_SEQ_GEN", allocationSize = 1, sequenceName = "STVTERM_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVTERM_SEQ_GEN")
    Long id

    /**
     * This field identifies the term code referenced in the Catalog, Recruiting, Admissions, Gen. Student, Registration, Student Billing and Acad. Hist. Modules. Reqd. value: 999999 - End of Time.
     */
    @Column(name = "STVTERM_CODE", nullable = false, unique = true, length = 6)
    String code

    /**
     * This field specifies the term associated with the term code. The term is identified by the academic year and term number and is formatted YYYYTT.
     */
    @Column(name = "STVTERM_DESC", nullable = false, length = 30)
    String description

    /**
     * This field identifies the term start date and is formatted DD-MON-YY.
     */
    @Column(name = "STVTERM_START_DATE", nullable = false)
    @Temporal(TemporalType.DATE)
    Date startDate

    /**
     * This field identifies the term end date and is fomatted DD-MON-YY.
     */
    @Column(name = "STVTERM_END_DATE", nullable = false)
    @Temporal(TemporalType.DATE)
    Date endDate

    /**
     * This field identifies the financial aid processing start and end years (e.g. The financial aid processing year 1988 - 1989 is formatted 8889.).
     */
    @Column(name = "STVTERM_FA_PROC_YR", length = 4)
    String financialAidProcessingYear

    /**
     * This field identifies the most recent date a record was created or updated.
     */
    @Column(name = "STVTERM_ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * This field identifies the financial aid award term.
     */
    @Column(name = "STVTERM_FA_TERM", length = 1)
    String financialAidTerm

    /**
     * This field identifies the financial aid award beginning period.
     */
    @Column(name = "STVTERM_FA_PERIOD", precision = 2)
    Integer financialAidPeriod

    /**
     * This field identifies the financial aid award ending period.
     */
    @Column(name = "STVTERM_FA_END_PERIOD", precision = 2)
    Integer financialEndPeriod

    /**
     * Housing Start Date.
     */
    @Column(name = "STVTERM_HOUSING_START_DATE", nullable = false)
    @Temporal(TemporalType.DATE)
    Date housingStartDate

    /**
     * Housing End Date.
     */
    @Column(name = "STVTERM_HOUSING_END_DATE", nullable = false)
    @Temporal(TemporalType.DATE)
    Date housingEndDate

    /**
     * System Required Indicator
     */
    @Type(type = "yes_no")
    @Column(name = "STVTERM_SYSTEM_REQ_IND")
    Boolean systemReqInd

    /**
     * SUMMER INDICATOR: Indicates a summer term to financial aid.
     */
    @Type(type = "yes_no")
    @Column(name = "STVTERM_FA_SUMMER_IND")
    Boolean financeSummerIndicator

    /**
     * Column for storing last modified by for STVTERM
     */
    @Column(name = "STVTERM_USER_ID", length = 30)
    String lastModifiedBy

    /**
     * Optimistic Lock Token for STVTERM
     */
    @Version
    @Column(name = "STVTERM_VERSION", nullable = false, precision = 19)
    Long version

    /**
     * Column for storing data origin for STVTERM
     */
    @Column(name = "STVTERM_DATA_ORIGIN", length = 30)
    String dataOrigin

    /**
     * Foreign Key : FK1_STVTERM_INV_STVACYR_CODE
     */
    @ManyToOne
    @JoinColumns([
    @JoinColumn(name = "STVTERM_ACYR_CODE", referencedColumnName = "stvacyr_code")
    ])
    AcademicYear acyr_code

    /**
     * Foreign Key : FK1_STVTERM_INV_STVTRMT_CODE
     */
    @ManyToOne
    @JoinColumns([
    @JoinColumn(name = "STVTERM_TRMT_CODE", referencedColumnName = "STVTRMT_CODE")
    ])
    TermType trmt_code


    public String toString() {
        "Term[id=$id, code=$code, description=$description, startDate=$startDate, endDate=$endDate, financialAidProcessingYear=$financialAidProcessingYear, lastModified=$lastModified, financialAidTerm=$financialAidTerm, financialAidPeriod=$financialAidPeriod, financialEndPeriod=$financialEndPeriod, housingStartDate=$housingStartDate, housingEndDate=$housingEndDate, systemReqInd=$systemReqInd, financeSummerIndicator=$financeSummerIndicator, lastModifiedBy=$lastModifiedBy, version=$version, dataOrigin=$dataOrigin]"
    }


    static constraints = {
        code(nullable: false, maxSize: 6)
        description(nullable: false, maxSize: 30)
        startDate(nullable: false)
        endDate(nullable: false)
        acyr_code(nullable: false)
        financialAidProcessingYear(nullable: true, maxSize: 4)
        financialAidTerm(nullable: true, maxSize: 1)
        financialAidPeriod(nullable: true)
        financialEndPeriod(nullable: true)
        housingStartDate(nullable: false)
        housingEndDate(nullable: false)
        systemReqInd(nullable: true)
        financeSummerIndicator(nullable: true, maxsize: 1)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }


    public static Object fetchBySomeAttribute() {
        def returnObj = [list: Term.list(sort: "code", order: "desc")]
        return returnObj
    }


    public static Object fetchBySomeAttribute(filter) {
        def returnObj = [list: Term.findAllByCodeIlikeOrDescriptionIlike("%" + filter + "%", "%" + filter + "%", [sort: "code", order: "desc"])]
        return returnObj
    }


    public static Term fetchPreviousTerm(String term) {
        Term termRec
        Term.withSession {session ->
            termRec = session.getNamedQuery('Term.fetchPreviousTerm').setString('term', term).list()[0]
        }
        return termRec

    }

    boolean equals(o) {
        if (this.is(o)) return true;
        if (getClass() != o.class) return false;

        Term term = (Term) o;

        if (acyr_code != term.acyr_code) return false;
        if (code != term.code) return false;
        if (dataOrigin != term.dataOrigin) return false;
        if (description != term.description) return false;
        if (endDate != term.endDate) return false;
        if (financeSummerIndicator != term.financeSummerIndicator) return false;
        if (financialAidPeriod != term.financialAidPeriod) return false;
        if (financialAidProcessingYear != term.financialAidProcessingYear) return false;
        if (financialAidTerm != term.financialAidTerm) return false;
        if (financialEndPeriod != term.financialEndPeriod) return false;
        if (housingEndDate != term.housingEndDate) return false;
        if (housingStartDate != term.housingStartDate) return false;
        if (id != term.id) return false;
        if (lastModified != term.lastModified) return false;
        if (lastModifiedBy != term.lastModifiedBy) return false;
        if (startDate != term.startDate) return false;
        if (systemReqInd != term.systemReqInd) return false;
        if (trmt_code != term.trmt_code) return false;
        if (version != term.version) return false;

        return true;
    }


    int hashCode() {
        int result;
        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + (financialAidProcessingYear != null ? financialAidProcessingYear.hashCode() : 0);
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0);
        result = 31 * result + (financialAidTerm != null ? financialAidTerm.hashCode() : 0);
        result = 31 * result + (financialAidPeriod != null ? financialAidPeriod.hashCode() : 0);
        result = 31 * result + (financialEndPeriod != null ? financialEndPeriod.hashCode() : 0);
        result = 31 * result + (housingStartDate != null ? housingStartDate.hashCode() : 0);
        result = 31 * result + (housingEndDate != null ? housingEndDate.hashCode() : 0);
        result = 31 * result + (systemReqInd != null ? systemReqInd.hashCode() : 0);
        result = 31 * result + (financeSummerIndicator != null ? financeSummerIndicator.hashCode() : 0);
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0);
        result = 31 * result + (acyr_code != null ? acyr_code.hashCode() : 0);
        result = 31 * result + (trmt_code != null ? trmt_code.hashCode() : 0);
        return result;
    }



    public static Term fetchMaxTermWithHousingStartDateLessThanEqualDate(Date housingStartDate) {
        Term.withSession { session ->
            session.getNamedQuery('Term.fetchMaxTermWithHousingStartDateLessThanEqualDate').setDate('housingStartDate', housingStartDate).list()[0]
        }
    }


    public static Term fetchMaxTermWithStartDateLessThanGivenDate(Date beginDate) {
        Term.withSession { session ->
            session.getNamedQuery('Term.fetchMaxTermWithStartDateLessThanGivenDate').setDate('givenDate', beginDate).list()[0]
        }
    }


    static List fetchAllByTerm(String filter, Integer max =0, Integer offset=0) {
        List terms = []
        Term.withSession { session ->
            org.hibernate.Query query = session.getNamedQuery('Term.fetchAllByTerm')
                    .setString("filter", prepareParams(filter))
            if (max) {
                query.setMaxResults(max)
            }
            if (offset) {
                query.setFirstResult(offset)
            }
            terms = query.list().collect{
                [code:it[0], description: it[1]]
            }
        }
        return terms
    }

    private static String prepareParams(value) {
        if (StringUtils.isBlank(value)) {
            value = "%"
        } else if (!(value =~ /%/)) {
            value = "%" + value + "%"
        }
        return value
    }

}
