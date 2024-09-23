package oleborn;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws SQLException, InterruptedException {
        Connection conn = DatabaseConnection.getConnection();
        Statement statement = conn.createStatement();
        conn.setAutoCommit(false);
        ResultSet rs;
        //--------------------------------------//

        statement.execute(SQLCommands.createTable());
        conn.commit();
        System.out.println("Таблица users создана!\n");

        Thread.sleep(1000);
        //---------------------------------------//
        Map<String, Integer> map = Map.of("Den", 1000000, "Andrey", 2000000, "Roman", 3000000);

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            statement.execute(SQLCommands.insertUser(entry.getKey(), entry.getValue()));
        }

        conn.commit();
        System.out.println("В таблицу users добавлено 3 человека!\n");
        Thread.sleep(1000);
        //---------------------------------------//

        rs = statement.executeQuery(SQLCommands.returnAllUsers());
        conn.commit();

        while (rs.next()) {
            UserDto userDto = new UserDto(rs.getInt("id"), rs.getString("name"), rs.getInt("salary"));
            System.out.printf("Порядковый номер: %d, имя: %s, заплата: %d\n", userDto.getId(), userDto.getName(), userDto.getSalary());
        }
        System.out.println("Из таблицы выведены в консоль все данные!\n");
        Thread.sleep(1000);
        //---------------------------------------//

        statement.execute(SQLCommands.deleteUser(1));
        conn.commit();

        rs = statement.executeQuery(SQLCommands.returnAllUsers());
        while (rs.next()) {
            UserDto userDto = new UserDto(rs.getInt("id"), rs.getString("name"), rs.getInt("salary"));
            System.out.printf("Порядковый номер: %d, имя: %s, заплата: %d\n", userDto.getId(), userDto.getName(), userDto.getSalary());
        }
        System.out.println("Из таблицы выведены в консоль все данные после удаления!\n");
        Thread.sleep(1000);
        //---------------------------------------//

        statement.execute(SQLCommands.updateSalary(2, 4000000));
        rs = statement.executeQuery(SQLCommands.returnUserByID(2));
        rs.next();

        UserDto userDto = new UserDto(rs.getInt("id"), rs.getString("name"), rs.getInt("salary"));
        System.out.printf("Порядковый номер: %d, имя: %s, заплата: %d\n", userDto.getId(), userDto.getName(), userDto.getSalary());

        System.out.println("Из таблицы выведен user с порядковым номером 2 в консоль!\n");
        Thread.sleep(1000);
        //---------------------------------------//

        statement.execute(SQLCommands.deleteTable("users"));
        conn.commit();
        System.out.println("Таблица users удалена!\n");
    }
}