package net.pilif0.calendar_generator;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * Represents a single event
 *
 * @author Filip Smola
 * @version 1.0
 */
public class Event {
    /** The date time formatter (':', ' ', and '-' will be removed) */
    public static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ISO_INSTANT;

    /** The event title */
    public final String title;
    /** The event start date */
    public final LocalDate startDate;
    /** The event end date */
    public final LocalDate endDate;
    /** The event start time */
    public final LocalTime startTime;
    /** The event end time */
    public final LocalTime endTime;
    /** The event location */
    public final String location;
    /** The date and time of object creation */
    public final LocalDateTime creation = LocalDateTime.now();
    /** The event description */
    public final String description;

    /**
     * Constructs the event from its information
     */
    public Event(String title,
                 LocalDate startDate,
                 LocalDate endDate,
                 LocalTime startTime,
                 LocalTime endTime,
                 String location,
                 String description) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.description = description;
    }

    /**
     * Generates a single iCalendar event entry
     *
     * @return The iCalendar event entry
     */
    public String toEntry(){
        StringBuilder result = new StringBuilder();

        result.append("BEGIN:VEVENT").append(System.lineSeparator())
                .append("DTSTART:").append(getStartDatetime()).append(System.lineSeparator())
                .append("DTEND:").append(getEndDatetime()).append(System.lineSeparator())
                .append("DTSTAMP:").append(getCreationDatetime()).append(System.lineSeparator())
                .append("UID:").append(getUID()).append(System.lineSeparator())
                .append("CREATED:").append(getCreationDatetime()).append(System.lineSeparator())
                .append("DESCRIPTION:").append(System.lineSeparator())
                .append("LAST-MODIFIED:").append(getCreationDatetime()).append(System.lineSeparator())
                .append("LOCATION:").append(getLocation()).append(System.lineSeparator())
                .append("SEQUENCE:0").append(System.lineSeparator())
                .append("STATUS:CONFIRMED").append(System.lineSeparator())
                .append("SUMMARY:").append(getDescription()).append(System.lineSeparator())
                .append("TRANSP:TRANSPARENT").append(System.lineSeparator())
                .append("END:VEVENT");

        return result.toString();
    }

    /**
     * Returns the start datetime formatted for printing into  the iCalendar file
     *
     * @return The resulting datetime String
     */
    private String getStartDatetime(){
        return LocalDateTime.of(startDate, startTime)
                .atZone(ZoneId.systemDefault())
                .format(DATETIME_FORMAT)
                .replaceAll("\\ |-|:", "");
    }

    /**
     * Returns the end datetime formatted for printing into  the iCalendar file
     *
     * @return The resulting datetime String
     */
    private String getEndDatetime(){
        return LocalDateTime.of(endDate, endTime)
                .atZone(ZoneId.systemDefault())
                .format(DATETIME_FORMAT)
                .replaceAll("\\ |-|:", "");
    }

    /**
     * Returns the creation datetime formatted for printing into  the iCalendar file
     *
     * @return The resulting datetime String
     */
    private String getCreationDatetime(){
        return creation
                .atZone(ZoneId.systemDefault())
                .format(DATETIME_FORMAT)
                .replaceAll("\\ |-|:", "");
    }

    /**
     * Formats the location String to be printed into the iCalendar file
     *
     * @return The formatted String
     */
    private String getLocation(){
        return location;
    }

    /**
     * Formats the location String to be printed into the iCalendar file
     *
     * @return The formatted String
     */
    private String getDescription(){
        return description;
    }

    /**
     * Generates the UID (using creation timestamp)
     *
     * @return The UID
     */
    private String getUID(){
        return creation.toEpochSecond(ZoneOffset.UTC) + "@pilif0.net";
    }

}
