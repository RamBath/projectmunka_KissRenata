package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import logics.Chests;
import logics.Item;
import logics.Monster;

public class GameRepository {
    private static final String URL = "jdbc:sqlserver://localhost;databaseName=adventure_game;encrypt=false";
    private static final String USER = "java";
    private static final String PASSWORD = "java";

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void createTables() {
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {

            String createItemTable = "IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='Item' AND xtype='U') "
                    + "CREATE TABLE Item ("
                    + "id INT IDENTITY(1,1) PRIMARY KEY,"
                    + "name VARCHAR(255),"
                    + "description TEXT,"
                    + "price INT,"
                    + "effect1 VARCHAR(255),"
                    + "effect2 VARCHAR(255),"
                    + "boost1 INT,"
                    + "boost2 INT"
                    + ")";
            stmt.execute(createItemTable);

            String createCharacterTable = "IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='Character' AND xtype='U') "
                    + "CREATE TABLE Character ("
                    + "id INT IDENTITY(1,1) PRIMARY KEY,"
                    + "health INT,"
                    + "attack INT,"
                    + "defense INT,"
                    + "agility INT,"
                    + "level INT,"
                    + "gold INT"
                    + ")";
            stmt.execute(createCharacterTable);

            String createCharacterInventoryTable = "IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='CharacterInventory' AND xtype='U') "
                    + "CREATE TABLE CharacterInventory ("
                    + "character_id INT,"
                    + "item_id INT,"
                    + "FOREIGN KEY (character_id) REFERENCES Character(id),"
                    + "FOREIGN KEY (item_id) REFERENCES Item(id)"
                    + ")";
            stmt.execute(createCharacterInventoryTable);

            String createMapTable = "IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='Map' AND xtype='U') "
                    + "CREATE TABLE Map ("
                    + "id INT IDENTITY(1,1) PRIMARY KEY,"
                    + "rowIndex INT,"
                    + "mapDataRow VARCHAR(MAX)"
                    + ")";
            stmt.execute(createMapTable);

            String createMonsterTable = "IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='Monster' AND xtype='U') "
                    + "CREATE TABLE Monster ("
                    + "id INT IDENTITY(1,1) PRIMARY KEY,"
                    + "health INT,"
                    + "attack INT,"
                    + "agility INT,"
                    + "goldDrop INT,"
                    + "x INT,"
                    + "y INT,"
                    + "monsterTypeCode VARCHAR(255),"
                    + "isAlive BIT"
                    + ")";
            stmt.execute(createMonsterTable);

            String createChestsTable = "IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='Chests' AND xtype='U') "
                    + "CREATE TABLE Chests ("
                    + "id INT IDENTITY(1,1) PRIMARY KEY,"
                    + "goldDrop INT,"
                    + "x INT,"
                    + "y INT,"
                    + "isActive BIT"
                    + ")";
            stmt.execute(createChestsTable);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertMonster(Monster monster) {
        String query = "INSERT INTO Monster (monsterTypeCode, health, attack, agility, goldDrop, x, y, isAlive) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = connect();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, monster.getMonsterTypeCode());
            statement.setInt(2, monster.getHealth());
            statement.setInt(3, monster.getAttack());
            statement.setInt(4, monster.getAgility());
            statement.setInt(5, monster.getGoldDrop());
            statement.setInt(6, monster.getX());
            statement.setInt(7, monster.getY());
            statement.setBoolean(8, monster.getIsAlive());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertChest(Chests chest) {
        String query = "INSERT INTO Chests (goldDrop, x, y, isActive) VALUES (?, ?, ?, ?)";
        try (Connection connection = connect();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, chest.getGoldDrop());
            statement.setInt(2, chest.getX());
            statement.setInt(3, chest.getY());
            statement.setBoolean(4, chest.isActive());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean monsterExists(String monsterTypeCode, int x, int y) {
        String query = "SELECT COUNT(*) FROM Monster WHERE monsterTypeCode = ? AND x = ? AND y = ?";
        try (Connection connection = connect();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, monsterTypeCode);
            statement.setInt(2, x);
            statement.setInt(3, y);

            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void populateMonsterTable(List<Monster> monsters) {
        for (Monster monster : monsters) {

            if (!monsterExists(monster.getMonsterTypeCode(), monster.getX(), monster.getY())) {

                insertMonster(monster);
                System.out.printf("Inserted Monster: %s at position (%d, %d)%n", monster.getMonsterTypeCode(),
                        monster.getX(), monster.getY());
            } else {
                System.out.printf("Monster already exists: %s at position (%d, %d)%n", monster.getMonsterTypeCode(),
                        monster.getX(), monster.getY());
            }
        }
    }

    public boolean chestExists(int x, int y) {
        String query = "SELECT COUNT(*) FROM Chests WHERE x = ? AND y = ?";
        try (Connection connection = connect();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, x);
            statement.setInt(2, y);

            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void overwriteMapTable(String[][] mapData) {
        if (mapData == null || mapData.length == 0 || mapData[0].length == 0) {
            System.out.println("mapData is null or empty, filling with '00000000'");
            mapData = new String[100][100];
            for (int i = 0; i < 100; i++) {
                for (int j = 0; j < 100; j++) {
                    mapData[i][j] = "00000000";
                }
            }
        }

        String deleteSQL = "DELETE FROM Map";
        String insertSQL = "INSERT INTO Map (rowIndex, mapDataRow) VALUES (?, ?)";

        try (Connection connection = connect();
                Statement deleteStatement = connection.createStatement();
                PreparedStatement insertStatement = connection.prepareStatement(insertSQL)) {

            deleteStatement.executeUpdate(deleteSQL);

            for (int i = 0; i < mapData.length; i++) {
                StringBuilder rowBuilder = new StringBuilder();
                for (String cell : mapData[i]) {
                    rowBuilder.append(cell).append(",");
                }
                String rowText = rowBuilder.toString();

                if (rowText.endsWith(",")) {
                    rowText = rowText.substring(0, rowText.length() - 1);
                }

                insertStatement.setInt(1, i);
                insertStatement.setString(2, rowText);
                insertStatement.executeUpdate();
            }

            System.out.println("Map data successfully overwritten.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void populateChest(List<Chests> chests) {
        for (Chests chest : chests) {

            if (!chestExists(chest.getX(), chest.getY())) {
                // debug
                insertChest(chest);
                System.out.printf("Inserted Chest: at position (%d, %d)%n", chest.getX(), chest.getY());
            } else {
                System.out.printf("Chest already exists:at position (%d, %d)%n", chest.getX(), chest.getY());
            }
        }
    }

    public Character getCharacter(int characterId, Character character) {

        characterId = 1;
        try (Connection conn = connect()) {
            String sql = "SELECT * FROM Character WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, characterId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {

                /*
                 * character.setHealth(rs.getInt("health"));
                 * character.setAttack(rs.getInt("attack"));
                 * character.setDefense(rs.getInt("defense"));
                 * character.setAgility(rs.getInt("agility"));
                 * character.setLevel(rs.getInt("level"));
                 * character.setGold(rs.getInt("gold"));
                 * character.setInventory(getCharacterInventory(characterId));
                 */
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return character;
    }

    private List<Item> getCharacterInventory(int characterId) {
        List<Item> inventory = new ArrayList<>();
        try (Connection conn = connect()) {
            String sql = "SELECT i.* FROM Item i JOIN CharacterInventory ci ON i.id = ci.item_id WHERE ci.character_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, characterId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Item item = new Item();
                item.setName(rs.getString("name"));
                item.setDescription(rs.getString("description"));
                item.setPrice(rs.getInt("price"));
                item.setEffect1(rs.getString("effect1"));
                item.setEffect2(rs.getString("effect2"));
                item.setBoost1(rs.getInt("boost1"));
                item.setBoost2(rs.getInt("boost2"));
                inventory.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return inventory;
    }

    public String getMap(int rowIndex) {
        String mapData = null;
        try (Connection conn = connect()) {
            String sql = "SELECT * FROM Map WHERE rowIndex = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, rowIndex);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                mapData = rs.getString("mapDataRow");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mapData;
    }

    public List<Monster> getMonsters() {
        List<Monster> monsters = new ArrayList<>();
        try (Connection conn = connect()) {
            String sql = "SELECT * FROM Monster";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Monster monster = new Monster();
                monster.setHealth(rs.getInt("health"));
                monster.setAttack(rs.getInt("attack"));
                monster.setAgility(rs.getInt("agility"));
                monster.setGoldDrop(rs.getInt("goldDrop"));
                monster.setX(rs.getInt("x"));
                monster.setY(rs.getInt("y"));
                monster.setMonsterTypeCode(rs.getString("monsterTypeCode"));
                monster.setAlive(rs.getInt("isAlive")==1);
                monsters.add(monster);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return monsters;
    }

    public List<Chests> getChests() {
        List<Chests> chests = new ArrayList<>();
        try (Connection conn = connect()) {
            String sql = "SELECT * FROM Chests";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Chests chest = new Chests();
                chest.setGoldDrop(rs.getInt("goldDrop"));
                chest.setX(rs.getInt("x"));
                chest.setY(rs.getInt("y"));
                chest.setActive(rs.getInt("isActive")==1);
                chests.add(chest);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return chests;
    }
}
