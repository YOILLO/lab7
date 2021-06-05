package database_managers;

import data.Furnish;
import data.House;
import data.Transport;
import main.Main;

import java.sql.*;

public class DatabaseManager {
    // Table names
    public static final String FLAT_TABLE = "flat";
    public static final String USER_TABLE = "users";
    public static final String COORDINATES_TABLE = "coordinates";
    public static final String HOUSE_TABLE = "house";
    // STUDY_GROUP column names
     /*
    CREATE TABLE flat(
            id SERIAL PRIMARY KEY,
            name           CHAR(50)    NOT NULL,
            coordinates_id INT,
            creation_date        TIMESTAMP,
            area      INT,
            number_of_rooms      INT,
            price      FLOAT,
            furnish        CHAR(50),
            transport            CHAR(50),
            group_admin_id        INT,
            user_id             INT
    );
      */
    public static final String FLAT_TABLE_ID_COLUMN = "id";
    public static final String FLAT_TABLE_NAME_COLUMN = "name";
    public static final String FLAT_TABLE_COORDINATES_ID = "coordinates_id";
    public static final String FLAT_TABLE_CREATION_DATE_COLUMN = "creation_date";
    public static final String FLAT_TABLE_AREA_COLUMN = "area";
    public static final String FLAT_TABLE_NUMBERS_OF_ROOMS_COLUMN = "number_of_rooms";
    public static final String FLAT_TABLE_PRICE_COLUMN = "price";
    public static final String FLAT_TABLE_FURNISH_COLUMN = "furnish";
    public static final String FLAT_TABLE_TRANSPORT_COLUMN = "transport";
    public static final String FLAT_TABLE_HOUSE_ID_COLUMN = "house_id";
    public static final String FLAT_TABLE_USER_ID_COLUMN = "user_id";
    // USER_TABLE column names
    public static final String USER_TABLE_ID_COLUMN = "id";
    public static final String USER_TABLE_USERNAME_COLUMN = "username";
    public static final String USER_TABLE_PASSWORD_COLUMN = "password";
    // COORDINATES_TABLE column names
    public static final String COORDINATES_TABLE_ID_COLUMN = "id";
    public static final String COORDINATES_TABLE_X_COLUMN = "x";
    public static final String COORDINATES_TABLE_Y_COLUMN = "y";
    // PERSON_TABLE column names
    public static final String HOUSE_TABLE_ID_COLUMN = "id";
    public static final String HOUSE_TABLE_NAME_COLUMN = "name";
    public static final String HOUSE_TABLE_YEAR_COLUMN = "year";
    public static final String HOUSE_TABLE_NUMBER_OF_FLOORS_COLUMN = "number_of_floors";
    /*
    CREATE TABLE house(
            id SERIAL PRIMARY KEY,
            name           CHAR(50)    NOT NULL,
            year        INT,
            number_of_floors         INT
    );

    CREATE TABLE coordinates(
            id SERIAL PRIMARY KEY,
            x FLOAT,
            y FLOAT
    );

    CREATE TABLE users (
        id SERIAL PRIMARY KEY,
        username VARCHAR ( 50 ) UNIQUE NOT NULL,
        password VARCHAR ( 50 ) NOT NULL
       );

     */

    private final String JDBC_DRIVER = "org.postgresql.Driver";

    private String url;
    private String user;
    private String password;
    private Connection base;

    public DatabaseManager(String ur, String usr, String pass) {
        url = ur;
        user = usr;
        password = pass;

        connectToDataBase();
    }

    private void connectToDataBase() {
        try {
            Class.forName(JDBC_DRIVER);
            base = DriverManager.getConnection(url, user, password);
            Main.logger.info("Соединение с базой данных установлено.");
        } catch (SQLException exception) {
            Main.logger.error("Произошла ошибка при подключении к базе данных!");
        } catch (ClassNotFoundException exception) {
            Main.logger.error("Драйвер управления базой дынных не найден!");
        }
    }

    public PreparedStatement getPreparedStatement(String sqlStatement, boolean generateKeys) {
        PreparedStatement preparedStatement;
        try {
            if (base == null) throw new SQLException();
            int autoGeneratedKeys = generateKeys ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS;
            preparedStatement = base.prepareStatement(sqlStatement, autoGeneratedKeys);
            Main.logger.info("Подготовлен SQL запрос '" + sqlStatement + "'.");
            return preparedStatement;
        } catch (SQLException exception) {
            if (base == null) Main.logger.error("Соединение с базой данных не установлено!");
        }
        return null;
    }


    public void closePreparedStatement(PreparedStatement sqlStatement) {
        if (sqlStatement == null) return;
        try {
            sqlStatement.close();
            Main.logger.info("Закрыт SQL запрос '" + sqlStatement + "'.");
        } catch (SQLException exception) {
            Main.logger.error("Произошла ошибка при закрытии SQL запроса '" + sqlStatement + "'.");
        }
    }

    public void closeConnection() {
        if (base == null) return;
        try {
            base.close();
            Main.logger.info("Соединение с базой данных разорвано.");
        } catch (SQLException exception) {
            Main.logger.error("Произошла ошибка при разрыве соединения с базой данных!");
        }
    }

    public void setCommitMode() {
        try {
            if (base == null) throw new SQLException();
            base.setAutoCommit(false);
        } catch (SQLException exception) {
            Main.logger.error("Произошла ошибка при установлении режима транзакции базы данных!");
        }
    }

    public void setNormalMode() {
        try {
            if (base == null) throw new SQLException();
            base.setAutoCommit(true);
        } catch (SQLException exception) {
            Main.logger.error("Произошла ошибка при установлении нормального режима базы данных!");
        }
    }

    public void commit() {
        try {
            if (base == null) throw new SQLException();
            base.commit();
        } catch (SQLException exception) {
            Main.logger.error("Произошла ошибка при подтверждении нового состояния базы данных!");
        }
    }

    public void rollback() {
        try {
            if (base == null) throw new SQLException();
            base.rollback();
        } catch (SQLException exception) {
            Main.logger.error("Произошла ошибка при возврате исходного состояния базы данных!");
        }
    }

    public void setSavepoint() {
        try {
            if (base == null) throw new SQLException();
            base.setSavepoint();
        } catch (SQLException exception) {
            Main.logger.error("Произошла ошибка при сохранении состояния базы данных!");
        }
    }
}
