import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Person {
    private Point2D position;
    private double angle;
    private double speed;
    private BufferedImage sprite;
    private String FavoriteGenre;
    private Media soundEffect;
    private MediaPlayer mediaPlayer;
    private String activity;

    private Point2D target;
    private double rotationSpeed;

    public Person(Point2D position, ArrayList GenreChance, int speed) {
        this.position = position;
        Imagedecider(GenreChance);
        this.angle = 0;
        this.speed = speed;
        this.target = new Point2D.Double(200, 200);
        this.rotationSpeed = 0.1;
    }

    public void Imagedecider(ArrayList<Integer> GenreChance) {
        int number = (int) (Math.random() * ((GenreChance.get(6) - 1) + 1)) + 1;
        if (GenreChance.get(0) <= number && number >= 0) {
            try {
                this.sprite = ImageIO.read(this.getClass().getResourceAsStream("/images/metal.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.FavoriteGenre = "metal";
            this.soundEffect = new Media(new File("resources/soundEffects/MetalScream.mp3").toURI().toString());
        } else if ((GenreChance.get(0) + GenreChance.get(1)) <= number && number > GenreChance.get(0)) {
            try {
                this.sprite = ImageIO.read(this.getClass().getResourceAsStream("/images/classic.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.FavoriteGenre = "classic";
            this.soundEffect = new Media(new File("resources/soundEffects/ClassicLaugh.mp3").toURI().toString());
        } else if ((GenreChance.get(0) + GenreChance.get(1) + GenreChance.get(2)) <= number && number > (GenreChance.get(0) + GenreChance.get(1))) {
            try {
                this.sprite = ImageIO.read(this.getClass().getResourceAsStream("/images/country.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.FavoriteGenre = "Country";
            this.soundEffect = new Media(new File("resources/soundEffects/CountryAlabama.mp3").toURI().toString());
        } else if ((GenreChance.get(6) - GenreChance.get(5) - GenreChance.get(4)) <= number && number > (GenreChance.get(0) + GenreChance.get(1) + GenreChance.get(2))) {
            try {
                this.sprite = ImageIO.read(this.getClass().getResourceAsStream("/images/rap.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.FavoriteGenre = "rap";
            this.soundEffect = new Media(new File("resources/soundEffects/ClassicLaugh.mp3").toURI().toString());
        } else if ((GenreChance.get(6) - GenreChance.get(5)) <= number && number > (GenreChance.get(6) - GenreChance.get(5) - GenreChance.get(4))) {

            try {
                this.sprite = ImageIO.read(this.getClass().getResourceAsStream("/images/Pop.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.FavoriteGenre = "Pop";
            this.soundEffect = new

                    Media(new File("resources/soundEffects/ClassicLaugh.mp3").toURI().toString());
        } else if (GenreChance.get(6) <= number && number > (GenreChance.get(6) - GenreChance.get(5))) {
            try {
                this.sprite = ImageIO.read(this.getClass().getResourceAsStream("/images/electro.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.FavoriteGenre = "Electro";
            this.soundEffect = new Media(new File("resources/soundEffects/ClassicLaugh.mp3").toURI().toString());
        } else {
            try {
                this.sprite = ImageIO.read(this.getClass().getResourceAsStream("/images/npc.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.FavoriteGenre = "npc";
            this.soundEffect = new Media(new File("resources/soundEffects/ClassicLaugh.mp3").toURI().toString());

        }
        this.mediaPlayer = new MediaPlayer(this.soundEffect);
    }

    public void update(ArrayList<Person> people) {
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

        boolean collided = false;

        for (Person other : people) {
            if (other != this && newPosition.distance(other.position) < 64) {
                collided = true;
            }
        }


        if (!collided) {
            this.position = newPosition;
        } else {
            this.angle -= this.rotationSpeed * 2;
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

    public void playSoundEffect() {
        if (this.FavoriteGenre.equals("metal")) {
            this.mediaPlayer.setVolume(0.05);
        }
        this.mediaPlayer.setStartTime(Duration.millis(0));
        this.mediaPlayer.play();
        this.mediaPlayer.stop();
        this.mediaPlayer.setStartTime(Duration.millis(0));

    }
}
