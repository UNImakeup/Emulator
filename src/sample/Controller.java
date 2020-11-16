package sample;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;

import javax.sound.sampled.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class Controller implements DroneCommander{

    public Canvas canvas;
    public Label instructionText;
    public Label connectionText;
    public GridPane gridPane;
    public GridPane gridPane1;
    public GridPane gridPane2;
    public GridPane gridPane3;
    private GraphicsContext graphicsContext;

    Figure activeFigure;

    private ObservableList<UdpPackage> loggedPackages = FXCollections.observableArrayList();

    //Bruger receiver og sender, for at den skal virke som packet sender.
    private UdpPackageReceiver receiver;
    private int x = 150;
    private int y = 150;
    private int xEnd = x+100;
    private int yEnd = y+100;
    private int life = 3;
    private int yup=-5; //Behøver nok ikke variable, bare skrive tallene hvor metoden køres.
    private int wap = 5; //Same

    private boolean l = true;

    private boolean droneDrawn = false;
    private boolean sunHitOnce = false;
    private boolean gameOver = false;

    public void initialize() throws FileNotFoundException {
        // runs when application GUI is ready
        System.out.println("ready!");


        graphicsContext = canvas.getGraphicsContext2D();


            setSurroundings(false);
            connectionText.setVisible(false);

        showImage("C:\\Users\\depay\\Downloads\\Interactive Digital Systems\\heart.png", 1, 0, gridPane1);
        showImage("C:\\Users\\depay\\Downloads\\Interactive Digital Systems\\heart.png", 1, 0, gridPane2);
        showImage("C:\\Users\\depay\\Downloads\\Interactive Digital Systems\\heart.png", 1, 0, gridPane3);

        //Fra UDPserver
        //add udp server/reeciver
        receiver = new UdpPackageReceiver(loggedPackages, 6000, this);
        new Thread(receiver).start(); //Starter ss thread. Den skal altså køre i sin egen seperate thread. Serveren og applicationen kører i hver sin thread. For at køre i en thread skal man følge nogle regler, der er beskrevet i en interface der bruges.
    }

    private void drawFigure(){
        if (droneDrawn == false) {
            graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); //Måske som metode.
            setSurroundings(false);
            activeFigure = new Circle();
            activeFigure.start = new Point((int) x, (int) y);
            activeFigure.end = new Point((int) xEnd, (int) yEnd);
            System.out.println("activeFigure start point is: " + activeFigure.start.toString());
            System.out.println("and end point is: " + activeFigure.end.toString());
            drawActiveFigure(activeFigure);
            connectionText.setVisible(true);
            droneDrawn = true;
        }
    }

    //Rename. Kunne vel egentlig også godt være private. Kan ikke se hvorfor andre metoder skal bruge den.
    private void move(int xUp, int yUp){
        if(droneDrawn == true) {
            graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); //Burde bare være clearCanvas metoden.
            setSurroundings(false);
            activeFigure = new Circle();
            x += xUp;
            y += yUp;
            xEnd += xUp;
            yEnd += yUp;
            activeFigure.start = new Point((int) x, (int) y);
            activeFigure.end = new Point((int) xEnd, (int) yEnd);
            System.out.println("activeFigure start point is: " + activeFigure.start.toString());
            System.out.println("and end point is: " + activeFigure.end.toString());
            drawActiveFigure(activeFigure);

            //kunne være i hit sun metode
            hitSun();

//Kunne være egen metode, for overskuelighed. Kunne have eget navn og genbruges. Bryde metoderne ned i så små som muligt. Overskuelighed. Man ved hvad metoden gør.
            hitWater();
        }
        }

        private void hitSun(){

            if (activeFigure.start.x <= 80 && activeFigure.start.y <= 80 && sunHitOnce == false) {
                //Laver mund om
                setSurroundings(true);

                try {
                    playSound("C:\\Users\\depay\\Downloads\\MyLinkedList\\sound\\yes.wav"); //Have helikopter lyd i stedet.
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (LineUnavailableException e) {
                    e.printStackTrace();
                } catch (UnsupportedAudioFileException e) {
                    e.printStackTrace();
                }
                System.out.println("you hit the SUN MY BROTHER!!!!!!!!!!!!!!!!!!");
                sunHitOnce = true;

            }

            if (activeFigure.start.x > 80 | activeFigure.start.y > 80) {
                sunHitOnce = false;
            }
        }

        private void hitWater(){
            if (activeFigure.end.y > 650 && l == true) {

                life -= 1;
                //Lives.setText(new String(String.valueOf(life)));
                System.out.println(life + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
                l = false;
                if(life > 0) {
                    try {
                        playSound("C:\\Users\\depay\\Downloads\\blob.wav"); //Have helikopter lyd i stedet.
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (LineUnavailableException e) {
                        e.printStackTrace();
                    } catch (UnsupportedAudioFileException e) {
                        e.printStackTrace();
                    }
                }

                switch (life) {
                    case (2):
                        gridPane3.setVisible(false);
                        break;
                    case (1):
                        gridPane2.setVisible(false);

                }
            }
            if (activeFigure.end.y < 650) {
                l = true;
            }
        }

    private void drawActiveFigure(Figure activeFigure) {
        graphicsContext.setStroke(Color.TURQUOISE);
        activeFigure.draw(graphicsContext);
    }

    public void droneCommand(String cmd)
    {
        if(life > 0) {
            switch (cmd) {
                case "w":
                    System.out.println("creating the drone");
                    drawFigure();
                    break;
                case "up":
                    boolean a = true;
                    while (a == true) {
                        //yup=yup-10;
                        System.out.println("moving the drone up");
                        move(0, yup);
                        if (cmd != "down") {
                            a = false;
                        }
                    }
                    break;
                case "down":
                    boolean b = true;
                    while (b == true) {
                        //wap=wap+10;
                        System.out.println("moving the drone down");
                        move(0, wap);
                        if (cmd != "down") {
                            b = false;
                        }
                    }
                    break;
                case "right":
                    boolean c = true;
                    while (c == true) {
                        System.out.println("movingthedronetotheright");
                        move(wap, 0);
                        if (cmd != "right") {
                            c = false;
                        }
                    }
                    break;
                case "left":
                    boolean d = true;
                    while (d == true) {
                        System.out.print("movingthedronetotheleft");
                        move(yup, 0);
                        if (cmd != "left") {
                            d = false;
                        }
                    }
                    break;
                case "nothing":
                    graphicsContext.setFill(Color.BLACK);
                    graphicsContext.setFont(new Font("arial", 20));
                    graphicsContext.fillText("lol", canvas.getWidth()/2-150, canvas.getHeight()/2);
                    TimerTask task = new TimerTask(){
                    @Override
                            public void run(){
                        graphicsContext.clearRect(0,0,canvas.getWidth(),canvas.getHeight());
                        setSurroundings(false);
                        move(0, 0);
                    }
                };
                Timer timer = new Timer();
                timer.schedule(task,500);


            }
        }else{
            //køre metode der skriver game over.
            if(gameOver == false) {
                gameOver();
                gameOver = true;
            }
        }
    }

    private void gameOver(){
        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); //Burde bare være clearCanvas metoden.
        graphicsContext.setFill(Color.AQUA);
        graphicsContext.fillRect(0,0, canvas.getWidth(), canvas.getHeight()); //Burde måske spille lyd; you crashed into the water, oh no.
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.setFont(new Font("arial", 20));
        graphicsContext.fillText("oh no, you crashed into the water :((((((", canvas.getWidth()/2-150, canvas.getHeight()/2);
        gridPane1.setVisible(false);
        connectionText.setVisible(false);

        try {
            playSound("C:\\Users\\depay\\Downloads\\dead.wav"); //Have helikopter lyd i stedet.
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

    private void playSound(String soundFile) throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        File f = new File(soundFile);
        AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());
        Clip clip = AudioSystem.getClip();
        clip.open(audioIn);
        clip.start();
    }

    private void setSurroundings(boolean hitSun){
            graphicsContext.setFill(Color.YELLOW);
            graphicsContext.fillOval(-50, -50, 150, 150);
            graphicsContext.setFill(Color.BLACK);
            graphicsContext.fillOval(25, 40, 10, 10);
            graphicsContext.fillOval(45, 30, 10, 10);

        if(hitSun==true) {
            graphicsContext.fillArc(37, 15, 30, 60, 210, 150, ArcType.OPEN);
        } else {
            graphicsContext.fillRect(34, 53, 25, 4);
        }

            graphicsContext.setFill(Color.AQUA);
            graphicsContext.fillRect(0, 600, canvas.getWidth(), canvas.getHeight() - 600); //water
            graphicsContext.setFill(Color.BISQUE);
            graphicsContext.fillRect(0, 600, canvas.getWidth(), 50); //Sand


    }

    private void showImage(String img, int r1, int r2, GridPane g) throws FileNotFoundException {
        Image image = new Image(new FileInputStream(img));


        //Setting the image view
        ImageView imageView = new ImageView(image);

        //Setting the position of the image
        imageView.setX(0);
        imageView.setY(0);

        //setting the fit height and width of the image view
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);
        g.add(imageView, r1, r2);
        imageView.setVisible(true);

    }
}
