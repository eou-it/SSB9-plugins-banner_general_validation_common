/*********************************************************************************
 Copyright 2009-2011 SunGard Higher Education. All Rights Reserved.
 This copyrighted software contains confidential and proprietary information of 
 SunGard Higher Education and its subsidiaries. Any use of this software is limited 
 solely to SunGard Higher Education licensees, and is further subject to the terms 
 and conditions of one or more written license agreements between SunGard Higher 
 Education and the licensee in question. SunGard, Banner and Luminis are either 
 registered trademarks or trademarks of SunGard Higher Education in the U.S.A. 
 and/or other regions and/or countries.
 **********************************************************************************/
/**
 Banner Automator Version: 0.1.1
 Generated: Mon Jan 03 15:56:54 CST 2011
 */
package com.sungardhe.banner.general.system

import org.hibernate.annotations.Type
import javax.persistence.*

/**
 * Learner Field of Study Type Validation Table
 */
@Entity
@Table(name = "GTVLFST")
class LearnerFieldStudy implements Serializable {

  /**
   * Surrogate ID for GTVLFST
   */
  @Id
  @Column(name = "GTVLFST_SURROGATE_ID")
  @SequenceGenerator(name = "GTVLFST_SEQ_GEN", allocationSize = 1, sequenceName = "GTVLFST_SURROGATE_ID_SEQUENCE")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GTVLFST_SEQ_GEN")
  Long id

  /**
   * FST CODE: Field of Study type code.
   */
  @Column(name = "GTVLFST_CODE", nullable = false, unique = true, length = 15)
  String code

  /**
   * FST DESCRIPTION: Description of the Field of Study Type code.
   */
  @Column(name = "GTVLFST_DESC", nullable = false, length = 30)
  String description

  /**
   * None
   */
  @Type(type = "yes_no")
  @Column(name = "GTVLFST_SYS_REQ_IND", nullable = false)
  Boolean systemRequiredIndicator

  /**
   * USER ID: The most recent user to create or update a record.
   */
  @Column(name = "GTVLFST_USER_ID")
  String lastModifiedBy

  /**
   * ACTIVITY DATE: The most recent date a record was created or updated
   */
  @Column(name = "GTVLFST_ACTIVITY_DATE")
  @Temporal(TemporalType.TIMESTAMP)
  Date lastModified

  /**
   * Version column which is used as a optimistic lock token for GTVLFST
   */
  @Version
  @Column(name = "GTVLFST_VERSION", nullable = false, precision = 19)
  Long version

  /**
   * Data Origin column for GTVLFST
   */
  @Column(name = "GTVLFST_DATA_ORIGIN", length = 30)
  String dataOrigin



  public String toString() {
    "LearnerFieldStudy[id=$id, code=$code, description=$description, systemRequiredIndicator=$systemRequiredIndicator, lastModifiedBy=$lastModifiedBy, lastModified=$lastModified, version=$version, dataOrigin=$dataOrigin]"
  }

  static constraints = {
    code(nullable: false, maxSize: 15)
    description(nullable: false, maxSize: 30)
    systemRequiredIndicator(nullable: false)
    dataOrigin(nullable: false, maxSize: 30)
  }


  boolean equals(o) {
    if (this.is(o)) return true

    if (!(o instanceof LearnerFieldStudy)) return false

    LearnerFieldStudy that = (LearnerFieldStudy) o

    if (code != that.code) return false
    if (dataOrigin != that.dataOrigin) return false
    if (description != that.description) return false
    if (id != that.id) return false
    if (lastModified != that.lastModified) return false
    if (lastModifiedBy != that.lastModifiedBy) return false
    if (systemRequiredIndicator != that.systemRequiredIndicator) return false
    if (version != that.version) return false

    return true
  }


  int hashCode() {
    int result

    result = (id != null ? id.hashCode() : 0)
    result = 31 * result + (code != null ? code.hashCode() : 0)
    result = 31 * result + (description != null ? description.hashCode() : 0)
    result = 31 * result + (systemRequiredIndicator != null ? systemRequiredIndicator.hashCode() : 0)
    result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
    result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
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
  /*PROTECTED REGION ID(learnerfieldstudy_custom_methods) ENABLED START*/

  /*PROTECTED REGION END*/
}
