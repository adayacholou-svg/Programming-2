import java.util.*;

public class ParkingManagementSystem {
    // Data structures
    private static List<Student> students = new ArrayList<>();
    private static List<ParkingSlot> parkingSlots = new ArrayList<>();
    private static List<String> pendingStudentIds = new ArrayList<>();
    private static List<String> approvedStudentIds = new ArrayList<>();
    private static Student currentStudent = null;
    
    private static final String STAFF_USERNAME = "staff";
    private static final String STAFF_PASSWORD = "admin123";
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        initializeData();
        mainMenu();
    }
    
    private static void initializeData() {
        initializeParkingSlots();
        initializeSampleStudents();
        
        // Sample Student IDs
        pendingStudentIds.addAll(Arrays.asList("2023001", "2023002", "2023003"));
        approvedStudentIds.addAll(Arrays.asList("2021001", "2021002"));
        
        System.out.println("=====================================");
        System.out.println("  PHINMA COC BUSINESS CENTER");
        System.out.println("     PARKING MANAGEMENT SYSTEM");
        System.out.println("=====================================\n");
    }
    
    private static void initializeParkingSlots() {
        parkingSlots.add(new ParkingSlot("A1", "Faculty Parking", 10));
        parkingSlots.add(new ParkingSlot("A2", "Student Parking - Lot A", 20));
        parkingSlots.add(new ParkingSlot("B1", "Student Parking - Lot B", 15));
        parkingSlots.add(new ParkingSlot("C1", "Visitor Parking", 5));
        parkingSlots.add(new ParkingSlot("D1", "Motorcycle Parking", 25));
    }
    
    private static void initializeSampleStudents() {
        students.add(new Student("2021001", "john_doe", "pass123", null));
        students.add(new Student("2021002", "jane_smith", "pass456", null));
    }
    
    private static void mainMenu() {
        int choice;
        do {
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("1. Student Login");
            System.out.println("2. Student Registration (Requires Approved Student ID)");
            System.out.println("3. Staff Login");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");
            choice = getIntInput();
            
            switch (choice) {
                case 1: studentLogin(); break;
                case 2: studentRegisterWithId(); break;
                case 3: staffLogin(); break;
                case 4: System.out.println("Thank you!"); break;
                default: System.out.println("Invalid choice!");
            }
        } while (choice != 4);
    }
    
    // ========== AUTHENTICATION ==========
    private static void studentLogin() {
        System.out.print("\nEnter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        
        currentStudent = login(username, password);
        if (currentStudent != null) {
            System.out.println("✅ Login successful! Welcome, " + username + "!");
            studentMenu();
        } else {
            System.out.println("❌ Invalid credentials!");
        }
    }
    
    private static Student login(String username, String password) {
        for (Student student : students) {
            if (student.getUsername().equals(username) && 
                student.getPassword().equals(password)) {
                return student;
            }
        }
        return null;
    }
    
    private static void studentRegisterWithId() {
        System.out.println("\n=== STUDENT REGISTRATION ===");
        viewAvailableStudentIds();
        
        System.out.print("Enter your Student ID: ");
        String studentId = scanner.nextLine();
        
        if (!approvedStudentIds.contains(studentId)) {
            System.out.println("❌ Student ID '" + studentId + "' not approved!");
            System.out.println("Contact admin to approve your ID.");
            return;
        }
        
        for (Student s : students) {
            if (s.getStudentId().equals(studentId)) {
                System.out.println("❌ Student ID already registered!");
                return;
            }
        }
        
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        
        for (Student s : students) {
            if (s.getUsername().equals(username)) {
                System.out.println("❌ Username already taken!");
                return;
            }
        }
        
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        
        students.add(new Student(studentId, username, password, null));
        System.out.println("✅ Registration successful for ID: " + studentId);
    }
    
    private static void viewAvailableStudentIds() {
        System.out.println("\n--- APPROVED Student IDs (Can Register) ---");
        if (approvedStudentIds.isEmpty()) {
            System.out.println("None");
        } else {
            for (int i = 0; i < approvedStudentIds.size(); i++) {
                System.out.println((i+1) + ". " + approvedStudentIds.get(i));
            }
        }
        
        System.out.println("\n--- PENDING Student IDs ---");
        if (pendingStudentIds.isEmpty()) {
            System.out.println("None");
        } else {
            for (int i = 0; i < pendingStudentIds.size(); i++) {
                System.out.println((i+1) + ". " + pendingStudentIds.get(i));
            }
        }
    }
    
    private static void staffLogin() {
        System.out.print("\nStaff username: ");
        String username = scanner.nextLine();
        System.out.print("Staff password: ");
        String password = scanner.nextLine();
        
        if (STAFF_USERNAME.equals(username) && STAFF_PASSWORD.equals(password)) {
            System.out.println("✅ Staff login successful!");
            staffMenu();
        } else {
            System.out.println("❌ Invalid credentials!");
        }
    }
    
    // ========== STUDENT MENU ==========
    private static void studentMenu() {
        int choice;
        do {
            System.out.println("\n=== STUDENT MENU ===");
            System.out.println("1. View Available Slots");
            System.out.println("2. Book Slot");
            System.out.println("3. Cancel Booking");
            System.out.println("4. View My Slot");
            System.out.println("5. Logout");
            System.out.print("Choice: ");
            choice = getIntInput();
            
            switch (choice) {
                case 1: viewSlots(); break;
                case 2: bookSlot(); break;
                case 3: cancelBooking(); break;
                case 4: viewCurrentBooking(); break;
                case 5: 
                    currentStudent = null;
                    System.out.println("Logged out!");
                    break;
                default: System.out.println("Invalid!");
            }
        } while (choice != 5);
    }
    
    // ========== PARKING OPERATIONS ==========
    private static void viewSlots() {
        System.out.println("\n=== PARKING SLOTS STATUS ===");
        System.out.printf("%-8s %-25s %-10s %-10s %-10s%n", 
            "SlotID", "Area", "Capacity", "Occupied", "Status");
        System.out.println("----------------------------------------------------------");
        
        for (ParkingSlot slot : parkingSlots) {
            String status = slot.isFull() ? "FULL" : "AVAILABLE";
            System.out.printf("%-8s %-25s %-10d %-10d %-10s%n", 
                slot.getSlotId(), slot.getArea(), 
                slot.getCapacity(), slot.getCurrentCount(), status);
        }
    }
    
    private static void bookSlot() {
        if (currentStudent.getCurrentSlot() != null) {
            System.out.println("You already have booking: " + currentStudent.getCurrentSlot());
            return;
        }
        
        viewSlots();
        System.out.print("\nEnter slot ID: ");
        String slotId = scanner.nextLine().toUpperCase();
        
        ParkingSlot slot = findSlotById(slotId);
        if (slot == null) {
            System.out.println("❌ Invalid slot ID!");
            return;
        }
        
        if (slot.isFull()) {
            System.out.println("❌ Slot is FULL!");
            return;
        }
        
        for (Student student : students) {
            if (student != currentStudent && slotId.equals(student.getCurrentSlot())) {
                System.out.println("❌ Slot booked by another student!");
                return;
            }
        }
        
        slot.incrementCount();
        currentStudent.setCurrentSlot(slotId);
        System.out.println("✅ Booking successful! Slot: " + slotId);
    }
    
    private static void cancelBooking() {
        if (currentStudent.getCurrentSlot() == null) {
            System.out.println("No active booking!");
            return;
        }
        
        ParkingSlot slot = findSlotById(currentStudent.getCurrentSlot());
        if (slot != null) {
            slot.decrementCount();
        }
        currentStudent.setCurrentSlot(null);
        System.out.println("✅ Booking cancelled!");
    }
    
    private static void viewCurrentBooking() {
        if (currentStudent.getCurrentSlot() == null) {
            System.out.println("No active booking.");
        } else {
            ParkingSlot slot = findSlotById(currentStudent.getCurrentSlot());
            System.out.println("Your slot: " + currentStudent.getCurrentSlot());
            if (slot != null) {
                System.out.println("Area: " + slot.getArea());
            }
        }
    }
    
    // ========== STAFF PANEL ==========
    private static void staffMenu() {
        int choice;
        do {
            System.out.println("\n=== STAFF PANEL ===");
            System.out.println("1. View All Students");
            System.out.println("2. View Reservations");
            System.out.println("3. View Parking Status");
            System.out.println("4. Student ID Management");
            System.out.println("5. Add Student ID");
            System.out.println("6. Approve Pending IDs");
            System.out.println("7. Back");
            System.out.print("Choice: ");
            choice = getIntInput();
            
            switch (choice) {
                case 1: viewAllStudents(); break;
                case 2: viewAllReservations(); break;
                case 3: viewSlots(); break;
                case 4: studentIdManagement(); break;
                case 5: addStudentId(); break;
                case 6: approvePendingIds(); break;
                case 7: return;
                default: System.out.println("Invalid!");
            }
        } while (choice != 7);
    }
    
    private static void viewAllStudents() {
        System.out.println("\n=== ALL STUDENTS ===");
        System.out.printf("%-12s %-15s %-10s %-15s%n", 
            "Student ID", "Username", "Status", "Current Slot");
        System.out.println("------------------------------------------------");
        
        for (Student student : students) {
            String status = student.getCurrentSlot() != null ? "PARKED" : "FREE";
            System.out.printf("%-12s %-15s %-10s %-15s%n", 
                student.getStudentId(), student.getUsername(), status, 
                student.getCurrentSlot() != null ? student.getCurrentSlot() : "-");
        }
    }
    
    private static void viewAllReservations() {
        System.out.println("\n=== ACTIVE RESERVATIONS ===");
        boolean hasBookings = false;
        for (Student student : students) {
            if (student.getCurrentSlot() != null) {
                ParkingSlot slot = findSlotById(student.getCurrentSlot());
                System.out.printf("ID:%s (%s) → %s (%s)%n", 
                    student.getStudentId(), student.getUsername(),
                    student.getCurrentSlot(), 
                    slot != null ? slot.getArea() : "Unknown");
                hasBookings = true;
            }
        }
        if (!hasBookings) System.out.println("No active reservations.");
    }
    
    // ========== STUDENT ID MANAGEMENT ==========
    private static void addStudentId() {
        System.out.print("\nEnter new Student ID (7 digits): ");
        String newId = scanner.nextLine();
        
        if (!newId.matches("\\d{7}")) {
            System.out.println("❌ Use 7 digits (e.g., 2023004)!");
            return;
        }
        
        if (approvedStudentIds.contains(newId) || pendingStudentIds.contains(newId)) {
            System.out.println("❌ ID already exists!");
            return;
        }
        
        pendingStudentIds.add(newId);
        System.out.println("✅ '" + newId + "' added to PENDING!");
    }
    
    private static void approvePendingIds() {
        if (pendingStudentIds.isEmpty()) {
            System.out.println("No pending IDs!");
            return;
        }
        
        System.out.println("\n--- PENDING IDs ---");
        for (int i = 0; i < pendingStudentIds.size(); i++) {
            System.out.println((i+1) + ". " + pendingStudentIds.get(i));
        }
        
        System.out.print("Approve which? (0=cancel): ");
        int choice = getIntInput();
        if (choice == 0) return;
        if (choice < 1 || choice > pendingStudentIds.size()) {
            System.out.println("Invalid!");
            return;
        }
        
        String approvedId = pendingStudentIds.remove(choice - 1);
        approvedStudentIds.add(approvedId);
        System.out.println("✅ '" + approvedId + "' APPROVED!");
    }
    
    private static void studentIdManagement() {
        System.out.println("\n=== ID MANAGEMENT ===");
        System.out.println("1. View All IDs");
        System.out.println("2. Delete Pending");
        System.out.println("3. Delete Approved");
        System.out.println("4. Back");
        int choice = getIntInput();
        
        switch (choice) {
            case 1: viewAvailableStudentIds(); break;
            case 2: deletePendingId(); break;
            case 3: deleteApprovedId(); break;
        }
    }
    
    private static void deletePendingId() {
        if (pendingStudentIds.isEmpty()) {
            System.out.println("No pending IDs!");
            return;
        }
        System.out.println("\n--- PENDING ---");
        for (int i = 0; i < pendingStudentIds.size(); i++) {
            System.out.println((i+1) + ". " + pendingStudentIds.get(i));
        }
        System.out.print("Delete which? ");
        int choice = getIntInput();
        if (choice > 0 && choice <= pendingStudentIds.size()) {
            String deleted = pendingStudentIds.remove(choice - 1);
            System.out.println("❌ '" + deleted + "' deleted!");
        }
    }
    
    private static void deleteApprovedId() {
        if (approvedStudentIds.isEmpty()) {
            System.out.println("No approved IDs!");
            return;
        }
        System.out.println("\n--- APPROVED ---");
        for (int i = 0; i < approvedStudentIds.size(); i++) {
            System.out.println((i+1) + ". " + approvedStudentIds.get(i));
        }
        System.out.print("Delete which? ");
        int choice = getIntInput();
        if (choice > 0 && choice <= approvedStudentIds.size()) {
            String deleted = approvedStudentIds.remove(choice - 1);
            System.out.println("❌ '" + deleted + "' deleted!");
        }
    }
    
    // ========== UTILITY METHODS ==========
    private static ParkingSlot findSlotById(String slotId) {
        for (ParkingSlot slot : parkingSlots) {
            if (slot.getSlotId().equals(slotId)) {
                return slot;
            }
        }
        return null;
    }
    
    private static int getIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.print("Enter number: ");
            scanner.next();
        }
        int input = scanner.nextInt();
        scanner.nextLine();
        return input;
    }
    
    // ========== CLASSES ==========
    static class Student {
        private String studentId, username, password, currentSlot;
        public Student(String studentId, String username, String password, String currentSlot) {
            this.studentId = studentId; this.username = username;
            this.password = password; this.currentSlot = currentSlot;
        }
        public String getStudentId() { return studentId; }
        public String getUsername() { return username; }
        public String getPassword() { return password; }
        public String getCurrentSlot() { return currentSlot; }
        public void setCurrentSlot(String currentSlot) { this.currentSlot = currentSlot; }
    }
    
    static class ParkingSlot {
        private String slotId, area; 
        private int capacity, currentCount = 0;
        public ParkingSlot(String slotId, String area, int capacity) {
            this.slotId = slotId; this.area = area; this.capacity = capacity;
        }
        public String getSlotId() { return slotId; }
        public String getArea() { return area; }
        public int getCapacity() { return capacity; }
        public int getCurrentCount() { return currentCount; }
        public boolean isFull() { return currentCount >= capacity; }
        public void incrementCount() { if (!isFull()) currentCount++; }
        public void decrementCount() { if (currentCount > 0) currentCount--; }
    }
}