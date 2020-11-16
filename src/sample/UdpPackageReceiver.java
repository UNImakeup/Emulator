package sample;

import javafx.collections.ObservableList;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.List;

public class UdpPackageReceiver implements Runnable { //Den implementerer runnable

    boolean running = false;
    DatagramSocket socket;
    private byte[] buf = new byte[256];
    int port;
    DroneCommander commander;
    ObservableList<UdpPackage> udpPackages;

    public UdpPackageReceiver(List udpPackages, int port, DroneCommander commander) {
        this.running = true;
        this.udpPackages = (ObservableList) udpPackages;
        this.commander = commander;
        this.port = port;
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void shutDown(){
        running = false;
    }

    @Override
    public void run() {
        while (running)
        {
            buf = new byte[256];
            DatagramPacket packet = new DatagramPacket(buf, buf.length); //Han laver sin egen package
            try {
                socket.receive(packet);
                //System.out.println("package arrived!");
                UdpPackage udpPackage = new UdpPackage("name", packet.getData(), packet.getAddress(), socket.getLocalAddress(), packet.getPort(), socket.getLocalPort());
                udpPackages.add(udpPackage);
                System.out.println(udpPackage.getASCII());

                String msg = new String(packet.getData()).trim();

                commander.droneCommand(msg); //Måske prøve at rykke det op.

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
