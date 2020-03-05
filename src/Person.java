import javafx.scene.image.Image;
import javafx.scene.media.Media;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Person {
    private Point2D position;
    private double angle;
    private double speed;
    private BufferedImage sprite;
    private String FavoriteGenre;
    private Media soundEffetc;

    private Point2D target;
    private double rotationSpeed;


    public Person(Point2D position, int personNo) {
        this.position = position;
        Imagedecider(personNo);
        this.angle = 0;
        this.speed = 2;
        this.target = new Point2D.Double(200, 200);
        this.rotationSpeed = 0.1;
    }

    public void Imagedecider(int number) {
        switch (number) {
            case 1:
                try {
                    this.sprite = ImageIO.read(this.getClass().getResourceAsStream("/images/classic.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.FavoriteGenre = "classic";
//                this.soundEffetc = new Media("/soundEffects/ClassicLaugh.mp3");
                break;
            case 2:
                try {
                    this.sprite = ImageIO.read(this.getClass().getResourceAsStream("/images/metal.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.FavoriteGenre = "metal";
//                this.soundEffetc = new Media("/soundEffects/MetalScream.mp3");
                break;
            case 3:
                try {
                    this.sprite = ImageIO.read(this.getClass().getResourceAsStream("/images/country.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.FavoriteGenre = "Country";
//                this.soundEffetc = new Media("/soundEffects/ClassicLaugh.mp3");
                break;
            case 4:
                try {
                    this.sprite = ImageIO.read(this.getClass().getResourceAsStream("/images/electro.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.FavoriteGenre = "Electro";
//                this.soundEffetc = new Media("/soundEffects/ClassicLaugh.mp3");
                break;
            case 5:
                try {
                    this.sprite = ImageIO.read(this.getClass().getResourceAsStream("/images/Pop.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.FavoriteGenre = "Pop";
//                this.soundEffetc = new Media("/soundEffects/ClassicLaugh.mp3");
                break;
            case 6:
                try {
                    this.sprite = ImageIO.read(this.getClass().getResourceAsStream("/images/rap.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.FavoriteGenre = "rap";
//                this.soundEffetc = new Media("/soundEffects/ClassicLaugh.mp3");
                break;
            default:
                try {
                    this.sprite = ImageIO.read(this.getClass().getResourceAsStream("/images/npc.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.FavoriteGenre = "npc";
//                this.soundEffetc = new Media("/soundEffects/ClassicLaugh.mp3");
                break;
        }
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
}
