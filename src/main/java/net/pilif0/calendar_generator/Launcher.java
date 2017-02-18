package net.pilif0.calendar_generator;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Launches the GUI
 *
 * @author Filip Smola
 * @version 1.0
 */
public class Launcher extends Application {
    /** The window width */
    public static final int WIDTH = 500;
    /** The window height */
    public static final int HEIGHT = 500;
    /** The window title */
    public static final String TITLE = "Calendar generator";
    /** The time formatter */
    public static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ISO_LOCAL_TIME;
    /** The time to use as "now" */
    public static final LocalTime NOW_TIME = LocalTime
            .now()
            .withSecond(0)
            .withNano(0);
    /** The date formatter */
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;
    /** The date to use as "today" */
    public static final LocalDate NOW_DATE = LocalDate.now();
    /** The date string converter */
    public static final StringConverter<LocalDate> DATE_CONVERTER = new StringConverter<LocalDate>(){
        @Override
        public String toString(LocalDate object) {
            return object.format(DATE_FORMAT);
        }

        @Override
        public LocalDate fromString(String string) {
            return LocalDate.parse(string, DATE_FORMAT);
        }
    };
    /** The style for displaying an error in the status bar */
    public static final String STYLE_ERROR = "-fx-color: red;";
    /** The style for displaying info in the status bar */
    public static final String STYLE_INFO = "-fx-color: black;";
    /** The style for displaying success in the status bar */
    public static final String STYLE_SUCCESS = "-fx-color: green;";

    /** Whether debug mode is enabled */
    public static boolean debug = false;
    /** The main window */
    private Stage window;
    /** The status message display label */
    private Label statusMsg;

    /**
     * Parses the command line arguments and launches the gui
     *
     * @param args Command line arguments
     */
    public static void main(String[] args){
        //Parse the arguments
        Arrays.stream(args)
                .forEach(x -> {
                    //Check debug flag
                    if(x.equals("debug")){
                        debug = true;
                    }
                });

        //Launch the GUI
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        //Set the window member
        window = primaryStage;

        //Prepare the root node
        BorderPane root = new BorderPane();

        //Prepare the form pane
        StackPane form = new StackPane();
        form.setPadding(new Insets(10));

        //Fill the scroll pane
        form.getChildren().add(buildEventForm());

        //Prepare the status bar (show latest message)
        statusMsg = new Label();
        HBox statusBar = new HBox(statusMsg);
        statusBar.setStyle(
                "-fx-border-style: solid none none none; " +
                        "-fx-border-color: lightgrey; " +
                        "-fx-padding: 0.2em; " +
                        "-fx-margin: 0.5em 0 0 0;");

        //Fill the root node
        root.setCenter(form);
        root.setBottom(statusBar);

        //Finalize the window
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle(TITLE);
        primaryStage.sizeToScene();
        primaryStage.show();

        //Display done message
        displayInfo("Done");
    }

