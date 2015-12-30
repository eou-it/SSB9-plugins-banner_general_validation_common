/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import net.hedtech.banner.query.DynamicFinder

import javax.persistence.*

/**
 * <p>Read only view for Academic Disciplines.</p>
 */
@Entity
@Table(name = "SVQ_MAJORS")
@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true, includeFields = true)
class AcademicDisciplineView implements Serializable {

    /**
     * This field specifies the GUID.
     */
    @Id
    @Column(name = "STVMAJR_GUID")
    String id

    /**
     * Surrogate ID for STVMAJR
     */
    @Column(name = "STVMAJR_SURROGATE_ID")
    Long surrogateId

    /**
     * VERSION for STVMAJR
     */
    @Version
    @Column(name = "STVMAJR_VERSION")
    Long version


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
     * Data Origin column for STVMAJR
     */
    @Column(name = "STVMAJR_DATA_ORIGIN", length = 30)
    String dataOrigin

    /**
     * This field specifies the type of STVMAJR.

     */
    @Column(name = "STVMAJR_VALID_TYPE", length = 30)
    String type



    def static countAll(filterData) {
        return finderByAll().count(filterData)
    }


    def static fetchSearch(filterData, pagingAndSortParams) {
        return finderByAll().find(filterData, pagingAndSortParams)

    }

    def private static finderByAll = {
        def query = "from AcademicDisciplineView a where 1 = 1"
        return new DynamicFinder(AcademicDisciplineView.class, query, "a")
    }
}
