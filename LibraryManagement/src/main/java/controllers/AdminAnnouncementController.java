package controllers;

import entities.Announcement;
import utils.PopupUtils;
import database.DatabaseController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;

import java.time.LocalDate;

public class AdminAnnouncementController {

    @FXML
    private TextArea content_TextArea;

    @FXML
    private DatePicker startTime_DatePicker;

    @FXML
    private DatePicker endTime_DatePicker;

    @FXML
    private Button send_Button;

    @FXML
    private void handleSendButton() {
        LocalDate startDate = startTime_DatePicker.getValue();
        LocalDate endDate = endTime_DatePicker.getValue();
        String content = content_TextArea.getText();
        if (endDate == null || startDate == null) {
            PopupUtils.showAlert("Date should not be null!");
            return;
        }
        if (startDate.isAfter(endDate)) {
            PopupUtils.showAlert("Start date should not be after end date!");
            return;
        }
        if (content.isEmpty()) {
            PopupUtils.showAlert("Content should not be empty!");
            return;
        }
        if (PopupUtils.showConfirmationDialog()) {
            return;
        }
        Announcement announcement = new Announcement(content, startDate, endDate);
        DatabaseController.saveEntity(announcement);
        PopupUtils.showAlert("Announcement sent successfully");
        SideBarLoader.reloadAnnouncement();
        cleanUp();
    }

    public void cleanUp() {
        content_TextArea.clear();
        endTime_DatePicker.setValue(null);
        startTime_DatePicker.setValue(null);
    }
}
