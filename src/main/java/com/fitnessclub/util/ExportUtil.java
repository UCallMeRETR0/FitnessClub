package com.fitnessclub.util;

import com.fitnessclub.exceptions.DatabaseException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExportUtil {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm");

    // Genereaza nume fisier automat cu timestamp
    public static String numeeFisier(String prefix, String extensie) {
        return prefix + "_" + LocalDateTime.now().format(FORMATTER) + "." + extensie;
    }

    public static void exportTxt(String cale, String titlu, List<String> linii) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(cale))) {
            String separator = "=".repeat(60);
            writer.write(separator); writer.newLine();
            writer.write("  " + titlu); writer.newLine();
            writer.write("  Generat: " + LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
            writer.newLine();
            writer.write(separator); writer.newLine();
            writer.newLine();
            for (String linie : linii) {
                writer.write(linie);
                writer.newLine();
            }
            writer.newLine();
            writer.write(separator); writer.newLine();
            writer.write("  Total inregistrari: " + linii.size()); writer.newLine();
            writer.write(separator); writer.newLine();
            System.out.println("Export TXT reusit: " + cale);
        } catch (IOException e) {
            throw new DatabaseException("Eroare la exportul TXT: " + e.getMessage(), e);
        }
    }

    public static void exportCsv(String cale, String header, List<String> linii) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(cale))) {
            writer.write(header);
            writer.newLine();
            for (String linie : linii) {
                writer.write(linie);
                writer.newLine();
            }
            System.out.println("Export CSV reusit: " + cale);
        } catch (IOException e) {
            throw new DatabaseException("Eroare la exportul CSV: " + e.getMessage(), e);
        }
    }
}