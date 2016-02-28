package com.scrapbook.abscbn100;

import javax.validation.constraints.NotNull;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by ronaldteodosio on 12/27/15.
 */
public class SpotsCheck {

    private @NotNull String date; // M/d/yy
    private @NotNull String time; // HH:mm
    private @NotNull String rating;  // 100.00
    private @NotNull String product;
    private @NotNull String broadcast; //

    private boolean isValid = true;
    private boolean hasException = false;
    public static Pattern yearDigitPattern = Pattern.compile(".*\\d{4}");
    public static Pattern hour24DigitPattern = Pattern.compile("\\d{2}:\\d{2}:\\d{2}");
    public static List<String> ratingHeader = Arrays.asList("RTG%", "RATING","ACTUAL RATING","ACTUAL RATING/");
    public static List<String> dateHeader = Arrays.asList("DATE","AIR DATE");
    public static List<String> timeHeader = Arrays.asList("START TIME", "AIR TIME");

    public SpotsCheck() {
        super();
    }

    public SpotsCheck(String date, String time, String rating) {
        this.setDate(date);
        this.setTime(time);
        this.setRating(rating);
        this.checkValid();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        /* if ((date != null) && yearDigitPattern.matcher(date).matches()) {
            String dateStr = date;
            DateFormat readFormat = new SimpleDateFormat("M/d/yyyy");
            DateFormat writeFormat = new SimpleDateFormat("M/d/yy");
            Date dateConv = null;
            try {
                dateConv = readFormat.parse( dateStr );
            }
            catch (ParseException e) {
                e.printStackTrace();
                setValid(false);
            }
            if(dateConv != null) {
                date = writeFormat.format( dateConv );
            }
        } */
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        if (time.contains("AM") || time.contains("PM") || time.contains("am") || time.contains("pm")) {
            String timeStr = time;
            DateFormat readFormat = new SimpleDateFormat( "hh:mm aa");
            DateFormat writeFormat = new SimpleDateFormat( "HH:mm");
            Date timeConv = null;
            try {
                timeConv = readFormat.parse(timeStr);
            }
            catch (ParseException e) {
                e.printStackTrace();
                setValid(false);
            }
            if (timeConv != null) {
                String formattedTime= writeFormat.format(timeConv);
                this.time = formattedTime;
            }
        } else if (hour24DigitPattern.matcher(time).matches()) {
            String timeStr = time;
            DateFormat readFormat = new SimpleDateFormat( "HH:mm:ss");
            DateFormat writeFormat = new SimpleDateFormat( "HH:mm");
            Date timeConv = null;
            try {
                timeConv = readFormat.parse(timeStr);
            }
            catch (ParseException e) {
                e.printStackTrace();
                setValid(false);
            }
            if (timeConv != null) {
                String formattedTime= writeFormat.format(timeConv);
                time = formattedTime;
                this.time = time;
            }
        }
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getBroadcast() {
        return broadcast;
    }

    public void setBroadcast(String broadcast) {
        this.broadcast = broadcast;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    private void checkValid() {
        setValid(true);
        if (this.date == null || this.date.trim().equals("") || this.time == null || this.time.trim().equals("") || this.rating == null || this.rating.trim().equals("")) {
            setValid(false);
        }
    }

    public static void test() {
        System.out.println(new SpotsCheck("1/1/2015","1:30 PM","10.12").toString());
        System.out.println(new SpotsCheck("1/1/15","14:30:01","10.12").toString());
        System.out.println(new SpotsCheck("1/1/2015","2:30 PM","10.12").toString());
        System.out.println(new SpotsCheck("1/2/2015","2:30 PM","10.12").toString());
        System.out.println(new SpotsCheck(null,"2:30 PM","10.12").toString());
        System.out.println(new SpotsCheck("","2:30 PM","10.12").toString());
        System.out.println(new SpotsCheck("","","10.12").toString());
        System.out.println(new SpotsCheck("ss","xx","10.13").toString());
        System.out.println(new SpotsCheck("2/2/2015","2:30 PM","10.12").toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SpotsCheck that = (SpotsCheck) o;

        if (getDate() != null ? !getDate().equals(that.getDate()) : that.getDate() != null) return false;
        if (getTime() != null ? !getTime().equals(that.getTime()) : that.getTime() != null) return false;
        return !(getRating() != null ? !getRating().equals(that.getRating()) : that.getRating() != null);

    }

    @Override
    public int hashCode() {
        int result = (getDate() != null && !getDate().trim().equals("")) ? getDate().hashCode() : 0;
        result = 31 * result + ((getTime() != null && !getTime().trim().equals(""))? getTime().hashCode() : 0);
        result = 31 * result + ((getRating() != null && !getRating().trim().equals("")) ? getRating().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        checkValid();
        return "SpotsCheck{" +
            "date='" + date + '\'' +
            ", time='" + time + '\'' +
            ", rating='" + rating + '\'' +
            ", isValid='" + isValid + '\'' +
            ", hashCode='" + hashCode() + '\'' +
            '}';
    }



    public static void main(String[] args) {
        System.out.println("1/1/15".hashCode());
        System.out.println("1/2/15".hashCode());
        System.out.println(new SpotsCheck("1/1/15","14:30:01","10.12").getDate());


        SpotsCheck.test();
    }
}
