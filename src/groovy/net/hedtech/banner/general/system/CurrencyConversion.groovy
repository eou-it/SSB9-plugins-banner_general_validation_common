/** *****************************************************************************
 Copyright 2017 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import net.hedtech.banner.general.crossproduct.Bank

import javax.persistence.*

/**
 * Currency conversion validation table.
 */
@Entity
@Table(name = "GTVCURR")
@NamedQueries([
        @NamedQuery(name = "CurrencyConversion.findByCurrencyConversion",
                query = """ FROM CurrencyConversion where (currencyConversion, rateEffectiveDate) in ( select currencyConversion, max(rateEffectiveDate) from CurrencyConversion group by currencyConversion) and currencyConversion = :currencyConversion """),
        @NamedQuery(name = "CurrencyConversion.fetchCurrentCurrencyConversion",
                query = """FROM CurrencyConversion a
                   WHERE a.rateEffectiveDate <= sysdate
                   AND a.rateNextChangeDate > sysdate
                   AND (a.rateTerminationDate > sysdate OR a.rateTerminationDate IS NULL)
                   AND a.statusIndicator = 'A'
                   AND a.currencyConversion = :currencyConversion""")
])
class CurrencyConversion implements Serializable {

    /**
     * Surrogate ID for GTVCURR
     */
    @Id
    @Column(name = "GTVCURR_SURROGATE_ID")
    @SequenceGenerator(name = "GTVCURR_SEQ_GEN", allocationSize = 1, sequenceName = "GTVCURR_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GTVCURR_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for GTVCURR
     */
    @Version
    @Column(name = "GTVCURR_VERSION")
    Long version

    /**
     * CURRENCY CODE: A code which uniquely identifies a type of currency.
     */
    @Column(name = "GTVCURR_CURR_CODE")
    String currencyConversion

    /**
     * EFFECTIVE DATE: The effective date of this particular record.
     */
    @Column(name = "GTVCURR_RATE_EFF_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date rateEffectiveDate

    /**
     * NEXT CHANGE DATE: The change date for this particular record; i.e., if the record included a termination date,
     * the next change date would reflect the date that the termination date was entered.  Requires at future change record.
     */
    @Column(name = "GTVCURR_RATE_NCHG_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date rateNextChangeDate

    /**
     * TERMINATION DATE: The date when this particular record is no longer in effect.
     */
    @Column(name = "GTVCURR_RATE_TERM_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date rateTerminationDate

    /**
     * TITLE: The description or title for this currency code.
     */
    @Column(name = "GTVCURR_TITLE")
    String title

    /**
     * STATUS INDICATOR: The current status of the associated validation table record.   (A or I)
     */
    @Column(name = "GTVCURR_STATUS_IND")
    String statusIndicator

    /**
     * ACCOUNTS PAYABLE ACCOUNT: Identifies the accounts payable account code to be used if an entity
     * intends to produce a check in a foreign currency rather than through a disbursing agent.
     */
    @Column(name = "GTVCURR_AP_ACCT")
    String accountsPayableAccount

    /**
     * NATION CODE: Identifies the nation code associated with this currency code.
     */
    @Column(name = "GTVCURR_NATION_CODE")
    String nation

    /**
     * CONVERSION TYPE: Identifies the type of conversion rate for this currency code.  Type may be daily or periodic.  (P, D or null)
     */
    @Column(name = "GTVCURR_CONV_TYPE")
    String conversionType

    /**
     * EXCHANGE ACCOUNT: Identifies the account code to be used when foreign currency checks are run and
     * contains the difference between the calculated exchange and invoice amount at base currency.
     */
    @Column(name = "GTVCURR_EXCH_ACCT")
    String exchangeAccount

    /**
     * DISBURSING AGENT PIDM: Personal identifcation number used when an entity intends to produce a check
     * in base currency with directions to a third party to produce the foreign currency check.
     */
    @Column(name = "GTVCURR_DISB_AGENT_PIDM")
    Integer disbursingAgentPidm

    /**
     * Accounts Payable account related to currency conversion for clients who do not have Banner Finance.
     */
    @Column(name = "GTVCURR_AP_ACCT2")
    String accountsPayableAccount2

    /**
     * Exchange account related to currency conversion for clients who do not have Banner Finance.
     */
    @Column(name = "GTVCURR_EXCH_ACCT2")
    String exchangeAccount2

    /**
     * ISO CODE:  The three character International Standards Organization (ISO) Standard Code associated with the user defined currency code.
     */
    @Column(name = "GTVCURR_SCOD_CODE_ISO")
    String standardCodeIso

    /**
     * ACTIVITY DATE: The date that information for this record on the table was entered or last updated.
     */
    @Column(name = "GTVCURR_ACTIVITY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    Date lastModified

    /**
     * USER IDENTIFICATION: The unique identification code of the user.
     */
    @Column(name = "GTVCURR_USER_ID")
    String lastModifiedBy

    /**
     * Data origin column for GTVCURR
     */
    @Column(name = "GTVCURR_DATA_ORIGIN")
    String dataOrigin

    /**
     * Foreign Key : FKV_GTVCURR_INV_GXVBANK_CODE
     */
    @ManyToOne
    @JoinColumns([
    @JoinColumn(name = "GTVCURR_BANK_CODE", referencedColumnName = "GXVBANK_BANK_CODE")
    ])
    Bank bank


    public String toString() {
        """CurrencyConversion[
					id=$id, 
					version=$version,
					currencyConversion=$currencyConversion,
					rateEffectiveDate=$rateEffectiveDate, 
					rateNextChangeDate=$rateNextChangeDate, 
					rateTerminationDate=$rateTerminationDate,
					title=$title, 
					statusIndicator=$statusIndicator, 
					accountsPayableAccount=$accountsPayableAccount, 
					nation=$nation, 
					conversionType=$conversionType, 
					exchangeAccount=$exchangeAccount,
					bank=$bank,
					disbursingAgentPidm=$disbursingAgentPidm,
					accountsPayableAccount2=$accountsPayableAccount2,
					exchangeAccount2=$exchangeAccount2,
					standardCodeIso=$standardCodeIso, 
					lastModified=$lastModified, 
					lastModifiedBy=$lastModifiedBy, 
					dataOrigin=$dataOrigin
					]"""
    }


    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof CurrencyConversion)) return false
        CurrencyConversion that = (CurrencyConversion) o
        if (id != that.id) return false
        if (version != that.version) return false
        if (rateEffectiveDate != that.rateEffectiveDate) return false
        if (rateNextChangeDate != that.rateNextChangeDate) return false
        if (rateTerminationDate != that.rateTerminationDate) return false
        if (title != that.title) return false
        if (statusIndicator != that.statusIndicator) return false
        if (accountsPayableAccount != that.accountsPayableAccount) return false
        if (nation != that.nation) return false
        if (conversionType != that.conversionType) return false
        if (exchangeAccount != that.exchangeAccount) return false
        if (disbursingAgentPidm != that.disbursingAgentPidm) return false
        if (accountsPayableAccount2 != that.accountsPayableAccount2) return false
        if (exchangeAccount2 != that.exchangeAccount2) return false
        if (standardCodeIso != that.standardCodeIso) return false
        if (lastModified != that.lastModified) return false
        if (lastModifiedBy != that.lastModifiedBy) return false
        if (dataOrigin != that.dataOrigin) return false
        if (currencyConversion != that.currencyConversion) return false
        if (bank != that.bank) return false
        return true
    }


    int hashCode() {
        int result
        result = (id != null ? id.hashCode() : 0)
        result = 31 * result + (version != null ? version.hashCode() : 0)
        result = 31 * result + (rateEffectiveDate != null ? rateEffectiveDate.hashCode() : 0)
        result = 31 * result + (rateNextChangeDate != null ? rateNextChangeDate.hashCode() : 0)
        result = 31 * result + (rateTerminationDate != null ? rateTerminationDate.hashCode() : 0)
        result = 31 * result + (title != null ? title.hashCode() : 0)
        result = 31 * result + (statusIndicator != null ? statusIndicator.hashCode() : 0)
        result = 31 * result + (accountsPayableAccount != null ? accountsPayableAccount.hashCode() : 0)
        result = 31 * result + (nation != null ? nation.hashCode() : 0)
        result = 31 * result + (conversionType != null ? conversionType.hashCode() : 0)
        result = 31 * result + (exchangeAccount != null ? exchangeAccount.hashCode() : 0)
        result = 31 * result + (disbursingAgentPidm != null ? disbursingAgentPidm.hashCode() : 0)
        result = 31 * result + (accountsPayableAccount2 != null ? accountsPayableAccount2.hashCode() : 0)
        result = 31 * result + (exchangeAccount2 != null ? exchangeAccount2.hashCode() : 0)
        result = 31 * result + (standardCodeIso != null ? standardCodeIso.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        result = 31 * result + (currencyConversion != null ? currencyConversion.hashCode() : 0)
        result = 31 * result + (bank != null ? bank.hashCode() : 0)
        return result
    }


    static constraints = {
        currencyConversion(nullable: false, maxSize: 4)
        rateEffectiveDate(nullable: false)
        rateNextChangeDate(nullable: false)
        rateTerminationDate(nullable: true)
        title(nullable: false, maxSize: 35)
        statusIndicator(nullable: false, maxSize: 1, inList: ["I", "A"])
        accountsPayableAccount(nullable: true, maxSize: 6)
        nation(nullable: true, maxSize: 5)
        conversionType(nullable: true, maxSize: 1, inList: ["P", "D"])
        exchangeAccount(nullable: true, maxSize: 6)
        bank(nullable: true)
        disbursingAgentPidm(nullable: true, min: -99999999, max: 99999999)
        accountsPayableAccount2(nullable: true, maxSize: 60)
        exchangeAccount2(nullable: true, maxSize: 60)
        standardCodeIso(nullable: true, maxSize: 3)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }

    //Read Only fields that should be protected against update
    public static readonlyProperties = ['currencyConversion', 'rateEffectiveDate', 'rateNextChangeDate']


    public static CurrencyConversion fetchCurrentCurrencyConversion( String currencyConversion ) {
        def currencyConversionList
        CurrencyConversion.withSession {session ->
            currencyConversionList = session.getNamedQuery( 'CurrencyConversion.fetchCurrentCurrencyConversion' ).setString( 'currencyConversion', currencyConversion ).list()
            return currencyConversionList[0]
        }
    }

}
