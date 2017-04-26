package tests;

import core.exception.FloorNotReachableException;
import core.exception.NearestNotFoundException;
import core.exception.PathNotFoundException;
import core.exception.WrongFloorException;
import models.path.Node;
import models.path.Path;
import models.path.PathfindingManager;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by mattm on 4/4/2017.
 */
public class PathfindingTests {

    public PathfindingTests() {}

    private PathfindingManager pm1;
    private PathfindingManager pm2;

    @Before
    public void setup1() {
        HashMap<Integer, Node> n = new HashMap<>();
        Node n1 = new Node(1, 1, 1, 4, false, "NONE");
        Node n2 = new Node(2, 2, 2, 4, false, "NONE");
        Node n3 = new Node(3, 3, 3, 4, false, "NONE");
        Node n4 = new Node(4,4, 4, 4, false, "NONE");
        Node n5 = new Node(5,5,5,5, false,"NONE");
        Node n6 = new Node(6,6,6,6, false,"NONE");
        try {
            n1.addConnection(n2);
            n2.addConnection(n3);
        } catch (WrongFloorException e) {
            // TODO
        }
        n.put(1, n1);
        n.put(2, n2);
        n.put(3, n3);
        n.put(4, n4);
        n.put(5, n5);
        n.put(6,n6);
        this.pm1 = new PathfindingManager(n);
    }

    @Before
    public void setup2() {
        HashMap<Integer, Node> n = new HashMap<>();
        Node n1  = new Node(1,  1, 1, 4, false, "NONE");
        Node n2  = new Node(2,  1, 2, 4, false, "NONE");
        Node n3  = new Node(3,  1, 3, 4, false, "NONE");
        Node n4  = new Node(4,  1, 4, 4, false, "NONE");
        Node n5  = new Node(5,  2, 5, 4, false, "NONE");
        Node n6  = new Node(6,  3, 6, 4, false, "NONE");
        Node n7  = new Node(7,  4, 7, 4, false, "NONE");
        Node n8  = new Node(8,  5, 7, 4, false, "NONE");
        Node n9  = new Node(9,  6, 7, 4, false, "NONE");
        Node n10 = new Node(10, 2, 3, 4, false, "NONE");
        Node n11 = new Node(11, 3, 3, 4, false, "NONE");
        Node n12 = new Node(12, 7, 7, 4, false, "NONE");
        Node n13 = new Node(13, 3, 4, 4, false, "NONE");
        Node n14 = new Node(14, 3, 5, 4, false, "NONE");
        Node n15 = new Node(15, 3, 7, 4, false, "NONE");
        Node n16 = new Node(16, 2, 7, 4, false, "NONE");
        Node n17 = new Node(17, 6, 6, 4, false, "NONE");
        Node n18 = new Node(18, 5, 5, 4, false, "NONE");
        Node n19 = new Node(19, 5, 4, 4, false, "NONE");
        Node n20 = new Node(20, 6, 2, 4, false, "NONE");
        Node n21 = new Node(21, 4, 2, 4, false, "NONE");
        Node n22 = new Node(22, 5, 1, 4, false, "NONE");
        Node n23 = new Node(23, 6, 4, 4, false, "NONE");
        Node n24 = new Node(24, 6, 2, 4, false, "NONE");
        Node n25 = new Node(25, 5, 2, 4, false, "NONE");
        Node n26 = new Node(26, 10, 12, 4, false, "NONE");
        Node n27 = new Node(27,11,22, 5, false,"NONE");

        n.put(1, n1); n.put(2, n2); n.put(3, n3); n.put(4, n4);
        n.put(5, n5); n.put(6, n6); n.put(7, n7); n.put(8, n8);
        n.put(9, n9); n.put(10, n10); n.put(11, n11); n.put(12, n12);
        n.put(13, n13); n.put(14, n14); n.put(15, n15); n.put(16, n16);
        n.put(17, n17); n.put(18, n18); n.put(19, n19); n.put(20, n20);
        n.put(21, n21); n.put(22, n22); n.put(23, n23); n.put(24, n24);
        n.put(25, n25);
        n.put(26, n26);
        n.put(27,n27);
        try {
            n1.addConnection(n2);
            n2.addConnection(n3);
            n3.addConnection(n4);
            n3.addConnection(n10);
            n4.addConnection(n5);
            n5.addConnection(n14);
            n6.addConnection(n7);
            n7.addConnection(n8);
            n7.addConnection(n15);
            n8.addConnection(n9);
            n9.addConnection(n12);
            n10.addConnection(n11);
            n11.addConnection(n13);
            n11.addConnection(n21);
            n13.addConnection(n14);
            n14.addConnection(n6);
            n15.addConnection(n16);
            n17.addConnection(n9);
            n18.addConnection(n17);
            n19.addConnection(n18);
            n19.addConnection(n23);
            n20.addConnection(n24);
            n21.addConnection(n22);
            n22.addConnection(n20);
            n24.addConnection(n25);
            n25.addConnection(n19);
        } catch (WrongFloorException e) {
            // TODO
        }
        pm2 = new PathfindingManager(n);
    }

