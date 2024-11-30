# Advanced Java Project

XU Ziyang S1P1/2024-2025

GitHub repository: https://github.com/Ziyang-Xu/advance-java-robot.git (since the last modifications are long ago and I barely remember the solution I made, so I simply made a new one for recapping and continuity)

## Task 1

### Installing and Running the Robotic Factory Simulator

>Open the class file to modify the code between lines 66 and 77 so that both robots must visit Machine 1 first and then Machine 2. Run the simulator again. What do you observe?

``` java
final Robot robot1 = new Robot(factory, jgraphPahtFinder, new CircularShape(5, 5, 2), new Battery(10), "Robot 1");
		robot1.addTargetComponent(machine1);
		robot1.addTargetComponent(machine2);
		robot1.addTargetComponent(new Conveyor(factory, conveyorShape, "Conveyor 1"));
		robot1.addTargetComponent(chargingStation);

		final FactoryPathFinder customPathFinder = new CustomDijkstraFactoryPathFinder(factory, 5);
		final Robot robot2 = new Robot(factory, customPathFinder, new CircularShape(45, 5, 2), new Battery(10), "Robot 2");
		// robot2.addTargetComponent(chargingStation);
		robot2.addTargetComponent(machine1);
		robot2.addTargetComponent(machine2);
		robot2.addTargetComponent(new Conveyor(factory, conveyorShape, "Conveyor 1"));
```

The above is the modification, and the observation is shown below:

![image-20241128114752994](/Users/Chaleur/Library/Application Support/typora-user-images/image-20241128114752994.png)

We can see that the two robotics encounter a live lock.

### Removing the Livelock

I changed a little bit of the original code provided in the guide:

``` java
@Override
	public Position getNextPosition() {
		return nextPosition;
	}

	@Override
	public boolean isLivelyLocked() {
		final Position nextPosition = getNextPosition();
		if (nextPosition == null) {
			return false;
		}
		final Component otherRobot = getFactory().getMobileComponentAt(nextPosition, this);
		return otherRobot != null && getPosition().equals(otherRobot.getNextPosition());
	}

	private int moveToNextPathPosition() {
		int displacement = 0;
		while (true) {
			final Motion motion = computeMotion();
			displacement = motion == null ? 0 : motion.moveToTarget();

			if (displacement != 0) {
				notifyObservers();
				break;
			} else if (isLivelyLocked()) {
				Position freeNeighbouringPosition;
				do {
					freeNeighbouringPosition = findFreeNeighbouringPosition();
					if (freeNeighbouringPosition != null) {
						nextPosition = freeNeighbouringPosition;
						computePathToCurrentTargetComponent();
						displacement = moveToNextPathPosition();
					}
				} while (isLivelyLocked() && freeNeighbouringPosition != null);
			} else {
				break;
			}
		}
		return displacement;
	}

	private Position findFreeNeighbouringPosition() {
		Position currentPosition = getPosition();
		List<Position> possiblePositions = List.of(
				new Position(currentPosition.getxCoordinate(), currentPosition.getyCoordinate() + 2 * this.getWidth()),
				new Position(currentPosition.getxCoordinate(), currentPosition.getyCoordinate() - 2 * this.getWidth()),
				new Position(currentPosition.getxCoordinate() - 2 * this.getWidth(), currentPosition.getyCoordinate()),
				new Position(currentPosition.getxCoordinate() + 2 * this.getWidth(), currentPosition.getyCoordinate()));

		for (Position pos : possiblePositions) {
			if (getFactory().getMobileComponentAt(pos, this) == null) {
				return pos;
			}
		}
		return null;
	}
```

to make the calculation of `nextPosition` in the loop of `isLivelyLocked` to prevent the case such that a robot makes a compromise but still in the live lock.

### Making the Factory Components Execution Parallel

After making the robots behave in a multi-threaded way, I made a lot of change to the existing code, except for the added one to implement runnable interface:

