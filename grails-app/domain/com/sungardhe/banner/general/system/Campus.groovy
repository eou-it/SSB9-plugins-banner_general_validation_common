/*********************************************************************************
 Copyright 2009-2011 SunGard Higher Education. All Rights Reserved.
 This copyrighted software contains confidential and proprietary information of 
 SunGard Higher Education and its subsidiaries. Any use of this software is limited 
 solely to SunGard Higher Education licensees, and is further subject to the terms 
 and conditions of one or more written license agreements between SunGard Higher 
 Education and the licensee in question. SunGard is either a registered trademark or
 trademark of SunGard Data Systems in the U.S.A. and/or other regions and/or countries.
 Banner and Luminis are either registered trademarks or trademarks of SunGard Higher 
 Education in the U.S.A. and/or other regions and/or countries.
 **********************************************************************************/
/**
 Banner Automator Version: 0.1.1
 Generated: Mon Jan 03 15:56:54 CST 2011
 */
package com.sungardhe.banner.general.system

import org.hibernate.annotations.Type
import javax.persistence.*

/**
 * Campus Validation Table
 */
@Entity
@Table(name = "STVCAMP")
class Campus implements Serializable {

  /**
   * Surrogate ID for STVCAMP.
   */
  @Id
  @Column(name = "STVCAMP_SURROGATE_ID")
  @SequenceGenerator(name = "STVCAMP_SEQ_GEN", allocationSize = 1, sequenceName = "STVCAMP_SURROGATE_ID_SEQUENCE")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVCAMP_SEQ_GEN")
  Long id

  /**
   * STVCAMP_DICD_CODE: District Identifier Code validated by form GTVDICD.
   */
  @Column(name = "STVCAMP_CODE", nullable = false, unique = true, length = 3)
  String code

  /**
   * This field defines the institution"s campus associated with the campus code.
   */
  @Column(name = "STVCAMP_DESC", length = 30)
  String description

  /**
   * This field identifies the most recent date a record was created or updated.
   */
  @Column(name = "STVCAMP_ACTIVITY_DATE")
  @Temporal(TemporalType.TIMESTAMP)
  Date lastModified

  /**
   * Foreign Key : FKV_STVCAMP_INV_GTVDICD_CODE
   */
  @ManyToOne
  @JoinColumns([
  @JoinColumn(name = "STVCAMP_DICD_CODE", referencedColumnName = "GTVDICD_CODE")
  ])
  GeographicRegionAsCostCenterInformationByDisctirctOrDivision districtIdentifierCode

  /**
   * Column for storing last modified by for STVCAMP.
   */
  @Column(name = "STVCAMP_USER_ID", nullable = false, length = 30)
  String lastModifiedBy

  /**
   * Optimistic Lock Token for STVCAMP.
   */
  @Version
  @Column(name = "STVCAMP_VERSION", nullable = false, precision = 19)
  Long version

  /**
   * Column for storing data origin for STVCAMP.
   */
  @Column(name = "STVCAMP_DATA_ORIGIN", length = 30)
  String dataOrigin


  public String toString() {
    "Campus[id=$id, code=$code, description=$description, lastModified=$lastModified, districtIdentifierCode=$districtIdentifierCode, lastModifiedBy=$lastModifiedBy, version=$version, dataOrigin=$dataOrigin]"
  }

  static constraints = {
    code(nullable: false, maxSize: 3)
    description(nullable: true, maxSize: 30)
    districtIdentifierCode(nullable: true)
  }


  boolean equals(o) {
    if (this.is(o)) return true

    if (!(o instanceof Campus)) return false

    Campus campus = (Campus) o

    if (code != campus.code) return false
    if (dataOrigin != campus.dataOrigin) return false
    if (description != campus.description) return false
    if (districtIdentifierCode != campus.districtIdentifierCode) return false
    if (id != campus.id) return false
    if (lastModified != campus.lastModified) return false
    if (lastModifiedBy != campus.lastModifiedBy) return false
    if (version != campus.version) return false

    return true
  }


  int hashCode() {
    int result

    result = (id != null ? id.hashCode() : 0)
    result = 31 * result + (code != null ? code.hashCode() : 0)
    result = 31 * result + (description != null ? description.hashCode() : 0)
    result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
    result = 31 * result + (districtIdentifierCode != null ? districtIdentifierCode.hashCode() : 0)
    result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
    result = 31 * result + (version != null ? version.hashCode() : 0)
    result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
    return result
  }

  //Read Only fields that should be protected against update
  public static readonlyProperties = ['code']
  /**
   * Please put all the custom methods/code in this protected section to protect the code
   * from being overwritten on re-generation
   */
  /*PROTECTED REGION ID(campus_custom_methods) ENABLED START*/

  /*PROTECTED REGION END*/
}
