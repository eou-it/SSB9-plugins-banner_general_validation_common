/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import javax.persistence.*

/**
 * <p> Academic Honor View to retrieve the Honors from STVHOND and STVHONR Tables</p>
 */

@Entity
@Table(name = "gvq_acad_honors")
@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true, includeFields = true)
@NamedQueries(value = [
        @NamedQuery(name = "AcademicHonorView.fetchByGuid",
                query = """FROM  AcademicHonorView honor
		   WHERE honor.id=:guid"""),
        @NamedQuery(name = "AcademicHonorView.fetchAll",
                query = """from AcademicHonorView a
           WHERE 1 = 1"""),
        @NamedQuery(name = "AcademicHonorView.fetchByType",
                query = """FROM  AcademicHonorView honor
		   WHERE honor.type=:type"""),
        @NamedQuery(name = "AcademicHonorView.countRecord",
                query = """SELECT count(*) FROM AcademicHonorView"""),
        @NamedQuery(name = "AcademicHonorView.fetchCountByType",
                query = """SELECT count(*) FROM  AcademicHonorView honor
           WHERE honor.type = :type"""),
        @NamedQuery(name = "AcademicHonorView.fetchAllByCodeInList",
                query = """FROM AcademicHonorView where code in (:codes)""")

])
class AcademicHonorView implements Serializable {

    /**
     * Guid for Honors
     * */

    @Id
    @Column(name = "GUID")
    String id

    /**
     * Code for Honor (STVHOND and STVHONR Table)
     * */
    @Column(name = "CODE")
    String code

    /**
     * Description about the Honors
     * */
    @Column(name = "TITLE")
    String title

    /**
     * Type of Honors
     * */
    @Column(name = "HONOR_TYPE")
    String type

    /**
     * VERSION: Optimistic lock token.
     * */
    @Version
    @Column(name = "VERSION", length = 19)
    Long version

    /**
     * @param params
     * @return count
     */
    public static Long countRecord() {
        return AcademicHonorView.withSession { session ->
            session.getNamedQuery('AcademicHonorView.countRecord').uniqueResult()
        }
    }

    /**
     * @param params
     * @return count
     */
    public static Long countRecordWithFilter(type) {
        return AcademicHonorView.withSession { session ->
            session.getNamedQuery('AcademicHonorView.fetchCountByType').setString('type', type).uniqueResult()
        }
    }

    /**
     * @param type , params
     * @return academicHonors List
     */
    public static List fetchByType(type, params) {
        return AcademicHonorView.withSession { session ->
            session.getNamedQuery('AcademicHonorView.fetchByType')
                    .setString('type', type)
                    .setMaxResults(params?.max as Integer)
                    .setFirstResult(params?.offset as Integer).list()
        }
    }

    /**
     * @param type , params
     * @return academicHonors List
     */
    public static List fetchAll(params) {
        return AcademicHonorView.withSession { session ->
            session.getNamedQuery('AcademicHonorView.fetchAll')
                    .setMaxResults(params?.max as Integer)
                    .setFirstResult(params?.offset as Integer).list()
        }
    }

    /**
     * @param guid
     * @return academicHonor
     */
    public static AcademicHonorView fetchByGuid(String guid) {
        return AcademicHonorView.withSession {
            session -> session.getNamedQuery('AcademicHonorView.fetchByGuid').setString('guid', guid).uniqueResult()
        }
    }

    public static List<AcademicHonorView> fetchAllByCodeInList(List<String> codes) {
        List<AcademicHonorView> academicHonorViewList = []

        if (codes) {
            AcademicHonorView.withSession { session ->
                academicHonorViewList = session.getNamedQuery("AcademicHonorView.fetchAllByCodeInList").setParameterList('codes', codes).list()
            }
        }

        return academicHonorViewList
    }

}
