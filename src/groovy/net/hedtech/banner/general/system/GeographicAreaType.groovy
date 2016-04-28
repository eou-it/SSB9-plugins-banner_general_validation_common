/*******************************************************************************
 Copyright 2016 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import javax.persistence.*

/**
 * Geographic Area Types Read only view table.
 */


@Entity
@Table(name="GVQ_GEOGRAPHIC_AREA_TYPES")
@EqualsAndHashCode(includeFields = true)
@ToString(includeNames = true, includeFields = true)
@NamedQueries(value = [
        @NamedQuery(name = "GeographicAreaType.fetchByGuid", query = """FROM GeographicAreaType p WHERE p.id = :guid"""),
        @NamedQuery(name = "GeographicAreaType.fetchAll", query = """FROM GeographicAreaType"""),
        @NamedQuery(name = "GeographicAreaType.countAll", query = """SELECT count(*) FROM GeographicAreaType""")
])
class GeographicAreaType implements Serializable{

    /**
     * The geographic area type guid
      */
    @Id
    @Column(name="GAT_GUID")
    String id

    /**
     * The geographic area type code
     */
    @Column(name="GAT_CODE")
    String code

    /**
     * The geographic area type description
     */
    @Column(name="GAT_DESCRIPTION")
    String description

    /**
     * The geographic area type title
     */
    @Column(name="GAT_TITLE")
    String title

    /**
     * The geographic area type version
     */
    @Column(name="GAT_VERSION")
    Long version

    /**
     * returns the geographic are type based on guid
     * @param guid
     * @return
     */
    public static GeographicAreaType fetchByGuid(String guid){
     return GeographicAreaType.withSession{ session ->
            session.getNamedQuery('GeographicAreaType.fetchByGuid').setString('guid',guid).uniqueResult()
        }
    }

    /**
     * returns the list of geographic are types
     * @param params
     * @return
     */
    public static List<GeographicAreaType> fetchAll(Map params){
        return GeographicAreaType.withSession{ session ->
            session.getNamedQuery('GeographicAreaType.fetchAll')
                    .setMaxResults(params?.max as Integer)
                    .setFirstResult((params?.offset ?: '0') as Integer)
                    .list();
        }
    }

    /**
     * returns the count of geographic are types
     * @return
     */
    public static Long countAll(){
        return GeographicAreaType.withSession{ session ->
            session.getNamedQuery('GeographicAreaType.countAll').uniqueResult()
        }
    }
}
