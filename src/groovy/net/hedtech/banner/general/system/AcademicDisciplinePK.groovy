package net.hedtech.banner.general.system

import org.hibernate.annotations.Type

import javax.persistence.Column
import javax.persistence.Embeddable

/**
 * Created by invthannee on 6/3/2015.
 */
@Embeddable
class AcademicDisciplinePK implements Serializable {

    /**
     * Surrogate ID for STVMAJR
     */

    @Column(name = "STVMAJR_SURROGATE_ID")
    Long surrogateId

    /**
     * This field indicates whether the area of study is a valid major.
     */
    @Type(type = "yes_no")
    @Column(name = "STVMAJR_VALID_MAJOR_IND")
    Boolean validMajorIndicator

    /**
     * This field indicates whether the area of study is a valid minor.
     */
    @Type(type = "yes_no")
    @Column(name = "STVMAJR_VALID_MINOR_IND")
    Boolean validMinorIndicator

    /**
     * This field indicates whether the area of study is a valid concentration.
     */
    @Type(type = "yes_no")
    @Column(name = "STVMAJR_VALID_CONCENTRATN_IND")
    Boolean validConcentratnIndicator


}

