package org.example;

import com.slack.api.bolt.App;
import com.slack.api.bolt.AppConfig;
import com.slack.api.bolt.jetty.SlackAppServer;
//import com.slack.api.model.command.Command;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SlackBot {
    public static void main(String[] args) throws Exception {
        String slackBotToken = "xoxb-6296878120468-6296897139716-7UgABnjTN2uJskQFztrK7MKX";
        String slackSigningSecret = "6177b8d561a6ecbaccba858512874bed";

        App app = new App(AppConfig.builder()
                .singleTeamBotToken(slackBotToken)
                .signingSecret(slackSigningSecret)
                .build());

        app.command("/query", (req, ctx) -> {
            System.out.println(req.getPayload().getText());
            String query = req.getPayload().getText();

            // Process the query and interact with PostgreSQL
            String result = processQuery(query);

            return ctx.ack("Received query: " + query + "\nResult: " + result);

            // return ctx.ack("Processing the query...");
        });

        SlackAppServer server = new SlackAppServer(app);
        server.start();
    }

//    private static String processQuery(String query) {
//        // Replace these values with your PostgreSQL connection details
//        String jdbcUrl = "jdbc:postgresql://localhost:5432/postgres";
//        String username = "postgres";
//        String password = "postgres";
//
//        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
//            // Use prepared statement to avoid SQL injection
//            String sql = "SELECT * FROM employees";
//            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//                preparedStatement.setString(1, query);
//                try (ResultSet resultSet = preparedStatement.executeQuery()) {
//                    if (resultSet.next()) {
//                        // Assuming your result is in the first column
//                        return resultSet.getString(1);
//                    } else {
//                        return "No result found for query: " + query;
//                    }
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            e.printStackTrace(); // Replace this with your logging mechanism
//            System.err.println("Error executing query: " + e.getMessage());
//            return "Error processing the query.";
//        }
//    }

//    private static String processQuery(String query) {
//        String jdbcUrl = "jdbc:postgresql://localhost:5432/postgres";
//        String username = "postgres";
//        String password = "postgres";
//
//        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
//            System.out.println("Connected to PostgreSQL successfully.");
//
//            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
//                ResultSet resultSet = preparedStatement.executeQuery()) {
//
//                    if (resultSet.next()) {
//                        // Assuming your result is in the first column
//                        return resultSet.getString(1);
//                    } else {
//                        return "No result found for query: " + query;
//                    }
//                }
//        } catch (SQLException e) {
//            e.printStackTrace(); // Replace this with your logging mechanism
//            return "Error processing the query: " + e.getMessage();
//        }
//    }

//    private static String processQuery(String query) {
//        String jdbcUrl = "jdbc:postgresql://localhost:5432/postgres";
//        String username = "postgres";
//        String password = "postgres";
//
//        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
//            // Print a success message to the console
//            System.out.println("Connected to PostgreSQL successfully.");
//
//            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
//                 ResultSet resultSet = preparedStatement.executeQuery()) {
//                System.out.println("This is the "+resultSet);
//                if (resultSet.next()) {
//                    // Assuming your result is in the first column
//                    return resultSet.getString(1);
//                } else {
//                    return "No result found for query: " + query;
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace(); // Replace this with your logging mechanism
//            return "Error processing the query: " + e.getMessage();
//        }
//    }



    private static String processQuery(String query) {
        String jdbcUrl = "jdbc:postgresql://localhost:5432/postgres";
        String username = "postgres";
        String password = "postgres";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            // Print a success message to the console
            System.out.println("Connected to PostgreSQL successfully.");

            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                StringBuilder resultBuilder = new StringBuilder();

                // Append header row
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    resultBuilder.append(resultSet.getMetaData().getColumnName(i)).append(" | ");
                }
                resultBuilder.append("\n");

                // Append data rows
                while (resultSet.next()) {
                    for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                        resultBuilder.append(resultSet.getString(i)).append(" | ");
                    }
                    resultBuilder.append("\n");
                }

                return resultBuilder.toString();
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Replace this with your logging mechanism
            return "Error processing the query: " + e.getMessage();
        }
    }


}
