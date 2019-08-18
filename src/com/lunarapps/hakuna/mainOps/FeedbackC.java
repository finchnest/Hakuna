package com.lunarapps.hakuna.mainOps;

import com.lunarapps.hakuna.ServiceNames;
import com.lunarapps.hakuna.models.Feedback;
import com.lunarapps.hakuna.network.SocketConnectionSingleton;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Date;

public class FeedbackC {

    public Button feedSubmit;
    public TextArea feedArea;
    private SocketConnectionSingleton mSingleton;

    public FeedbackC() {
        feedSubmit = new Button();
        feedSubmit.setOnMouseClicked(new FeedBackSubmitter());
        mSingleton = SocketConnectionSingleton.getInstance();

    }

    private class FeedBackSubmitter implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {

            if (feedArea.getText().length() > 0) {

                Feedback feed = new Feedback(new MainController().getUser(), feedArea.getText(),
                        Date.valueOf(Long.toString(System.currentTimeMillis())));

                try {
                    mSingleton.printWriter.write(ServiceNames.FEEDBACK.toString());
                    mSingleton.printWriter.flush();
                    mSingleton.objectOutputStream.writeObject(feed);
                    mSingleton.objectOutputStream.flush();

                    boolean feedVal=Boolean.parseBoolean(new BufferedReader(mSingleton.inputStreamReader).readLine());

                    if(feedVal){
                        feedArea.setText("successfully submitted");
                    }else{
                        feedArea.setText("Error Occured");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                feedArea.setText("feedback section is empty");
            }
        }
    }
}
