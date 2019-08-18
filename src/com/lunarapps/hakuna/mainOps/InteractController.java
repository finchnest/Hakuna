package com.lunarapps.hakuna.mainOps;

import com.lunarapps.hakuna.ServiceNames;
import com.lunarapps.hakuna.models.File;
import com.lunarapps.hakuna.models.History;
import com.lunarapps.hakuna.models.Image;
import com.lunarapps.hakuna.network.SocketConnectionSingleton;
import com.sun.javaws.exceptions.BadJARFileException;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class InteractController implements Initializable {
    @FXML
    private FontAwesomeIconView searchIcon;
    @FXML
    private Pane operationPane;
    @FXML
    private TextField searchField;
    @FXML
    private ListView searchResultListView;
    @FXML
    private GridPane featuredGrid;
    private SocketConnectionSingleton mSingleton = SocketConnectionSingleton.getInstance();
    private int methodIdentifier;


    InteractController() {
        searchField = new TextField();
        searchField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                perfSearch(event);
            }
        });

        searchIcon = new FontAwesomeIconView();
        searchIcon.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                perfSearch(event);
            }
        });

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        home(new javafx.event.ActionEvent());
    }

    public void runUpload(ActionEvent actionEvent) {

        try {
            Parent uploadView = FXMLLoader.load(getClass().getResource("../layouts/upload.fxml"));
            operationPane.getChildren().removeAll();
            operationPane.getChildren().setAll(uploadView);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void home(ActionEvent actionEvent) {

        /*the grid is already defined in default interact window so there is no need
         * to load it. what remains is to ask the server to bring the 4 most downloaded
         * items from the database and display them in the grid provided*/

        //the database request code goes here


        try {
            mSingleton.printWriter.write(ServiceNames.HOME.toString());
            mSingleton.printWriter.flush();

            ArrayList home = new ArrayList();
            home = (ArrayList) mSingleton.objectInputStream.readObject();


            //the featured grid will be decorated with the most downloaded items
            //from the database


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void girdLoader() {
        try {
            Parent gridView = FXMLLoader.load(getClass().getResource("../layouts/gridbase.fxml"));
            operationPane.getChildren().removeAll();
            operationPane.getChildren().setAll(gridView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void myImages(ActionEvent actionEvent) {
        methodIdentifier = 1;
        girdLoader();
    }

    public void myDocs(ActionEvent actionEvent) {
        methodIdentifier = 2;
        girdLoader();
    }

    public void sharedImages(ActionEvent actionEvent) {
        methodIdentifier = 3;
        girdLoader();
    }

    public void sharedDocs(ActionEvent actionEvent) {
        methodIdentifier = 4;
        girdLoader();
    }

    public void logout(ActionEvent actionEvent) throws IOException {
        Parent main = FXMLLoader.load(getClass().getResource("../layouts/main.fxml"));
        MainController mc = new MainController();
        mc.setUser(null);
        mc.getMainFrame().getChildren().removeAll();
        mc.getMainFrame().getChildren().setAll(main);
    }

    int getMethodIdentifier() {
        return methodIdentifier;
    }


    public void searchHistory(ActionEvent actionEvent) {
        operationPane.getChildren().removeAll();

        try {
            mSingleton.printWriter.write(ServiceNames.HISTORY.toString());
            mSingleton.printWriter.flush();

            mSingleton.objectOutputStream.writeObject(new MainController().getUser());
            mSingleton.objectOutputStream.flush();

            ArrayList userRec = (ArrayList) mSingleton.objectInputStream.readObject();
            for (Object objHis : userRec) {
                History oneHis = (History) objHis;

                //from this history object we can extract the info and
                //dynamically display it on our list views by generating
                //list views

                ListView hisView = new ListView();

//                operationPane.getChildren().set()

            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


    public void feedback(ActionEvent actionEvent) {
        try {
            Parent feedView = FXMLLoader.load(getClass().getResource("../layouts/feedback.fxml"));
            operationPane.getChildren().removeAll();
            operationPane.getChildren().setAll(feedView);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void help(ActionEvent actionEvent) {

        //real code does not go here

        //a new layout is needed

        /*this includes the about section also and a how-to-do guide
         *it will be a pane under scroll pane and a link to my github*/

    }

    public void searchOnKeyTyped(KeyEvent keyEvent) {

        mSingleton.printWriter.write(ServiceNames.SEARCH.toString() + "&%" + searchField.getText());
        mSingleton.printWriter.flush();

        ArrayList<ArrayList> res;
        try {
            res = (ArrayList<ArrayList>) mSingleton.objectInputStream.readObject();
            ArrayList<Image> images = (ArrayList<Image>) res.get(0);
            ArrayList<File> files = (ArrayList<File>) res.get(1);

            ArrayList<String> display = new ArrayList<String>();

            for (Image im : images) {
                display.add(im.getName());
            }
            for (File fil : files) {
                display.add(fil.getName());

            }


            /*create a ListView element/object for each string object in
             * display var and display that string in the searchResultListView
             * on each ListView item click, a call to perfSearch will be sent
             * with the appropriate argument value*/




        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void perfSearch(Event Event) {
        if (searchField.getText().length() > 0) {
            try {
                Parent searchView = FXMLLoader.load(getClass().getResource("../layouts/searchresult.fxml"));
                operationPane.getChildren().removeAll();
                operationPane.getChildren().setAll(searchView);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            searchField.setText("write something to search");
        }
    }


    TextField getSearchField() {
        return searchField;
    }

}