    /**
     * Builds the event input form
     *
     * @return The event form scene graph
     */
    public Node buildEventForm(){
        //Prepare the result
        GridPane result = new GridPane();
        result.setAlignment(Pos.CENTER);
        result.setHgap(5);
        result.setVgap(5);

        //Constrain the columns
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(25);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(25);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(25);
        ColumnConstraints col4 = new ColumnConstraints();
        col4.setPercentWidth(25);

        //Prepare row pointer
        int row = 0;

        //Create title label
        Label titleL = new Label("Title:");
        result.add(titleL, 0, row);

        //Create title input
        TextField titleField = new TextField("Untitled event");
        titleField.setId("title");
        titleField.requestFocus();
        result.add(titleField, 1, row, 3, 1);

        //Create start datetime label
        Label startL = new Label("Start:");

        //Create start date input
        DatePicker startDate = new DatePicker(NOW_DATE);
        startDate.setConverter(DATE_CONVERTER);
        startDate.setId("start-date");

        //Create start time input
        TextField startTime = new TextField(NOW_TIME.format(TIME_FORMAT));
        startTime.setId("start-time");
        startTime.setPrefColumnCount(5);

        //Add start datetime row
        result.addRow(++row, startL, startDate, startTime);

        //Create start datetime label
        Label endL = new Label("End:");

        //Create end date input
        DatePicker endDate = new DatePicker(NOW_DATE);
        endDate.setConverter(DATE_CONVERTER);
        endDate.setId("end-date");

        //Create end time input
        TextField endTime = new TextField(NOW_TIME.plusHours(1).format(TIME_FORMAT));
        endTime.setId("end-time");
        endTime.setPrefColumnCount(5);

        //Add end datetime row
        result.addRow(++row, endL, endDate, endTime);

        //Add separation row
        Label empty1 = new Label();
        result.addRow(++row, empty1);

        //Add repetition label
        Label repetitionL = new Label("Repetition:");
        result.add(repetitionL, 0, ++row);

        //Add a checkbox for each day of the week
        CheckBox boxMo = new CheckBox("Monday");
        boxMo.setId("box-mo");
        result.add(boxMo, 1, row);
        CheckBox boxTu = new CheckBox("Tuesday");
        boxTu.setId("box-tu");
        result.add(boxTu, 1, ++row);
        CheckBox boxWe = new CheckBox("Wednesday");
        boxWe.setId("box-we");
        result.add(boxWe, 1, ++row);
        CheckBox boxTh = new CheckBox("Thursday");
        boxTh.setId("box-th");
        result.add(boxTh, 1, ++row);
        CheckBox boxFr = new CheckBox("Friday");
        boxFr.setId("box-fr");
        result.add(boxFr, 1, ++row);
        CheckBox boxSa = new CheckBox("Saturday");
        boxSa.setId("box-sa");
        result.add(boxSa, 1, ++row);
        CheckBox boxSu = new CheckBox("Sunday");
        boxSu.setId("box-su");
        result.add(boxSu, 1, ++row);

        //Create repetition start date label
        Label repetitionStartL = new Label("From:");

        //Create repetition start date input
        DatePicker repetitionStart = new DatePicker(NOW_DATE);
        repetitionStart.setConverter(DATE_CONVERTER);
        repetitionStart.setId("repetition-start");

        //Add repetition start date row
        result.addRow(++row, repetitionStartL, repetitionStart);

        //Create repetition end date label
        Label repetitionEndL = new Label("To:");

        //Create repetition end date input
        DatePicker repetitionEnd = new DatePicker(NOW_DATE.plusWeeks(1));
        repetitionEnd.setConverter(DATE_CONVERTER);
        repetitionEnd.setId("repetition-end");

        //Add repetition end date row
        result.addRow(++row, repetitionEndL, repetitionEnd);

        //Add separation row
        Label empty2 = new Label();
        result.addRow(++row, empty2);

        //Add location label
        Label locationL = new Label("Location:");
        result.add(locationL, 0, ++row, 1, 1);

        //Add location input
        TextField location = new TextField();
        location.setId("location");
        result.add(location, 1, row, 3, 1);

        //Add description label
        Label descriptionL = new Label("Description:");
        result.add(descriptionL, 0, ++row, 1, 1);

        //Add description input
        TextArea description = new TextArea();
        description.setId("description");
        result.add(description, 1, row, 3, 2);

        //Iterate row again because of row span of description
        row++;

        //Add reset button
        Button resetB = new Button("Reset");
        resetB.setId("reset-button");
        resetB.setOnAction(e -> reset(result));
        result.add(resetB, 0, ++row, 1, 1);

        //Create export to existing button
        Button exportToExistingB = new Button("Export to existing");
        exportToExistingB.setId("export-to-existing-button");
        exportToExistingB.setOnAction(e -> exportToExisting(result));

        //Export export to new button
        Button exportToNewB = new Button("Export to new");
        exportToNewB.setId("export-to-new-button");
        exportToNewB.setOnAction(e -> exportToNew(result));

        //Add both export buttons to the right-bottom corner
        HBox exportButtons = new HBox(exportToExistingB, exportToNewB);
        exportButtons.setAlignment(Pos.BASELINE_RIGHT);
        result.add(exportButtons, 3, row);

        return result;
    }

