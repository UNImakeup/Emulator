package sample;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
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
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Controller implements DroneCommander{

    public Slider sliderPenSize;
    public ColorPicker colorpicker;
    public Label labelPenSize;
    public ComboBox<Figure> comboBoxFigure;
    public Canvas canvas;
    public Label quoteText;
    public Label quoteTextSource;
    public Button clearCanvasButton;
    public javafx.scene.layout.HBox HBox;
    public Label Lives;
    public AnchorPane anchorPane;
    public GridPane gridPane;
    public GridPane gridPane1;
    public GridPane gridPane2;
    public GridPane gridPane3;
    private GraphicsContext graphicsContext;

    ArrayList<Figure> canvasFigures = new ArrayList<>();
    Figure activeFigure;

    private ObservableList<UdpPackage> loggedPackages = FXCollections.observableArrayList();

    //Bruger receiver og sender, for at den skal virke som packet sender.
    private UdpPackageReceiver receiver;
    private DatagramSocket sender;
    private int x;
    private int y;
    private int xEnd = x+200;
    private int yEnd = y+200;
    private int life = 3;
    private int yup=-10; //Behøver nok ikke variable, bare skrive tallene hvor metoden køres.
    private int wap = 10; //Same

    private boolean l = true;

    public void initialize() throws FileNotFoundException {
        // runs when application GUI is ready
        //Fra drawing on canvas
        System.out.println("ready!");

//        comboBoxFigure.getItems().addAll(new Circle());

        graphicsContext = canvas.getGraphicsContext2D();


            setSurroundings();
            //Lives.setText(String.valueOf(life));

        showImage("C:\\Users\\depay\\Downloads\\Job og Muligheder\\WP-Rowing\\DSC_6569.JPG", 1, 0, gridPane1);
        showImage("C:\\Users\\depay\\Downloads\\Job og Muligheder\\WP-Rowing\\DSC_6569.JPG", 1, 0, gridPane2);
        showImage("C:\\Users\\depay\\Downloads\\Job og Muligheder\\WP-Rowing\\DSC_6569.JPG", 1, 0, gridPane3);




        //Fra UDPserver
        //add udp server/reeciver
        receiver = new UdpPackageReceiver(loggedPackages, 6000, this);
        new Thread(receiver).start(); //Starter ss thread. Den skal altså køre i sin egen seperate thread. Serveren og applicationen kører i hver sin thread. For at køre i en thread skal man følge nogle regler, der er beskrevet i en interface der bruges.
        //receiveUdpMessage();

        /*
        Task remove = new Task<Void>() {
            @Override public Void call() {
                gridPane.getChildren().remove(1);
                return null;
            }
        };

        new Thread(remove).start();

         */

        //create udp sender
        try {
            sender = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        } //sende beskeder(?)


    }
//Bruger vi ikke
    public void updateQuote(String quote, String source)
    {
        quoteText.setText(quote);
        quoteTextSource.setText(source);
    }
//Bruger vi ikke
    public void setPenSize(MouseEvent mouseEvent) {
        labelPenSize.setText(""+(int)sliderPenSize.getValue());
    }
//Tror ikke vi bruger den
    public void setFigure(ActionEvent actionEvent) {

    }

    //Kan slettes, men måske bruge timeren et andet sted.
    public void canvasClicked(MouseEvent mouseEvent)  {
        //comboBoxFigure.hide();
        //Timer T = new Timer();

        //System.out.println(comboBoxFigure.getValue());
        //if (comboBoxFigure.getValue() != null) {
        //comboBoxFigure.setValue();

        if (activeFigure == null) {
            //activeFigure = comboBoxFigure.getValue().getCopy();
            activeFigure = new Circle();
            //activeFigure.start = new Point((int) mouseEvent.getX(), (int) mouseEvent.getY());
            activeFigure.start = new Point((int) 208, (int) 232);
            System.out.println("activefigure start point: " + activeFigure.start.toString());
        } else {
            activeFigure.end = new Point((int) 342, (int) 365);
            System.out.println("activeFigure start point is: " + activeFigure.start.toString());
            System.out.println("and end point is: " + activeFigure.end.toString());
            //canvasFigures.add(activeFigure);
            drawActiveFigure(activeFigure);
            activeFigure = null;
        }
        //Thread.sleep(3000);
        //TimeUnit.SECONDS.sleep(4);
        //Thread.sleep(3000);

        //graphicsContext.clearRect(0,0, canvas.getWidth(), canvas.getHeight());


        TimerTask task= new TimerTask() {
            @Override
            public void run() {
                graphicsContext.clearRect(0,0, canvas.getWidth(), canvas.getHeight());
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 3000);




        //}
        /*else
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Uuups!");
            alert.setHeaderText("No figure selected!");
            alert.setContentText("Choose a figure in the combobox");


            alert.showAndWait();
        }

         */

    }


    public void drawFigure(){
            graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            setSurroundings();
            //activeFigure = comboBoxFigure.getValue().getCopy();
            activeFigure = new Circle();
            //activeFigure.start = new Point((int) mouseEvent.getX(), (int) mouseEvent.getY());
            activeFigure.start = new Point((int) x, (int) y);
            System.out.println("activefigure start point: " + activeFigure.start.toString());

            activeFigure.end = new Point((int) xEnd, (int) yEnd);
            System.out.println("activeFigure start point is: " + activeFigure.start.toString());
            System.out.println("and end point is: " + activeFigure.end.toString());
            //canvasFigures.add(activeFigure);
            drawActiveFigure(activeFigure);
    }

    //Rename. Kunne vel egentlig også godt være private. Kan ikke se hvorfor andre metoder skal bruge den.
    public void moveUp(int xUp, int yUp){
            graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); //Burde bare være clearCanvas metoden.
            setSurroundings();
            activeFigure = new Circle();
            x += xUp;
            y += yUp;
            xEnd += xUp;
            yEnd += yUp;
            //activeFigure.start = new Point((int) mouseEvent.getX(), (int) mouseEvent.getY());
            activeFigure.start = new Point((int) x, (int) y);
            System.out.println("activefigure start point: " + activeFigure.start.toString());

            activeFigure.end = new Point((int) xEnd, (int) yEnd);
            System.out.println("activeFigure start point is: " + activeFigure.start.toString());
            System.out.println("and end point is: " + activeFigure.end.toString());
            //canvasFigures.add(activeFigure);
            drawActiveFigure(activeFigure);

//Kunne være egen metode, for overskuelighed. Kunne have eget navn og genbruges. Bryde metoderne ned i så små som muligt. Overskuelighed. Man ved hvad metoden gør.
            if (activeFigure.end.y > 650 && l == true) {

                life -= 1;
                //Lives.setText(new String(String.valueOf(life)));
                System.out.println(life + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
                l = false;

                switch (life){
                    case(2):
                        gridPane3.setVisible(false);
                        break;
                    case(1):
                        gridPane2.setVisible(false);

                }

                //gridPane.setVisible(false);

                //Lives.setText(String.valueOf(life));
                //Skal måske lave a la quoteupdater
                /*
                if(life < 3) {
                    removeImage(life - 1);
                }

                 */
            }

            //l = true;
            if (activeFigure.end.y < 650) {
                l = true;
            }
        }

    private void drawActiveFigure(Figure activeFigure) {
        //graphicsContext.setStroke(colorpicker.getValue());
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
                        moveUp(0, yup);
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
                        moveUp(0, wap);
                        if (cmd != "down") {
                            b = false;
                        }
                    }
                    break;
                case "right":
                    boolean c = true;
                    while (c == true) {
                        System.out.println("movingthedronetotheright");
                        moveUp(wap, 0);
                        if (cmd != "right") {
                            c = false;
                        }
                    }
                    break;
                case "left":
                    boolean d = true;
                    while (d == true) {
                        System.out.print("movingthedronetotheleft");
                        moveUp(yup, 0);
                        if (cmd != "left") {
                            d = false;
                        }
                    }

            }
        }else{
            //køre metode der skriver game over.
            gameOver();
        }
    }

    public void gameOver(){
        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); //Burde bare være clearCanvas metoden.
        graphicsContext.setFill(Color.AQUA);
        graphicsContext.fillRect(0,0, canvas.getWidth(), canvas.getHeight()); //Burde måske spille lyd; you crashed into the water, oh no.
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.setFont(new Font("arial", 20));
        graphicsContext.fillText("oh no, you crashed into the water :((((((", canvas.getWidth()/2-150, canvas.getHeight()/2);
        gridPane1.setVisible(false);
        try {
            playSound("C:\\Users\\depay\\Downloads\\MyLinkedList\\sound\\yes.wav"); //Have helikopter lyd i stedet.
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
        //Lives.setVisible(false);
        //removeImage(1);
        //gridPane.setVisible(false);
    }

    //Kan nok bare slettes.
    public void clearCanvas(ActionEvent actionEvent) {
        //graphicsContext.clearRect(0,0, canvas.getWidth(), canvas.getHeight());
        //clearCanvasButton.setCancelButton(true); //Tror bare vi starter med denne. Som en start butyon der sætter gang i emulatoren. Behøver man måske ikke. Ved bare ikke rigtig. Lad os prøve at sout commands her.
        //HBox.getChildren().remove(clearCanvasButton); //Prøvede at fjerne knappen når man trykker. Kan så stadig have et klik der sætter igang med alt det med at lave den første cirkel, hvor man ved at modtage signaler kan opdatere positionen.
        //System.out.println(loggedPackages.get(loggedPackages.lastIndexOf(loggedPackages))); //Skal få tilføjet til loggedpackages når vi modtager beskeder tror jeg. Ellers kalde noget bestemt i receiver, så den tegner. Burde nok bare få den til at tegne uden click
        //drawFigure();
        //System.out.println(receiver.udpPackages.get(receiver.udpPackages.size()).getASCII());
        /*
        if(receiver.udpPackages.get(receiver.udpPackages.size()-1).getASCII() == "b"){
            //drawFigure();
            System.out.println("up");
        }
*/
        ObservableList<UdpPackage> a = receiver.udpPackages;
        String b = new String(a.get(1).getASCII().trim());
        System.out.println(b + "value");

        if( b.equals(a)){
            drawFigure();
        }
    }

    void playSound(String soundFile) throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        File f = new File(soundFile);
        AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());
        Clip clip = AudioSystem.getClip();
        clip.open(audioIn);
        clip.start();
    }

    void setSurroundings(){
        graphicsContext.setFill(Color.YELLOW);
        graphicsContext.fillOval(-50,-50,150,150);
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillOval(25,40,10,10);
        graphicsContext.fillOval(45,30,10,10);
        graphicsContext.fillArc(37, 15, 30, 60, 210, 150, ArcType.OPEN);
        graphicsContext.setFill(Color.AQUA);
        graphicsContext.fillRect(0,600,canvas.getWidth(),canvas.getHeight() - 600); //water
        graphicsContext.setFill(Color.BISQUE);
        graphicsContext.fillRect(0,600,canvas.getWidth(),50); //Sand

    }

    void showImage(String img, int r1, int r2, GridPane g) throws FileNotFoundException {
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
