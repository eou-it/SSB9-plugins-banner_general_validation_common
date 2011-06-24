/** *****************************************************************************
 Â© 2011 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 ****************************************************************************** */
/**
 Banner Automator Version: 0.1.1
 Generated: Mon Jan 03 15:56:54 CST 2011
 */
package com.sungardhe.banner.general.system

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
