package fr.mpiffault.agento.model;

import junit.framework.TestCase;

public class DirectionTest extends TestCase {
    Direction direction;
    public void setUp() throws Exception {
        super.setUp();
        direction = new Direction(Direction.NORTH);
    }

    public void testGetVector() throws Exception {
        Vector vector = direction.getVector();

        assertEquals(0d,vector.getDx(),0.001);
        assertEquals(-1d, vector.getDy(), 0.001);
    }
}