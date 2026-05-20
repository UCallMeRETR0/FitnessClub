package com.fitnessclub.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import java.io.IOException;

public class MainController {

    @FXML private StackPane contentPane;
    @FXML private Button btnDashboard;
    @FXML private Button btnClienti;
    @FXML private Button btnAntrenori;
    @FXML private Button btnAbonamente;
    @FXML private Button btnAntrenamente;
    @FXML private Button btnRapoarte;
    @FXML private Button btnGrafice;

    private Button activeBtn;

    @FXML private Button btnCautare;

    @FXML
    public void showCautare() {
        incarcaView("cautare.fxml");
        setActiveBtn(btnCautare);
    }

    @FXML
    public void initialize() {
        showDashboard();
    }

    @FXML
    public void showDashboard() {
        incarcaView("dashboard.fxml");
        setActiveBtn(btnDashboard);
    }

    @FXML
    public void showClienti() {
        incarcaView("client.fxml");
        setActiveBtn(btnClienti);
    }

    @FXML
    public void showAntrenori() {
        incarcaView("antrenor.fxml");
        setActiveBtn(btnAntrenori);
    }

    @FXML
    public void showAbonamente() {
        incarcaView("abonament.fxml");
        setActiveBtn(btnAbonamente);
    }

    @FXML
    public void showAntrenamente() {
        incarcaView("antrenament.fxml");
        setActiveBtn(btnAntrenamente);
    }

    @FXML
    public void showRapoarte() {
        incarcaView("raport.fxml");
        setActiveBtn(btnRapoarte);
    }

    @FXML
    public void showGrafice() {
        incarcaView("grafice.fxml");
        setActiveBtn(btnGrafice);
    }

    private void incarcaView(String fxml) {
        try {
            String cale = "/com/fitnessclub/" + fxml;
            java.net.URL url = getClass().getResource(cale);
            System.out.println("Incarc: " + cale + " -> URL: " + url);
            if (url == null) {
                System.err.println("FISIER NEGASIT: " + cale);
                return;
            }
            FXMLLoader loader = new FXMLLoader(url);
            Node view = loader.load();
            contentPane.getChildren().setAll(view);
        } catch (IOException e) {
            System.err.println("Eroare IO la: " + fxml + " -> " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Eroare la: " + fxml + " -> " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setActiveBtn(Button btn) {
        if (activeBtn != null) {
            activeBtn.getStyleClass().remove("sidebar-btn-active");
        }
        activeBtn = btn;
        activeBtn.getStyleClass().add("sidebar-btn-active");
    }
}