package net.pilif0.calendar_generator;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.time.LocalDate;
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
        ScrollPane root = new ScrollPane();
        root.setPadding(new Insets(10));

        //Fill the scroll pane
        root.setContent(buildEventForm());

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

        //Prepare row pointer
        int row = 0;

        //Add the title input
        TextField titleField = new TextField("Untitled event");
        titleField.setId("title-field");
        titleField.requestFocus();
        //titleField.setPrefColumnCount(32);
        result.add(titleField,0, row, 4, 1);

        //Add start date input
        DatePicker startDate = new DatePicker(LocalDate.now());
        startDate.setId("start-date");
        result.add(startDate, 0, ++row, 1, 1);

        //Add start time input
        TextField startTime = new TextField(/*TODO add current time*/);
        startTime.setId("start-time");
        startTime.setPrefColumnCount(5);
        result.add(startTime, 1, row, 1, 1);
        
        //Add end date input
        DatePicker endDate = new DatePicker(LocalDate.now());
        endDate.setId("end-date");
        result.add(endDate, 2, row, 1, 1);

        //Add end time input
        TextField endTime = new TextField(/*TODO add current time*/);
        endTime.setId("end-time");
        endTime.setPrefColumnCount(5);
        result.add(endTime, 3, row, 1, 1);

        //TODO add repetition settings
        //  selection of days of the week to repeat on
        //  selection of datetime interval over which to spread

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

        //Add export to existing button
        Button exportToExistingB = new Button("Export to existing");
        exportToExistingB.setId("export-to-existing-button");
        exportToExistingB.setOnAction(e -> System.out.println("Export to existing pressed")/*TODO add proper handler*/);
        result.add(exportToExistingB, 2, row, 1, 1);

        //Add export to new button
        Button exportToNewB = new Button("Export to new");
        exportToNewB.setId("export-to-new-button");
        exportToNewB.setOnAction(e -> System.out.println("Export to new pressed")/*TODO add proper handler*/);
        result.add(exportToNewB, 3, row, 1, 1);

        return result;
    }
}
