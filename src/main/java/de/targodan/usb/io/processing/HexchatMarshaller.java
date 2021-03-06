/*
 * The MIT License
 *
 * Copyright 2017 Luca Corbatto.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package de.targodan.usb.io.processing;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This implementation of Marshaller marshalls lines as formatted by the HexChat
 * IRC client.
 *
 * @author Luca Corbatto
 */
public class HexchatMarshaller extends AbstractMarshaller {
    protected Pattern usernameMessagePattern;
    
    /**
     * Constructs a HexchatMarshaller.
     */
    public HexchatMarshaller() {
        this.usernameMessagePattern = Pattern.compile("(.*?)\t(.*)");
    }
    
    /**
     * Parses the month by String identifier.
     * 
     * @param month The month to be parsed.
     * @return the parsed month.
     * @throws IllegalArgumentException If the given String cannot be parsed.
     */
    protected Month parseMonth(String month) {
        switch(month.toLowerCase()) {
            case "jan":
                return Month.JANUARY;
            case "feb":
                return Month.FEBRUARY;
            case "mar":
                return Month.MARCH;
            case "apr":
                return Month.APRIL;
            case "may":
                return Month.MAY;
            case "jun":
                return Month.JUNE;
            case "jul":
                return Month.JULY;
            case "aug":
                return Month.AUGUST;
            case "sep":
                return Month.SEPTEMBER;
            case "oct":
                return Month.OCTOBER;
            case "nov":
                return Month.NOVEMBER;
            case "dec":
                return Month.DECEMBER;
            default:
                throw new IllegalArgumentException("\""+month+"\" is not a valid month.");
        }
    }
    
    /**
     * Returns the year of the IRCMessage.
     * 
     * This is guessing the year of the message. As the year is not logged by
     * HexChat we expect it to be the current year unless we *just* had new 
     * years eve.
     * 
     * @param messageMonth The month in which the message was sent.
     * @return The year the message was most likely sent in.
     */
    protected int getYear(Month messageMonth) {
        // Sort of a workaround in case this runs over newyears.
        int currentYear = LocalDate.now().getYear();
        Month currentMonth = LocalDate.now().getMonth();
        
        if(messageMonth == Month.DECEMBER && currentMonth == Month.JANUARY) {
            return currentYear-1;
        } else {
            return currentYear;
        }
    }
    
    /**
     * Parses the given String as a date time.
     * 
     * @param dateTime The String representation of the date time.
     * @return the parsed LocalDateTime.
     * @throws IllegalArgumentException if the String could not be parsed.
     */
    protected LocalDateTime parseDateTime(String dateTime) {
        String[] parts = dateTime.split(" ");
        
        String month = parts[0];
        String day = parts[1];
        String time = parts[2];
        
        String[] timeParts = time.split(":");
        
        Month m = this.parseMonth(month);
        
        return LocalDateTime.of(
                    this.getYear(m),
                    m,
                    Integer.valueOf(day),
                    Integer.valueOf(timeParts[0]),
                    Integer.valueOf(timeParts[1]),
                    Integer.valueOf(timeParts[2])
                );
    }
   
    @Override
    public IRCMessage marshall(Object o) {
        if(o == null || !(o instanceof String)) {
            throw new IllegalArgumentException("Parameter for HexchatMarshaller needs to be a String.");
        }
        String line = (String)o;
        
        if(line.length() < 17) {
            return null;
        }
        
        String dateTime = line.substring(0, 15);
        LocalDateTime timestamp = this.parseDateTime(dateTime);
        String event = line.substring(16);
        
        Matcher matcher = this.usernameMessagePattern.matcher(event);
        if(!matcher.find()) {
            throw new IllegalArgumentException("String \""+event+"\" does not fit the expected pattern.");
        }
        
        // Hexchat has a single file per channel, so just assume "#fuelrats" here. SingleChannelFileDataSource will overwrite this if necessary.
        IRCMessage msg = new IRCMessage(timestamp, this.sanitizeUsername(matcher.group(1)), "#fuelrats", matcher.group(2));
        return msg;
    }
}