``` java
@Override
	public boolean isLivelyLocked() {
		final Position nextPosition = getNextPosition();
		if (nextPosition == null) {
			return false;
		}
		final Component otherRobot = getFactory().getMobileComponentAt(nextPosition, this);
		return otherRobot != null && getPosition().equals(otherRobot.getNextPosition());
	}

	private void findFreeNeighbouringPosition(int index) {
		Position currentPosition = getPosition();
		List<Position> possiblePositions = List.of(
				new Position(currentPosition.getxCoordinate(), currentPosition.getyCoordinate() + 2 * this.getWidth()),
				new Position(currentPosition.getxCoordinate(), currentPosition.getyCoordinate() - 2 * this.getWidth()),
				new Position(currentPosition.getxCoordinate() - 2 * this.getWidth(), currentPosition.getyCoordinate()),
				new Position(currentPosition.getxCoordinate() + 2 * this.getWidth(), currentPosition.getyCoordinate()));
		if (getFactory().getMobileComponentAt(possiblePositions.get(index), this) == null) {
			nextPosition = possiblePositions.get(index);
		}
		return;
	}

	private int moveToNextPathPosition() {
		final Motion motion = computeMotion();
		int displacement = motion == null ? 0 : motion.moveToTarget();

		if (displacement != 0) {
			notifyObservers();
		} else if (isLivelyLocked()) {
			boolean moved = false;
			while (!moved) {
				for (int index = 0; index < 4; index++) {
					findFreeNeighbouringPosition(index);
					if (nextPosition != null) {
						Motion nextMotion = computeMotion();
						if (nextMotion != null && nextMotion.moveToTarget() != 0) {
							displacement = 1;
							moved = true;
							notifyObservers();
							break;
						}
					}
				}
				if (!moved && !isLivelyLocked()) {
					break;
				}
			}
		}
		return displacement;
	}
```

In the above, instead the previous solution, I made the find free position method accept one parameter called `index` to allow manipulation from the moveToNextPathPosition instead of doing function-scale iterations.

## Task 2

### Check that Logging Works Fine

![image-20241128190528220](/Users/Chaleur/Library/Application Support/typora-user-images/image-20241128190528220.png)

>Note that the log file is written in xml format, while the logging messages are written in plain text in the console. How do you explain this difference?

XML files are for machines to read and transfer while plain texts are for human to read.

### Implement a Simple Distributed “Hello World” Software Application

RequestProcessor:

```java
package src;

import java.io.*;
import java.net.Socket;

public class RequestProcessor implements Runnable {
    private Socket socket;

    public RequestProcessor(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            InputStream inpStr = socket.getInputStream();
            Reader strReader = new InputStreamReader(inpStr);
            BufferedReader buffReader = new BufferedReader(strReader);

            // Read and decode input request
            String message = buffReader.readLine();
            System.out.println("Received message: " + message);

            OutputStream outStr = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(outStr, true); // Autoflush

            // Build and write response
            String response = "I received " + message + "!";
            writer.println(response);

        } catch (IOException e) {
            // Handle exceptions
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                // Handle exceptions
                e.printStackTrace();
            }
        }
    }
}
```

WebServer:

```java
package src;

import src.RequestProcessor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {
    public static void main(String args[]) {
        try (
                ServerSocket serverSocket = new ServerSocket(80);
        ) {
            do {
                try {
                    Socket socket = serverSocket.accept();
                    Runnable reqProcessor = new RequestProcessor(socket);
                    new Thread(reqProcessor).start();
                }
                catch (IOException ex) {
                    // - handle exceptions
                }
            } while (true);
        }
        catch (IOException ex) {
            // - handle exceptions
        }
    }
}
```

Client:

```java
package src;

import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 80)) {
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Send message
            String message = "my message";
            writer.println(message);

            // Read response
            String response = reader.readLine();
            System.out.println("Server response: " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

result:

![image-20241129155625357](/Users/Chaleur/Library/Application Support/typora-user-images/image-20241129155625357.png)

![image-20241129155633289](/Users/Chaleur/Library/Application Support/typora-user-images/image-20241129155633289.png)

### Migrate the Robotic Factory Simulator so that the Model is Persisted on a Server

As is shown in the project itself.

## Task 3

