package com.lunarapps.hakuna.mainOps;

import com.lunarapps.hakuna.ServiceNames;
import com.lunarapps.hakuna.models.File;
import com.lunarapps.hakuna.models.Image;
import com.lunarapps.hakuna.models.User;
import com.lunarapps.hakuna.network.SocketConnectionSingleton;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class SearchResultC implements Initializable {


    public AnchorPane searchResultView;
    private ListView searchElement;
    private SocketConnectionSingleton mSingleton = SocketConnectionSingleton.getInstance();
    private TextField searchField = new InteractController().getSearchField();
    private User u = new MainController().getUser();
    private Logger logger = Logger.getLogger(getClass().getName());

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadData();
    }

    private void loadData() {

        try {
            mSingleton.printWriter.write(ServiceNames.SEARCH.toString() + "&%" + searchField.getText());
            mSingleton.printWriter.flush();

            ArrayList<ArrayList> res = (ArrayList<ArrayList>) mSingleton.objectInputStream.readObject();
            ArrayList<Image> images = (ArrayList<Image>) res.get(0);
            ArrayList<File> files = (ArrayList<File>) res.get(1);

            if (images.size() == 0 && files.size() == 0) {
                searchField.setText("No images or PDFs with " + searchField.getText() + " name found");
            } else {

                ListView<Image> imageList = new ListView<>();

                imageList.getItems().setAll(images);
                imageList.setFixedCellSize(40);
//                list.setPrefSize(120, 100);
                VBox box = new VBox();
                box.getChildren().addAll(imageList);
                box.setSpacing(10);
//                VBox.setVgrow(list, Priority.ALWAYS);
                searchResultView.getChildren().addAll(box);

                imageList.setCellFactory(cf -> new MImageCell());

                imageList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    logger.info("currently selected item is '" + newValue.getName() + "'");
                });

                imageList.setOnMouseClicked(event -> {
                    Image x = imageList.getSelectionModel().getSelectedItem();

                    ButtonType share = new ButtonType("Share Me", ButtonBar.ButtonData.OK_DONE);
                    ButtonType download = new ButtonType("Download", ButtonBar.ButtonData.APPLY);
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "What do you want to do ?", share, download);
                    alert.setTitle("Choose Action");

                    if (alert.getResult() == share) {
                        //share code
                        share(x);

                    } else if (alert.getResult() == download) {
                        //download code
                        download(x);
                    }
                });

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void share(Image x) {
        mSingleton.printWriter.write(ServiceNames.SHARE.toString());
        mSingleton.printWriter.flush();
        //the server needs the images path, and the user's username to complete this task

        String message = x.getName() + "!@#$" + x.getPath() + "!@#$" + x.getUploader().getUsername() + "!@#$" +
                u.getUsername() + "!@#$" + x.getType();

        try {
            mSingleton.objectOutputStream.writeObject(message);
            boolean valid = Boolean.parseBoolean(new BufferedReader(mSingleton.inputStreamReader).readLine());
            if (!valid) {
                searchField.setText("error occurred!");
            }
            searchField.setText("successfully shared!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void download(Image x) {
        String downloadFolder = System.getProperty("user.home") + "\\Downloads\\" + u.getUsername();
        java.io.File file = new java.io.File(downloadFolder);
        boolean v = file.mkdir();
        boolean t = writeBytesToFile(x.getImagesBytes(), downloadFolder, x.getName());

        if (v && t) {
            searchField.setText("successful download!");
        }
    }

    private boolean writeBytesToFile(byte[] imageBytes, String destination, String namie) {
        try {
            FileOutputStream fos = new FileOutputStream(destination + java.io.File.separator + namie);
            fos.write(imageBytes);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static class MImageCell extends javafx.scene.control.ListCell<Image> {

        @Override
        protected void updateItem(Image item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                setText(item.getName());
                ImageView iv = new ImageView();
                iv.setImage(new javafx.scene.image.Image("com/lunarapps/hakuna/res/pdf_icon.png"));//no need to use object property here
                setGraphic(iv);
            }
        }
    }
}
