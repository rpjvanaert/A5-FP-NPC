import javafx.animation.*;
import javafx.application.Application;
import javafx.event.*;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.stage.*;
import javafx.util.Duration;

import java.util.*;

public class Clock extends Application {

    private int speed = 4;
    private int minute = -1;
    private int hour = 0;
    private double minuteSpeed;
    private double DigitalclockSpeed;
    private double hourSpeed;


    public static void main(String[] args) throws Exception { launch(args); }
    public void start(final Stage stage) throws Exception {
        // construct the analogueClock pieces.
        final Circle face     = new Circle(100, 100, 100);
        face.setId("face");
        final Line hourHand   = new Line(0, 0, 0, -50);
        hourHand.setTranslateX(100);   hourHand.setTranslateY(100);
        hourHand.setId("hourHand");
        final Line minuteHand = new Line(0, 0, 0, -75);
        minuteHand.setTranslateX(100); minuteHand.setTranslateY(100);
        minuteHand.setId("minuteHand");
        final Circle spindle = new Circle(100, 100, 5);
        spindle.setId("spindle");

        this.hourSpeed = 12.0/this.speed;
        this.minuteSpeed = 60.0/this.speed;
        this.DigitalclockSpeed = this.minuteSpeed/60.0;

        Group ticks = new Group();
        for (int i = 0; i < 12; i++) {
            Line tick = new Line(0, -83, 0, -93);
            tick.setTranslateX(100); tick.setTranslateY(100);
            tick.getStyleClass().add("tick");
            tick.getTransforms().add(new Rotate(i * (360 / 12)));
            ticks.getChildren().add(tick);
        }
        final Group analogueClock = new Group(face, ticks, spindle, hourHand, minuteHand);

        // construct the digitalClock pieces.
        final Label digitalClock = new Label();
        digitalClock.setId("digitalClock");

        // determine the starting time.
        Calendar calendar            = GregorianCalendar.getInstance();
        final double seedMinuteDegrees  = 0 * (360 / 60);
        final double seedHourDegrees    = 0 * (360 / 12);

        // define rotations to map the analogueClock to the current time.
        final Rotate hourRotate      = new Rotate(seedHourDegrees);
        final Rotate minuteRotate    = new Rotate(seedMinuteDegrees);
        hourHand.getTransforms().add(hourRotate);
        minuteHand.getTransforms().add(minuteRotate);

        // the hour hand rotates twice a day.
        final Timeline hourTime = new Timeline(
                new KeyFrame(
                        Duration.minutes(this.hourSpeed),
                        new KeyValue(
                                hourRotate.angleProperty(),
                                360 + seedHourDegrees,
                                Interpolator.LINEAR
                        )
                )
        );

        // the minute hand rotates once an hour.
        final Timeline minuteTime = new Timeline(
                new KeyFrame(
                        Duration.seconds(this.minuteSpeed),
                        new KeyValue(
                                minuteRotate.angleProperty(),
                                360 + seedMinuteDegrees,
                                Interpolator.LINEAR
                        )
                )
        );

        // the digital clock updates once a second.
        final Timeline digitalTime = new Timeline(
                new KeyFrame(Duration.seconds(0),
                        new EventHandler<ActionEvent>() {
                            @Override public void handle(ActionEvent actionEvent) {
                                Calendar calendar            = GregorianCalendar.getInstance();
                                minute++;
                                String hourString = "";
                                String minuteString ="";
                                if (minute>=60){
                                    minute = 0;
                                    hour++;
                                }
                                if (minute>=10){
                                    minuteString = minute + "";
                                }
                                else {
                                    minuteString = "0"+minute;
                                }
                                if (hour>=24){
                                    hour=0;
                                }
                                if (hour>=10){
                                    hourString = hour + "";
                                }
                                else {
                                    hourString = "0" + hour;
                                }
                                digitalClock.setText(hourString + ":" + minuteString);
                            }
                        }
                ),
                new KeyFrame(Duration.seconds(this.DigitalclockSpeed))
        );

        // time never ends.
        hourTime.setCycleCount(Animation.INDEFINITE);
        minuteTime.setCycleCount(Animation.INDEFINITE);
        digitalTime.setCycleCount(Animation.INDEFINITE);

        // start the analogueClock.
        digitalTime.play();
        minuteTime.play();
        hourTime.play();

        stage.initStyle(StageStyle.TRANSPARENT);

        // fade out the scene and shut it down when the mouse is clicked on the clock.
        analogueClock.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                analogueClock.setMouseTransparent(true);
                FadeTransition fade = new FadeTransition(Duration.seconds(1.2), analogueClock);
                fade.setOnFinished(new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent actionEvent) {
                        stage.close();
                    }
                });
                fade.setFromValue(1);
                fade.setToValue(0);
                fade.play();
            }
        });

        // layout the scene.
        final VBox layout = new VBox();
        layout.getChildren().addAll(analogueClock, digitalClock);
        layout.setAlignment(Pos.CENTER);
        final Scene scene = new Scene(layout, Color.TRANSPARENT);
        scene.getStylesheets().add(getResource("clock.css"));
        stage.setScene(scene);

        // allow the clock background to be used to drag the clock around.
        final Delta dragDelta = new Delta();
        layout.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                // record a delta distance for the drag and drop operation.
                dragDelta.x = stage.getX() - mouseEvent.getScreenX();
                dragDelta.y = stage.getY() - mouseEvent.getScreenY();
                scene.setCursor(Cursor.MOVE);
            }
        });
        layout.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                scene.setCursor(Cursor.HAND);
            }
        });
        layout.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                stage.setX(mouseEvent.getScreenX() + dragDelta.x);
                stage.setY(mouseEvent.getScreenY() + dragDelta.y);
            }
        });
        layout.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                if (!mouseEvent.isPrimaryButtonDown()) {
                    scene.setCursor(Cursor.HAND);
                }
            }
        });
        layout.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                if (!mouseEvent.isPrimaryButtonDown()) {
                    scene.setCursor(Cursor.DEFAULT);
                }
            }
        });

        // show the scene.
        stage.show();
    }

    private String pad(int fieldWidth, char padChar, String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = s.length(); i < fieldWidth; i++) {
            sb.append(padChar);
        }
        sb.append(s);

        return sb.toString();
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    static String getResource(String path) {
        return Clock.class.getResource(path).toExternalForm();
    }

    // records relative x and y co-ordinates.
    class Delta { double x, y; }
}
