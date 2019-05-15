/*********************************************************************************
 Copyright 2014-2016 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
package net.hedtech.banner.general.utility

import net.hedtech.banner.query.DynamicFinder

import javax.persistence.*

/**
 * Information Text table.
 */
@Entity
@Table(name = "GURINFO")
@NamedQueries(value = [
        @NamedQuery(name = "InformationText.fetchInfoTextByRole",
                query = """ FROM InformationText a
                               WHERE a.pageName = :pageName
                               AND a.persona IN (:roleCode)
                               AND a.locale IN (:locale)
                               AND trunc(sysdate) BETWEEN trunc(NVL( a.startDate, (sysdate - 1) ) ) AND trunc( NVL( a.endDate, (sysdate + 1) ))
                               ORDER BY a.label, a.sequenceNumber """),
        @NamedQuery(name = "InformationText.fetchInfoTextByRoleAndLabel",
                query = """ FROM InformationText a
                                   WHERE a.pageName = :pageName
                                   AND a.persona IN (:roleCode)
                                   AND a.locale IN (:locale)
                                   AND a.label = :labelText
                                   AND trunc(sysdate) BETWEEN trunc(NVL( a.startDate, (sysdate - 1) ) ) AND trunc( NVL( a.endDate, (sysdate + 1) ))
                                   ORDER BY a.label, a.sequenceNumber """),
        @NamedQuery(name = "InformationText.fetchInfoTextByRoleAndLabels",
                query = """ FROM InformationText a
                                   WHERE a.pageName = :pageName
                                   AND a.persona IN (:roleCode)
                                   AND a.locale IN (:locale)
                                   AND a.label IN (:labelText)
                                   AND trunc(sysdate) BETWEEN trunc(NVL( a.startDate, (sysdate - 1) ) ) AND trunc( NVL( a.endDate, (sysdate + 1) ))
                                   ORDER BY a.label, a.sequenceNumber """)

])
class InformationText implements Serializable {

    @Id
    @Column(name = "GURINFO_SURROGATE_ID")
    @SequenceGenerator(name = "GURINFO_SEQ_GEN", allocationSize = 1, sequenceName = "GURINFO_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GURINFO_SEQ_GEN")
    Long id

    /**
     * PAGE NAME: Name of associated page.
     */
    @Column(name = "GURINFO_PAGE_NAME")
    String pageName

    /**
     * LABEL: Short label, used to select which set of text items to print for a page.
     */
    @Column(name = "GURINFO_LABEL")
    String label

    /**
     * TEXT TYPE: Type of text.    Types - NF- Notification, T-Tooltip, P-Panel, H-Header, N - Normal
     */
    @Column(name = "GURINFO_TEXT_TYPE")
    String textType

    /**
     * SEQUENCE NUMBER : Sequence number for this text item.
     */
    @Column(name = "GURINFO_SEQUENCE_NUMBER")
    Integer sequenceNumber

    /**
     * ROLE CODE: Role associated with the text.
     */
    @Column(name = "GURINFO_ROLE_CODE")
    String persona

    /**
     * TEXT: Text to be displayed on web page when this item is selected.
     */
    @Column(name = "GURINFO_TEXT")
    String text

    /**
     * LOCALE: Locale of the text
     */
    @Column(name = "GURINFO_LOCALE")
    String locale

    /**
     * COMMENT: Comment about this text item.
     */
    @Column(name = "GURINFO_COMMENT")
    String comment

    /**
     * START DATE: The date from when the text should be displayed.
     */
    @Column(name = "GURINFO_START_DATE")
    @Temporal(TemporalType.DATE)
    Date startDate

    /**
     * END DATE: The date until when the text should be displayed.
     */
    @Column(name = "GURINFO_END_DATE")
    @Temporal(TemporalType.DATE)
    Date endDate

    /**
     * SOURCE INDICATOR : Source Indicator: This field indicates if the row is (B)aseline or (L)ocal. The default value is B.
     */
    @Column(name = "GURINFO_SOURCE_INDICATOR")
    String sourceIndicator = 'B'

    /**
     * DATA ORIGIN: Source system that created or updated the data.
     */
    @Column(name = "GURINFO_DATA_ORIGIN")
    String dataOrigin

