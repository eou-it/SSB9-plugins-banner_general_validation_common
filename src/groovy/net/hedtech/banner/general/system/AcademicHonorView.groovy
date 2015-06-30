/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.system
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.NamedQueries
import javax.persistence.NamedQuery
import javax.persistence.Table
/**
 * <p> Academic Honor View to retrieve the Honors from STVHOND and STVHONR Tables</p>
 */

@Entity
@Table(name = "gvq_acad_honors")
@NamedQueries(value = [
        @NamedQuery(name = "AcademicHonorView.fetchByGuid",
                query = """FROM  AcademicHonorView honor
		   WHERE honor.guid=:guid"""),
        @NamedQuery(name = "AcademicHonorView.fetchByType",
                query = """FROM  AcademicHonorView honor
		   WHERE honor.type=:type"""),
        @NamedQuery(name = "AcademicHonorView.fetchByCode",
                query = """FROM  AcademicHonorView honor
		   WHERE honor.code=:code and honor.type=:type""")
])
class AcademicHonorView implements Serializable{

    /**
     * Guid for Honors
     * */
    @Id
    @Column(name = "GUID")
    String guid

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
     * DATA ORIGIN: Source system that created or updated the section
     */
    @Column(name = "DATAORIGIN")
    String dataOrigin

    /**
     * Type of Honors
     * */
    @Column(name = "HONOR_TYPE")
    String type


    public static AcademicHonorView fetchByCode( String code,String type) {
        def academicHonor
        AcademicHonorView.withSession {
            session ->
                academicHonor = session.getNamedQuery( 'AcademicHonorView.fetchByCode' ).setString( 'code', code ).setString('type',type).uniqueResult()
        }
        return academicHonor
    }

    public static List fetchByType(String type){
        def academicHonors=[]
        AcademicHonorView.withSession {
            session ->
                academicHonors = session.getNamedQuery( 'AcademicHonorView.fetchByType' ).setString( 'type', type ).list()
        }
        return academicHonors
    }

    public static AcademicHonorView fetchByGuid( String guid) {
        def academicHonor
        AcademicHonorView.withSession {
            session ->
                academicHonor = session.getNamedQuery( 'AcademicHonorView.fetchByGuid' ).setString( 'guid', guid ).uniqueResult()
        }
        return academicHonor
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        AcademicHonorView that = (AcademicHonorView) o

        if (code != that.code) return false
        if (dataOrigin != that.dataOrigin) return false
        if (description != that.description) return false
        if (guid != that.guid) return false
        if (type != that.type) return false

        return true
    }

    int hashCode() {
        int result
        result = (guid != null ? guid.hashCode() : 0)
        result = 31 * result + (code != null ? code.hashCode() : 0)
        result = 31 * result + (description != null ? description.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        result = 31 * result + (type != null ? type.hashCode() : 0)
        return result
    }


    @Override
    public String toString() {
        return "AcademicHonorView{" +
                "guid='" + guid + '\'' +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", dataOrigin='" + dataOrigin + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
