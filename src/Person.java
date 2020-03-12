import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * A class to represent a visitor
 */
public class Person {
    private Point2D position;
    private double angle;
    private double speed;
    private BufferedImage sprite;
    private String favoriteGenre;
    private Media soundEffect;
    private MediaPlayer mediaPlayer;
    private String activity;
    private int negativeFeedback = 5;

    private Point2D target;
    private double rotationSpeed;
    private String targetMapName;

    /**
     * A constructor of Person
     * @param position the starting position of the Person
     * @param genreChanceList a list for the probability of liking a Genre
     * @param speed the movement speed of the Person
     */
    public Person(Point2D position, ArrayList<Integer> genreChanceList, int speed) {
        this.position = position;
        imageDecider(genreChanceList);
        this.angle = 0;
        this.speed = speed;
        this.target = position;
        this.rotationSpeed = 100;
        this.targetMapName = selectRandomMap();
    }

    /**
     * Assings the corresponding image and sound according to the liked superGenre
     * @param genreChance
     */
    public void imageDecider(ArrayList<Integer> genreChance) {
        int number = (int) (Math.random() * ((genreChance.get(6) - 1) + 1)) + 1;

        if (genreChance.get(0) >= number && number > 0) {
            try {
                this.sprite = ImageIO.read(this.getClass().getResourceAsStream("/images/metal.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.favoriteGenre = "metal";
            this.soundEffect = new Media(new File("resources/soundEffects/MetalScream.mp3").toURI().toString());
        } else if ((genreChance.get(0) + genreChance.get(1)) >= number && number > genreChance.get(0)) {
            try {
                this.sprite = ImageIO.read(this.getClass().getResourceAsStream("/images/classic.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.favoriteGenre = "classic";
            this.soundEffect = new Media(new File("resources/soundEffects/ClassicLaugh.mp3").toURI().toString());
        } else if ((genreChance.get(0) + genreChance.get(1) + genreChance.get(2)) >= number && number > (genreChance.get(0) + genreChance.get(1))) {
            try {
                this.sprite = ImageIO.read(this.getClass().getResourceAsStream("/images/country.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.favoriteGenre = "Country";
            this.soundEffect = new Media(new File("resources/soundEffects/CountryAlabama.mp3").toURI().toString());
        } else if ((genreChance.get(6) - genreChance.get(5) - genreChance.get(4)) >= number && number > (genreChance.get(0) + genreChance.get(1) + genreChance.get(2))) {
            try {
                this.sprite = ImageIO.read(this.getClass().getResourceAsStream("/images/rap.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.favoriteGenre = "rap";
            this.soundEffect = new Media(new File("resources/soundEffects/ClassicLaugh.mp3").toURI().toString());
        } else if ((genreChance.get(6) - genreChance.get(5)) >= number && number > (genreChance.get(6) - genreChance.get(5) - genreChance.get(4))) {

            try {
                this.sprite = ImageIO.read(this.getClass().getResourceAsStream("/images/Pop.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.favoriteGenre = "Pop";
            this.soundEffect = new

                    Media(new File("resources/soundEffects/ClassicLaugh.mp3").toURI().toString());
        } else if (genreChance.get(6) >= number && number > (genreChance.get(6) - genreChance.get(5))) {
            try {
                this.sprite = ImageIO.read(this.getClass().getResourceAsStream("/images/electro.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.favoriteGenre = "Electro";
            this.soundEffect = new Media(new File("resources/soundEffects/ClassicLaugh.mp3").toURI().toString());
        } else {
            try {
                this.sprite = ImageIO.read(this.getClass().getResourceAsStream("/images/npc.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.favoriteGenre = "npc";
            this.soundEffect = new Media(new File("resources/soundEffects/ClassicLaugh.mp3").toURI().toString());

        }
        this.mediaPlayer = new MediaPlayer(this.soundEffect);
    }

    /**
     * decides the behavior of the Person
     */
    public void choiceMaker() {
        int number = (int) (Math.random() * ((10 - 1) + 1)) + 1;
        if (number > 5/*this.favoriteGenre==genre.getSuperGenre()*/) {
            //change back once integrated with the main application
            if (number < 2) {
                System.out.println("didn't go, so idle");
                this.negativeFeedback--;
            } else {
                System.out.println("did go to the show");
                this.negativeFeedback = 5;
            }
        } else {
            if (number <= this.negativeFeedback) {
                System.out.println("didn't go, so idle");
                this.negativeFeedback--;
            } else {
                System.out.println("did go to the show");
                this.negativeFeedback = 5;
            }
        }
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void update(ArrayList<Person> people) {

        if (hasArrivedAtDestination()){
            this.targetMapName = selectRandomMap();
            setNextTarget();
        } else if (hasArrivedAtTarget()){
            setNextTarget();
        }


        double targetAngle = Math.atan2(this.target.getY() - this.position.getY(),
                this.target.getX() - this.position.getX());

        double angleDifference = this.angle - targetAngle;
        while (angleDifference < -Math.PI)
            angleDifference += 2 * Math.PI;
        while (angleDifference > Math.PI)
            angleDifference -= 2 * Math.PI;


        if (Math.abs(angleDifference) < this.rotationSpeed)
            this.angle = targetAngle;
        else if (angleDifference < 0)
            this.angle += this.rotationSpeed;
        else
            this.angle -= this.rotationSpeed;

        Point2D newPosition = new Point2D.Double(this.position.getX() + this.speed * Math.cos(this.angle),
                this.position.getY() + this.speed * Math.sin(this.angle));

        //colliding handler
        boolean collided = false;

        for (Person other : people) {
            if (other != this && newPosition.distance(other.position) < 32) {
                collided = true;
            }
        }

        if (!collided) {
            this.position = newPosition;
        } else {
            this.target = PathCalculator.findRandomClosestWalkable(this.position,this.targetMapName);
        }
    }

    public void draw(Graphics2D g) {
        g.drawImage(sprite, getTransform(), null);
    }

    public Point2D getPosition() {
        return position;
    }

    private AffineTransform getTransform() {
        AffineTransform tx = new AffineTransform();
        tx.translate(position.getX() - this.sprite.getWidth() / 2, position.getY() - this.sprite.getHeight() / 2);
        tx.rotate(this.angle, this.sprite.getWidth() / 2, this.sprite.getHeight() / 2);
        return tx;
    }

    public void setTarget(Point2D target) {
        this.target = target;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    /**
     * Plays an soundEffect according to the genre
     */
    public void playSoundEffect() {
        if (this.favoriteGenre.equals("metal")) {
            this.mediaPlayer.setVolume(0.05);
        }
        this.mediaPlayer.setStartTime(Duration.millis(0));
        this.mediaPlayer.play();
        this.mediaPlayer.stop();
        this.mediaPlayer.setStartTime(Duration.millis(0));

    }

    public void setTargetMapName(String mapName){
        this.targetMapName = mapName;
    }

    /**
     * checks if the Person has arrived at the target
     * @return
     */
    public boolean hasArrivedAtTarget(){
        double distanceAmount = 17;
        return position.distance(target.getX(), target.getY()) < distanceAmount;
    }

    /**
     *checks if the Person has arrived at it's end destination
     */
    private boolean hasArrivedAtDestination() {
        double distanceAmount = 16;
        if(this.target.distance(new Point2D.Double(-1,-1)) < distanceAmount){
            return true;
        }
        return false;
    }

    public void setNextTarget(){
        this.target = PathCalculator.nextTarget(this.position, targetMapName);
    }

    /**
     * For testing purposes!
     * Selects a random distanceMap
     * @return the name of the map
     */
    public String selectRandomMap(){
        Random random = new Random();
        String mapName = null;
        int randomNumber = random.nextInt(3);
        switch (randomNumber){
            case 0: mapName = "TestMap1";
            break;
            case 1: mapName = "TestMap2";
            break;
            case 2: mapName = "TestMap3";
            break;

        }
        return mapName;
    }
}