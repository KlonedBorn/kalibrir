package com.kloneborn.controller;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.kloneborn.KalibrirApp;

public class WordCounterController implements Initializable {

    @FXML
    private Label lbAppVersion;

    @FXML
    private Label lbParagraphCount;

    @FXML
    private Label lbWordCount;

    @FXML
    private TextArea tbTextBox;

    private IntegerProperty wordCount;
    private IntegerProperty paragraphCount;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        wordCount = new SimpleIntegerProperty();
        paragraphCount = new SimpleIntegerProperty();
        lbAppVersion.setText("Version: " + KalibrirApp.VERSION);
        lbWordCount.textProperty().bind(wordCount.asString("Word Count: %d"));
        lbParagraphCount.textProperty().bind(paragraphCount.asString("Paragraph Count: %d"));

        tbTextBox.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    newValue = newValue.strip();
                    if (newValue.isEmpty()) {
                        this.wordCount.set(0);
                        this.paragraphCount.set(0);
                        return;
                    }
                    // Calculate letter count
                    int letterCount = newValue.replaceAll("\\s", "").length();

                    // Calculate word count
                    String[] words = newValue.split("\\s+");
                    int wordCount = words.length;

                    // Calculate paragraph count
                    int paragraphCount = newValue.split("\n\n+").length;

                    this.wordCount.set(wordCount);
                    this.paragraphCount.set(paragraphCount);
                });

        // Enable drag-and-drop functionality for the TextArea
        tbTextBox.setOnDragOver(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY);
            } else {
                event.consume();
            }
        });

        tbTextBox.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                success = handleDroppedFiles(db.getFiles());
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    private boolean handleDroppedFiles(List<File> files) {
        for (File file : files) {
            try {
                // Check if the file is too big (e.g., limit to 10 MB)
                if (file.length() > 10 * 1024 * 1024) {
                    showAlert("File is too big", "The dropped file is too large (maximum size: 10 MB).");
                    return false;
                }

                // Attempt to read the contents of the file as text
                String fileContents = readTextFile(file);

                if (fileContents == null) {
                    showAlert("Not a text file", "The dropped file does not contain text data.");
                    return false;
                }

                // Set the file contents to the TextArea
                tbTextBox.setText(fileContents);

                // Calculate word and paragraph counts
                String newValue = fileContents.strip();
                int wordCount = newValue.split("\\s+").length;
                int paragraphCount = newValue.split("\n\n+").length;

                this.wordCount.set(wordCount);
                this.paragraphCount.set(paragraphCount);
            } catch (IOException e) {
                e.printStackTrace();
                return false; // Handle the error appropriately
            }
        }
        return true;
    }

    private String readTextFile(File file) throws IOException {
        // Attempt to read the file as text
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder fileContents = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                fileContents.append(line).append("\n");
            }
            return fileContents.toString();
        } catch (IOException e) {
            // An IOException occurred, indicating that the file is not a text file
            return null;
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}