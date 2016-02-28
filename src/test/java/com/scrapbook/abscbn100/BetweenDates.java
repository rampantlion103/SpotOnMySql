package com.scrapbook.abscbn100;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


/**
 * Created by ronaldteodosio on 2/21/16.
 */
public class BetweenDates {

    public static void main(String args[]) {
        /*
        LocalDate today = LocalDate.now();
        LocalDate tommorow = LocalDate.of(2016, 2, 24);
        if(tommorow.isAfter(today))
        {
            System.out.println("the date comes after today");
        }


        if(tommorow.isEqual(today))
        {
            System.out.println("the date is today");
        }


        if(tommorow.isBefore(today))
        {
            System.out.println("the date was past");
        }

        */

        LocalDate spotDate = LocalDate.of(2016, 2, 22);
        LocalDate startDate = null; //LocalDate.of(2016, 2, 24);
        LocalDate endDate = LocalDate.of(2016, 2, 26);
        spotDate = convertStringDate("2/24/2016");
        startDate = convertStringDate("2/24/2016");
        endDate = convertStringDate("2/26/2016");
        if (startDate.isEqual(spotDate) || endDate.isEqual(spotDate) || (spotDate.isAfter(startDate) && spotDate.isBefore(endDate))) {
            System.out.println("TRUE");
        } else {
            System.out.println("FALSE");
        }


    }


    static LocalDate convertStringDate (String date) {
        final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("M/d/yyyy");
        return LocalDate.parse(date, DATE_FORMAT);
    }



}
