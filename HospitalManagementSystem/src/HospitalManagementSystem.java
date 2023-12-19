import java.sql.*;
import java.util.*;


public class HospitalManagementSystem {
    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String userName = "root";
    private static final String password = "root";

    public static void main(String[] args) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch(ClassNotFoundException e){
            e.printStackTrace();
        }
        Scanner sc = new Scanner(System.in);
        try{
            Connection connection = DriverManager.getConnection(url, userName, password);
            Patient patient = new Patient(connection, sc);
            Doctor doctor = new Doctor(connection);
            Admin admin = new Admin(connection, sc);
            while(true){
                System.out.println("HOSPITAL MANAGEMENT SYSTEM ");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patients");
                System.out.println("3. View Doctors");
                System.out.println("4. Book Appointment");
                System.out.println("5. Add Doctors");
                System.out.println("6. Exit");
                System.out.println("Enter your choice: ");
                int choice = sc.nextInt();
                switch (choice) {
                    case 1:
                       patient.addPatient();
                       System.out.println();
                       break;
                    case 2:
                        patient.ViewPatient();
                        System.out.println();
                        break;
                    case 3:
                        doctor.viewDoctors();
                        System.out.println();
                        break;
                    case 4:
                        bookAppointment(patient, doctor, connection, sc);
                        System.out.println();
                        break;
                    case 5:
                        if(admin.isAdmin()){
                            admin.addDoctor(connection, sc);
                        }
                        else{
                            System.out.println("Please enter valid username and password");
                        }
                        break;
                    case 6:
                        System.out.println("THANK YOU! FOR USING HOSPITAL MANAGEMENT SYSTEM!!");
                        System.exit(0);
                
                    default:
                        System.out.println("Please Enter a Valid Choice");
                        break;
                }
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void bookAppointment(Patient patient, Doctor doctor,Connection connection,Scanner scanner){
        System.out.println("Enter Patient id");
        int patientId = scanner.nextInt();
        System.out.println("Enter Doctor id");
        int doctorId = scanner.nextInt();
        System.out.println("Enter appointment date (yyyy-mm--dd)");
        String appointmentDate = scanner.nextLine();
        if(patient.getPatientById(patientId) && doctor.getDoctorById(doctorId)){
             if(checkDoctorAvailability(doctorId,appointmentDate,connection)){
                String sql = "Insert into appointments(patient_id,doctor_id,appointment_date) values(?,?,?)";
                try{
                  PreparedStatement pstmt = connection.prepareStatement(sql);
                  pstmt.setInt(1,patientId);
                  pstmt.setInt(2, doctorId);
                  pstmt.setString(3, appointmentDate);
                  int i = pstmt.executeUpdate();
                  if(i > 0){
                    System.out.println("Appointed Booked Successfully");
                  }
                  else{
                    System.out.println("Failed to book Appointment");
                  }
                }
                catch(SQLException e){
                    e.printStackTrace();
                }
             }
             else{
                System.out.println("Doctor Not available on this date");
             }
        }
        else{
            System.out.println("Either doctor or patient does not exist");
        }
    }

    public static boolean checkDoctorAvailability(int doctorId,String appointmentDate,Connection connection){
        String sql = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, doctorId);
            preparedStatement.setString(2, appointmentDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                int count = resultSet.getInt(1);
                if(count==0){
                    return true;
                }else{
                    return false;
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}