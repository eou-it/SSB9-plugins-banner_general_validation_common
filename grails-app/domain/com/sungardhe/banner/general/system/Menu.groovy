/** *****************************************************************************
 © 2011 SunGard Higher Education.  All Rights Reserved.

 CONFIDENTIAL BUSINESS INFORMATION

 THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 ****************************************************************************** */
/**
 Banner Automator Version: 0.1.1
 Generated: Fri Apr 01 14:11:59 IST 2011
 */
package com.sungardhe.banner.general.system

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.Version
import javax.persistence.Temporal
import javax.persistence.TemporalType
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Type

/**
 * Menu Code Table
 */
/*PROTECTED REGION ID(menu_namedqueries) ENABLED START*/
//TODO: NamedQueries that needs to be ported:
/**
 * Where clause on this entity present in forms:
 * Order by clause on this entity present in forms:
 * Form Name: GTVMENU
 *  gtvmenu_code

 */
/*PROTECTED REGION END*/
@Entity
@Table(name = "GTVMENU")
class Menu implements Serializable {

    /**
     * Surrogate ID for GTVMENU
     */
    @Id
    @Column(name = "GTVMENU_SURROGATE_ID")
    @GeneratedValue(generator = "triggerAssigned")
    @GenericGenerator(name = "triggerAssigned", strategy = "com.sungardhe.banner.framework.persistence.util.TriggerAssignedIdentityGenerator")
    Long id

    /**
     * Optimistic lock token for GTVMENU
     */
    @Version
    @Column(name = "GTVMENU_VERSION", nullable = false, precision = 19)
    Long version

    /**
     * Menu Code.
     */
    @Column(name = "GTVMENU_CODE", nullable = false, unique = true, length = 10)
    String code

    /**
     * Menu Code Description.
     */
    @Column(name = "GTVMENU_DESC", nullable = false, length = 30)
    String description

    /**
     * Date menu code was last created or modified.
     */
    @Column(name = "GTVMENU_ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * Last modified by column for GTVMENU
     */
    @Column(name = "GTVMENU_USER_ID", length = 30)
    String lastModifiedBy

    /**
     * Data origin column for GTVMENU
     */
    @Column(name = "GTVMENU_DATA_ORIGIN", length = 30)
    String dataOrigin



    public String toString() {
        """Menu[
					id=$id,
					version=$version,
					code=$code,
					description=$description,
					lastModified=$lastModified,
					lastModifiedBy=$lastModifiedBy,
					dataOrigin=$dataOrigin]"""
    }


    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof Menu)) return false
        Menu that = (Menu) o
        if (id != that.id) return false
        if (version != that.version) return false
        if (code != that.code) return false
        if (description != that.description) return false
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
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        return result
    }


    static constraints = {
        code(nullable: false, maxSize: 10)
        description(nullable: false, maxSize: 30)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
        /**
         * Please put all the custom constraints in this protected section to protect the code
         * from being overwritten on re-generation
         */
        /*PROTECTED REGION ID(menu_custom_constraints) ENABLED START*/

        /*PROTECTED REGION END*/
    }

    /*PROTECTED REGION ID(menu_readonly_properties) ENABLED START*/
    //Read Only fields that should be protected against update
    public static readonlyProperties = ['code']
    /*PROTECTED REGION END*/
    /**
     * Please put all the custom/transient attributes with @Transient annotations in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(menu_custom_attributes) ENABLED START*/

    /*PROTECTED REGION END*/

    /**
     * Please put all the custom methods/code in this protected section to protect the code
     * from being overwritten on re-generation
     */
    /*PROTECTED REGION ID(menu_custom_methods) ENABLED START*/

    /*PROTECTED REGION END*/
}
