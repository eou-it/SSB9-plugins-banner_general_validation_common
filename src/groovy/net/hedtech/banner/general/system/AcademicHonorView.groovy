package net.hedtech.banner.general.system
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.NamedQueries
import javax.persistence.NamedQuery
import javax.persistence.Table
/**
 * <p> Academic Honor View to retrive the Honors </p>
 * @author Sitakant
 */

@Entity
@Table(name = "svq_stvhonr_stvhond")
@NamedQueries(value = [
        @NamedQuery(name = "AcademicHonorView.fetchByGuid",
                query = """FROM  AcademicHonorView honor
		   WHERE honor.guid=:guid"""),
        @NamedQuery(name = "AcademicHonorView.fetchByType",
                query = """FROM  AcademicHonorView honor
		   WHERE honor.type=:type"""),
        @NamedQuery(name = "AcademicHonorView.fetchByCode",
                query = """FROM  AcademicHonorView honor
		   WHERE honor.code=:code""")
])
class AcademicHonorView implements Serializable{

    /**
     * Guid for Honors
     * */
    @Id
    @Column(name = "GUID")
    String guid

    /**
     * Code for Honor . Primary Key for STVHOND and STVHONR
     * */
    @Column(name = "CODE")
    String code

    /**
     * Description about the Honors
     * */
    @Column(name = "DESCRIPTION")
    String description

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


    public static AcademicHonorView fetchByCode( String code) {
        def academicHonor
        AcademicHonorView.withSession {
            session ->
                academicHonor = session.getNamedQuery( 'AcademicHonorView.fetchByCode' ).setString( 'code', code ).uniqueResult()
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
}
