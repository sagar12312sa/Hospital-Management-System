import java.sql.*;
import java.util.*;


public class Patient{
    
    private Connection connection;
    private Scanner scanner;

    public Patient(Connection connection,Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }
    public void addPatient(){
        System.out.println("Enter Patients name");
        String name = scanner.nextLine();
        System.out.println("Enter patient's age");
        int age = scanner.nextInt();
        System.out.println("Enter Patient's Gender");
        String gender = scanner.nextLine();
        try{
           String sql = "Insert into patients(name,age,gender) values(?,?,?)";
           PreparedStatement pstmt = connection.prepareStatement(sql);
           pstmt.setString(1, name);
           pstmt.setInt(2, age);
           pstmt.setString(3, gender);

           int i = pstmt.executeUpdate();
           if(i > 0){
             System.out.println("Patient Added Successfully");
           }
           else{
            System.out.println("Something went wrong");
           }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void ViewPatient(){
        String sql = "Select * from patients";
        try{
            PreparedStatement pstmt = connection.prepareStatement(sql);
            ResultSet res = pstmt.executeQuery();
            System.out.println("Patients :");
            System.out.println("+------------+--------------------+----------+------------+");
            System.out.println("| Patient Id | Name               | Age      | Gender     |");
            System.out.println("+------------+--------------------+----------+------------+");
            while(res.next()){
               int id = res.getInt("id");
               String name = res.getString("name");
               int age = res.getInt("age");
               String gender = res.getString("gender");
                System.out.printf("| %-10s | %-18s | %-8s | %-10s |\n", id, name, age, gender);
                System.out.println("+------------+--------------------+----------+------------+");

            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }


    public boolean getPatientById(int id){
        String sql = "select * from patients where id = ?";
        try{
           PreparedStatement pstmt = connection.prepareStatement(sql);
           ResultSet res = pstmt.executeQuery();
           if(res.next()){
            return true;
           }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
