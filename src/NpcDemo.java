import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    private int amount = 30;
    private int speed = 4;

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        mainPane.setCenter(canvas);
        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());
        new AnimationTimer() {
            long last = -1;
            @Override
            public void handle(long now) {
                if(last == -1)
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
            for(Person person : people) {
                person.setTarget(new Point2D.Double(e.getX(), e.getY()));
            }
        });
    }


    ArrayList<Person> people;


    public void init() {
        this.people = new ArrayList<>();

        for(int i = 0; i < this.amount; i++) {
            int number = (int)(Math.random() * ((6 - 1) + 1)) + 1;
            Person added = new Person(new Point2D.Double(Math.random()*1800, Math.random()*1000), number, this.speed);
//            added.onMousePressedProperty(e ->{
//                    added.playSoundEffect();
//            });
            this.people.add(added);
        }
    }

    public void draw(FXGraphics2D g2)
    {
        g2.setTransform(new AffineTransform());
        g2.setBackground(new Color(100,75,75));
        g2.clearRect(0,0,(int)canvas.getWidth(), (int)canvas.getHeight());


        for(Person person : people) {
            person.draw(g2);
        }

    }

    public void update(double frameTime) {
        for(Person person : people) {
            person.update(people);
        }
    }
}