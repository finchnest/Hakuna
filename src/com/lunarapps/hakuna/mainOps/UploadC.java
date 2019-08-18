package com.lunarapps.hakuna.mainOps;

import com.lunarapps.hakuna.ServiceNames;
import com.lunarapps.hakuna.models.Accessibility;
import com.lunarapps.hakuna.models.Type;
import com.lunarapps.hakuna.network.SocketConnectionSingleton;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;

public class UploadC {

    public ImageView preview;
    @FXML
    private TextField filePath;
    @FXML
    private CheckBox publicM;
    @FXML
    private CheckBox privateM;
    @FXML
    private TextField fileName;

    private SocketConnectionSingleton singleton = SocketConnectionSingleton.getInstance();

    private com.lunarapps.hakuna.models.Image mImage;


    public void chooseFile(MouseEvent mouseEvent) throws MalformedURLException {

        filePath.setText("");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(
                "Image Files", "*.jpeg", "*.png", "*.jpg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(fileName.getScene().getWindow());

        if (selectedFile != null) {

            String imageFile = selectedFile.toURI().toURL().toString();
            filePath.setText(imageFile);
            preview.setImage(new Image(imageFile));
        } else {
            filePath.setText("Image selection cancelled!");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Please Select a File");
            alert.setContentText("You didn't select a file!");
            alert.showAndWait();
        }


    }

    public void selectOne(MouseEvent mouseEvent) {
        if (publicM.isSelected()) {
            privateM.setSelected(false);
        } else if (privateM.isSelected()) {
            publicM.setSelected(false);
        }
    }

    public void perfUpload(MouseEvent mouseEvent) throws IOException {

        /*then show the status of the upload, whether it was
         * a successful operation or not*/

        Accessibility access = Accessibility.PRIVATE;
        if (publicM.isSelected())
            access = Accessibility.PUBLIC;

        if (fileName.getText().length() != 0 && filePath.getText().length() != 0) {

            FileInputStream fis = new FileInputStream(filePath.getText());
            byte[] imageBytes = IOUtils.toByteArray(fis);

            if (imageBytes.length < 1024 * 1024) {
                mImage = new com.lunarapps.hakuna.models.Image(fileName.getText(),
                        new MainController().getUser(), access, Type.IMAGE, filePath.getText(), 0, imageBytes);

                singleton.printWriter.write(ServiceNames.UPLOAD.toString());
                singleton.printWriter.flush();

                /*the upload operation has to be done in a separate thread so that
                 * it can show progress bar n doesn't disturb/slow down the GUI n other ops*/

                Thread upl = new Thread(new UploadHelper());
                upl.start();

                String booleanValue = new BufferedReader(singleton.inputStreamReader).readLine();
                boolean valid = Boolean.parseBoolean(booleanValue);

                if (valid) {
                    fileName.setText("successfully uploaded!");
                } else {
                    fileName.setText("error occured!");
                }


            } else {
                filePath.setText("choose an image less than 1 MB");
            }

        } else {
            fileName.setText("Choose identifier name");
        }

    }

    class UploadHelper implements Runnable {
        @Override
        public void run() {
            try {
                singleton.objectOutputStream.writeObject(mImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}