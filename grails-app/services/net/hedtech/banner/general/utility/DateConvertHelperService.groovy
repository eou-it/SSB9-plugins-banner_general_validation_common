/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 ***************************************************************************** */
package net.hedtech.banner.general.utility

import groovy.sql.Sql

/**
 * This class is used to handle the UTC date time format conversion.
 */
class DateConvertHelperService {

    def sessionFactory

    /**
     * converting date into utc date time format 'yyyy-MM-dd'T'HH:mm:ssZ'
     * @param date
     * @param timeZone
     * @return
     */
    def convertDateIntoUTCFormat(Date date, def timeZone = null) {
        if(!date){
            return date
        }
        def dbtimezone
        timeZone = timeZone ?: getDBTimeZone()
        if (timeZone) {
            if (timeZone.size() == 1) {
                dbtimezone = timeZone[0][0]
            }
        }
        return date?.format("yyyy-MM-dd'T'HH:mm:ss") + dbtimezone
    }

    /**
     * fetching time zone from database
     * @return
     */
    def getDBTimeZone() {
        def conn
        def rows
        try {
            String query = 'select DBTIMEZONE from dual'
            conn = sessionFactory.currentSession.connection()
            Sql sql = new Sql(conn)
            rows = sql.rows(query)
            log.debug "Database timezone : ${rows}"
        }
        finally {
            conn?.close()
        }
        return rows
    }
}
