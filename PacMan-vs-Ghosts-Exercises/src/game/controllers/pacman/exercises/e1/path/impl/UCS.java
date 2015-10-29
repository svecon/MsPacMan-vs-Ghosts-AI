package game.controllers.pacman.exercises.e1.path.impl;

import game.controllers.Direction;
import java.util.Collection;

import game.controllers.pacman.exercises.e1.graph.Graph;
import game.controllers.pacman.exercises.e1.graph.Node;
import game.controllers.pacman.exercises.e1.graph.Link;
import game.controllers.pacman.exercises.e1.path.IPathFinder;
import game.controllers.pacman.exercises.e1.path.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * TODO: Implement UNIFORM-COST-SEARCH
 *
 * @author Jimmy
 */
public class UCS implements IPathFinder {

    Graph graph;
    Node start;
    Node goal;

    ArrayList<Node> closedList;
    LinkedList<Node> openedList;
    LinkedList<Integer> openedLengths;
    Map<Node, Node> parentMap;

    int steps;

    boolean isFinished;
    boolean isPathFound;
    boolean isRunning;

    Path path;

    public UCS() {
        closedList = new ArrayList<>();
        openedList = new LinkedList<>();
        openedLengths = new LinkedList<>();
        parentMap = new HashMap<>();
    }

    @Override
    public void init(Graph graph, Node start, Node goal) {
        this.graph = graph;
        this.start = start;
        this.goal = goal;
        this.isRunning = true;

        openedLengths.add(0);
        openedList.add(start);
    }

    private void bubbleDown(int i) {
        Node temp = openedList.get(i);
        int tempLength = openedLengths.get(i);
        
        while (i > 0 && openedLengths.get(i - 1) > tempLength) {
            openedLengths.set(i, openedLengths.get(i - 1));
            openedList.set(i, openedList.get(i - 1));
            --i;
        }
        openedList.set(i, temp);
        openedLengths.set(i, tempLength);
    }

    @Override
    public void step() {
        if (this.isFinished()) {
            return;
        }

        if (openedList.isEmpty()) {
            this.isFinished = true;
            this.isRunning = false;
            return;
        }
        
        Node curr = openedList.remove(0);
        int currLength = openedLengths.remove(0);

        for (Map.Entry<Direction, Link> item : curr.links.entrySet()) {
            Direction dir = item.getKey();
            Link link = item.getValue();

            Node next = link.n1 == curr ? link.n2 : link.n1;

            if (next == goal) {
                parentMap.put(next, curr);
                createPath();
                return;
            }

            if (closedList.contains(next)) {
                // do nothing or reopen?
            } else if (openedList.contains(next)) {
                int newDist = currLength + link.distance;

                int i = openedList.indexOf(next);

                if (newDist < openedLengths.get(i)) {
                    openedLengths.set(i, newDist);
                    bubbleDown(i);
                }

            } else {
                int newDist = currLength + link.distance;
                openedList.add(next);
                openedLengths.add(newDist);

                bubbleDown(openedList.size() - 1);

                parentMap.put(next, curr);
            }

        }

        closedList.add(curr);
        steps++;
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }

    @Override
    public boolean isPathFound() {
        return isPathFound;
    }

    private void createPath() {
        this.isFinished = true;
        this.isPathFound = true;
        this.isRunning = false;

        Node curr = this.goal;
        // create path
        ArrayDeque<Node> foundPath = new ArrayDeque<>();
        foundPath.addFirst(curr);

        while (parentMap.containsKey(curr)) {
            curr = parentMap.get(curr);
            foundPath.addFirst(curr);
        }

        path = new Path(foundPath.toArray(new Node[0]));
    }

    @Override
    public Path getPath() {
        if (!this.isFinished()) {
            return null;
        }

        return path;
    }

    @Override
    public Node getParent(Node node) {
        if (parentMap.containsKey(node)) {
            return parentMap.get(node);
        }

        return null;
    }

    @Override
    public Collection<Node> getClosedList() {
        return closedList;
    }

    @Override
    public Collection<Node> getOpenList() {
        return openedList;
    }

    @Override
    public int getSteps() {
        return steps;
    }

    @Override
    public Node getStart() {
        return start;
    }

    @Override
    public Node getGoal() {
        return goal;
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public String getName() {
        return "UCS[" + getSteps() + "]";
    }

    @Override
    public void reset() {
        steps = 0;
        isFinished = false;
        isPathFound = false;
        isRunning = false;

        goal = null;
        start = null;
        graph = null;
        path = null;

        openedList.clear();
        openedLengths.clear();
        closedList.clear();
        parentMap.clear();
    }

}