    /**
     * ACTIVITY DATE: The date that information in this record was entered or last updated.
     */
    @Column(name = "GURINFO_ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * USER ID: The user ID of the person who inserted or last updated this record.
     */
    @Column(name = "GURINFO_USER_ID")
    String lastModifiedBy

    /**
     * VERSION: Optimistic lock token.
     */
    @Version
    @Column(name = "GURINFO_VERSION",precision = 19)
    Long version

    //Static Literal constants defined for the query params
    private static final String PAGENAME = "pageName"
    private static final String ROLECODE = "roleCode"
    private static final String LOCALE ="locale"
    private static final String LABEL_TEXT = "labelText"
    private static final String BASE_LINE = "baseline"
    private static final String LOCAL ="local"

    public String toString() {
        """InformationText[
					id=$id,
					pageName=$pageName,
                    label=$label,
					textType=$textType,
					sequenceNumber=$sequenceNumber,
					persona=$persona,
					text=$text,
					locale=$locale,
                    comment=$comment,
                    startDate=$startDate,
                    endDate=$endDate,
                    sourceIndicator=$sourceIndicator,
					lastModified=$lastModified,
					lastModifiedBy=$lastModifiedBy,
                    version=$version,
					dataOrigin=$dataOrigin]"""
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof InformationText)) return false
        InformationText that = (InformationText) o
        if (id != that.id) return false
        if (pageName != that.pageName) return false
        if (label != that.label) return false
        if (textType != that.textType) return false
        if (sequenceNumber != that.sequenceNumber) return false
        if (persona != that.persona) return false
        if (text != that.text) return false
        if (locale != that.locale) return false
        if (comment != that.comment) return false
        if (startDate != that.startDate) return false
        if (endDate != that.endDate) return false
        if (sourceIndicator != that.sourceIndicator) return false
        if (version != that.version) return false
        if (lastModified != that.lastModified) return false
        if (lastModifiedBy != that.lastModifiedBy) return false
        if (dataOrigin != that.dataOrigin) return false
        return true
    }

    int hashCode() {
        int result
        result = (id != null ? id.hashCode() : 0)
        result = 31 * result + (pageName != null ? pageName.hashCode() : 0)
        result = 31 * result + (label != null ? label.hashCode() : 0)
        result = 31 * result + (textType != null ? textType.hashCode() : 0)
        result = 31 * result + (sequenceNumber != null ? sequenceNumber.hashCode() : 0)
        result = 31 * result + (persona != null ? persona.hashCode() : 0)
        result = 31 * result + (locale != null ? locale.hashCode() : 0)
        result = 31 * result + (text != null ? text.hashCode() : 0)
        result = 31 * result + (comment != null ? comment.hashCode() : 0)
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0)
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0)
        result = 31 * result + (sourceIndicator != null ? sourceIndicator.hashCode() : 0)
        result = 31 * result + (version != null ? version.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        return result
    }

    static constraints = {
        pageName(blank: false, nullable: false, maxSize: 200)
        label(blank: false, nullable: false, maxSize: 200)
        textType(blank: false, nullable: false, maxSize: 20, inList:['NF','T','P','H','N'])
        sequenceNumber(nullable: false, min: 0, max: 99999)
        persona(blank: false, nullable: false, maxSize: 30)
        locale(blank: false, nullable: false, maxSize: 20)
        sourceIndicator(blank: false, nullable: false, maxSize: 1, inList:['B','L'])
        text(nullable: true, maxSize: 4000)
        comment(nullable: true, maxSize: 2000)
        startDate(nullable: true)
        endDate(nullable: true, validator: { val, obj ->
            if(val && !obj.startDate){
                return 'invalid.missingStartDate'
            }
            if ( val && obj.startDate && (val < obj.startDate) ) {
                return 'invalid.startDateLessThanEndDate'
            }
        } )
        dataOrigin(nullable: true, maxSize: 30)
        lastModifiedBy(nullable: true, maxSize: 30)
        lastModified(nullable: true)
    }

    def static countAll(filterData) {
        finderByAll().count(filterData)
    }


    def static fetchSearch(filterData, pagingAndSortParams) {
        def informationTexts = finderByAll().find(filterData, pagingAndSortParams)
        return informationTexts
    }


    def private static finderByAll = {
        def query = """FROM  InformationText a"""
        return new DynamicFinder(InformationText.class, query, "a")
    }

    /**
     * fetchInfoTextByRoles method returns info text for the given roles irrespective of the labels.
     * @param pageName
     * @param roleCode
     * @param locale
     * @return
     */

    public static List<InformationText> fetchInfoTextByRoles(String pageName, List<String> roleCode, List<String> locale) {
        def infoTextResponse = []
        roleCode.collate(1000).each { miniRoleCode ->
            InformationText.withSession {session ->
                def infoText =  session.getNamedQuery('InformationText.fetchInfoTextByRole')
                        .setString(PAGENAME, pageName)
                        .setParameterList(ROLECODE, miniRoleCode)
                        .setParameterList(LOCALE,locale)
                        .list()
                infoTextResponse << infoText
            }
        }
        infoTextResponse.flatten().unique()
    }


    /**
     * fetchInfoTextByRolesAndLabel method returns info text for the given roles and
     * specific label provided.
     * @param pageName
     * @param roleCode
     * @param locale
     * @param label
     * @return
     */
    public static List<InformationText> fetchInfoTextByRolesAndLabel(String pageName, List<String> roleCode, List<String> locale, String label) {
        def infoTextResponse = []
        roleCode.collate(1000).each { miniRoleCode ->
            InformationText.withSession { session ->
                def infoText = session.getNamedQuery('InformationText.fetchInfoTextByRoleAndLabel')
                        .setString(PAGENAME, pageName)
                        .setParameterList(ROLECODE, miniRoleCode)
                        .setParameterList(LOCALE, locale)
                        .setString(LABEL_TEXT, label)
                        .list()
                infoTextResponse << infoText
            }
        }
        infoTextResponse.flatten().unique()
    }


    /**
     * fetchInfoTextByRolesAndLabels method returns Tool tip and help text info for the given roles and
     * specific label provided.
     * @param pageName
     * @param roleCode
     * @param locale
     * @param label
     * @return
     */
    public static List<InformationText> fetchInfoTextByRoleAndLabels(String pageName, List<String> roleCode, List<String> locale, List<String> label) {
        def infoTextResponse = []
        roleCode.collate(1000).each { miniRoleCode ->
            InformationText.withSession { session ->
                def infoText = session.getNamedQuery('InformationText.fetchInfoTextByRoleAndLabels')
                        .setString(PAGENAME, pageName)
                        .setParameterList(ROLECODE, miniRoleCode)
                        .setParameterList(LOCALE, locale)
                        .setParameterList(LABEL_TEXT, label)
                        .list()
                infoTextResponse << infoText
            }
        }
        infoTextResponse.flatten().unique()
    }

}