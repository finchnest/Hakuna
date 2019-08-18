package com.lunarapps.hakuna.mainOps;

import com.lunarapps.hakuna.models.Sex;
import com.lunarapps.hakuna.models.User;
import com.lunarapps.hakuna.ServiceNames;
import com.lunarapps.hakuna.network.SocketConnectionSingleton;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupController {

    @FXML
    private TextField messageField;
    @FXML
    private CheckBox female;
    @FXML
    private CheckBox male;
    @FXML
    private TextField firstname;
    @FXML
    private TextField lastname;
    @FXML
    private TextField email;
    @FXML
    private TextField chosenUsername;
    @FXML
    private TextField chosenPass;
    @FXML
    private TextField confirmedPass;

    public SignupController() {
    }

    @FXML
    private void perfCreateAcct(MouseEvent mouseEvent) {

        if (checkNameRegex(mouseEvent) &&
                checkLastRegex(mouseEvent) &&
                (male.isSelected() || female.isSelected()) &&
                checkEmailRegex(mouseEvent) &&
                (chosenUsername.getText().length() != 0 && chosenUsername.getText().length() < 40) &&
                checkConfirm(mouseEvent)) {

            //make overloaded user object constructor
            //one for users who have emails and another for those who haven't

            Sex sex=Sex.MALE;
            if(female.isSelected())
                sex=Sex.FEMALE;

            User user;
            if(email.getText()!=null){
                 user=new User(firstname.getText(), lastname.getText(), sex, email.getText(), chosenUsername.getText(), confirmedPass.getText());
            }else{
                user=new User(firstname.getText(), lastname.getText(), sex, chosenUsername.getText(), confirmedPass.getText());
            }

            SocketConnectionSingleton mSingleton = SocketConnectionSingleton.getInstance();

            try {
                mSingleton.printWriter.write(ServiceNames.ADD_USER.toString());
                mSingleton.printWriter.flush();
                mSingleton.objectOutputStream.writeObject(user);
                mSingleton.objectOutputStream.flush();


                String addValue = new BufferedReader(mSingleton.inputStreamReader).readLine();
                boolean added = Boolean.parseBoolean(addValue);

                if(added){
                    chosenUsername.setText("");
                    chosenPass.setText("");
                    confirmedPass.setText("");
                    email.setText("");
                    firstname.setText("");
                    lastname.setText("");
                    male.setSelected(false);
                    female.setSelected(false);
                    messageField.setText("successfully registered");
                }else{
                    messageField.setText("This username is taken");
                }

            }catch (IOException e){
                e.getMessage();
            }

        } else {
            messageField.setText("info correction needed!!");
        }

    }

    @FXML
    private boolean checkNameRegex(MouseEvent mouseEvent) {
        if (firstname.getText().length() == 0 || firstname.getText().length() > 30) {
            firstname.setText("Enter your first name properly");
            return false;
        }
        return true;
    }

    @FXML
    private boolean checkLastRegex(MouseEvent mouseEvent) {
        if (lastname.getText().length() == 0 || lastname.getText().length() > 30) {
            lastname.setText("Enter your last name properly");
            return false;
        }
        return true;
    }

    @FXML
    private void selectOne(MouseEvent mouseEvent) {
        if (male.isSelected()) {
            female.setSelected(false);
        } else if (female.isSelected()) {
            male.setSelected(false);
        }
    }

    @FXML
    private boolean checkEmailRegex(MouseEvent mouseEvent) {
        Pattern emailRegex = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailRegex.matcher(email.getText());
        if(email.getText().length()!=0){

            if (!matcher.matches()) {
                email.setText("Enter a proper email");
                return false;
            }
            return true;
        }
        return true;
    }

    @FXML
    private void checkPassRegex(MouseEvent mouseEvent) {
        String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
        if (!chosenPass.getText().matches(pattern)) {
            chosenPass.setText("Choose a stronger password");

        } else {
            if (confirmedPass.getText().length() != 0) {
                if (!checkConfirm(mouseEvent)) {
                    confirmedPass.setText("passwords don't match");
                }
            }
        }

    }

    @FXML
    private boolean checkConfirm(MouseEvent mouseEvent) {
        if (!chosenPass.getText().equals(confirmedPass.getText()) || confirmedPass.getText().length()>40) {
            confirmedPass.setText("passwords don't match");
            return false;
        }
        return true;
    }

}

