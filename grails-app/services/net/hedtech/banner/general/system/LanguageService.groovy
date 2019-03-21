/*******************************************************************************
 Copyright 2013-2019 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package net.hedtech.banner.general.system

import net.hedtech.banner.service.ServiceBase
import grails.gorm.transactions.Transactional

@Transactional
class LanguageService extends ServiceBase {


    public Language fetchByCode(String code) {
        Language entity
        if (code) {
            Language.withSession { session ->
                def query = session.getNamedQuery('Language.fetchByCode')
                query.setString('code', code)
                entity = query.uniqueResult()
            }
        }
        return entity
    }


    public def fetchAllByCodeInList(Collection<String> codes) {
        Collection<Language> entities
        if (codes) {
            entities = Language.withSession { session ->
                session.getNamedQuery('Language.fetchAllByCodeInList').setParameterList('codes', codes).list()
            }
        }

        return entities
    }


    def getCodeToIsoCodeMap(Collection<String> languageCodeList) {

        Map<String, String> languageToIsoMap = [:]
        List<State> isoCodeList = this.fetchAllByCodeInList(languageCodeList)
        isoCodeList?.each {
            languageToIsoMap.put(it.code, it.isoCode)
        }
        return languageToIsoMap
    }


    def fetchAllByIsoCode(String isoCode) {
        Collection<Language> entities = []
        if (isoCode) {
            entities = Language.withSession { session ->
                session.getNamedQuery('Language.fetchAllByIsoCode').setString('isoCode', isoCode).list()
            }
        }
        return entities
    }

}