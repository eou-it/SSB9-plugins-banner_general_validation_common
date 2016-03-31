/*******************************************************************************
 Copyright 2015-2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import net.hedtech.banner.query.DynamicFinder

import javax.persistence.*

/**
 *<p>Read only view for Academic Credentials.</p>
 */

@Entity
@Table(name = "gvq_academic_credentials")
@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true, includeFields = true)
@NamedQueries(value = [
        @NamedQuery(name = "AcademicCredential.fetchByGuid", query = """FROM AcademicCredential p WHERE p.guid = :guid""")
])
class AcademicCredential implements Serializable{

    /**
     * Surrogate ID for STVDEGC
     */
    @Id
    @Column(name = "STVDEGC_SURROGATE_ID")
    String id

    /**
     * This field identifies the degree code referenced in the Recruiting, Admissions, Gen. Student, Registration, Academic History and Degree Audit Modules. 000000 - Degree Not Declared.
     */
    @Column(name = "STVDEGC_CODE")
    String code

    /**
     * This field specifies the degree (e.g. Bachelor of Business Administration, Master of Arts, Juris Doctor, etc.) associated with the degree code.
     */
    @Column(name = "STVDEGC_DESC")
    String description

    /**
     * supplementary description from SDE
     */
    @Column(name = "DESCRIPTION")
    String suplementaryDesc

    /**
     * Last Modified By column for STVDEGC
     */
    @Column(name = "STVDEGC_USER_ID", length = 30)
    String lastModifiedBy

    /**
     * Data Origin column for STVDEGC
     */
    @Column(name = "STVDEGC_DATA_ORIGIN", length = 30)
    String dataOrigin

    /**
     * This field identifies the most recent date a record was created or updated.
     */
    @Column(name = "STVDEGC_ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * This field specifies the type of Degree.
     */
    @Column(name = "TYPE", length = 30)
    String type

    /**
     * This field specifies the GUID.
     */
    @Column(name = "GORGUID_GUID")
    String guid

    /**
     * Optimistic lock token for STVDEGC
     */
    @Version
    @Column(name = "STVDEGC_VERSION")
    Long version


    def static countAll(filterData) {
       return finderByAll().count(filterData)
    }


    def static fetchSearch(filterData, pagingAndSortParams) {
        return finderByAll().find(filterData, pagingAndSortParams)

    }

    def private static finderByAll = {
        def query = "from AcademicCredential a where 1 = 1"
        return new DynamicFinder(AcademicCredential.class, query, "a")
    }

    /**
     * fetching AcademicCredential data based on guid
     * @param guid
     * @return AcademicCredential
     */
    public static AcademicCredential fetchByGuid(String guid){
        AcademicCredential academicCredential = AcademicCredential.withSession{ session ->
            session.getNamedQuery('AcademicCredential.fetchByGuid').setString('guid',guid).uniqueResult()
        }
        return academicCredential
    }

}
