package lesson3;

import java.sql.*;

public class OldHomework {

  /**
   * Повторить все, что было на семниаре на таблице Student с полями
   * 1. id - bigint
   * 2. first_name - varchar(256)
   * 3. second_name - varchar(256)
   * 4. group - varchar(128)
   * <p>
   * Написать запросы:
   * 1. Создать таблицу
   * 2. Наполнить таблицу данными (insert)
   * 3. Поиск всех студентов
   * 4. Поиск всех студентов по имени группы
   * <p>
   * Доп. задания:
   * 1. ** Создать таблицу group(id, name); в таблице student сделать внешний ключ на group
   * 2. ** Все идентификаторы превратить в UUID
   * <p>
   * Замечание: можно использовать ЛЮБУЮ базу данных: h2, postgres, mysql, ...
   */

  // UUID uuid = UUID.randomUUID();
  // UUID uuid1 = UUID.fromString(cc3100f9-8f57-4e54-aebb-a8a8ad03ab0a);
  // https://www.uuidgenerator.net/


  public static void main(String[] args) throws SQLException {
    try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sem_3_sql", "root", "61208619")) {
      try (Statement statement = connection.createStatement()) {
//        statement.execute("""
//          create table student(
//          id uuid default random_uuid(),
//            name varchar(256)
//          )
//          """);
        statement.execute("""
                create table student(
                id bigint,
                first_name varchar(256),
                second_name varchar(256),
                group varchar(128)
                )
                """);
      }

//      String sql = "insert into person(name) values";
//      for (int i = 1; i <= 2; i++) {
//        sql += "('" + "Test #" + i + "')";
//        if (i != 2) {
//          sql += ", ";
//        }
//      }
//
//      String[] generatedColumns = {"id"};
//      try (PreparedStatement statement = connection.prepareStatement(sql, generatedColumns)) {
//        System.out.println(statement.executeUpdate());
//
//        ResultSet keys = statement.getGeneratedKeys();
//        while (keys.next()) {
//          System.out.println(keys.getString("id"));
//        }
//
//      }
//
//      System.out.println();
//      System.out.println();
//      System.out.println();
//      try (Statement statement = connection.createStatement()) {
//        ResultSet resultSet = statement.executeQuery("select id, name from person");
//        while (resultSet.next()) {
//          System.out.println(resultSet.getString("id"));
//          System.out.println(resultSet.getString("name"));
//        }
//      }
    }
  }

}
