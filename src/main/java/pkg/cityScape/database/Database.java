package pkg.cityScape.database;

import pkg.cityScape.manager.ConfigManager;
import pkg.cityScape.model.Town;

import java.sql.*;

public class Database {

    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/CityTest";
        String user = "root";
        String password = "Luigi24062005!";

        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
            throw e;
        }
    }

    public static void addTown(Town town) throws SQLException {
        String insertQuery = "INSERT INTO towns (idPL, comayor, gold_bank, is_spawn_open, mayor, regions_count, spawn_cost, town_name, server_account_nickname, citizen_count) VALUES (?,?,?,?,?,?,?,?,?,?)";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            preparedStatement.setInt(1, town.getId());
            preparedStatement.setString(2, town.getComayor() != null ? town.getComayor().getPlayer().getName() : "None");
            preparedStatement.setInt(3, town.getGoldBank());
            preparedStatement.setBoolean(4, town.isSpawnOpen());
            preparedStatement.setString(5, town.getMayor().getPlayer().getName());
            preparedStatement.setInt(6, town.getRegionsCount());
            preparedStatement.setInt(7, town.getSpawnCost());
            preparedStatement.setString(8, town.getTownName());
            preparedStatement.setString(9, ConfigManager.getNickDatabase()); // Associa la città a un ServerAccount
            preparedStatement.setInt(10, town.getCitizenCount());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("City inserted successfully!");
            } else {
                System.out.println("Insertion failed.");
            }
        }
    }

    public static boolean getTownById(int townId) throws SQLException {
        String query = "SELECT * FROM towns WHERE idPL = ? AND server_account_nickname = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, townId);
            preparedStatement.setString(2, ConfigManager.getNickDatabase());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("City PRESENTE successfully!");
                    return true;
                }
            }
        }
        return false;
    }

    public static void updateTown(Town town) throws SQLException {
        String updateQuery = "UPDATE towns SET comayor = ?, gold_bank = ?, is_spawn_open = ?, mayor = ?, regions_count = ?, spawn_cost = ?, town_name = ?, citizen_count = ? WHERE idPL = ? AND server_account_nickname = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            preparedStatement.setString(1, town.getComayor() != null ? town.getComayor().getPlayer().getName() : "None");
            preparedStatement.setInt(2, town.getGoldBank());
            preparedStatement.setBoolean(3, town.isSpawnOpen());
            preparedStatement.setString(4, town.getMayor().getPlayer().getName());
            preparedStatement.setInt(5, town.getRegionsCount());
            preparedStatement.setInt(6, town.getSpawnCost());
            preparedStatement.setString(7, town.getTownName());
            preparedStatement.setInt(8, town.getCitizenCount());
            preparedStatement.setInt(9, town.getId());          // Identificatore città
            preparedStatement.setString(10, ConfigManager.getNickDatabase());       // Identificatore account server

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Aggiornamento riuscito per la città: " + town.getTownName());
            } else {
                System.out.println("Nessuna riga aggiornata per la città: " + town.getTownName());
            }
        } catch (SQLException e) {
            System.err.println("Errore durante l'aggiornamento della città " + town.getTownName() + ": " + e.getMessage());
            throw e;
        }
    }
}