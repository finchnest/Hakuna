package com.lunarapps.hakuna.mainOps;

import com.lunarapps.hakuna.ServiceNames;
import com.lunarapps.hakuna.models.Image;
import com.lunarapps.hakuna.models.User;
import com.lunarapps.hakuna.network.SocketConnectionSingleton;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GridbaseC implements Initializable {

    public ScrollPane gridScroller;
    private User u = new MainController().getUser();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadData();
    }

    private void loadData() {

        int identifier = new InteractController().getMethodIdentifier();
 
        SocketConnectionSingleton mSingleton = SocketConnectionSingleton.getInstance();

        switch (identifier) {

            case 1:

                try {
                    mSingleton.printWriter.write(ServiceNames.MY_IMAGES.toString());
                    mSingleton.printWriter.flush();

                    ArrayList<Image> userImages = (ArrayList<Image>) mSingleton.objectInputStream.readObject();

                    GridPane gridPane = new GridPane();
                    gridPane.setPrefSize(180, 180);
                    gridPane.setVgap(5);
                    gridPane.setHgap(20);
                    gridPane.setLayoutY(10);
                    gridPane.setLayoutX(40);//there will be a 40px gap from the first and last grids to the parent

                    int imageCtr = 0;

                    M:
                    for (int i = 0; i < (int) Math.ceil(userImages.size() / 4); i++) {

                        for (int j = 0; j < 4; j++) {

                            if (imageCtr < userImages.size()) {

                                Image mImage = userImages.get(imageCtr);

                                ImageView view = new ImageView();
                                view.setFitHeight(120);
                                view.setFitWidth(120);
                                view.setLayoutX(30);
                                view.setLayoutY(5);
                                view.setImage(new javafx.scene.image.Image(new ByteArrayInputStream(mImage.getImagesBytes())));

                                Label name = new Label();
                                name.setPrefHeight(15);
                                name.setLayoutX(30);
                                name.setText(mImage.getName());

                                Label downloadCount = new Label();
                                downloadCount.setPrefHeight(15);
                                downloadCount.setLayoutX(40);
                                downloadCount.setText("Download Count " + mImage.getDownloadCount());

                                VBox vbox = new VBox();
                                vbox.setPrefSize(180, 180);
                                vbox.setSpacing(10);
                                vbox.getChildren().addAll(view, name, downloadCount);
                                vbox.setId((Integer.toString(imageCtr)));


                                gridPane.add(vbox, i, j);

                                imageCtr += 1;
                            } else {
                                break M;
                            }
                        }

                    }

                    gridPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            Node clickedNode = event.getPickResult().getIntersectedNode();
                            if (clickedNode != gridPane) {
                                Node parent = clickedNode.getParent();
                                while (parent != gridPane) {
                                    clickedNode = parent;
                                    parent = clickedNode.getParent();
                                }
/*
                                when the parent of the clicked node becomes the gridpane,
                                then that is the node we want to operate on!!

                                Integer collIndex = GridPane.getColumnIndex(clickedNode);
                                Integer rowIndex = GridPane.getRowIndex(clickedNode);

                                for (Node node : gridPane.getChildren()) {
                                    VBox vbo = (VBox) node;
                                }
*/
                            }
                            VBox vb = (VBox) clickedNode;
                            Image clicked = userImages.get(Integer.parseInt(vb.getId()));

                            ButtonType download = new ButtonType("Download", ButtonBar.ButtonData.APPLY);
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to download " +
                                    "this Image ?", download);
                            alert.setTitle("Download Action");

                            if (alert.getResult() == download) {
                                //download code
                                download(clicked);
                            }

                        }
                    });

                    //add the gridpane to the scrollpane or pane
                    gridScroller = new ScrollPane(gridPane);
                    gridScroller.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);


                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    mSingleton.printWriter.write(ServiceNames.MY_DOCS.toString());
                    mSingleton.printWriter.flush();

                    ArrayList userDocs = new ArrayList();
                    userDocs = (ArrayList) mSingleton.objectInputStream.readObject();


                    GridPane gridPane = new GridPane();
                    gridPane.setPrefSize(180, 180);
                
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                try {
                    mSingleton.printWriter.write(ServiceNames.SHARED_IMAGES.toString());
                    mSingleton.printWriter.flush();

                    ArrayList sharedImages = new ArrayList();
                    sharedImages = (ArrayList) mSingleton.objectInputStream.readObject();


                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                try {
                    mSingleton.printWriter.write(ServiceNames.SHARED_DOCS.toString());
                    mSingleton.printWriter.flush();

                    ArrayList sharedDocs = new ArrayList();
                    sharedDocs = (ArrayList) mSingleton.objectInputStream.readObject();


                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }

    }

    private void download(Image x) {
        String downloadFolder = System.getProperty("user.home") + "\\Downloads\\" + u.getUsername();
        java.io.File file = new java.io.File(downloadFolder);
        boolean v = file.mkdir();
        boolean t = writeBytesToFile(x.getImagesBytes(), downloadFolder, x.getName());

        if (v && t) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Successful Download");
            alert.setHeaderText("Success");
            alert.setContentText("The image is successfully downloaded to he downloads folder!");
            alert.showAndWait();
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
}
