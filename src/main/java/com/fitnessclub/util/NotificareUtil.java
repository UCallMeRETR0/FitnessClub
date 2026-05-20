package com.fitnessclub.util;

import com.fitnessclub.model.Abonament;
import com.fitnessclub.service.AbonamentService;
import javafx.scene.control.*;
import javafx.stage.StageStyle;

import java.util.List;

public class NotificareUtil {

    public static void verificaExpirari() {
        AbonamentService abonamentService = new AbonamentService();
        List<Abonament> expira = abonamentService.abonamenteCareExpiraCurand(7);

        if (expira.isEmpty()) return;

        // Construim mesajul
        StringBuilder sb = new StringBuilder();
        for (Abonament a : expira) {
            sb.append(String.format("• %s %s — %s — expiră pe %s (%d zile)\n",
                    a.getClient().getPrenume(),
                    a.getClient().getNume(),
                    a.getTip().name(),
                    a.getDataExpirare().toString(),
                    a.zileRamase()));
        }

        // Dialog custom
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("⚠️ Notificare Abonamente");
        alert.setHeaderText("Există " + expira.size()
                + " abonament(e) care expiră în următoarele 7 zile!");
        alert.setContentText(sb.toString());
        alert.initStyle(StageStyle.DECORATED);

        // Stilizare dialog
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle(
                "-fx-background-color: #242424;" +
                        "-fx-border-color: #ef9f27;" +
                        "-fx-border-width: 2px;"
        );
        dialogPane.lookup(".content.label").setStyle(
                "-fx-text-fill: #e0e0e0;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-family: 'Segoe UI';"
        );
        dialogPane.lookup(".header-panel").setStyle(
                "-fx-background-color: #1a1a1a;"
        );
        dialogPane.lookup(".header-panel .label").setStyle(
                "-fx-text-fill: #ef9f27;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-family: 'Segoe UI';"
        );

        // Buton OK stilizat
        Button btnOk = (Button) dialogPane.lookupButton(ButtonType.OK);
        btnOk.setStyle(
                "-fx-background-color: #ef9f27;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 6;" +
                        "-fx-padding: 6 20;"
        );

        alert.showAndWait();
    }
}