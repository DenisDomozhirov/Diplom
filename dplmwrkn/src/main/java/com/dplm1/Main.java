package com.dplm1;

import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;

public class Main extends Application {
    private TextArea pdfContentArea;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("PDF Viewer");

        // Меню
        MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("File");
        MenuItem openFileItem = new MenuItem("Open PDF");
        MenuItem exitItem = new MenuItem("Exit");
        menuFile.getItems().addAll(openFileItem, exitItem);
        menuBar.getMenus().add(menuFile);

        // Область для отображения содержимого PDF
        pdfContentArea = new TextArea();
        pdfContentArea.setEditable(false);
        pdfContentArea.setWrapText(true);

        // Основной макет
        BorderPane layout = new BorderPane();
        layout.setTop(menuBar);
        layout.setCenter(pdfContentArea);

        // Действия меню
        openFileItem.setOnAction(e -> openPDF(primaryStage));
        exitItem.setOnAction(e -> primaryStage.close());

        // Создание сцены и её установка
        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void openPDF(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        fileChooser.setTitle("Select a PDF File");

        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            try (PDDocument document = PDDocument.load(file)) {
                PDFTextStripper pdfStripper = new PDFTextStripper();
                String content = pdfStripper.getText(document);
                pdfContentArea.setText(content);
            } catch (IOException e) {
                showAlert("Error", "Failed to open PDF file: " + e.getMessage());
            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
