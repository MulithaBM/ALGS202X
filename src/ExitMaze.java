import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class ExitMaze {
    private int start = -1;
    private int end = -1;
    private int connectedComponentCount = 0; // check if needed
    private HashMap<Integer, HashSet<Integer>> neighbors = new HashMap<>();
    private HashMap<Integer, HashSet<Integer>> neighborsCopy;
    private final ArrayList<ArrayList<Integer>> connectedComponents = new ArrayList<>();

    public ExitMaze () {
        handleInput();
        getConnectedComponents();
        printConnectedComponents();

        if (start > 0 && end > 0) {
            getCopy();
            System.out.println("Has exit: " + hasExit(start, end));
        }
    }

    private void handleInput() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            String[] firstLine = br.readLine().split("\\s+");

            int vertexCount = Integer.parseInt(firstLine[0].trim());
            int edgeCount = Integer.parseInt(firstLine[1].trim());

            for (int i = 1; i <= vertexCount; i++) {
                neighbors.put(i, new HashSet<>());
            }

            for (int i = 0; i < edgeCount; i++) {
                String[] edge = br.readLine().split("\\s+");

                int v1 = Integer.parseInt(edge[0].trim());
                int v2 = Integer.parseInt(edge[1].trim());

                neighbors.get(v1).add(v2);
                neighbors.get(v2).add(v1);
            }

            neighborsCopy = new HashMap<>(neighbors);

            if (br.ready()) {
                String[] startEndLine = br.readLine().split("\\s+");

                start = Integer.parseInt(startEndLine[0].trim());
                end = Integer.parseInt(startEndLine[1].trim());
            }

        } catch (IOException e) {
            System.out.println("Error reading input: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input format: " + e.getMessage());
        }
    }

    private boolean hasExit (int start, int end) {
        if (start == end) {
            return true;
        }

        HashSet<Integer> localNeighbors = neighbors.get(start);
        removeVertex(start);

        for (Integer neighbor : localNeighbors) {
            if (neighbor == end || hasExit(neighbor, end)) {
                return true;
            }
        }

        return false;
    }

    private void getConnectedComponents() {
        while (!neighbors.isEmpty()) {
            connectedComponents.add(new ArrayList<>());

            int vertex = neighbors.keySet().iterator().next();

            dfs(vertex);

            connectedComponentCount++;
        }
    }

    private void dfs(int vertex) {
        if (neighbors.containsKey(vertex)) {
            connectedComponents.get(connectedComponentCount).add(vertex);

            HashSet<Integer> localNeighbors = neighbors.get(vertex);

            removeVertex(vertex);

            if (localNeighbors == null) {
                return;
            }

            for (int neighbor : localNeighbors) {
                dfs(neighbor);
            }
        }
    }

    private void printConnectedComponents () {
        System.out.println("No. of connected components: " + connectedComponentCount);

        System.out.println("Connected components:");

        for (ArrayList<Integer> connectedComponent : connectedComponents) {
            String componentString = connectedComponent.stream()
                    .map(String::valueOf)
                    .reduce((x, y) -> x + "," + y)
                    .orElse("");

            System.out.printf("[ %s ]\n", componentString);
        }
    }

    private void removeVertex(int vertex) {
        HashSet<Integer> localNeighbors = neighbors.get(vertex);
        neighbors.remove(vertex);

        for (int neighbor : localNeighbors) {
            neighbors.get(neighbor).remove(vertex);
        }
    }

    private void getCopy() {
        neighbors = new HashMap<>(neighborsCopy);
    }
}
