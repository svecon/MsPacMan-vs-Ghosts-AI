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
import java.util.Map;

/**
 * TODO: Implement DEAPTH-FIRST-SEARCH
 *
 * @author Jimmy
 */
public class DFS implements IPathFinder {

    Graph graph;
    Node start;
    Node goal;

    ArrayList<Node> closedList;
    ArrayDeque<Node> openedList;
    Map<Node, Node> parentMap;

    int steps;

    boolean isFinished;
    boolean isPathFound;
    boolean isRunning;

    Path path;

    public DFS() {
        closedList = new ArrayList<>();
        openedList = new ArrayDeque<>();
        parentMap = new HashMap<>();
    }

    @Override
    public void init(Graph graph, Node start, Node goal) {
        this.graph = graph;
        this.start = start;
        this.goal = goal;
        this.isRunning = true;

        openedList.add(start);
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

        Node curr = openedList.pop();

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
                // do nothing?
            } else {
                openedList.addFirst(next);
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
        return "DFS[" + getSteps() + "]";
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
        closedList.clear();
        parentMap.clear();
    }

}
