import java.util.*;

// Enhanced Elevator System
public class Elevator {
    private static final int MIN_FLOOR = 0;
    private static final int MAX_FLOOR = 10;
    private static int processingTime = 500; // ms
    private int currentFloor;
    private Direction currentDirection;
    private boolean[] currentFloorDestinations;  // Tracks floor destinations
    private PriorityQueue<Request> requestQueue; // Priority-based requests

    public Elevator() {
        this.currentFloor = 0;
        this.currentDirection = Direction.UP;
        this.currentFloorDestinations = new boolean[MAX_FLOOR + 1];
        this.requestQueue = new PriorityQueue<>(Comparator.comparingInt(Request::getPriority)); // Prioritize requests
    }

    public void start() throws InterruptedException {
        currentDirection = Direction.UP;
        do {
            System.out.println("--------");
            processFloor(currentFloor);
            System.out.println("--------");
        } while (currentDirection != Direction.IDLE);
        System.out.println("No more requests, elevator is idle.");
    }

    public void callElevator(int start, int destination, int priority) {
        if (isInvalidFloor(start) || isInvalidFloor(destination) || start == destination) {
            System.out.println("INVALID FLOORS. Try again");
            return;
        }
        requestQueue.add(new Request(start, destination, priority));  // Add prioritized requests
    }

    private void processFloor(int floor) throws InterruptedException {
        if (currentFloorDestinations[floor]) {
            System.out.println("UN-BOARDING at Floor: " + floor);
        }

        // Process requests at the current floor
        Request currentRequest = requestQueue.peek();
        if (currentRequest != null && currentRequest.getStartFloor() == floor) {
            System.out.println("BOARDING at Floor: " + floor);
            currentFloorDestinations[currentRequest.getDestinationFloor()] = true;
            requestQueue.poll();  // Remove request after boarding
        }

        currentFloorDestinations[floor] = false;  // Done with this floor
        moveElevator();
    }

    private void moveElevator() throws InterruptedException {
        if (!Arrays.asList(currentFloorDestinations).contains(true) && requestQueue.isEmpty()) {
            currentDirection = Direction.IDLE;
            return;
        } else if (isInvalidFloor(currentFloor + 1)) {
            currentDirection = Direction.DOWN;
        } else if (isInvalidFloor(currentFloor - 1)) {
            currentDirection = Direction.UP;
        }

        switch (currentDirection) {
            case UP:
                moveUp();
                break;
            case DOWN:
                moveDown();
                break;
            default:
                System.out.println("Elevator Malfunctioned");
        }
    }

    private void moveUp() throws InterruptedException {
        currentFloor++;
        System.out.println("GOING UP TO " + currentFloor);
        Thread.sleep(processingTime);
    }

    private void moveDown() throws InterruptedException {
        currentFloor--;
        System.out.println("GOING DOWN TO " + currentFloor);
        Thread.sleep(processingTime);
    }

    private boolean isInvalidFloor(int floor) {
        return floor < MIN_FLOOR || floor > MAX_FLOOR;
    }
}

class Request {
    private final int startFloor;
    private final int destinationFloor;
    private final int priority;  // Lower values mean higher priority

    public Request(int start, int destination, int priority) {
        this.startFloor = start;
        this.destinationFloor = destination;
        this.priority = priority;
    }

    public int getStartFloor() {
        return startFloor;
    }

    public int getDestinationFloor() {
        return destinationFloor;
    }

    public int getPriority() {
        return priority;
    }
}

// ElevatorManager to handle multiple elevators
class ElevatorManager {
    private List<Elevator> elevators;

    public ElevatorManager(int numElevators) {
        elevators = new ArrayList<>(numElevators);
        for (int i = 0; i < numElevators; i++) {
            elevators.add(new Elevator());
        }
    }

    public void assignRequest(int start, int destination, int priority) {
        // Assign request to the nearest available elevator
        Elevator bestElevator = findBestElevator(start);
        bestElevator.callElevator(start, destination, priority);
    }

    private Elevator findBestElevator(int startFloor) {
        // Simple heuristic to find the nearest elevator (could be improved)
        return elevators.get(0);  // Placeholder, logic to find best elevator can be added
    }

    public void startAllElevators() throws InterruptedException {
        for (Elevator elevator : elevators) {
            elevator.start();
        }
    }
}
