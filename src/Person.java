import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

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

    private Point2D target;
    private double rotationSpeed;

    private static final int Classic = 1;
    private static final int Metal = 2;
    private static final int Country = 3;
    private static final int Electro = 4;
    private static final int Pop = 5;
    private static final int Rap = 6;

    public Person(Point2D position, int personNo, int speed) {
        this.position = position;
        Imagedecider(personNo);
        this.angle = 0;
        this.speed = speed;
        this.target = new Point2D.Double(200, 200);
        this.rotationSpeed = 0.1;
    }

    public void Imagedecider(int number) {
        switch (number) {
            case Classic:
                try {
                    this.sprite = ImageIO.read(this.getClass().getResourceAsStream("/images/classic.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.FavoriteGenre = "classic";
                this.soundEffect = new Media(new File("resources/soundEffects/ClassicLaugh.mp3").toURI().toString());
                break;
            case Metal:
                try {
                    this.sprite = ImageIO.read(this.getClass().getResourceAsStream("/images/metal.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.FavoriteGenre = "metal";
                this.soundEffect = new Media(new File("resources/soundEffects/MetalScream.mp3").toURI().toString());
                break;
            case Country:
                try {
                    this.sprite = ImageIO.read(this.getClass().getResourceAsStream("/images/country.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.FavoriteGenre = "Country";
                this.soundEffect = new Media(new File("resources/soundEffects/ClassicLaugh.mp3").toURI().toString());
                break;
            case Electro:
                try {
                    this.sprite = ImageIO.read(this.getClass().getResourceAsStream("/images/electro.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.FavoriteGenre = "Electro";
                this.soundEffect = new Media(new File("resources/soundEffects/ClassicLaugh.mp3").toURI().toString());
                break;
            case Pop:
                try {
                    this.sprite = ImageIO.read(this.getClass().getResourceAsStream("/images/Pop.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.FavoriteGenre = "Pop";
                this.soundEffect = new Media(new File("resources/soundEffects/ClassicLaugh.mp3").toURI().toString());
                break;
            case Rap:
                try {
                    this.sprite = ImageIO.read(this.getClass().getResourceAsStream("/images/rap.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.FavoriteGenre = "rap";
                this.soundEffect = new Media(new File("resources/soundEffects/ClassicLaugh.mp3").toURI().toString());
                break;
            default:
                try {
                    this.sprite = ImageIO.read(this.getClass().getResourceAsStream("/images/npc.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.FavoriteGenre = "npc";
                this.soundEffect= new Media(new File("resources/soundEffects/ClassicLaugh.mp3").toURI().toString());
                break;
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

    private AffineTransform getTransform() {
        AffineTransform tx = new AffineTransform();
        tx.translate(position.getX() - this.sprite.getWidth() / 2, position.getY() - this.sprite.getHeight() / 2);
        tx.rotate(this.angle, this.sprite.getWidth() / 2, this.sprite.getHeight() / 2);
        return tx;
    }

    public void setTarget(Point2D target) {
        this.target = target;
    }

    public void playSoundEffect(){
        this.mediaPlayer.play();
    }
}
