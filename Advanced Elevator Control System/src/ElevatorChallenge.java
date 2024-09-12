import java.util.*;

// Main class to simulate the elevator system
public class ElevatorChallenge {
    public static void main(String[] args) throws InterruptedException {
        ElevatorManager manager = new ElevatorManager(3);  // 3 elevators in the system
        manager.assignRequest(0, 5, 1);  // Call elevator from floor 0 to 5 with high priority
        manager.assignRequest(2, 7, 2);
        manager.assignRequest(1, 3, 3);

        manager.startAllElevators();  // Start all elevators
    }
}
