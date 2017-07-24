/*******************************************************************************
 Copyright 2017 Ellucian Company L.P. and its affiliates.
 ***************************************************************************** */
package net.hedtech.banner.general.utility

import groovy.sql.Sql
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.exceptions.BusinessLogicValidationException
import net.hedtech.banner.general.common.GeneralValidationCommonConstants
import org.apache.log4j.Logger

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat

/**
 * This class is used to handle the UTC date time format conversion.
 */
class DateConvertHelperService {

    def static sessionFactory
    private static final log = Logger.getLogger(DateConvertHelperService.class)

    /**
     * converting date into utc date time format 'yyyy-MM-dd'T'HH:mm:ssXXX'
     * @param date
     * @param timeZone
     * @return
     */
    def static convertDateIntoUTCFormat(Date date) {
        if(!date){
            return null
        }
        //we are not doing proper date conversion using dateformatter as banner does not have timezone
        //storage capability due to which we loose the timezone info
        //and dates change between what is saved and what is read
        //we only convert the date part and simply attach T00:00:00+00:00 to denote UTC
        DateFormat dateFormat = new SimpleDateFormat(GeneralValidationCommonConstants.UTC_DATE_FORMAT_WITHOUT_TIMEZONE);
        //dateFormat.setTimeZone(TimeZone.getTimeZone(GeneralValidationCommonConstants.UTC_TIME_ZONE));
        return dateFormat.format(date)+"+00:00";
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

    static String convertDateIntoUTCFormat(Date date,String time) {
        if(!date){
            return null
        }
        if(!time){
            return convertDateIntoUTCFormat(date);
        }
        time = time ? time.substring( 0, 2 ) + ':' + time.substring( 2, 4 ) + ':' + '00' : null
        return convertDateIntoUTCFormat(Date.parse(GeneralValidationCommonConstants.DATETIME_WITHOUT_TIMEZONE,date.format(GeneralValidationCommonConstants.DATE_WITHOUT_TIMEZONE)+" "+time))
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

    /**
     * converting UTC date in String to the date of server running it.
     * @return date object of server running it
     */
      static Date convertUTCStringToServerDate(String utc) {
          Date utcDate
        if(!utc){
            return null
        }
          try{
              //we are not doing proper date conversion using dateformatter as banner does not have timezone
              //storage capability due to which we loose the timezone info
              //and dates change between what is saved and what is read
              //we only parse the date component from the given object
              return new SimpleDateFormat(GeneralValidationCommonConstants.UTC_DATE_FORMAT_WITHOUT_TIMEZONE).parse(utc)
          }catch (ParseException pe){
              throw new ApplicationException(this.class.simpleName, new BusinessLogicValidationException("invalid.date.format", []))
          }
    }

    static Date convertDateStringToServerDate(String date) {
        Date serverDate
        if(!date){
            return null
        }
        if (!date.matches("([0-9]{4})-([0-9]{2})-([0-9]{2})")){
            throw new ApplicationException(this.class.simpleName, new BusinessLogicValidationException("invalid.date.format", []))
        }

        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat(GeneralValidationCommonConstants.DATE_WITHOUT_TIMEZONE)
            dateFormat.setLenient(false)
            serverDate = dateFormat.parse(date)
        }catch (ParseException pe){
            throw new ApplicationException(this.class.simpleName, new BusinessLogicValidationException("invalid.date.format", []))
        }
        return serverDate
    }
}
