package lesson3;

import java.sql.*;
import java.util.UUID;

public class JDBC {

  public static void main(String[] args) {
    // jpa
    // JPA = Jakarta Persistence API (Entity)
    // JDBC = Java Database Connectivity =
    // Driver - нечто, позволяющее подключаться к конкретной базе данных
    // PostgresDriver, OracleDriver, MySQLDriver, ...
    // DriverManager - класс, который управляет драйверами
    // Connection - интерфейс, описывающий соединение с базой данных

    // postgres - jdbc:postgresql://localhost:5432/database_name
    // mysql - jdbc:mysql://10.50.128.3/mydbname

    // try with resources

    try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:testdb", "root", "root")) {
          acceptConnection(connection);

          // !!!!!!! Если не используется "try с ресурсами" - обязательно закрыть:
          // connection.close();

    } catch (SQLException e) {
      System.err.println("Не удалось подключиться к БД: " + e.getMessage());
    }
  }

  static void acceptConnection(Connection connection) throws SQLException {
    // Statement - интерфейс, описывающий конкретный запрос в БД
    try (Statement statement = connection.createStatement()) {
      // execute, executeUpdate, executeQuery
      statement.execute("""
        create table person(
          id bigint,
          name varchar(256),
          age smallint
        )
        """);
    }

    try (Statement statement = connection.createStatement()) {
      int count = statement.executeUpdate("""
        insert into person(id, name, age) values
        (1, 'Igor', 25),
        (2, 'John', 36),
        (3, 'Peter', 47),
        (4, 'Васян Огурцов', 17)
        """);
      System.out.println("Количество вставленных строк: " + count);
    }

    try (Statement statement = connection.createStatement()) {
      int count = statement.executeUpdate("""
        update person
        set age = -1
        where id > 1
        """);
      System.out.println("Количество обновленных строк: " + count);
    }

    // ResultSet
    try (Statement statement = connection.createStatement()) {
      ResultSet resultSet = statement.executeQuery("""
        select id, name, age
        from person
        """);

      //
      // (1, 'Igor', 25),
      // (2, 'John', 36),
      // (3, 'Peter', 47)
      // >

      while (resultSet.next()) {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        int age = resultSet.getInt("age");
        System.out.println("Прочитана строка: " + String.format("%s, %s, %s", id, name, age));
      }
    }

    removePersonById(connection, "1");
  }

  // вот этот метод вызывается ПРИ передаче параметра с браузера
  static void removePersonById(Connection connection, String idParameter) throws SQLException {
    try (PreparedStatement preparedStatement = connection.prepareStatement("delete from person where id = ?1 and name = ?2")) {
      preparedStatement.setLong(1, Integer.parseInt(idParameter));
      preparedStatement.setString(2, "Igor");
      int deletedRowsCount = preparedStatement.executeUpdate();
      System.out.println("Удалено строк: " + deletedRowsCount);
    }

    // ResultSet
    try (Statement statement = connection.createStatement()) {
      ResultSet resultSet = statement.executeQuery("""
        select id, name, age
        from person
        """);

      System.out.println("Записи после удаления: ");
      while (resultSet.next()) {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        int age = resultSet.getInt("age");
        System.out.println("Прочитана строка: " + String.format("%s, %s, %s", id, name, age));
      }
    }

  }

}
