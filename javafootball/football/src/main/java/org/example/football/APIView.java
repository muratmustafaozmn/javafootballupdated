package org.example.football;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class APIView {

    @FXML
    private TableView<Match> table;

    @FXML
    private TableColumn<Match, String> homeTeamColumn;

    @FXML
    private TableColumn<Match, String> awayTeamColumn;

    @FXML
    private TableColumn<Match, String> dateColumn;

    @FXML
    public void initialize() {
        setupTableColumns();
    }

    private void setupTableColumns() {
        homeTeamColumn.setCellValueFactory(new PropertyValueFactory<>("homeTeam"));
        awayTeamColumn.setCellValueFactory(new PropertyValueFactory<>("awayTeam"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
    }

    @FXML
    public void fetchAndDisplayMatches() {
        try {
            String jsonResponse = fetchMatches();
            List<Match> matches = parseMatches(jsonResponse);
            table.getItems().setAll(matches);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String fetchMatches() throws Exception {
        String url = "https://api.football-data.org/v4/matches";
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestProperty("X-Auth-Token", "7c58047dcdc245bb8c9d6165090cc4f1"); // Replace with your API key
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        return response.toString();
    }

    private List<Match> parseMatches(String json) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Match>>() {}.getType();
        return gson.fromJson(json, listType);
    }
}