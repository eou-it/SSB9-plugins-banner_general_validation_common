/** *****************************************************************************
 Copyright 2020 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import javax.persistence.*

/**
 * Comment Type Validation Table
 */

@Entity
@Table(name = "STVCMTT")
@NamedQueries(value = [
        @NamedQuery(name = "CommentTypeValidation.fetchByCode",
                query = """FROM CommentTypeValidation a WHERE a.code = :code)""")

])
class CommentTypeValidation implements Serializable {

    /**
     * Surrogate ID for STVTERM
     */
    @Id
    @Column(name = "STVCMTT_SURROGATE_ID")
    @SequenceGenerator(name = "STVCMTT_SEQ_GEN", allocationSize = 1, sequenceName = "STVCMTT_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STVCMTT_SEQ_GEN")
    Long id

    /**
     * This field identifies the comment type code referenced on the Comment Form(SPACMNT)
     */
    @Column(name = "STVCMTT_CODE", nullable = false, unique = true, length = 6)
    String code

    /**
     * This field specifies the free-format comment associated with the comment code.
     */
    @Column(name = "STVCMTT_DESC", nullable = false, length = 30)
    String description

    /**
     * This field identifies the most recent date a record was created or updated.
     */
    @Column(name = "STVCMTT_ACTIVITY_DATE", nullable = false)
    @Temporal(TemporalType.DATE)
    Date lastModified

    /**
     * Optimistic Lock Token for STVCMTT
     */
    @Version
    @Column(name = "STVCMTT_VERSION", nullable = false, precision = 19)
    Long version


    /**
     * The user ID of the person who inserted or last updated this record.
     */
    @Column(name = "STVCMTT_USER_ID")
    String lastModifiedBy

    /**
     * Column for storing data origin for STVCMTT
     */
    @Column(name = "STVCMTT_DATA_ORIGIN")
    String dataOrigin

    public static def fetchByCode(String code) {
        def commentType
        CommentTypeValidation.withSession { session ->
            commentType = session.getNamedQuery('CommentTypeValidation.fetchByCode').setString('code', code).uniqueResult()
        }
        return commentType
    }
}
