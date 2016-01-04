/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 ***************************************************************************** */
package net.hedtech.banner.general.utility

import groovy.sql.Sql
import org.apache.log4j.Logger

/**
 * This class is used to handle the UTC date time format conversion.
 */
class DateConvertHelperService {

    def static sessionFactory
    private static final log = Logger.getLogger(getClass())

    /**
     * converting date into utc date time format 'yyyy-MM-dd'T'HH:mm:ssZ'
     * @param date
     * @param timeZone
     * @return
     */
    def static convertDateIntoUTCFormat(Date date, def timeZone = null) {
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
     * converting date into utc date time format 'yyyy-MM-dd'T'HH:mm:ssZ'
     * if the time is null , returns date in UTC date time format with HH:mm:ss as 00:00:00 with the timeZone
     *if the time is not null , returns date as UTC date time format along with actual time with the timeZone
     * @param date
     * @param timeZone
     * @param time
     * @return
     */
    def static convertDateIntoUTCFormat(Date date,String time,def timeZone = null) {
        if(!date){
            return date
        }
        def dbtimezone
        time = time ? time?.substring( 0, 2 ) + ':' + time?.substring( 2, 4 ) + ':' + '00' : null
        timeZone = timeZone ?: getDBTimeZone()
        if (timeZone && timeZone.size() == 1) {
            dbtimezone = timeZone[0][0]
        }
        if(!time){
            return date?.format("yyyy-MM-dd'T'HH:mm:ss") + dbtimezone
        }
        return date?.format("yyyy-MM-dd'T'")+time+ dbtimezone
    }

    /**
     * fetching time zone from database
     * @return
     */
    def static getDBTimeZone() {
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