    /**
     * Converts information from the form into a list of {@code Event} objects
     *
     * @param root The root of the form
     * @return The {@code Event} objects
     */
    private static List<Event> convert(Parent root){
        //Find the title
        String title = ((TextField) root.lookup("#title")).getText();

        //Find the start date
        LocalDate startDate = ((DatePicker) root.lookup("#start-date")).getValue();

        //Find the end date
        LocalDate endDate = ((DatePicker) root.lookup("#end-date")).getValue();

        //Find the start time
        LocalTime startTime = LocalTime.parse(((TextField) root.lookup("#start-time")).getText(), TIME_FORMAT);

        //Find the end time
        LocalTime endTime = LocalTime.parse(((TextField) root.lookup("#end-time")).getText(), TIME_FORMAT);

        //Find the location
        String location = ((TextField) root.lookup("#location")).getText();

        //Find the description
        String description = ((TextArea) root.lookup("#description")).getText();

        //Find the repeat values for each day
        boolean[] repeat = new boolean[7];
        repeat[0] = ((CheckBox) root.lookup("#box-mo")).isSelected();
        repeat[1] = ((CheckBox) root.lookup("#box-tu")).isSelected();
        repeat[2] = ((CheckBox) root.lookup("#box-we")).isSelected();
        repeat[3] = ((CheckBox) root.lookup("#box-th")).isSelected();
        repeat[4] = ((CheckBox) root.lookup("#box-fr")).isSelected();
        repeat[5] = ((CheckBox) root.lookup("#box-sa")).isSelected();
        repeat[6] = ((CheckBox) root.lookup("#box-su")).isSelected();
        boolean shouldRepeat = repeat[0] || repeat[1] || repeat[2] || repeat[3] || repeat[4] || repeat[5] || repeat[6];

        //Find the repeat from date
        LocalDate repeatFrom = ((DatePicker) root.lookup("#repetition-start")).getValue();

        //Find the repeat to date
        LocalDate repeatTo = ((DatePicker) root.lookup("#repetition-end")).getValue();

        //DEBUG: print the data
        if(debug) {
            String debugMsg = (new StringBuilder("[DEBUG] Form -> Event conversion:")).append(System.lineSeparator())
                    .append("Title: ").append(title).append(System.lineSeparator())
                    .append("Start date: ").append(DATE_FORMAT.format(startDate)).append(System.lineSeparator())
                    .append("Start time: ").append(TIME_FORMAT.format(startTime)).append(System.lineSeparator())
                    .append("End date: ").append(DATE_FORMAT.format(endDate)).append(System.lineSeparator())
                    .append("End time: ").append(TIME_FORMAT.format(endTime)).append(System.lineSeparator())
                    .append("Location: ").append(location).append(System.lineSeparator())
                    .append("Description: ").append(description).append(System.lineSeparator())
                    .append("Repeat: ").append(Arrays.toString(repeat)).append(System.lineSeparator())
                    .append("Repeat start date: ").append(DATE_FORMAT.format(repeatFrom)).append(System.lineSeparator())
                    .append("Repeat end date: ").append(DATE_FORMAT.format(repeatTo)).append(System.lineSeparator())
                    .toString();
            System.out.println(debugMsg);
        }

        //Prepare result
        List<Event> result = new ArrayList<>();

        //Handle repetition
        if(shouldRepeat){
            //Case: repetition requested

            //Iterate for each day between bounds (inclusive start, exclusive end)
            LocalDate current = repeatFrom;
            while(current.isEqual(repeatFrom)
                    || (current.isAfter(repeatFrom) && current.isBefore(repeatTo))){
                //Check whether that day of the week should repeat
                if(repeat[current.getDayOfWeek().getValue() - 1]){
                    //Create the event at that day
                    Event event = new Event(
                            title,
                            current,
                            current.plusDays(endDate.toEpochDay() - startDate.toEpochDay()),
                            startTime,
                            endTime,
                            location,
                            description);
                    result.add(event);
                }

                //Increase day
                current = current.plusDays(1);
            }
        }else{
            //Case: no repetition
            Event event = new Event(
                    title,
                    startDate,
                    endDate,
                    startTime,
                    endTime,
                    location,
                    description);
            result.add(event);
        }

        //DEBUG: print number of events created
        if(debug){
            System.out.printf("\nConverted form into %d event(s)\n", result.size());
        }

        return result;
    }

