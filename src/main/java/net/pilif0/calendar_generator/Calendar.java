package net.pilif0.calendar_generator;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

/**
 * Represents an iCalendar file
 *
 * @author Filip Smola
 * @version 1.0
 */
public class Calendar {
    /** The contents of the empty file */
    public static final String EMPTY_FILE = "BEGIN:VCALENDAR\n" +
            "PRODID:-//Filip Smola//Calendar Generator//EN\n" +
            "VERSION:2.0\n" +
            "CALSCALE:GREGORIAN\n" +
            "METHOD:PUBLISH\n" +
            "X-WR-CALNAME:Generated calendar\n" +
            "X-WR-TIMEZONE:" + TimeZone.getDefault().getID() +
            "END:VCALENDAR";

    /** The path to the file */
    public final Path path;
    /** The file prefix (calendar info, timezone, ...) */
    public final String prefix;
    /** The body of the file (event blocks) */
    private String events;
    /** The file suffix (calendar end) */
    public final String suffix;

    /**
     * Reads a calendar from a iCalendar file
     *
     * @param file The file to read
     */
    public Calendar(Path file){
        //Check the path is a file
        if(!Files.exists(file)){
            throw new IllegalArgumentException("The calendar file does not exist.");
        }
        if(Files.isDirectory(file)){
            throw new IllegalArgumentException("The calendar file is not a file.");
        }

        //Keep the absolute path
        path = file.toAbsolutePath();

        //DEBUG: print read note
        if(Launcher.debug){
            System.out.printf("Reading calendar from \'%s\'\n", path);
        }

        //Read the file contents
        String contents = readFile(file);

        //Check the lines where read
        if(contents == null || contents.equals("")){
            throw new IllegalArgumentException("The calendar file could not be read.");
        }

        //Separate the prefix (before first event or end of calendar)
        int prefixEnd = contents.indexOf("BEGIN:VEVENT");
        if(prefixEnd == -1){
            prefixEnd = contents.indexOf("END:VCALENDAR");
        }
        prefix = contents.substring(0, prefixEnd);

        //Separate the events (from first event to end of last)
        int eventsStart = contents.indexOf("BEGIN:VEVENT");
        int eventsEnd = contents.lastIndexOf("END:VEVENT");
        if(eventsStart == -1){
            //Case: no events

            events = "";
        }else {
            //Case: events present

            //Make end inclusive
            eventsEnd += 10;

            events = contents.substring(eventsStart, eventsEnd);
        }

        //Separate the suffix (from last event end to end of file)
        int suffixStart = contents.lastIndexOf("END:VEVENT");
        if(suffixStart == -1){
            //Case: no events

            //Take everything after prefix
            suffixStart = prefixEnd;
        }else{
            //Case: events present

            //Make start exclusive
            suffixStart += 10;
        }
        suffix = contents.substring(suffixStart);

        //DEBUG: print prefix and suffix (skip events because that is usually long)
        if(Launcher.debug){
            String msg = (new StringBuilder("iCalendar file read:")).append(System.lineSeparator())
                    .append(" - Prefix:").append(System.lineSeparator())
                    .append(prefix).append(System.lineSeparator())
                    .append(" - Suffix:").append(System.lineSeparator())
                    .append(suffix).append(System.lineSeparator())
                    .toString();
            System.out.print(msg);
        }
    }

    /**
     * Reads the file into a a string
     *
     * @param file The file to read
     * @return The contents or {@code null} when an error occurred
     */
    private static String readFile(Path file){
        try {
            return new String(Files.readAllBytes(file));
        } catch (IOException e) {
            //DEBUG: print message
            if(Launcher.debug) {
                System.out.printf("Calendar file could not be read (%s).\n", e.getLocalizedMessage());
            }
            return null;
        }
    }

    /**
     * Generates the iCalendar file contents
     *
     * @return The iCalendar file contents
     */
    public String export(){
        return prefix + events + "\n" + suffix;
    }

    /**
     * Adds the event to the calendar
     *
     * @param e The event to add
     */
    public void addEvent(Event e){
        events += "\n" + e.toEntry();
    }

    /**
     * Adds all the events to the calendar
     *
     * @param e The events to add
     */
    public void addEvents(Event... e){
        Arrays
            .stream(e)
            .forEach(this::addEvent);
    }

    /**
     * Creates a new empty iCalendar file at the path
     *
     * @param file The path to the new file
     * @return The {@code Calendar} instance of the file or {@code null} when an error occurred
     */
    public static Calendar createFile(Path file){
        //DEBUG: print creation note
        if(Launcher.debug){
            System.out.printf("Creating calendar at \'%s\'\n", file);
        }

        //Create the file
        try {
            file.toFile().createNewFile();
        } catch (IOException e) {
            System.out.printf("Calendar file could not be created (%s).\n", e.getLocalizedMessage());
            return null;
        }

        //Write the content
        try {
            Files.write(file, EMPTY_FILE.getBytes());
        } catch (IOException e) {
            System.out.printf("Calendar file could not be written to (%s).\n", e.getLocalizedMessage());
            return null;
        }

        //Instantiate the calendar object
        Calendar result = new Calendar(file);

        return result;
    }

    /**
     * Saves the iCalendar to the file
     *
     * @return {@code true} on success, {@code false} on failure
     */
    public boolean save(){
        //Write the content
        try {
            Files.write(path, export().getBytes());
        } catch (IOException e) {
            System.out.printf("Calendar file could not be written to (%s).\n", e.getLocalizedMessage());
            return false;
        }
        return true;
    }
}
