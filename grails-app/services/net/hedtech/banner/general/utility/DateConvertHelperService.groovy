/*******************************************************************************
 Copyright 2015 Ellucian Company L.P. and its affiliates.
 ***************************************************************************** */
package net.hedtech.banner.general.utility

import groovy.sql.Sql
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import org.apache.log4j.Logger

import java.text.DateFormat
import java.text.SimpleDateFormat

/**
 * This class is used to handle the UTC date time format conversion.
 */
class DateConvertHelperService {

    def static sessionFactory
    private static final log = Logger.getLogger(getClass())

    /**
     * converting date into utc date time format 'yyyy-MM-dd'T'HH:mm:ssXXX'
     * @param date
     * @param timeZone
     * @return
     */
    def static convertDateIntoUTCFormat(Date date) {
        if(!date){
            return date
        }
        DateFormat dateFormat = new SimpleDateFormat(GeneralValidationCommonConstants.UTC_DATE_FORMAT);
        dateFormat.setTimeZone(TimeZone.getTimeZone(GeneralValidationCommonConstants.UTC_TIME_ZONE));
        return dateFormat.format(date);
    }

    /**
     * converting date into utc date time format 'yyyy-MM-dd'T'HH:mm:ssXXX' which is ISO-8601 standard format
     * if the time is null, returns date in UTC date time format with HH:mm:ss as 00:00:00 with the timeZone
     * if the time is not null, returns date as UTC date time format along with actual time with the timeZone
     * @param date
     * @param timeZone
     * @param time
     * @return
     */

    def static convertDateIntoUTCFormat(Date date,String time) {
        if(!date){
            return date
        }
        if(!time){
            return convertDateIntoUTCFormat(date);
        }
        time = time ? time.substring( 0, 2 ) + ':' + time.substring( 2, 4 ) + ':' + '00' : null
        return convertDateIntoUTCFormat(Date.parse("yyyy-MM-dd HH:mm:ss",date.format("yyyy-MM-dd")+" "+time))
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
