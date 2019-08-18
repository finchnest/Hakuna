package com.lunarapps.hakuna.mainOps;


import com.lunarapps.hakuna.models.User;
import com.lunarapps.hakuna.ServiceNames;
import com.lunarapps.hakuna.network.SocketConnectionSingleton;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;


public class MainController {

    @FXML
    private Pane signupPane;
    @FXML
    private TextField usernameInput;
    @FXML
    private PasswordField passwordInput;
    @FXML
    private Button loginBtn;
    @FXML
    private AnchorPane mainFrame;

    private User user;

    double x = 0;
    double y = 0;

    public MainController() {
        loginBtn = new Button();
        loginBtn.setOnMouseClicked(new LogInListener());
    }


    public void performSignup(MouseEvent mouseEvent) {
        Parent second = null;
        try {
            second = FXMLLoader.load(getClass().getResource("../layouts/signup.fxml"));

        } catch (IOException e) {
            e.printStackTrace();
        }
        signupPane.getChildren().removeAll();
        signupPane.getStylesheets().clear();
        signupPane.getChildren().setAll(second);
    }

    AnchorPane getMainFrame() {
        return mainFrame;
    }

    public void setUser(User u) {
        this.user = u;
    }

    public User getUser() {
        return user;
    }


    private class LogInListener implements EventHandler<MouseEvent> {//generic type that takes MouseEvent objects


        @Override
        public void handle(MouseEvent event) {
            if (usernameInput.getText().length() > 0 && passwordInput.getText().length() > 0) {

                String concat = usernameInput.getText() + "!@#" + passwordInput.getText();
                SocketConnectionSingleton mSingleton = SocketConnectionSingleton.getInstance();

                try {
                    mSingleton.printWriter.write(ServiceNames.CHECK.toString());
                    mSingleton.printWriter.flush();
                    mSingleton.objectOutputStream.writeObject(concat);
                    mSingleton.objectOutputStream.flush();

                    String booleanValue = new BufferedReader(mSingleton.inputStreamReader).readLine();
                    boolean valid = Boolean.parseBoolean(booleanValue);
                    if (valid) {

                        //code to accept the user object goes here
                        //that user object will be used to customize that
                        //user's page

                        user = (User) mSingleton.objectInputStream.readObject();

                        Parent interact = null;
                        try {
                            interact = FXMLLoader.load(getClass().getResource("../layouts/interact.fxml"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mainFrame.getChildren().removeAll();
                        mainFrame.getChildren().setAll(interact);
                    } else {
                        usernameInput.setText("incorrect information");
                    }


                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

            } else if (usernameInput.getText().length() > 0 && passwordInput.getText().length() == 0) {
                passwordInput.setText("please enter your password");
            } else if (usernameInput.getText().length() == 0 && passwordInput.getText().length() > 0) {
                usernameInput.setText("please enter your username");
            } else {
                passwordInput.setText("please enter your password");
                usernameInput.setText("please enter your username");
            }
        }
    }

    /*if the methods are defined in the fxml file, annotate them with @FXML annotation
     * if they are private. If they are public, there is no need*/

    @FXML
    private void dragged(MouseEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);
    }

    @FXML
    private void pressed(MouseEvent event) {
        x = event.getSceneX();
        y = event.getSceneY();
    }


}
