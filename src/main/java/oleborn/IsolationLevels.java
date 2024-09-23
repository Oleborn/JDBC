package oleborn;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class IsolationLevels {
    public static void main(String[] args) throws InterruptedException {
        Thread threadA = new Thread(IsolationLevels::executeTransactionA);
        Thread threadB = new Thread(IsolationLevels::executeTransactionB);

        threadA.start();
        threadB.start();

        threadA.join();
        threadB.join();

    }
    private static void executeTransactionA() {
        try(Connection connection = DatabaseConnection.getConnection()){
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            try(ResultSet resultSet = connection.createStatement().executeQuery(SQLCommands.returnSalaryByID(2))){
                if(resultSet.next()){
                    double salary = resultSet.getDouble("salary");
                    System.out.println("threadA запрос №1: зарплата до обновления: " + salary);
                }
            }

            Thread.sleep(5000);

            try(ResultSet resultSet = connection.createStatement().executeQuery(SQLCommands.returnSalaryByID(2))){
                if(resultSet.next()){
                    double salary = resultSet.getDouble("salary");
                    System.out.println("threadA запрос №2: зарплата после обновления: " + salary);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            connection.commit();

            Thread.sleep(1000);
            try(ResultSet resultSet = connection.createStatement().executeQuery(SQLCommands.returnSalaryByID(2))){
                if(resultSet.next()){
                    double salary = resultSet.getDouble("salary");
                    System.out.println("threadA запрос №3 после комита: зарплата после обновления: " + salary);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }
    private static void executeTransactionB() {
        try(Connection connection = DatabaseConnection.getConnection()){
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);

            Thread.sleep(500);

            connection.prepareStatement(SQLCommands.updateSalary(2, 50000)).executeUpdate();

            connection.commit();
            System.out.println("threadB обновление зарплаты до 50000! Закомичено");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
