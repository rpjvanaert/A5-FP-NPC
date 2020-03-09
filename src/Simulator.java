import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Simulator extends Application {

    private ResizableCanvas canvas;
    private ArrayList<Person> people;

    private int peopleAmount = 30;
    private int stageAmount = 6;
    private int toiletAmount = 20;
    private int globalSpeed = 4;

    private boolean predictedGuests = true;
    private ArrayList<Integer> Prediction = new ArrayList<>();
    private CameraTransform cameraTransform;
    private boolean showNull = false;

    private static DistanceMap[] distanceMaps;

    @Override
    public void start(Stage stage) {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(this::draw, mainPane);
        mainPane.setCenter(canvas);
        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());
        this.cameraTransform = new CameraTransform(canvas);
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

<<<<<<< HEAD:src/Simulator.java
        canvas.setOnMouseMoved(e -> {
=======


        canvas.setOnMouseMoved(e ->
        {
>>>>>>> master:src/NpcDemo.java
            double zoom = this.cameraTransform.getZoom();
            for (Person person : people) {
                person.setTarget(cameraTransform.getRelPoint2D(e.getX(), e.getY()));
            }
        });

<<<<<<< HEAD:src/Simulator.java
        canvas.setOnMouseClicked(this::clickAction);
=======
        canvas.setOnMouseClicked(e -> {
            clickAction(e);
            if (e.getButton() == MouseButton.SECONDARY){
                this.showNull = !this.showNull;
                if (this.showNull){
                    System.out.println("Shows: Non CameraTransformed");
                } else {
                    System.out.println("Shows: CameraTransformed");
                }
            } else if (e.getButton() == MouseButton.PRIMARY){
                this.init();
            }
        });
>>>>>>> master:src/NpcDemo.java
    }

    public void init() {
        this.people = new ArrayList<>();
        this.distanceMaps = new DistanceMap[stageAmount + toiletAmount];

        // initializeDistanceMap();

        createPredictions();
        spawnPeople(peopleAmount);
    }

    public void update(double frameTime) {
        for (Person person : people) {
            person.update(people);
        }
    }

    public void draw(FXGraphics2D g2) {
        Point2D p2d = this.cameraTransform.getCenterPoint();
        double zoom = cameraTransform.getZoom();
<<<<<<< HEAD:src/Simulator.java
        g2.clearRect(-(int) p2d.getX(), -(int) p2d.getY(), (int) (canvas.getWidth() / zoom), (int) (canvas.getHeight() / zoom));
        g2.setTransform(this.cameraTransform.getTransform());
=======
        g2.clearRect(-(int)p2d.getX(), -(int)p2d.getY(), (int) (canvas.getWidth() / zoom), (int) (canvas.getHeight() / zoom));
        if (!this.showNull){
            g2.setTransform(this.cameraTransform.getTransform());
        } else {
            g2.setTransform(new AffineTransform());
        }
>>>>>>> master:src/NpcDemo.java
        g2.setBackground(Color.WHITE);
        Shape rect = new Rectangle2D.Double(0, 0, 2500, 2500);
        g2.setPaint(Color.BLACK);
        g2.draw(rect);

        for (Person person : people) {
            person.draw(g2);
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

//        Point2D newSpawnLocation = new Point2D.Double(Math.random() * 1800, Math.random() * 1000);
        Point2D newSpawnLocation = new Point2D.Double(700, 1200);

        for (int i = 0; i < amount; i++) {

            if (canSpawn(newSpawnLocation)) {
                this.people.add(new Person(new Point2D.Double(newSpawnLocation.getX(),
                        newSpawnLocation.getY() + i * 32), this.Prediction, this.globalSpeed));
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

    public void setPeopleAmount(int peopleAmount) {
        this.peopleAmount = peopleAmount;
    }

    public int getGlobalSpeed() {
        return globalSpeed;
    }

    public int getPeopleAmount() {
        return peopleAmount;
    }

    public void setGlobalSpeed(int globalSpeed) {
        this.globalSpeed = globalSpeed;
    }

    public void setPredictedGuests(boolean predictedGuests) {
        this.predictedGuests = predictedGuests;
    }

    public void createPredictions() {
        int Total = 6;
        int metal = 1;
        int Country = 1;
        int classic = 1;
        int Rap = 1;
        int Pop = 1;
        int electro = 1;

        this.Prediction.add(metal);
        this.Prediction.add(classic);
        this.Prediction.add(Country);
        this.Prediction.add(Rap);
        this.Prediction.add(Pop);
        this.Prediction.add(electro);
        this.Prediction.add(Total);
    }

    public static DistanceMap getDistanceMap(String mapName){
        for (DistanceMap dm : distanceMaps) {
            if (dm.getMapName().equals(mapName))
                return dm;
        }

        return null;
    }
}