package lesson3;

import java.sql.*;

public class JDBC_mysql {

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

    try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sem_3_sql", "root", "61208619")) {
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

//      statement.execute("""
//        create table student(
//          id bigint,
//          first_name varchar(256),
//          second_name varchar(256),
//          group_name varchar(128)
//        )
//        """);
    }

    try (Statement statement = connection.createStatement()) {
//      System.out.println("\nДобавляем данные в таблицу:");
//      int count = statement.executeUpdate("""
//        insert into student(id, first_name, second_name, group_name) values
//        (1, 'Фрося', 'Зяблова', 'Java'),
//        (2, 'Манвел', 'Канселян', 'Java'),
//        (3, 'Иван', 'Иванов', 'Java'),
//        (4, 'Васян', 'Огурцов', 'Test'),
//        (5, 'Изольда', 'Квакшина', 'Test')
//        """);
//      System.out.println("Количество вставленных строк: " + count);
    }

    try (Statement statement = connection.createStatement()) {
//      System.out.println("\nИзменение данных:");
//      int count = statement.executeUpdate("""
//        update student
//        set group_name =  'Test_1'
//        where id > 3
//        """);
//      System.out.println("Количество обновленных строк: " + count);
    }

    // ResultSet
    try (Statement statement = connection.createStatement()) {
      System.out.println("\nВыполняем SQL-запрос:");
      ResultSet resultSet = statement.executeQuery("""
        select id, first_name, second_name, group_name
        from student
        """);

      while (resultSet.next()) {
        int id = resultSet.getInt("id");
        String first_name = resultSet.getString("first_name");
        String second_name = resultSet.getString("second_name");
        String group_name = resultSet.getString("group_name");
        System.out.println(String.format("%s, %s, %s, %s", id, first_name, second_name, group_name));
      }
    }

    try (Statement statement = connection.createStatement()) {
//      System.out.println("\nДобавляем данные:");
//      int count = statement.executeUpdate("""
//        insert into student(id, first_name, second_name, group_name) values
//        (6, 'Владимир', 'Ульянов', 'Java'),
//        (7, 'Фридрих', 'Энгельс', 'Test')
//        """);
//      System.out.println("Количество вставленных строк: " + count);
    }

    try (Statement statement = connection.createStatement()) {
      System.out.println("Попытка выполнить второй SQL-запрос:");
      ResultSet resultSet = statement.executeQuery("""
        select id, first_name, second_name, group_name
        from student
        """);

      while (resultSet.next()) {
        int id = resultSet.getInt("id");
        String first_name = resultSet.getString("first_name");
        String second_name = resultSet.getString("second_name");
        String group_name = resultSet.getString("group_name");
        System.out.println(String.format("%s, %s, %s, %s", id, first_name, second_name, group_name));
      }
    }

    removePersonById(connection, "7");
  }

  // вот этот метод вызывается ПРИ передаче параметра с браузера
  static void removePersonById(Connection connection, String idParameter) throws SQLException {
    System.out.println("Попытка удаления данных");

    try (PreparedStatement preparedStatement = connection.prepareStatement("delete from student where id = ?1 and group_name = ?2")) {
//    try (PreparedStatement preparedStatement = connection.prepareStatement("delete from student where id = 7")) {
      System.out.println("\nдобежали");
      preparedStatement.setLong(1, Integer.parseInt(idParameter));
      preparedStatement.setString(2, "Test");
      System.out.println("\nдобежали-2");
      int deletedRowsCount = preparedStatement.executeUpdate();
//      int deletedRowsCount = preparedStatement.executeUpdate();
      System.out.println("\nдобежали-3");
      System.out.println("Удалено строк: " + deletedRowsCount);
    }

    // ResultSet
    try (Statement statement = connection.createStatement()) {
      ResultSet resultSet = statement.executeQuery("""
        select id, first_name, second_name, group_name
        from student
        """);

      System.out.println("Записи после удаления: ");
      while (resultSet.next()) {
        int id = resultSet.getInt("id");
        String first_name = resultSet.getString("first_name");
        String second_name = resultSet.getString("second_name");
        String group_name = resultSet.getString("group_name");
        System.out.println(String.format(String.format("%s, %s, %s, %s", id, first_name, second_name, group_name)));
      }
    }

  }

}
