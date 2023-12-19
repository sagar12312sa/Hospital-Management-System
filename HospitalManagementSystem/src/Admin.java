
import java.sql.*;
import java.util.*;
public class Admin {
    private Connection connection;
    private Scanner scanner;

    Admin(Connection connection,Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    } 

    public boolean isAdmin(){
        String userName,password;
        scanner.nextLine();
        System.out.println("Enter your username");
        userName = scanner.next();
        System.out.println("Enter password");
        password = scanner.next();
        

        return userName.equals("Sagar")&&password.equals("1234");
    }

    public void addDoctor(Connection connection,Scanner scanner){
        String sql = "Insert into doctors(name,specialisation) values(?,?)";
        scanner.nextLine();
        System.out.println("Enter Doctors Name");
        String name = scanner.nextLine();
        System.out.println("Enter Specialisation");
        String specialization = scanner.nextLine();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2,specialization);
            int i = preparedStatement.executeUpdate();
            if(i > 0){
                System.out.println("Doctor Added Successfully");
            }
            else{
                System.out.println("Failed to add doctor");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}