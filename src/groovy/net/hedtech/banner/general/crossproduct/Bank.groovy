/** *****************************************************************************
 Copyright 2013 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.crossproduct

import javax.persistence.*

/**
 * Bank Validation Table
 */

@Entity
@Table(name = "GXVBANK")
class Bank implements Serializable {

    /**
     * Surrogate ID for GXVBANK
     */
    @Id
    @Column(name = "GXVBANK_SURROGATE_ID")
    @SequenceGenerator(name = "GXVBANK_SEQ_GEN", allocationSize = 1, sequenceName = "GXVBANK_SURROGATE_ID_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GXVBANK_SEQ_GEN")
    Long id

    /**
     * Optimistic lock token for GXVBANK
     */
    @Version
    @Column(name = "GXVBANK_VERSION")
    Long version

    /**
     * BANK CODE:  A user-defined value which is used for identification purposes on  all transactions.
     * All bank codes and other attributes are displayed on form GXVBANK.
     */
    @Column(name = "GXVBANK_BANK_CODE")
    String bank 

    /**
     * EFFECTIVE DATE:  The effective date of this particular record.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "GXVBANK_EFF_DATE")
    Date effectiveDate

    /**
     * NEXT CHANGE DATE:  The change date for this particular record.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "GXVBANK_NCHG_DATE")
    Date nextChangeDate

    /**
     * Personal IDENTIFICATION NUMBER : PIDM as set in SPRIDEN.
     */
    @Column(name = "GXVBANK_BANK_CODE_PIDM")
    Integer bankPidm

    /**
     * BANK ACCOUNT NUMBER:  This is the bank assigned number unique to this account and is the number that is encoded on the checks.
     */
    @Column(name = "GXVBANK_ACCT_NUM")
    String bankAccountNumber

    /**
     * BANK ACCOUNT NAME:  The name of the banking institution where the installment has accounts.
     */
    @Column(name = "GXVBANK_ACCT_NAME")
    String bankAccountName

    /**
     * DIRECT DEPOSIT STATUS : Indicator whether bank does direct deposit payments, (A)ctive and (I)nactive.
     */
    @Column(name = "GXVBANK_ACH_STATUS")
    String achStatus

    /**
     * STATUS INDICATOR:  The current status of the associated validation table record.
     */
    @Column(name = "GXVBANK_STATUS_IND")
    String statusIndicator

    /**
     * TERMINATION DATE:  The date when the record is no longer in effect.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "GXVBANK_TERM_DATE")
    Date terminationDate

    /**
     * CHART OF ACCOUNTS CODE:  The primary identification code for any chart of accounts which uniquely
     * identifies that chart from any other in a multi-chart environment.
     */
    @Column(name = "GXVBANK_COAS_CODE")
    String chartOfAccounts

    /**
     * CASH ACCOUNT CODE:  This is the account number and title of the installments cash balances in the bank fund.
     */
    @Column(name = "GXVBANK_ACCT_CODE_CASH")
    String accountCash

    /**
     * INTERFUND CASH ACCOUNT CODE:  This is the acount number and title of the installments cash balance in each operating ledger.
     */
    @Column(name = "GXVBANK_ACCT_CODE_INTERFUND")
    String accountInterfund

    /**
     * FUND CODE:  A code which uniquely identifies a fiscal entity.
     */
    @Column(name = "GXVBANK_FUND_CODE")
    String fund

    /**
     * CURRENCY CODE: Currency code established for the bank. (Note a FK does not exist to GTVCURR?
     */
    @Column(name = "GXVBANK_CURR_CODE")
    String currencyConversion

    /**
     * DESTINATION ROUTING NO:  The routing number of the clearing house bank uses.
     */
    @Column(name = "GXVBANK_ACH_DEST_NO")
    String achDestinationRoutingNumber

    /**
     * ORIGINATING ROUTING NO:  The routing number of the bank.
     */
    @Column(name = "GXVBANK_ACH_ORIG_NO")
    String achOriginatingRoutingNumber

    /**
     * DESTINATION BANK NAME:  The name of the clearing house bank uses.
     */
    @Column(name = "GXVBANK_ACH_DEST_NAME")
    String achDestinationName

    /**
     * ORIGINATING BANK NAME:  The short name of the bank should be less than 15 characters long.
     */
    @Column(name = "GXVBANK_ACH_SHORT_ORIG_NAME")
    String achOriginatingNameShort

