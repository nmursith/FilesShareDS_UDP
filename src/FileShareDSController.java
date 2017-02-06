import JavaBootStrapServer.Neighbour;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.*;
import java.security.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by nifras on 1/12/17.
 */
public class FileShareDSController {
    public Button connectButton;
    public TextField searchFile;
    public Button searchButton;
    ServerController serverController;
    Stage stage;
    @FXML
    ListView<String> filesIhave;// = new ListView<String>();
    ObservableList<String> items = FXCollections.observableArrayList ();

    @FXML
    ListView<String> filesAvailable;// = new ListView<String>();
    ObservableList<String> availableItems = FXCollections.observableArrayList ();


    ArrayList<Neighbour> nodes;
    Neighbour myself;
    ArrayList<String> files;
    int hops = 20;

    private boolean isConnected = false;

    public long start=0, middle =0, end =0;
    public void init() {
        System.out.println(System.currentTimeMillis());
        filesIhave.setItems(items);
        filesAvailable.setItems(availableItems);
        files = new ReadFile().getFilePerNode();
        serverController = new ServerController(this);
        items.addAll(files);

        //search();

    }
    public void search(){

        String file =   searchFile.getText() ;//"\"Mission Impossible\"";
        file = file.replace(" ", "*");
        String request = "SER "+  myself.getIp() +" " + myself.getPort() + " "+file +" "+ hops + " " +System.currentTimeMillis() ;
        search(request);
    }

    public void search(String request){
        //length SER IP port file_name hops

        int size = request.length() + 5;
        DecimalFormat myFormatter = new DecimalFormat("0000");
        String output = myFormatter.format(size);
        request =  output +" " + request ;

        System.out.println(request);

        start = System.currentTimeMillis();

        for (Neighbour node: nodes) {

            final String finalRequest = request;
            new Thread(() -> {
                  try {
                  DatagramSocket sock = new DatagramSocket();
                  InetAddress server = InetAddress.getByName(node.getIp());

                  DatagramPacket dpReply = new DatagramPacket(finalRequest.getBytes() , finalRequest.getBytes().length , server , node.getPort());
                  sock.send(dpReply);
                  byte[] buffer = new byte[65536];
                  DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
                  sock.receive(incoming);

                  byte[] data = incoming.getData();
                  String s = new String(data, 0, incoming.getLength());
                  System.out.println(s);
                  sock.close();

              } catch (UnknownHostException e) {
                  e.printStackTrace();
              } catch (SocketException e) {
                  e.printStackTrace();
              } catch (IOException e) {
                  e.printStackTrace();
              }
              }).start();
        }

    }

    public ObservableList<String> getItems() {
        return items;
    }

    public void setItems(ObservableList<String> items) {
        this.items = items;
    }

    public ObservableList<String> getAvailableItems() {
        return availableItems;
    }

    public void setAvailableItems(ObservableList<String> availableItems) {
        this.availableItems = availableItems;
    }

    public Neighbour getMyself() {
        return myself;
    }

    public void setMyself(Neighbour myself) {
        this.myself = myself;
    }

    public void download(ActionEvent actionEvent) {

        try {
            String file =filesAvailable.getSelectionModel().getSelectedItem();
            items.add(file);
            files.add(file);
        }
        catch (Exception e){

        }
        availableItems.clear();

    }

    public void delete(ActionEvent actionEvent) {
    }

    public void connect(ActionEvent actionEvent) {
        start =System.currentTimeMillis();
        if(!isConnected){

            nodes = serverController.connect();
            if(serverController.isConnected()){
                connectButton.setText("Disconnect");
                isConnected = true;
                //length JOIN IP_address port_no
                JoinLeave("JOIN");
            }

        }
        else {
            serverController.disconnect();
            if(!serverController.isConnected()){
                connectButton.setText("Connect");
                isConnected = false;
                JoinLeave("LEAVE");
                //ength LEAVE IP_address port_no

            }

        }

        end =System.currentTimeMillis();
        System.err.println("Time Elapsed to Join  "+ (end - start)+"ms");
        start = end = 0;


        searchButton.setDisable(false);
        this.setTitle();



        if(!nodes.isEmpty()) {
            myself = nodes.get(nodes.size() -1);
            nodes.remove(nodes.size() - 1);
        }
    }

    public void JoinLeave(String command){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                for(int i=0; i<nodes.size(); i++){
                    String request = command +" " + myself.getIp() + " "+ myself.getPort();
                    new CommandSender(nodes.get(i).getIp(), nodes.get(i).getPort(), request).start();
                }
            }
        });
    }
    public void setTitle(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                stage.setTitle("FileShareDS@"+ myself.getIp()+":"+myself.getPort() );
            }
        });

    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
