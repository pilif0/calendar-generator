package net.pilif0.calendar_generator;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

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

    /** Whether debug mode is enabled */
    public static boolean debug = false;

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
        //Prepare the main pane
        StackPane root = new StackPane();
        root.setPadding(new Insets(10));

        //Fill the scroll pane
        root.getChildren().add(buildEventForm());

        //Finalize the window
        Scene scene = new Scene(root/*, WIDTH, HEIGHT*/);
        primaryStage.setScene(scene);
        primaryStage.setTitle(TITLE);
        primaryStage.sizeToScene();     //TODO remove this when done and set proper WIDTH and HEIGHT
        primaryStage.show();
    }

    /**
     * Builds the event input form
     *
     * @return The event form scene graph
     */
    public static Node buildEventForm(){
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
        titleField.setId("title-field");
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
        boxMo.setId("box-tu");
        result.add(boxTu, 1, ++row);
        CheckBox boxWe = new CheckBox("Wednesday");
        boxMo.setId("box-we");
        result.add(boxWe, 1, ++row);
        CheckBox boxTh = new CheckBox("Thursday");
        boxMo.setId("box-th");
        result.add(boxTh, 1, ++row);
        CheckBox boxFr = new CheckBox("Friday");
        boxMo.setId("box-fr");
        result.add(boxFr, 1, ++row);
        CheckBox boxSa = new CheckBox("Saturday");
        boxMo.setId("box-sa");
        result.add(boxSa, 1, ++row);
        CheckBox boxSu = new CheckBox("Sunday");
        boxMo.setId("box-su");
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

        //Iterate row again because of rowspan of description
        row++;

        //Add reset button
        Button resetB = new Button("Reset");
        resetB.setId("reset-button");
        resetB.setOnAction(e -> System.out.println("Reset pressed")/*TODO add proper handler*/);
        result.add(resetB, 0, ++row, 1, 1);

        //Create export to existing button
        Button exportToExistingB = new Button("Export to existing");
        exportToExistingB.setId("export-to-existing-button");
        exportToExistingB.setOnAction(e -> System.out.println("Export to existing pressed")/*TODO add proper handler*/);

        //Export export to new button
        Button exportToNewB = new Button("Export to new");
        exportToNewB.setId("export-to-new-button");
        exportToNewB.setOnAction(e -> System.out.println("Export to new pressed")/*TODO add proper handler*/);

        //Add both export buttons to the right-bottom corner
        HBox exportButtons = new HBox(exportToExistingB, exportToNewB);
        exportButtons.setAlignment(Pos.BASELINE_RIGHT);
        result.add(exportButtons, 3, row);

        return result;
    }
}
