/*******************************************************************************
 Copyright 2014 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general.overall

import org.hibernate.annotations.Type

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = 'GOVROLE')
class Role implements Serializable {

    /**
     * Internal identification number of the person
     */
    @Column(name = "SPRIDEN_PIDM")
    Integer pidm

    /**
     * Student role indicator
     */
    @Column(name = "GOVROLE_STUDENT_IND")
    @Type(type = "yes_no")
    Boolean studentIndicator = false

    /**
     * Faculty role indicator
     */
    @Column(name = "GOVROLE_FACULTY_IND")
    @Type(type = "yes_no")
    Boolean facultyIndicator = false

    /**
     * Employee role indicator
     */
    @Column(name = "GOVROLE_EMPLOYEE_IND")
    @Type(type = "yes_no")
    Boolean employeeIndicator = false

    /**
     * Alumni role indicator
     */
    @Column(name = "GOVROLE_ALUMNI_IND")
    @Type(type = "yes_no")
    Boolean alumniIndicator = false

    /**
     * Finance role indicator
     */
    @Column(name = "GOVROLE_FINANCE_IND")
    @Type(type = "yes_no")
    Boolean financeIndicator = false

    /**
     * Friend role indicator
     */
    @Column(name = "GOVROLE_FRIEND_IND")
    @Type(type = "yes_no")
    Boolean friendIndicator = false

    /**
     * Finaid role indicator
     */
    @Column(name = "GOVROLE_FINAID_IND")
    @Type(type = "yes_no")
    Boolean finaidIndicator = false

    /**
     * Student Aid for Canada role indicator
     */
    @Column(name = "GOVROLE_BSAC_IND")
    @Type(type = "yes_no")
    Boolean studentAidForCanadaIndicator = false


    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false
        Role role = (Role) o
        if (alumniIndicator != role.alumniIndicator) return false
        if (employeeIndicator != role.employeeIndicator) return false
        if (facultyIndicator != role.facultyIndicator) return false
        if (finaidIndicator != role.finaidIndicator) return false
        if (financeIndicator != role.financeIndicator) return false
        if (friendIndicator != role.friendIndicator) return false
        if (pidm != role.pidm) return false
        if (studentAidForCanadaIndicator != role.studentAidForCanadaIndicator) return false
        if (studentIndicator != role.studentIndicator) return false
        return true
    }


    int hashCode() {
        int result
        result = (pidm != null ? pidm.hashCode() : 0)
        result = 31 * result + (studentIndicator != null ? studentIndicator.hashCode() : 0)
        result = 31 * result + (facultyIndicator != null ? facultyIndicator.hashCode() : 0)
        result = 31 * result + (employeeIndicator != null ? employeeIndicator.hashCode() : 0)
        result = 31 * result + (alumniIndicator != null ? alumniIndicator.hashCode() : 0)
        result = 31 * result + (financeIndicator != null ? financeIndicator.hashCode() : 0)
        result = 31 * result + (friendIndicator != null ? friendIndicator.hashCode() : 0)
        result = 31 * result + (finaidIndicator != null ? finaidIndicator.hashCode() : 0)
        result = 31 * result + (studentAidForCanadaIndicator != null ? studentAidForCanadaIndicator.hashCode() : 0)
        return result
    }


    public String toString() {
        """Role[
                pidm=$pidm,
                studentIndicator=$studentIndicator,
                facultyIndicator=$facultyIndicator,
                employeeIndicator=$employeeIndicator,
                alumniIndicator=$alumniIndicator,
                financeIndicator=$financeIndicator,
                friendIndicator=$friendIndicator,
                finaidIndicator=$finaidIndicator,
                studentAidForCanadaIndicator=$studentAidForCanadaIndicator]"""
    }
}