    /**
     * ORGANIZATION NUMBER TYPE : Type code used to identify the type of Organization Identification.
     */
    @Column(name = "GXVBANK_COMPANY_TYPE")
    String companyType

    /**
     * ORGANIZATION IDENTIFICATION NUMBER : Code used to identify an originator(creater) of direct deposit tape.
     */
    @Column(name = "GXVBANK_COMPANY_ID")
    String companyId

    /**
     * ORIGINATING LONG NAME: The long name of the bank.
     */
    @Column(name = "GXVBANK_ACH_ORIG_NAME")
    String achOriginatingName

    /**
     * ORIGINATING BANK ROUTING NUMBER: The routing number that is associated with the originating bank.
     */
    @Column(name = "GXVBANK_BANK_ROUT_NUM")
    String bankOriginatingRoutingNumber

    /**
     * ACH FILE NUMBER : The number of direct deposit tape file that has been produced from this bank.
     */
    @Column(name = "GXVBANK_ACH_FILE_NUMBER")
    Integer achFileNumber

    /**
     * ACTIVITY DATE:  The date when the information for this record on the table was entered or last updated.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "GXVBANK_ACTIVITY_DATE")
    Date lastModified

    /**
     * USER IDENTIFICATION:  The unique identification code of the user.
     */
    @Column(name = "GXVBANK_USER_ID")
    String lastModifiedBy

    /**
     * Data origin column for GXVBANK
     */
    @Column(name = "GXVBANK_DATA_ORIGIN")
    String dataOrigin


