package net.hedtech.banner.general.overall

import groovy.sql.Sql
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.crossproduct.BankRoutingInfo
import net.hedtech.banner.service.ServiceBase

class BankRoutingInfoService extends ServiceBase {

    def preCreate( map ) {
        throw new ApplicationException( Position, "@@r1:unsupported.operation@@" )
    }


    def preDelete( map ) {
        throw new ApplicationException( Position, "@@r1:unsupported.operation@@" )
    }


    def preUpdate( map ) {
        throw new ApplicationException( Position, "@@r1:unsupported.operation@@" )
    }

    def validateRoutingNumber(routingNum) {
        validateRoutingNumFormat(routingNum)

        def bankInfo = BankRoutingInfo.fetchByRoutingNum(routingNum)

        if(!bankInfo){
            throw new ApplicationException(this, "@@r1:invalidRoutingNum@@")
        }

        bankInfo
    }

    def validateRoutingNumFormat(routingNum) {
        Sql sql = new Sql(sessionFactory.getCurrentSession().connection())
        def valid
        sql.call("{$Sql.VARCHAR = call goksels.f_validate_bank_rout_num(${routingNum})}") {result -> valid = result}

        if(valid != 'Y'){
            throw new ApplicationException(this, "@@r1:invalidRoutingNumFmt@@")
        }
    }
}
