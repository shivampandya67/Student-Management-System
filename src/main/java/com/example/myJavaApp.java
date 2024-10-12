
package com.example;

import java.io.*;
import java.sql.*;
import java.util.*;

public class myJavaApp {
    

    public static void main(String[] args) {
        
        try (Scanner scanner = new Scanner(System.in)) {

            System.out.print("Semester (e.g. FA2003, SP2003, SU2003): ");
            String semester = scanner.nextLine();

            System.out.print("Enter your username: ");
            String username = scanner.nextLine();

            System.out.print("Enter your password: ");
            String password = scanner.nextLine();

            // Authenticate user from the database
            String role = authenticateUser(username, password);

            if (role != null) {
                System.out.println("Login successful. Welcome, " + role + "!");
                // Open the respective menu based on the role
                openMenuForRole(role,username,semester);
            } else {
                System.out.println("Login failed. Invalid credentials.");
            }
        }

    }


    // Authentication method
    private static String authenticateUser(String username, String password) {
        String role = null;
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://mysql:3306/mydatabase", "user", "userpassword")) {
            // Check if the user is a student
            String studentQuery = "SELECT password, 'Student' as role FROM STUDENTINFO WHERE sid = ?";
            try (PreparedStatement stmt = conn.prepareStatement(studentQuery)) {
                stmt.setString(1, username);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next() && rs.getString("password").equals(password)) {
                        return rs.getString("role");
                    }
                }
            }

            // If not a student, check if the user is a staff member
            String staffQuery = "SELECT password, staffType as role FROM STAFF WHERE tid = ?";
            try (PreparedStatement stmt = conn.prepareStatement(staffQuery)) {
                stmt.setString(1, username);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next() && rs.getString("password").equals(password)) {
                        return rs.getString("role");  // role could be Registrar, DepartmentStaff, etc.
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Database connection error.");
        }
        return role; // Return null if authentication fails
    }


    private static void openMenuForRole(String role,String username,String semester) {
        Scanner scanner = new Scanner(System.in);
        switch (role) {
            case "Student":
                // Open student menu
                System.out.println("Opening student menu...");
                studentMenu(scanner,username,semester);
                break;
            case "Registrar":
                // Open registrar menu
                System.out.println("Opening registrar menu...");
                registrarMenu(scanner,semester);
                break;
            case "DepartmentStaff":
                // Open department staff menu

                System.out.println("Opening department staff menu...");
                departmentMenu(scanner,semester);
                break;
            default:
                System.out.println("Role not recognized.");
        }
    }


    // Registrar menu
    private static void registrarMenu(Scanner scanner, String semester) {
        String option;
        do {
            System.out.println("\n*************************************************");
            System.out.println("    Welcome to the TechEduPro - Online Registration System");
            System.out.println("                 Registrar Staff                     ");
            System.out.println("*************************************************");
            System.out.println("1. Load Sections from File");
            System.out.println("2. Load Grades from File");
            System.out.println("3. Increase Section Cap");
            System.out.println("4. Display Term Schedule");
            System.out.println("5. Display Student Transcript");
            System.out.println("6. Display Student Schedule and Fee Detail");
            System.out.println("q. Quit");

            System.out.print("Type in your option: ");
            option = scanner.next();
            switch (option) {
                case "1":
                    loadSectionsFromFile();
                    break;
                case "2":
                    loadGradesFromFile();
                    break;
                case "3":
                    increaseSectionCap(semester);
                    break;
                case "4":
                    displayTermSchedule(semester);
                    break;
                case "5":
                    displayStudentTranscript();
                    break;
                case "6":
                    displayStudentScheduleAndFee();
                    break;
                case "q":
                    System.out.println("Returning to the main menu...");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }while (!option.equals("q"));
    }

    // Department staff menu
    private static void departmentMenu(Scanner scanner,String semester) {
        String option;
        do {
            System.out.println("\n*************************************************");
            System.out.println("    Welcome to the TechEduPro - Online Registration System");
            System.out.println("                   Department Staff                   ");
            System.out.println("*************************************************");
            System.out.println("1. Authorize Student into Section");
            System.out.println("2. Overflow Student into Section");
            System.out.println("3. Add Assistantship on System");
            System.out.println("4. Generate Class List");
            System.out.println("q. Quit");

            System.out.print("Type in your option: ");
            option = scanner.next();
            switch (option) {
                case "1":
                    authorizeStudent(scanner);
                    break;
                case "2":
                    overflowStudent(scanner,semester);
                    break;
                case "3":
                    addAssistantship(scanner);
                    break;
                case "4":
                    generateClassList(scanner);
                    break;
                case "q":
                    System.out.println("Returning to the main menu...");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }while (!option.equals("q"));
    }

    // Student menu

    private static void studentMenu(Scanner scanner,String username,String semester) {
        String option;
        do {
            System.out.println("\n*************************************************");
            System.out.println("    Welcome to the TechEduPro - Online Registration System");
            System.out.println("                      Student                       ");
            System.out.println("*************************************************");
            System.out.println("1. Add a Section");
            System.out.println("2. Drop a Section");
            System.out.println("3. See Schedule for a Term");
            System.out.println("4. See Fee Detail");
            System.out.println("5. See Transcript");
            System.out.println("q. Quit");

            System.out.print("Type in your option: ");
            option = scanner.next();
            switch (option) {
                case "1":
                    addSection(scanner, username,semester);
                    break;
                case "2":
                    dropSection(scanner, username);
                    break;
                case "3":
                    seeSchedule(scanner, username);
                    break;
                case "4":
                    seeFeeDetail(scanner, username,semester);
                    break;
                case "5":
                    seeTranscript(scanner, username);
                    break;
                case "q":
                    System.out.println("Returning to the main menu...");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        } while (!option.equals("q"));
    }

    // Placeholder methods for functionality:
private static void loadSectionsFromFile() {
    Scanner scanner = new Scanner(System.in);
    System.out.print("File Name: ");
    String fileName = scanner.nextLine();

    // Use ClassLoader to load the file
    ClassLoader classLoader = myJavaApp.class.getClassLoader();
    InputStream inputStream = classLoader.getResourceAsStream("Files/" + fileName);

    if (inputStream == null) {
        System.out.println("Error: File not found.");
        return;
    }

    // Load sections logic (assumes the sections.dat file exists)
    try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
        String term = br.readLine(); // Read the term
        String year = br.readLine(); // Read the year
        String line;
        while ((line = br.readLine()) != null) {
            // Parse and insert each section into the database
            String[] sectionData = line.split(",");

            // Check if any field is missing or blank before parsing
            if (sectionData.length != 11 || sectionData[0].isEmpty() || sectionData[2].isEmpty() || sectionData[3].isEmpty() || sectionData[8].isEmpty()) {
                System.out.println("Error: Missing or invalid data in sections.dat file.");
                continue;  // Skip this line and go to the next
            }

            // Insert into SECTIONS table
            String insertQuery = "INSERT IGNORE INTO SECTIONS (Term, CRN, Year, CPrefix, CNo, Section, Days, StartTime, EndTime, Room, Cap, Instructor, auth) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://mysql:3306/mydatabase", "user", "userpassword");
                 PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
                stmt.setString(1, term);
                stmt.setInt(2, Integer.parseInt(sectionData[0]));  // CRN
                stmt.setInt(3, Integer.parseInt(year));  // Year
                stmt.setString(4, sectionData[1]);  // Course Prefix
                stmt.setInt(5, Integer.parseInt(sectionData[2]));  // Course Number
                stmt.setInt(6, Integer.parseInt(sectionData[3]));  // Section
                stmt.setString(7, sectionData[4]);  // Days
                stmt.setString(8, sectionData[5]);  // Start Time
                stmt.setString(9, sectionData[6]);  // End Time
                stmt.setString(10, sectionData[7]);  // Room
                stmt.setInt(11, Integer.parseInt(sectionData[8]));  // Capacity
                stmt.setString(12, sectionData[9]);  // Instructor
                stmt.setString(13, sectionData[10]);  // Auth Required (0 or 1)

                stmt.executeUpdate();
            }
        }
        System.out.println("Sections Loaded");
    } catch (IOException e) {
        e.printStackTrace();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


private static void loadGradesFromFile() {
    Scanner scanner = new Scanner(System.in);
    System.out.print("File Name: ");
    String fileName = scanner.nextLine();

    // Use ClassLoader to load the file from the resources folder
    ClassLoader classLoader = myJavaApp.class.getClassLoader();
    InputStream inputStream = classLoader.getResourceAsStream("Files/" + fileName);  // assuming the file is in the 'Files' subfolder inside 'resources'

    if (inputStream == null) {
        System.out.println("Error: File not found.");
        return;
    }

    // Load grades logic (assumes the grades.dat file exists)
    try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
        String term = br.readLine(); // Read the term
        String year = br.readLine(); // Read the year
        String line;
        while ((line = br.readLine()) != null) {
            String[] gradeData = line.split(",");  // Split by comma

            // Check if data is complete and correct, but IGNORE invalid entries
            if (gradeData.length < 3 || gradeData[0].isEmpty() || gradeData[1].isEmpty() || gradeData[2].isEmpty()) {
                System.out.println("Error: Missing or invalid data in grades.dat file.");
                continue;  // Skip this line and go to the next
            }

            int sid = Integer.parseInt(gradeData[0].trim());   // Student ID
            int crn = Integer.parseInt(gradeData[1].trim());   // CRN
            double gpa = Double.parseDouble(gradeData[2].trim());  // GPA

            // Insert into ENROLLMENT table or update GPA if SID and CRN already exist
            String insertOrUpdateQuery = "INSERT INTO ENROLLMENT (SID, term, crn, year, GPA) " +
                    "VALUES (?, ?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE GPA = VALUES(GPA)";

            try (Connection conn = DriverManager.getConnection("jdbc:mysql://mysql:3306/mydatabase", "user", "userpassword");
                 PreparedStatement stmt = conn.prepareStatement(insertOrUpdateQuery)) {
                stmt.setInt(1, sid);  // SID
                stmt.setString(2, term);  // Term
                stmt.setInt(3, crn);  // CRN
                stmt.setInt(4, Integer.parseInt(year));  // Year
                stmt.setDouble(5, gpa);  // GPA

                stmt.executeUpdate();
            }
        }
        System.out.println("Grades loaded and updated.");
    } catch (IOException e) {
        e.printStackTrace();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}



    private static void increaseSectionCap(String semester) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("CRN: ");
        int crn = Integer.parseInt(scanner.next());
        int year = Integer.parseInt(semester.substring(2));

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://mysql:3306/mydatabase", "user", "userpassword")) {
            // Fetch old capacity
            String capacityQuery = "SELECT Cap FROM SECTIONS WHERE CRN = ? AND year = ?";
            try (PreparedStatement capacityStmt = conn.prepareStatement(capacityQuery)) {
                capacityStmt.setInt(1, crn);
                capacityStmt.setInt(2, year);
                ResultSet rs = capacityStmt.executeQuery();

                if (rs.next()) {
                    int oldCap = rs.getInt("Cap");
                    System.out.println("Old Capacity is " + oldCap);
                    System.out.print("New Capacity: ");
                    int newCap = Integer.parseInt(scanner.next());

                    PreparedStatement updateStmt = conn.prepareStatement("UPDATE SECTIONS SET Cap = ? WHERE CRN = ? AND year = ?");
                    updateStmt.setInt(1, newCap);
                    updateStmt.setInt(2, crn);
                    updateStmt.setInt(3, year);
                    updateStmt.executeUpdate();

                    System.out.println("Cap Updated for CRN " + crn + " in Year " + year + ".");
                } else {
                    System.out.println("Section with CRN " + crn + " for Year " + year + " not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static void displayTermSchedule(String semester) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://mysql:3306/mydatabase", "user", "userpassword")) {
            String sql = "SELECT S.CRN, S.CPrefix, S.CNo, S.Section, S.Days, S.StartTime, S.EndTime, " +
                    "S.Room, S.Cap, COUNT(E.SID) AS CurrentEnrollment, S.Instructor, S.auth " +
                    "FROM SECTIONS S " +
                    "LEFT JOIN ENROLLMENT E ON S.CRN = E.CRN AND S.Term = E.Term AND S.Year = E.Year " +
                    "WHERE S.Term = ? " +
                    "GROUP BY S.CRN, S.CPrefix, S.CNo, S.Section, S.Days, S.StartTime, S.EndTime, " +
                    "S.Room, S.Cap, S.Instructor, S.auth";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, semester);
            ResultSet rs = stmt.executeQuery();

            System.out.printf("%-6s %-10s %-6s %-8s %-39s %-8s %-6s %-6s %-8s %-15s %-4s\n","CRN","Course","Sec","Days","Time","Room","Cap","Cur","Avail","Instructor","Auth");
            System.out.println("---------------------------------------------------------------------------------------------------------------------------");
            while (rs.next()) {
                int currentEnrollment = rs.getInt("CurrentEnrollment");
                int cap = rs.getInt("Cap");
                int availableSeats = cap - currentEnrollment;

                System.out.printf("%-6d %-10s %-6d %-8s %-12s %-8s %-6d %-6d %-8d %-15s %-4s\n",
                        rs.getInt("CRN"),
                        rs.getString("CPrefix") + rs.getInt("CNo"),
                        rs.getInt("Section"),
                        rs.getString("Days"),
                        rs.getString("StartTime") + "-" + rs.getString("EndTime"),
                        rs.getString("Room"),
                        cap,
                        currentEnrollment,
                        availableSeats,
                        rs.getString("Instructor"),
                        rs.getString("auth").equals("Y") ? "Y" : "N");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void displayStudentTranscript() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Student ID: ");
        int sid = Integer.parseInt(scanner.next());

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://mysql:3306/mydatabase", "user", "userpassword")) {
            String query = "SELECT E.CRN, S.CPrefix, S.CNo " +
                    "FROM ENROLLMENT E " +
                    "JOIN SECTIONS S ON E.CRN = S.CRN AND E.Term = S.Term AND E.Year = S.Year " +
                    "WHERE E.SID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, sid);
                ResultSet rs = stmt.executeQuery();

                System.out.println("CRN    Course");
                System.out.println("---------------------");
                while (rs.next()) {
                    System.out.printf("%-6d %-7s\n",
                            rs.getInt("CRN"), rs.getString("CPrefix") + rs.getInt("CNo"));
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void displayStudentScheduleAndFee() {
        Scanner scanner=new Scanner(System.in);
        System.out.print("Student ID: ");
        int sid = Integer.parseInt(scanner.next());

        System.out.print("Enter Term: ");
        String term = scanner.next();

        String url = "jdbc:mysql://mysql:3306/mydatabase"; // Update your database URL if needed
        String user = "user"; // Update your database user
        String password = "userpassword"; // Update your database password

        // Query to get student schedule and fees
        String scheduleQuery = "SELECT DISTINCT E.CRN, S.CPrefix, S.CNo, S.Section, S.Days, S.StartTime, S.EndTime, S.Room, " +
                "S.Instructor, F.FeeName, F.Amount, F.FinancialAidAmount, " +
                "F.InStateOrOutOfStateFeeAmount, F.LastDateOfSubmission, (F.Amount) AS TotalFee " +
                "FROM ENROLLMENT E " +
                "JOIN SECTIONS S ON E.CRN = S.CRN AND E.Term = S.Term AND E.Year = S.Year " +
                "JOIN Courses C ON S.CPrefix = C.CPrefix AND S.CNo = C.CNo " +
                "JOIN STUDENTINFO SI ON E.SID = SI.sid " +
                "JOIN FEES F ON SI.FeeID = F.FeeID " +
                "WHERE E.SID = ? AND E.Term = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(scheduleQuery)) {

            stmt.setInt(1, sid);
            stmt.setString(2, term);
            ResultSet rs = stmt.executeQuery();

            // Display the results
            System.out.printf("%-6s %-10s %-6s %-8s %-35s %-8s %-12s %-6s\n", "CRN", "Course", "Sec", "Days", "Time", "Room", "Instructor", "Fee");
            System.out.println("-----------------------------------------------------------------------------------------------------");

            while (rs.next()) {
                int crn = rs.getInt("CRN");
                String course = rs.getString("CPrefix") + rs.getInt("CNo");
                int section = rs.getInt("Section");
                String days = rs.getString("Days");
                String time = rs.getString("StartTime") + "-" + rs.getString("EndTime");
                String room = rs.getString("Room");
                String instructor = rs.getString("Instructor");
                double totalFee = rs.getDouble("TotalFee");

                System.out.printf("%-6d %-10s %-4d %-6s %-30s %-8s %-12s %-6.2f\n",
                        crn, course, section, days, time, room, instructor, totalFee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void authorizeStudent(Scanner scanner) {
        System.out.print("Enter CRN: ");
        String crnInput = scanner.next();
        System.out.print("Enter SID: ");
        long sid = scanner.nextLong();

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://mysql:3306/mydatabase", "user", "userpassword")) {
            String checkAuthQuery = "SELECT auth, term, year FROM SECTIONS WHERE crn = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkAuthQuery)) {
                checkStmt.setString(1, crnInput);
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next()) {
                    String authNeeded = rs.getString("auth");
                    String term = rs.getString("term"); // Fetch term from SECTIONS
                    int year = rs.getInt("year");      // Fetch year from SECTIONS

                    if ("N".equals(authNeeded)) {
                        System.out.println("No need to authorize - This section does not need authorization.");
                    } else {
                        // Authorize student
                        String authorizeQuery = "INSERT INTO AUTHORIZATION (term, year, crn, sid, authType, authDate, status) VALUES (?, ?, ?, ?, ?, NOW(), 'Authorized')";
                        try (PreparedStatement authorizeStmt = conn.prepareStatement(authorizeQuery)) {
                            authorizeStmt.setString(1, term);   // Use term from SECTIONS
                            authorizeStmt.setInt(2, year);      // Use year from SECTIONS
                            authorizeStmt.setString(3, crnInput);
                            authorizeStmt.setLong(4, sid);
                            authorizeStmt.setString(5, "Authorized");

                            authorizeStmt.executeUpdate();
                            System.out.println("Student " + sid + " authorized into CRN " + crnInput + ".");

                            // After authorizing, update the SECTIONS table's `auth` column to 'N'
                            String updateAuthQuery = "UPDATE SECTIONS SET auth = 'N' WHERE crn = ?";
                            try (PreparedStatement updateStmt = conn.prepareStatement(updateAuthQuery)) {
                                updateStmt.setString(1, crnInput);
                                updateStmt.executeUpdate();
                                System.out.println("Authorization completed. SECTIONS auth column updated to 'N' for CRN " + crnInput + ".");
                            }
                        }
                    }
                } else {
                    System.out.println("CRN not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static void overflowStudent(Scanner scanner,String semester) {
        System.out.print("Enter CRN: ");
        String crn = scanner.next();
        System.out.print("Enter SID: ");
        int sid = scanner.nextInt();
        int year = Integer.parseInt(semester.substring(2));; // Get Year from the user

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://mysql:3306/mydatabase", "user", "userpassword")) {
            // Check section capacity
            String query = "SELECT (SELECT COUNT(*) FROM ENROLLMENT e WHERE e.crn = s.crn) AS total_enrolled, s.cap " +
                    "FROM SECTIONS s WHERE s.crn = ? AND s.term = ? AND s.year = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, Integer.parseInt(crn));
            ps.setString(2, semester);
            ps.setInt(3, year);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int totalEnrolled = rs.getInt("total_enrolled");
                int capacity = rs.getInt("cap");

                if (totalEnrolled >= capacity) {
                    // Overflow student
                    String insertOverflow = "INSERT INTO AUTHORIZATION (CRN, SID, AuthType, Term, Year,authDate, Status) " +
                            "VALUES (?, ?, 'OVFL', ?, ?, NOW(),'Approved')";
                    PreparedStatement insertPs = conn.prepareStatement(insertOverflow);
                    insertPs.setInt(1, Integer.parseInt(crn));
                    insertPs.setInt(2, sid);
                    insertPs.setString(3, semester); // Use the user input for term
                    insertPs.setInt(4, year);    // Use the user input for year
                    insertPs.executeUpdate();
                    System.out.println("Student " + sid + " overflowed into CRN " + crn + " for term " + semester + " in year " + year);
                } else {
                    System.out.println("No need to overflow - Space still available in this section.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static String getStudentNameBySID(long sid) {
        String studentName = null;

        String query = "SELECT Name, lName FROM STUDENTINFO WHERE sid = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://mysql:3306/mydatabase", "user", "password");
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, sid);  // Set the SID in the query
            ResultSet rs = stmt.executeQuery();

            // If the student exists, retrieve and format the name
            if (rs.next()) {
                String firstName = rs.getString("Name");
                String lastName = rs.getString("lName");
                studentName = firstName + " " + lastName;
            } else {
                System.out.println("Error: Student with SID " + sid + " not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Database connection error.");
        }

        return studentName;
    }

    private static void addAssistantship(Scanner scanner) {
        System.out.print("Student Id: ");
        int sid = Integer.parseInt(scanner.next());

        // Assuming you have a method to fetch student name by SID

            // Add student to the assistantship list (assuming a table exists)
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://mysql:3306/mydatabase", "user", "userpassword")) {
                String updateAssistantship = "UPDATE STUDENTINFO SET gradAssistant = 1 WHERE SID = ?";
                PreparedStatement ps = conn.prepareStatement(updateAssistantship);
                ps.setInt(1, sid);
                int updatedRows = ps.executeUpdate();

                if (updatedRows > 0) {
                    System.out.println("Student " + sid + " has been added to the Assistantship List.");
                } else {
                    System.out.println("Failed to add student to Assistantship List.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    private static void generateClassList(Scanner scanner) {
        System.out.print("CRN: ");
        int crn = Integer.parseInt(scanner.next());

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://mysql:3306/mydatabase", "user", "userpassword")) {
            // Fetch course details including cprefix+cno, cTitle, Instructor, and Term
            String query = "SELECT e.sid, s.fName, s.IName, sec.cprefix, sec.cno, c.cTitle, sec.instructor, e.term " +
                    "FROM ENROLLMENT e " +
                    "JOIN STUDENTINFO s ON e.sid = s.sid " +
                    "JOIN SECTIONS sec ON e.crn = sec.crn " +
                    "JOIN Courses c ON sec.cprefix = c.cprefix AND sec.cno = c.cno " +
                    "WHERE e.crn = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, crn);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String cprefix = rs.getString("cprefix");
                int cno = rs.getInt("cno");
                String ctitle = rs.getString("cTitle");
                String instructor = rs.getString("instructor");
                String term = rs.getString("term");

                // Display the course information
                System.out.println(cprefix + " " + cno + ", " + ctitle);
                System.out.println(term);  // Display the term (e.g., SP 2003)
                System.out.println("Instructor: " + instructor);
                System.out.println("---------------------------------------------");
            }

            // Display the student information
            System.out.printf("%-10s %-20s %-20s%n", "SID", "LNAME", "FNAME");
            System.out.println("---------------------------------------------");

            do {
                int sid = rs.getInt("sid");
                String lname = rs.getString("IName");
                String fname = rs.getString("fName");
                System.out.printf("%-10d %-20s %-20s%n", sid, lname, fname);
            } while (rs.next());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    // 1.Add section method
public static void addSection(Scanner scanner, String username, String semester) {

    System.out.print("Enter CRN: ");
    String crnInput = scanner.next();

    if (!crnInput.matches("\\d+")) {
        System.out.println("Error: Invalid CRN input.");
        return;
    }

    int crn = Integer.parseInt(crnInput);

    try (Connection conn = DriverManager.getConnection("jdbc:mysql://mysql:3306/mydatabase", "user", "userpassword")) {
        // Fetch student type and major
        String studentTypeQuery = "SELECT sType, major FROM STUDENTINFO WHERE sid = ?";
        String studentType = "";
        String major = "";

        try (PreparedStatement studentTypeStmt = conn.prepareStatement(studentTypeQuery)) {
            studentTypeStmt.setInt(1, Integer.parseInt(username));
            ResultSet studentTypeRs = studentTypeStmt.executeQuery();

            if (studentTypeRs.next()) {
                studentType = studentTypeRs.getString("sType");
                major = studentTypeRs.getString("major");
            } else {
                System.out.println("Error: Student with ID " + username + " not found.");
                return;
            }
        }

        // Fetch the course details, including credits and schedule
        String courseQuery = "SELECT c.cprefix, c.cno, c.cTitle, s.auth, s.startTime, s.endTime, c.cCredits " +
                "FROM Courses c JOIN SECTIONS s ON c.cprefix = s.cprefix AND c.cno = s.cno WHERE s.crn = ?";

        try (PreparedStatement stmt = conn.prepareStatement(courseQuery)) {
            stmt.setInt(1, crn);  // Set the CRN in the query
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String cprefix = rs.getString("cprefix");
                int cno = rs.getInt("cno");
                String ctitle = rs.getString("cTitle");
                String authRequired = rs.getString("auth");
                String startTime = rs.getString("startTime");
                String endTime = rs.getString("endTime");
                int courseCredits = rs.getInt("cCredits");

                // Check if undergraduate student is registering for graduate courses
                if (studentType.equals("UGRAD") && cno >= 6000) {
                    System.out.println("Error: Undergraduate students are not allowed to register for graduate courses (6000 and above).");
                    return;
                }

                // Calculate total credits student has already registered for
                String creditQuery = "SELECT SUM(c.cCredits) AS totalCredits " +
                        "FROM ENROLLMENT e " +
                        "JOIN SECTIONS s ON e.crn = s.crn AND e.term = s.term AND e.year = s.year " +
                        "JOIN Courses c ON s.cprefix = c.cprefix AND s.cno = c.cno " +
                        "WHERE e.sid = ? AND e.term = ? AND e.year = 2024";
                
                int totalCredits = 0;
                try (PreparedStatement creditStmt = conn.prepareStatement(creditQuery)) {
                    creditStmt.setInt(1, Integer.parseInt(username));
                    creditStmt.setString(2, semester);
                    ResultSet creditRs = creditStmt.executeQuery();

                    if (creditRs.next()) {
                        totalCredits = creditRs.getInt("totalCredits");
                    }
                }

                // Add the current course's credits
                totalCredits += courseCredits;

                // Check for credit limit (20 for undergraduates, 15 for graduates)
                if ((studentType.equals("UGRAD") && totalCredits > 20) ||
                    (studentType.equals("GRAD") && totalCredits > 15)) {
                    System.out.println("Error: Credit limit exceeded. You can't register for this course.");
                    return;
                }

                // Check for time overlap
                String timeOverlapQuery = "SELECT s.startTime, s.endTime " +
                        "FROM ENROLLMENT e " +
                        "JOIN SECTIONS s ON e.crn = s.crn AND e.term = s.term AND e.year = s.year " +
                        "WHERE e.sid = ? AND e.term = ? AND e.year = 2024 " +
                        "AND ((s.startTime <= ? AND s.endTime >= ?) OR (s.startTime <= ? AND s.endTime >= ?))";

                try (PreparedStatement timeOverlapStmt = conn.prepareStatement(timeOverlapQuery)) {
                    timeOverlapStmt.setInt(1, Integer.parseInt(username));
                    timeOverlapStmt.setString(2, semester);
                    timeOverlapStmt.setString(3, startTime);
                    timeOverlapStmt.setString(4, startTime);
                    timeOverlapStmt.setString(5, endTime);
                    timeOverlapStmt.setString(6, endTime);

                    ResultSet timeRs = timeOverlapStmt.executeQuery();
                    if (timeRs.next()) {
                        System.out.println("Error: Time overlap detected with another course.");
                        return;
                    }
                }

                // If all checks pass, insert enrollment
                System.out.println(cprefix + cno + ", " + ctitle + " ADDED.");

                String insertQuery = "INSERT INTO ENROLLMENT (sid, term, crn, year, GPA, branch, tookFinAid, createdAt, updatedAt) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                    insertStmt.setInt(1, Integer.parseInt(username));
                    insertStmt.setString(2, semester);
                    insertStmt.setInt(3, crn);
                    insertStmt.setInt(4, 2024);
                    insertStmt.setString(5, null); // GPA set to null
                    insertStmt.setString(6, major); // Major from STUDENTINFO
                    insertStmt.setBoolean(7, true); // tookFinAid set to true

                    insertStmt.executeUpdate();
                    System.out.println("Enrollment added for student ID " + username + " for term " + semester);
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println("Error while inserting enrollment.");
                }

            } else {
                System.out.println("Error: Course with CRN " + crn + " not found.");
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("Database connection error.");
    }
}

    //2. drop section method
    private static void dropSection(Scanner scanner, String username) {
        System.out.print("Enter CRN: ");
        String crnInput = scanner.next();

// Validate CRN input is numeric
        if (!crnInput.matches("\\d+")) {
            System.out.println("Error: Invalid CRN input.");
            return;
        }

        int crn = Integer.parseInt(crnInput);

// Connect to the MySQL database and drop the enrollment if it exists
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://mysql:3306/mydatabase", "user", "userpassword")) {
            // Check if the student is enrolled in this course
            String checkEnrollmentQuery = "SELECT * FROM ENROLLMENT WHERE sid = ? AND crn = ?";
            try (PreparedStatement checkEnrollmentStmt = conn.prepareStatement(checkEnrollmentQuery)) {
                checkEnrollmentStmt.setString(1, username); // Using the logged-in username as SID
                checkEnrollmentStmt.setInt(2, crn);
                ResultSet enrollmentRs = checkEnrollmentStmt.executeQuery();

                if (enrollmentRs.next()) {
                    // Fetch course details to print after dropping
                    String selectCourseQuery = "SELECT c.cprefix, c.cno, c.ctitle FROM Courses c " +
                            "JOIN SECTIONS s ON c.cprefix = s.cprefix AND c.cno = s.cno WHERE s.crn = ?";
                    try (PreparedStatement selectCourseStmt = conn.prepareStatement(selectCourseQuery)) {
                        selectCourseStmt.setInt(1, crn);
                        ResultSet courseRs = selectCourseStmt.executeQuery();

                        String cprefix = null;
                        int cno = 0;
                        String ctitle = null;

                        if (courseRs.next()) {
                            cprefix = courseRs.getString("cprefix");
                            cno = courseRs.getInt("cno");
                            ctitle = courseRs.getString("ctitle");
                        }

                        // Drop the student's enrollment from the ENROLLMENT table
                        String deleteEnrollmentQuery = "DELETE FROM ENROLLMENT WHERE sid = ? AND crn = ?";
                        try (PreparedStatement deleteEnrollmentStmt = conn.prepareStatement(deleteEnrollmentQuery)) {
                            deleteEnrollmentStmt.setString(1, username);
                            deleteEnrollmentStmt.setInt(2, crn);
                            int enrollmentRowsAffected = deleteEnrollmentStmt.executeUpdate();

                            if (enrollmentRowsAffected > 0) {
                                if (cprefix != null && cno != 0 && ctitle != null) {
                                    System.out.println(cprefix + cno + ", " + ctitle + " DROPPED.");
                                }
                                System.out.println("Dropped enrollment for course with CRN " + crn + ".");
                            } else {
                                System.out.println("Error: Could not drop enrollment.");
                            }
                        }
                    }
                } else {
                    System.out.println("Error: You are not enrolled in course with CRN " + crn + ".");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Database connection error.");
        }



    }
    //3. See schedule method
    private static void seeSchedule(Scanner scanner, String username) {
        System.out.print("Enter the term (e.g., SP2024): ");
        String term = scanner.next();

        // Connect to the MySQL database
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://mysql:3306/mydatabase", "user", "userpassword")) {
            // SQL query to get the enrolled courses and their details for the specified term
            String query = "SELECT e.crn, c.cprefix, c.cno, c.ctitle, s.days, s.startTime, s.endTime, s.room, s.instructor " +
                    "FROM ENROLLMENT e " +
                    "JOIN SECTIONS s ON e.crn = s.crn AND e.term = s.term " +
                    "JOIN Courses c ON s.cprefix = c.cprefix AND s.cno = c.cno " +
                    "WHERE e.sid = ? AND e.term = ?";

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username); // Using the logged-in username as SID
                stmt.setString(2, term);      // Set the term in the query

                ResultSet rs = stmt.executeQuery();

                // Check if the student has any courses enrolled in that term
                if (!rs.isBeforeFirst()) { // Check if the result set is empty
                    System.out.println("No courses found for the term " + term + ".");
                } else {
                    // Print header
                    System.out.println("Option 3 Interface:");
                    System.out.println("Term: " + term);
                    System.out.printf("%-10s %-10s %-40s %-5s %-10s %-10s %-10s%n",
                            "CRN", "Course", "Title", "Days", "Time", "Room", "Instructor");
                    System.out.println("-----------------------------------------------------------------------------------------------------------------");

                    // Iterate through the result set and display the schedule
                    while (rs.next()) {
                        int crn = rs.getInt("crn");
                        String cprefix = rs.getString("cprefix");
                        int cno = rs.getInt("cno");
                        String ctitle = rs.getString("ctitle");
                        String days = rs.getString("days");
                        String startTime = rs.getString("startTime");
                        String endTime = rs.getString("endTime");
                        String room = rs.getString("room");
                        String instructor = rs.getString("instructor");

                        // Format time
                        String time = startTime.substring(11, 16) + "-" + endTime.substring(11, 16); // Extract hours from timestamp

                        // Print the course details
                        System.out.printf("%-10d %-10s %-40s %-5s %-10s %-10s %-10s%n",
                                crn, cprefix + cno, ctitle, days, time, room, instructor);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Database connection error.");
        }

    }

    private static void seeFeeDetail(Scanner scanner,String username, String semester) {
        String termInput = semester;

        if (termInput.length() < 4) {
            System.out.println("Error: Invalid term format. Please enter in the format 'SPYYYY' or 'FAYYYY'.");
            return;
        }

        String termType = termInput.substring(0, 2);
        String year = termInput.substring(2);

        String termName;

        if ("SP".equalsIgnoreCase(termType)) {
            termName = "SPRING " + year;
        } else if ("FA".equalsIgnoreCase(termType)) {
            termName = "FALL " + year;
        } else {
            System.out.println("Error: Invalid term type. Please enter 'SP' for Spring or 'FA' for Fall.");
            return;
        }
        System.out.println("Term: " + termName);
        // Connect to the MySQL database to fetch the fee based on the term
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://mysql:3306/mydatabase", "user", "userpassword")) {
            // Query to retrieve fee details for the specified student
            String feeDetailsQuery = "SELECT f.Amount, f.installments, f.feeName, f.financialAidAmount, f.inStateOrOutOfState " +
                    "FROM FEES f " +
                    "JOIN STUDENTINFO s ON s.feeID = f.FeeID " +
                    "WHERE s.sid = ?";

            try (PreparedStatement stmt = conn.prepareStatement(feeDetailsQuery)) {
                stmt.setString(1, username); // Use the input username as SID
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    double amount = rs.getDouble("Amount");
                    int installments = rs.getInt("installments");
                    String feeName = rs.getString("feeName");
                    double financialAidAmount = rs.getDouble("financialAidAmount");
                    int inStateOrOutOfState = rs.getInt("inStateOrOutOfState");

                    // Display fee details
                    System.out.printf("Fee Details for SID: %s%n", username);
                    System.out.printf("Amount: %.2f%n", amount);
                    System.out.printf("Installments: %d%n", installments);
                    System.out.printf("Fee Name: %s%n", feeName);
                    System.out.printf("Financial Aid Amount: %.2f%n", financialAidAmount);
                    System.out.printf("Status: %s%n", (inStateOrOutOfState == 1 ? "In-State" : "Out-State"));
                } else {
                    System.out.println("No fee information found for the specified SID.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Database connection error.");
        }
    }

    private static void seeTranscript(Scanner scanner, String username) {
        System.out.println("Transcript:");

        // Connect to the MySQL database
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://mysql:3306/mydatabase", "user", "userpassword")) {
            // Updated SQL query to get the transcript details including cCredits
            String query = "SELECT e.term, e.GPA AS semesterGPA, e.crn, s.cprefix, s.cno, c.ctitle, c.cCredits " +
                    "FROM ENROLLMENT e " +
                    "JOIN SECTIONS s ON e.crn = s.crn AND e.term = e.term " +
                    "JOIN Courses c ON s.cprefix = c.cprefix AND s.cno = c.cno " +
                    "WHERE e.sid = ?";

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username); // Using the logged-in username as SID
                ResultSet rs = stmt.executeQuery();

                Map<String, List<CourseData>> termCourses = new HashMap<>();
                double totalGPA = 0.0;
                int termCount = 0;

                while (rs.next()) {
                    String term = rs.getString("term");
                    double gpa = rs.getDouble("semesterGPA");
                    String cprefix = rs.getString("cprefix");
                    int cno = rs.getInt("cno");
                    String ctitle = rs.getString("ctitle");
                    int crn = rs.getInt("crn");
                    int cCredits = rs.getInt("cCredits");

                    // Store courses by term
                    termCourses.computeIfAbsent(term, k -> new ArrayList<>()).add(new CourseData(cprefix, cno, ctitle, crn, cCredits, gpa));
                    totalGPA += gpa; // Accumulate GPA for average calculation
                    termCount++;
                }

                // Display the transcript
                for (Map.Entry<String, List<CourseData>> entry : termCourses.entrySet()) {
                    String term = entry.getKey();
                    List<CourseData> courses = entry.getValue();

                    System.out.println(term);
                    System.out.printf("%-20s %-50s %-10s %-10s%n", "Course", "Title", "Credits", "GPA");
                    System.out.println("---------------------------------------------------------------------------------------------");
                    for (CourseData course : courses) {
                        System.out.printf("%-20s %-50s %-10d %-10.2f%n",
                                course.cprefix+course.cno, course.ctitle, course.cCredits, course.gpa);
                    }
                    double semesterGPA = courses.stream().mapToDouble(c -> c.gpa).average().orElse(0.0);
                    System.out.printf("Semester GPA: %.2f%n", semesterGPA);
                    System.out.println();
                }

                double overallGPA = termCount > 0 ? totalGPA / termCount : 0.0;
                System.out.printf("Overall GPA: %.2f%n", overallGPA);

            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Database connection error.");
        }
    }

    private static class CourseData {
        String cprefix;
        int cno;
        String ctitle;
        int crn;
        int cCredits; // Added field for credits
        double gpa;

        CourseData(String cprefix, int cno, String ctitle, int crn, int cCredits, double gpa) {
            this.cprefix = cprefix;
            this.cno = cno;
            this.ctitle = ctitle;
            this.crn = crn;
            this.cCredits = cCredits; // Initialize credits
            this.gpa = gpa;
        }
    }

}
