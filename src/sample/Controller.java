package sample;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import sample.Circle;
import sample.Figure;

import java.awt.*;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

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
    private GraphicsContext graphicsContext;

    ArrayList<Figure> canvasFigures = new ArrayList<>();
    Figure activeFigure;

    private ObservableList<UdpPackage> loggedPackages = FXCollections.observableArrayList();

    //Bruger receiver og sender, for at den skal virke som packet sender.
    private UdpPackageReceiver receiver;
    private DatagramSocket sender;
    private int x;
    private int y;
    private int xEnd = 200;
    private int yEnd = 200;

    public void initialize()
    {
        // runs when application GUI is ready
        //Fra drawing on canvas
        System.out.println("ready!");

        comboBoxFigure.getItems().addAll(new Circle());

        graphicsContext = canvas.getGraphicsContext2D();




        //Fra UDPserver
        //add udp server/reeciver
        receiver = new UdpPackageReceiver(loggedPackages, 6000, this);
        new Thread(receiver).start(); //Starter ss thread. Den skal altså køre i sin egen seperate thread. Serveren og applicationen kører i hver sin thread. For at køre i en thread skal man følge nogle regler, der er beskrevet i en interface der bruges.
        //receiveUdpMessage();

        //create udp sender
        try {
            sender = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        } //sende beskeder(?)


    }

    public void updateQuote(String quote, String source)
    {
        quoteText.setText(quote);
        quoteTextSource.setText(source);
    }

    public void setPenSize(MouseEvent mouseEvent) {
        labelPenSize.setText(""+(int)sliderPenSize.getValue());
    }

    public void setFigure(ActionEvent actionEvent) {

    }

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
        //System.out.println("CanvasClick " + mouseEvent.getX() + ":" + mouseEvent.getY());
        //comboBoxFigure.hide();
        //Timer T = new Timer();

        //System.out.println(comboBoxFigure.getValue());
        //if (comboBoxFigure.getValue() != null) {
        //comboBoxFigure.setValue();
        graphicsContext.clearRect(0,0, canvas.getWidth(), canvas.getHeight());
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
            //activeFigure = null;

        //Thread.sleep(3000);
        //TimeUnit.SECONDS.sleep(4);
        //Thread.sleep(3000);

        //graphicsContext.clearRect(0,0, canvas.getWidth(), canvas.getHeight());


    }

    public void moveUp(int xUp, int yUp){

            //activeFigure = comboBoxFigure.getValue().getCopy();
        graphicsContext.clearRect(0,0, canvas.getWidth(), canvas.getHeight());
            activeFigure = new Circle();
            //activeFigure.start = new Point((int) mouseEvent.getX(), (int) mouseEvent.getY());
            activeFigure.start = new Point((int) x + xUp, (int) y + yUp);
            System.out.println("activefigure start point: " + activeFigure.start.toString());

            activeFigure.end = new Point((int) xEnd + xUp, (int) yEnd + yUp);
            System.out.println("activeFigure start point is: " + activeFigure.start.toString());
            System.out.println("and end point is: " + activeFigure.end.toString());
            //canvasFigures.add(activeFigure);
            drawActiveFigure(activeFigure);
            //activeFigure = null;


    }

    private void drawActiveFigure(Figure activeFigure) {
        //graphicsContext.setStroke(colorpicker.getValue());
        graphicsContext.setStroke(Color.TURQUOISE);
        activeFigure.draw(graphicsContext);
    }

    public void droneCommand(String cmd)
    {
        switch (cmd){
            case "w":
                System.out.println("creating the drone");
                drawFigure();
                break;
                case "up":
                    boolean a = true;
                    while(a==true) {
                        System.out.println("moving the drone up");
                        moveUp(0, -10);
                        if(cmd!="up"){
                            a = false;
                        }
                    }
                break;
            case "down":

        }
    }

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
}