    /**
     * Exports the event(s) to a new iCalendar file
     *
     * @param form The root of the event form
     */
    private void exportToNew(Parent form){
        //DEBUG: print message
        if(debug){
            System.out.println("[DEBUG] \"Export to new\" button pressed");
        }

        //Select the new file
        FileChooser fc = new FileChooser();
        fc.setTitle("Save iCalendar");
        fc.setInitialDirectory(Paths.get(System.getProperty("user.home")).toFile());
        fc.setInitialFileName("calendar.ics");
        File file = fc.showSaveDialog(window);
        if(file == null) return;        //Skip on cancel
        Calendar cal = Calendar.createFile(file.toPath());

        //Check calendar exists
        if(cal == null){
            //Case: calendar was not loaded
            displayError("Calendar could not be loaded.");
            return;
        }

        //Convert the form to events
        List<Event> events = convert(form);

        //Write to the calendar
        cal.addEvents(events.toArray(new Event[0]));
        if(cal.save()){
            displaySuccess("Events saved to \'"+file.getAbsolutePath()+"\'.");
        }else{
            displayError("Could not saved events to \'" + file.getAbsolutePath() + "\'.");
        }
    }

    /**
     * Resets the event form
     *
     * @param form The root of the event form
     */
    private static void reset(Parent form){
        //DEBUG: print message
        if(debug){
            System.out.println("[DEBUG] \"Reset\" button pressed");
        }

        //Reset the title
        ((TextField) form.lookup("#title")).setText("Untitled event");

        //Reset the start date
        ((DatePicker) form.lookup("#start-date")).setValue(NOW_DATE);

        //Reset the end date
        ((DatePicker) form.lookup("#end-date")).setValue(NOW_DATE);

        //Reset the start time
        ((TextField) form.lookup("#start-time")).setText(NOW_TIME.format(TIME_FORMAT));

        //Reset the end time
        ((TextField) form.lookup("#end-time")).setText(NOW_TIME.plusHours(1).format(TIME_FORMAT));

        //Reset the location
        ((TextField) form.lookup("#location")).setText("");

        //Reset the description
        ((TextArea) form.lookup("#description")).setText("");

        //Reset the repeat values for each day
        ((CheckBox) form.lookup("#box-mo")).setSelected(false);
        ((CheckBox) form.lookup("#box-tu")).setSelected(false);
        ((CheckBox) form.lookup("#box-we")).setSelected(false);
        ((CheckBox) form.lookup("#box-th")).setSelected(false);
        ((CheckBox) form.lookup("#box-fr")).setSelected(false);
        ((CheckBox) form.lookup("#box-sa")).setSelected(false);
        ((CheckBox) form.lookup("#box-su")).setSelected(false);

        //Reset the repeat from date
        ((DatePicker) form.lookup("#repetition-start")).setValue(NOW_DATE);

        //Reset the repeat to date
        ((DatePicker) form.lookup("#repetition-end")).setValue(NOW_DATE.plusWeeks(1));
    }

    /**
     * Exports the event(s) into an existing iCalendar file
     *
     * @param form The root of the event form
     */
    private void exportToExisting(Parent form){
        //DEBUG: print message
        if(debug){
            System.out.println("[DEBUG] \"Export to existing\" button pressed");
        }

        //Select the file
        FileChooser fc = new FileChooser();
        fc.setTitle("Open iCalendar");
        fc.setInitialDirectory(Paths.get(System.getProperty("user.home")).toFile());
        fc.setInitialFileName("calendar.ics");
        File file = fc.showOpenDialog(window);
        if(file == null) return;        //Skip on cancel
        Calendar cal = null;
        try {
            cal = new Calendar(file.toPath());
        }catch(IllegalArgumentException e){
            displayError("Calendar could not be loaded.");
            return;
        }

        //Convert the form to events
        List<Event> events = convert(form);

        //Write to the calendar
        cal.addEvents(events.toArray(new Event[0]));
        if(cal.save()){
            displaySuccess("Events saved to \'"+file.getAbsolutePath()+"\'.");
        }else{
            displayError("Could not saved events to \'" + file.getAbsolutePath() + "\'.");
        }
    }

    /**
     * Displays an error message in the status bar
     *
     * @param msg The message to display
     */
    public void displayError(String msg){
        statusMsg.setTextFill(Color.RED);
        statusMsg.setText(msg);

        //Print to console when in debug
        if(debug){
            System.out.println("[ERROR] " + msg);
        }
    }

    /**
     * Displays an info message in the status bar
     *
     * @param msg The message to display
     */
    public void displayInfo(String msg){
        statusMsg.setTextFill(Color.BLACK);
        statusMsg.setText(msg);
    }

    /**
     * Displays an success message in the status bar
     *
     * @param msg The message to display
     */
    public void displaySuccess(String msg){
        statusMsg.setTextFill(Color.GREEN);
        statusMsg.setText(msg);
    }


}
