import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.channels.NetworkChannel;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class NpcDemo extends Application {

    private ResizableCanvas canvas;
    private ArrayList<Person> people;
    private int amount = 30;
    private int speed = 4;
    private Boolean PredictedGuests = true;
    private ArrayList<Integer> Prediction = new ArrayList<>();

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        predictedvisitors();
        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());
        new AnimationTimer() {
            long last = -1;

            @Override
            public void handle(long now) {
                if (last == -1)
                    last = now;
                update((now - last) / 1000000000.0);
                last = now;
                draw(g2d);
            }
        }.start();

        stage.setScene(new Scene(mainPane, 1500, 800));
        stage.setTitle("NPCs");
        stage.show();
        draw(g2d);

        canvas.setOnMouseMoved(e ->
        {
            for (Person person : people) {
                person.setTarget(new Point2D.Double(e.getX(), e.getY()));
            }
        });

        canvas.setOnMouseClicked(e -> clickAction(e));

    }


    public void init() {
        this.people = new ArrayList<>();
        spawnPeople(30);
    }

    public void draw(FXGraphics2D g2) {
        g2.setTransform(new AffineTransform());
        g2.setBackground(new Color(100, 75, 75));
        g2.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());


        for (Person person : people) {
            person.draw(g2);
        }

    }

    public void update(double frameTime) {
        for (Person person : people) {
            person.update(people);
        }
    }

    public boolean canSpawn(Point2D spawnPosition) {
        if (this.people.size() <= 0) {
            return true;
        }
        for (Person person : people) {
            if (spawnPosition.distance(person.getPosition()) < 64) {
                return false;
            }
        }
        return true;
    }

    public void spawnPeople(int amount) {
        int failedSpawnAttempts = 0;

        for (int i = 0; i < amount; i++) {
            Point2D newSpawnLocation = new Point2D.Double(Math.random() * 1800, Math.random() * 1000);
            if (canSpawn(newSpawnLocation)) {
                this.people.add(new Person(newSpawnLocation, this.Prediction, this.speed));
                failedSpawnAttempts = 0;
            } else {
                failedSpawnAttempts++;
                if (failedSpawnAttempts > amount * 0.1) {
                    return;
                }

            }
        }
    }

    public void clickAction(MouseEvent e) {
        for (Person person : this.people) {
            if (person.getPosition().distance(new Point2D.Double(e.getX(), e.getY())) < 32) {
                person.playSoundEffect();
            }
        }
    }


    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getSpeed() {
        return speed;
    }

    public int getAmount() {
        return amount;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setPredictedGuests(Boolean predictedGuests) {
        PredictedGuests = predictedGuests;
    }

    public void predictedvisitors() {
        int Total = 6;
        int metal = 1;
        int Country = 1;
        int classic = 1;
        int Rap = 1;
        int Pop = 1;
        int Electro = 1;
        if (this.PredictedGuests) {
//            for (Show show : DataController.getPlanner().getShows()) {
//                String superGenre = show.getGenre.getSuperGenre();
//                if (superGenre.equals("metal")) {
//                    metal++;
//                } else if (superGenre.equals("Country")) {
//                    Country++;
//                } else if (superGenre.equals("Classic")) {
//                    classic++;
//                } else if (superGenre.equals("Rap")) {
//                    Rap++;
//                } else if (superGenre.equals("Pop")) {
//                    Pop++;
//                } else if (superGenre.equals("Electro")) {
//                    Electro++;
//                }
//
//
//                Total++;
//            }
        }
        this.Prediction.add(metal);
        this.Prediction.add(classic);
        this.Prediction.add(Country);
        this.Prediction.add(Rap);
        this.Prediction.add(Pop);
        this.Prediction.add(Electro);
        this.Prediction.add(Total);
    }


}