    public String toString() {
        """Bank[
					id=$id, 
					version=$version, 
					bank=$bank,
					effectiveDate=$effectiveDate,
					nextChangeDate=$nextChangeDate, 
					bankPidm=$bankPidm,
					bankAccountNumber=$bankAccountNumber,
	                bankAccountName=$bankAccountName,
                    achStatus=$achStatus,
                    statusIndicator=$statusIndicator,
                    terminationDate=$terminationDate,
					chartOfAccounts=$chartOfAccounts,
                    accountCash=$accountCash,
					accountInterfund=$accountInterfund,
                    fund=$fund,
                    currencyConversion=$currencyConversion,
                    achDestinationRoutingNumber=$achDestinationRoutingNumber,
					achOriginatingRoutingNumber=$achOriginatingRoutingNumber,
					achDestinationName=$achDestinationName,
                    achOriginatingNameShort=$achOriginatingNameShort,
                    companyType=$companyType,
                    companyId=$companyId,
                    achOriginatingName=$achOriginatingName,
					bankOriginatingRoutingNumber=$bankOriginatingRoutingNumber,
                    achFileNumber=$achFileNumber,
					lastModified=$lastModified,
					lastModifiedBy=$lastModifiedBy, 
					dataOrigin=$dataOrigin]"""
    }


    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof Bank)) return false
        Bank that = (Bank) o
        if (id != that.id) return false
        if (version != that.version) return false
        if (effectiveDate != that.effectiveDate) return false
        if (nextChangeDate != that.nextChangeDate) return false
        if (bankPidm != that.bankPidm) return false
        if (bankAccountNumber != that.bankAccountNumber) return false
        if (bankAccountName != that.bankAccountName) return false
        if (achStatus != that.achStatus) return false
        if (statusIndicator != that.statusIndicator) return false
        if (terminationDate != that.terminationDate) return false
        if (chartOfAccounts != that.chartOfAccounts) return false
        if (accountCash != that.accountCash) return false
        if (accountInterfund != that.accountInterfund) return false
        if (fund != that.fund) return false
        if (achDestinationRoutingNumber != that.achDestinationRoutingNumber) return false
        if (achOriginatingRoutingNumber != that.achOriginatingRoutingNumber) return false
        if (achDestinationName != that.achDestinationName) return false
        if (achOriginatingNameShort != that.achOriginatingNameShort) return false
        if (companyType != that.companyType) return false
        if (companyId != that.companyId) return false
        if (achOriginatingName != that.achOriginatingName) return false
        if (bankOriginatingRoutingNumber != that.bankOriginatingRoutingNumber) return false
        if (achFileNumber != that.achFileNumber) return false
        if (lastModified != that.lastModified) return false
        if (lastModifiedBy != that.lastModifiedBy) return false
        if (dataOrigin != that.dataOrigin) return false
        if (bank != that.bank) return false
        if (currencyConversion != that.currencyConversion) return false
        return true
    }


    int hashCode() {
        int result
        result = (id != null ? id.hashCode() : 0)
        result = 31 * result + (version != null ? version.hashCode() : 0)
        result = 31 * result + (effectiveDate != null ? effectiveDate.hashCode() : 0)
        result = 31 * result + (nextChangeDate != null ? nextChangeDate.hashCode() : 0)
        result = 31 * result + (bankPidm != null ? bankPidm.hashCode() : 0)
        result = 31 * result + (bankAccountNumber != null ? bankAccountNumber.hashCode() : 0)
        result = 31 * result + (bankAccountName != null ? bankAccountName.hashCode() : 0)
        result = 31 * result + (achStatus != null ? achStatus.hashCode() : 0)
        result = 31 * result + (statusIndicator != null ? statusIndicator.hashCode() : 0)
        result = 31 * result + (terminationDate != null ? terminationDate.hashCode() : 0)
        result = 31 * result + (chartOfAccounts != null ? chartOfAccounts.hashCode() : 0)
        result = 31 * result + (accountCash != null ? accountCash.hashCode() : 0)
        result = 31 * result + (accountInterfund != null ? accountInterfund.hashCode() : 0)
        result = 31 * result + (fund != null ? fund.hashCode() : 0)
        result = 31 * result + (achDestinationRoutingNumber != null ? achDestinationRoutingNumber.hashCode() : 0)
        result = 31 * result + (achOriginatingRoutingNumber != null ? achOriginatingRoutingNumber.hashCode() : 0)
        result = 31 * result + (achDestinationName != null ? achDestinationName.hashCode() : 0)
        result = 31 * result + (achOriginatingNameShort != null ? achOriginatingNameShort.hashCode() : 0)
        result = 31 * result + (companyType != null ? companyType.hashCode() : 0)
        result = 31 * result + (companyId != null ? companyId.hashCode() : 0)
        result = 31 * result + (achOriginatingName != null ? achOriginatingName.hashCode() : 0)
        result = 31 * result + (bankOriginatingRoutingNumber != null ? bankOriginatingRoutingNumber.hashCode() : 0)
        result = 31 * result + (achFileNumber != null ? achFileNumber.hashCode() : 0)
        result = 31 * result + (lastModified != null ? lastModified.hashCode() : 0)
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0)
        result = 31 * result + (dataOrigin != null ? dataOrigin.hashCode() : 0)
        result = 31 * result + (bank != null ? bank.hashCode() : 0)
        result = 31 * result + (currencyConversion != null ? currencyConversion.hashCode() : 0)
        return result
    }


    static constraints = {
        bank(nullable: false, maxSize: 2)
        effectiveDate(nullable: false)
        nextChangeDate(nullable: false)
        bankPidm(nullable: false, min: -99999999, max: 99999999)
        bankAccountNumber(nullable: false, maxSize: 17)
        bankAccountName(nullable: false, maxSize: 35)
        achStatus(nullable: false, maxSize: 1, inList: ["A", "I"])
        statusIndicator(nullable: false, maxSize: 1, inList: ["A"])
        terminationDate(nullable: true)
        chartOfAccounts(nullable: true, maxSize: 1)
        accountCash(nullable: true, maxSize: 6)
        accountInterfund(nullable: true, maxSize: 6)
        fund(nullable: true, maxSize: 6)
        currencyConversion(nullable: true, maxSize: 4)     //A Fk to GTVCURR does not exist ?
        achDestinationRoutingNumber(nullable: true, maxSize: 10)
        achOriginatingRoutingNumber(nullable: true, maxSize: 10)
        achDestinationName(nullable: true, maxSize: 23)
        achOriginatingNameShort(nullable: true, maxSize: 15)
        companyType(nullable: true, maxSize: 1)
        companyId(nullable: true, maxSize: 9)
        achOriginatingName(nullable: true, maxSize: 23)
        bankOriginatingRoutingNumber(nullable: true, maxSize: 9)
        achFileNumber(nullable: true, min: -9999, max: 9999)
        lastModified(nullable: true)
        lastModifiedBy(nullable: true, maxSize: 30)
        dataOrigin(nullable: true, maxSize: 30)
    }

    //Read Only fields that should be protected against update
    public static readonlyProperties = ['bank', 'effectiveDate', 'nextChangeDate']


}
