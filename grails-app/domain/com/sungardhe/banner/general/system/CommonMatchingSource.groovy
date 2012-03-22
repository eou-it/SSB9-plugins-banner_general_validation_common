/** *****************************************************************************
 Copyright 2009-2011 SunGard Higher Education. All Rights Reserved.

 This copyrighted software contains confidential and proprietary information of
 SunGard Higher Education and its subsidiaries. Any use of this software is limited
 solely to SunGard Higher Education licensees, and is further subject to the terms
 and conditions of one or more written license agreements between SunGard Higher
 Education and the licensee in question. SunGard is either a registered trademark or
 trademark of SunGard Data Systems in the U.S.A. and/or other regions and/or countries.
 Banner and Luminis are either registered trademarks or trademarks of SunGard Higher
 Education in the U.S.A. and/or other regions and/or countries.
 ****************************************************************************** */
/**
 Banner Automator Version: 1.29
 Generated: Fri Mar 16 11:44:04 EDT 2012
 */
package com.sungardhe.banner.general.system

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.Version
import javax.persistence.NamedQueries
import javax.persistence.NamedQuery
import javax.persistence.Transient

import javax.persistence.GenerationType
import javax.persistence.SequenceGenerator

import org.hibernate.annotations.GenericGenerator

import org.hibernate.annotations.Type
import javax.persistence.Temporal
import javax.persistence.TemporalType

/**
 * Common Matching Source Code Table
 */
@Entity
@Table(name = "GTVCMSC")
class CommonMatchingSource implements Serializable {

    /**
     * Surrogate ID for GTVCMSC
     */
    @Id
    @Column(name = "GTVCMSC_SURROGATE_ID")
    @SequenceGenerator(name = "GTVCMSC_SEQ_GEN", allocationSize = 1, sequenceName = "GTVCMSC_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GTVCMSC_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for GTVCMSC
     */
    @Version
    @Column(name = "GTVCMSC_VERSION", nullable = false, precision = 19)
    Long version

    /**
     * SOURCE CODE: Code to identify the source of data to by used by Common Matching process.
     */
    @Column(name = "GTVCMSC_CODE", nullable = false, unique = true, length = 20)
    String code

    /**
     * DESCRIPTION: Description associated with Source Code to be used by Common Matching Rules.
     */
    @Column(name = "GTVCMSC_DESC", nullable = false, length = 60)
    String description

    /**
     * LONG DESCRIPTION: Column to hold a long description.
     */
    @Column(name = "GTVCMSC_LONG_DESC", length = 4000)
    String longDescription

    /**
     * ACTIVITY DATE: Date record was created or last updated.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "GTVCMSC_ACTIVITY_DATE")
    Date lastModified

    /**
     * USER ID: User ID of the User who created or last updated the record.
     */
    @Column(name = "GTVCMSC_USER_ID", length = 30)
    String lastModifiedBy

    /**
     * DATA ORIGIN: Source system that created or updated the row
     */
    @Column(name = "GTVCMSC_DATA_ORIGIN", length = 30)
    String dataOrigin



    public String toString() {
        """CommonMatchingSource[
					id=$id,
					version=$version,
					code=$code,
					description=$description,
					longDescription=$longDescription,
					lastModified=$lastModified,
					lastModifiedBy=$lastModifiedBy,
					dataOrigin=$dataOrigin]"""
    }


    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof CommonMatchingSource)) return false
        CommonMatchingSource that = (CommonMatchingSource) o
        if (id != that.id) return false
        if (version != that.version) return false
        if (code != that.code) return false
        if (description != that.description) return false
        if (longDescription != that.longDescription) return false
        if (lastModified != that.lastModified) return false
        if (lastModifiedBy != that.lastModifiedBy) return false
        if (dataOrigin != that.dataOrigin) return false
        return true
    }


    int hashCode() {
        int result
        result = (id != null ? id.hashCode() : 0)
        result = 31 * result + (version != null ? version.hashCode() : 0)
        result = 31 * result + (code != null ? code.hashCode() : 0)
        result = 31 * result + (description != null ? description.hashCode() : 0)
        result = 31 * result + (longDescription != null ? longDescription.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        return result
    }


    static constraints = {
        code(nullable: false, maxSize: 20)
        description(nullable: false, maxSize: 60)
        longDescription(nullable: true, maxSize: 4000)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }

    //Read Only fields that should be protected against update
    public static readonlyProperties = ['code']
}
