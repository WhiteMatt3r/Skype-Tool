package main;

import com.skype.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.awt.*;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private ComboBox<Friend> populateFriends;

    @FXML
    private ComboBox<Profile.Status> statusComboBox;

    @FXML
    private TextField amount, message, cAmount, duration, display, mood;

    @FXML
    private TextArea messages;

    private Profile profile = Skype.getProfile();

    @FXML
    public void clearHistory() {

        try {

            Skype.clearChatHistory();

        } catch (SkypeException e) {
            //
        }

        try {
            Skype.clearCallHistory();
        } catch (SkypeException e) {
            //
        }

    }

    @FXML
    public void sendSpam() {
        // Implement weird quotes
        try {

            if (populateFriends.getItems().size() > 0) {

                int numAmount = 1;

                try {

                    numAmount = Integer.parseInt(amount.getText());

                } catch (Exception e) {

                    amount.setText("1");

                }

                for (int i = 0; i < numAmount; i++) {

                    Skype.chat(populateFriends.getSelectionModel().getSelectedItem().id).send(message.getText());

                }

            }

        } catch (SkypeException e) {

            System.out.println(e.getMessage());

        }

    }

    @FXML
    public void sendCalls() {

        if (populateFriends.getItems().size() > 0) {

            int numAmount = 1;

            try {

                numAmount = Integer.parseInt(cAmount.getText());

            } catch (Exception e) {

                cAmount.setText("1");

            }

            int dAm = 1;

            try {

                dAm = Integer.parseInt(duration.getText());

            } catch (Exception e) {

                duration.setText("1000");

            }

            final String id = populateFriends.getSelectionModel().getSelectedItem().id;

            if(dAm < 750) {
                duration.setText("750");
                dAm = 750;
            }

            for (int i = 0; i < numAmount; i++) {

                try {

                    Desktop.getDesktop().browse(new URI("skype:" + id));
                    Thread.sleep(dAm);
                    if (Skype.getAllActiveCalls().length > 0) {

                        Call call = Skype.getAllActiveCalls()[0];
                        while (true) {
                            try {

                                call.finish();
                                break;

                            } catch (Exception e) {
                                //
                            }

                            if (call.getStatus() == Call.Status.FINISHED) {

                                break;

                            }

                        }

                    }

                } catch (Exception e) {
                    //
                }

            }

        }

    }

    @FXML
    public void changeDisp() {

        try {

            profile.setFullName(display.getText());

        } catch (SkypeException e) {
            //
        }

    }

    @FXML
    public void changeMood() {

        try {

            profile.setMoodMessage(mood.getText());

        } catch (SkypeException e) {
            //
        }

    }

    @FXML
    public void changeStatus() {

        try {

            profile.setStatus(statusComboBox.getSelectionModel().getSelectedItem());

        } catch (SkypeException e){
            //
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        amount.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {

                    if (newValue.length() == 0) {

                        amount.setText("");

                        return;
                    }

                    if (Integer.parseInt(newValue) < 0 || Integer.parseInt(newValue) >= 5000) throw new Exception();

                } catch (Exception e) {

                    amount.setText(oldValue);

                }
            }
        });

        cAmount.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {

                    if (newValue.length() == 0) {

                        cAmount.setText("");

                        return;
                    }

                    if (Integer.parseInt(newValue) < 0 || Integer.parseInt(newValue) >= 2000) throw new Exception();

                } catch (Exception e) {

                    cAmount.setText(oldValue);

                }
            }
        });

        duration.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {

                    if (newValue.length() == 0) {

                        duration.setText("");

                        return;
                    }

                    if (Integer.parseInt(newValue) < 0 || Integer.parseInt(newValue) >= 2000) throw new Exception();

                } catch (Exception e) {

                    duration.setText(oldValue);

                }
            }
        });

        statusComboBox.getItems().addAll(Profile.Status.ONLINE,Profile.Status.OFFLINE,Profile.Status.INVISIBLE,Profile.Status.DND,Profile.Status.AWAY);

        try {

            statusComboBox.getSelectionModel().select(profile.getStatus());
            mood.setText(profile.getMoodMessage());
            display.setText(profile.getFullName());
            ContactList contacts = Skype.getContactList();

            Chat[] chats = Skype.getAllChats();

            for (Chat c : chats) {

                try {

                    for (ChatMessage message : c.getAllChatMessages()){

                        if(message.getSenderDisplayName().length() <= 0) {
                            messages.appendText(message.getSender() + " reads " + message.getContent() + "\n");
                        } else {
                            messages.appendText(message.getSenderDisplayName() + " reads " + message.getContent() + "\n");
                        }

                    }

                } catch (SkypeException e) {
                    e.printStackTrace();
                }

            }

            for (com.skype.Friend c : contacts.getAllFriends()) {

                String fullName = c.getFullName();

                if (fullName == null || fullName.length() <= 0) {

                    populateFriends.getItems().add(new Friend(c.getId(), c.getId()));

                } else {

                    populateFriends.getItems().add(new Friend(c.getId(), fullName));

                }

            }

            if (populateFriends.getItems().size() > 0) {

                populateFriends.getSelectionModel().select(0);

            }

        } catch (SkypeException e) {
            e.printStackTrace();
        }

    }

    private class Friend {

        public String id, display;

        public Friend(String id, String display) {

            this.id = id;
            this.display = display;

        }

        @Override
        public String toString() {

            return display;

        }

    }

}
