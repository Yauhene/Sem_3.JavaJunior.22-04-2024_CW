package lesson3;

import java.sql.*;

public class Homework_JDBC_mysql {

  public static void main(String[] args) {

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

      // Создание таблицы групп
      //
      statement.execute("""
        create table group_table(
          id varchar(36) default (uuid()) primary key,
          group_name varchar(30)
        )
        """);
    }
    // Заполнение таблицы групп
    //
    try (Statement statement = connection.createStatement()) {
      System.out.println("\nДобавляем данные в таблицу групп:");
      int count = statement.executeUpdate("""
        insert into group_table(group_name) values
        ('Java'),
        ('Test')
        """);
      System.out.println("Количество вставленных строк: " + count);
    }


    // Создаем таблицу студентов
    //

    try (Statement statement = connection.createStatement()) {
      // execute, executeUpdate, executeQuery

      statement.execute("""
        create table student(
          id varchar(36) default (uuid()) primary key,
          first_name varchar(256),
          second_name varchar(256),
          group_id varchar(128)
        )
        """);
    }

    // Добавляем данные в таблицу студентов
    //
    try (Statement statement = connection.createStatement()) {
      System.out.println("\nДобавляем данные в таблицу студентов:");
      int count = statement.executeUpdate("""
        insert into student(first_name, second_name, group_id) values
        ('Фрося', 'Зяблова', '50ec54e5-0799-11ef-b5d0-0a0027000005'),
        ('Манвел', 'Канселян', '50ec54e5-0799-11ef-b5d0-0a0027000005'),
        ('Иван', 'Иванов', '50ec54e5-0799-11ef-b5d0-0a0027000005'),
        ('Васян', 'Огурцов', '50ec5a7f-0799-11ef-b5d0-0a0027000005'),
        ('Изольда', 'Квакшина', '50ec5a7f-0799-11ef-b5d0-0a0027000005')
        """);
      System.out.println("Количество вставленных строк: " + count);
    }

    // Изменяем название группы 'Test' -> 'Test_1'
    //
    try (Statement statement = connection.createStatement()) {
      System.out.println("\nИзменение данных:");
      int count = statement.executeUpdate("""
              update group_table
              set group_name =  'Test_1'
              where group_name = 'Test'
              """);
      System.out.println("Количество обновленных строк: " + count);
    }

    // ResultSet

    // Выполнение SQL-запроса (все студенты)
    //
    try (Statement statement = connection.createStatement()) {
      System.out.println("\nВыполняем SQL-запрос:");
      ResultSet resultSet = statement.executeQuery("""
              select id, first_name, second_name, group_id
              from student
              """);
      while (resultSet.next()) {
        String id = resultSet.getString("id");
        String first_name = resultSet.getString("first_name");
        String second_name = resultSet.getString("second_name");
        String group_id = resultSet.getString("group_id");
        System.out.println(String.format("%s, %s, %s, %s", id, first_name, second_name, group_id));
      }
    }

    // Добавляем студентов в таблицу
    //
    try (Statement statement = connection.createStatement()) {
      System.out.println("\nДобавляем данные:");
      int count = statement.executeUpdate("""
              insert into student(first_name, second_name, group_id) values
              ('Владимир', 'Ульянов', '50ec54e5-0799-11ef-b5d0-0a0027000005'),
              ('Фридрих', 'Энгельс', '50ec5a7f-0799-11ef-b5d0-0a0027000005')
              """);
      System.out.println("Количество вставленных строк: " + count);
    }

    try (Statement statement = connection.createStatement()) {
      System.out.println("Попытка выполнить второй SQL-запрос:");
      ResultSet resultSet = statement.executeQuery("""
              select id, first_name, second_name, group_id
              from student
              """);
      while (resultSet.next()) {
        String id = resultSet.getString("id");
        String first_name = resultSet.getString("first_name");
        String second_name = resultSet.getString("second_name");
        String group_id = resultSet.getString("group_id");
        System.out.println(String.format("%s, %s, %s, %s", id, first_name, second_name, group_id));
      }
    }



    removePersonById(connection, "21cdf5f5-07a0-11ef-b5d0-0a0027000005");

    sqlQuery("", connection);

    sqlQuery("select id, first_name, second_name, group_id\n" +
            "              from student where group_id = '50ec54e5-0799-11ef-b5d0-0a0027000005'", connection);

    try (Statement statement = connection.createStatement()) {
      System.out.println("\nВыполнение sql-запроса с left join:");
      ResultSet resultSet = statement.executeQuery(
              "select first_name, second_name, student.group_id, group_table.id, group_table.group_name " +
                      "from student " +
                      "left join group_table on student.group_id = group_table.id"
      );
      while (resultSet.next()) {
        String id = resultSet.getString("id");
        String first_name = resultSet.getString("first_name");
        String second_name = resultSet.getString("second_name");
        String group_id = resultSet.getString("group_id");
        String group_name = "группа " + resultSet.getString("group_name");
        System.out.println(String.format("%s %s, %s", first_name, second_name, group_name));
      }
    }

  }

  // вот этот метод вызывается ПРИ передаче параметра с браузера
  static void removePersonById(Connection connection, String idParameter) throws SQLException {
    System.out.println("\nУдаление данных");

    try (PreparedStatement preparedStatement = connection.prepareStatement("delete from student where id = ? and group_id = ?")) {
      preparedStatement.setString(1, idParameter);
      preparedStatement.setString(2, "50ec54e5-0799-11ef-b5d0-0a0027000005");
      int deletedRowsCount = preparedStatement.executeUpdate();
      System.out.println("Удалено строк: " + deletedRowsCount);
    }

    // ResultSet
    try (Statement statement = connection.createStatement()) {
      ResultSet resultSet = statement.executeQuery("""
              select id, first_name, second_name, group_id
              from student
              """);

      System.out.println("Записи после удаления: ");
      while (resultSet.next()) {
        String id = resultSet.getString("id");
        String first_name = resultSet.getString("first_name");
        String second_name = resultSet.getString("second_name");
        String group_id = resultSet.getString("group_id");
        System.out.println(String.format(String.format("%s, %s, %s, %s", id, first_name, second_name, group_id)));
      }
    }

  }

  static void sqlQuery(String str, Connection connection) {
    try (Statement statement = connection.createStatement()) {
      System.out.println("\nВыполняем SQL-запрос:");
      if (str.isEmpty()) {
        str = "select id, first_name, second_name, group_id" +
        " from student ";
      }
      ResultSet resultSet = statement.executeQuery(str);

      System.out.println("Вывод результата SQL-запроса == " + "'" + str + "'");
      while (resultSet.next()) {
        String id = resultSet.getString("id");
        String first_name = resultSet.getString("first_name");
        String second_name = resultSet.getString("second_name");
        String group_id = resultSet.getString("group_id");
        System.out.println(String.format("%s, %s, %s, %s", id, first_name, second_name, group_id));
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

  }
}