    @Test
    public void testAStar1() throws PathNotFoundException, NearestNotFoundException, FloorNotReachableException  {
        pm1.selectAlgorithm("A* Search");

        Path expected = new Path();
        expected.addInOrder(pm1.getNode(1));
        expected.addInOrder(pm1.getNode(2));
        expected.addInOrder(pm1.getNode(3));
        try {
            assertEquals(expected, pm1.findPath(pm1.getNode(1), pm1.getNode(3)));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testAStar2() throws PathNotFoundException, NearestNotFoundException, FloorNotReachableException  {
        pm2.selectAlgorithm("A* Search");

        Path expected = new Path();
        expected.addInOrder(pm2.getNode(1));
        expected.addInOrder(pm2.getNode(2));
        expected.addInOrder(pm2.getNode(3));
        expected.addInOrder(pm2.getNode(4));
        expected.addInOrder(pm2.getNode(5));
        expected.addInOrder(pm2.getNode(14));
        expected.addInOrder(pm2.getNode(6));
        expected.addInOrder(pm2.getNode(7));
        expected.addInOrder(pm2.getNode(8));
        expected.addInOrder(pm2.getNode(9));
        expected.addInOrder(pm2.getNode(12));
        try {
            assertEquals(expected, pm2.findPath(pm2.getNode(1), pm2.getNode(12)));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(expected = PathNotFoundException.class)
    public void testAStar3() throws PathNotFoundException, NearestNotFoundException, FloorNotReachableException {
        pm1.selectAlgorithm("A* Search");

        pm1.findPath(pm1.getNode(1), pm1.getNode(4));
    }

    @Test(expected = PathNotFoundException.class)
    public void testAStar4() throws PathNotFoundException, NearestNotFoundException, FloorNotReachableException {
        pm2.selectAlgorithm("A* Search");

        pm2.findPath(pm2.getNode(26), pm2.getNode(1));
    }

    @Test
    public void testBFS1() throws PathNotFoundException, NearestNotFoundException, FloorNotReachableException  {
        pm1.selectAlgorithm("Breadth-First Search");

        Path expected = new Path();
        expected.addInOrder(pm1.getNode(1));
        expected.addInOrder(pm1.getNode(2));
        expected.addInOrder(pm1.getNode(3));
        try {
            assertEquals(expected, pm1.findPath(pm1.getNode(1), pm1.getNode(3)));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testBFS2() throws PathNotFoundException, NearestNotFoundException, FloorNotReachableException  {
        pm2.selectAlgorithm("Breadth-First Search");

        Path expected = new Path();
        expected.addInOrder(pm2.getNode(1));
        expected.addInOrder(pm2.getNode(2));
        expected.addInOrder(pm2.getNode(3));
        expected.addInOrder(pm2.getNode(4));
        expected.addInOrder(pm2.getNode(5));
        expected.addInOrder(pm2.getNode(14));
        expected.addInOrder(pm2.getNode(6));
        expected.addInOrder(pm2.getNode(7));
        expected.addInOrder(pm2.getNode(8));
        expected.addInOrder(pm2.getNode(9));
        expected.addInOrder(pm2.getNode(12));
        try {
            assertEquals(expected, pm2.findPath(pm2.getNode(1), pm2.getNode(12)));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(expected = PathNotFoundException.class)
    public void testBFS3() throws PathNotFoundException, NearestNotFoundException, FloorNotReachableException {
        pm1.selectAlgorithm("Breadth-First Search");

        pm1.findPath(pm1.getNode(1), pm1.getNode(4));
    }

    @Test(expected = PathNotFoundException.class)
    public void testBFS4() throws PathNotFoundException, NearestNotFoundException, FloorNotReachableException {
        pm2.selectAlgorithm("Breadth-First Search");

        pm2.findPath(pm2.getNode(26), pm2.getNode(1));
    }

    @Test
    public void testDFS1() throws PathNotFoundException, NearestNotFoundException, FloorNotReachableException  {
        pm1.selectAlgorithm("Depth-First Search");

        Path expected = new Path();
        expected.addInOrder(pm1.getNode(1));
        expected.addInOrder(pm1.getNode(2));
        expected.addInOrder(pm1.getNode(3));
        try {
            assertEquals(expected, pm1.findPath(pm1.getNode(1), pm1.getNode(3)));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testDFS2() throws PathNotFoundException, NearestNotFoundException, FloorNotReachableException  {
        pm2.selectAlgorithm("Depth-First Search");

        Path expected = new Path();
        expected.addInOrder(pm2.getNode(1));
        expected.addInOrder(pm2.getNode(2));
        expected.addInOrder(pm2.getNode(3));
        expected.addInOrder(pm2.getNode(4));
        expected.addInOrder(pm2.getNode(5));
        expected.addInOrder(pm2.getNode(14));
        expected.addInOrder(pm2.getNode(13));
        expected.addInOrder(pm2.getNode(11));
        expected.addInOrder(pm2.getNode(21));
        expected.addInOrder(pm2.getNode(22));
        expected.addInOrder(pm2.getNode(20));
        expected.addInOrder(pm2.getNode(24));
        expected.addInOrder(pm2.getNode(25));
        expected.addInOrder(pm2.getNode(19));
        expected.addInOrder(pm2.getNode(18));
        expected.addInOrder(pm2.getNode(17));
        expected.addInOrder(pm2.getNode(9));
        expected.addInOrder(pm2.getNode(12));
        try {
            assertEquals(expected, pm2.findPath(pm2.getNode(1), pm2.getNode(12)));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(expected = PathNotFoundException.class)
    public void testDFS3() throws PathNotFoundException, NearestNotFoundException, FloorNotReachableException {
        pm1.selectAlgorithm("Depth-First Search");

        pm1.findPath(pm1.getNode(1), pm1.getNode(4));
    }

    @Test(expected = PathNotFoundException.class)
    public void testDFS4() throws PathNotFoundException, NearestNotFoundException, FloorNotReachableException {
        pm2.selectAlgorithm("Depth-First Search");

        pm2.findPath(pm2.getNode(26), pm2.getNode(1));
    }
}
