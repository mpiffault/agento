package fr.mpiffault.agento.model;

import fr.mpiffault.agento.model.geometry.Direction;
import fr.mpiffault.agento.model.geometry.Position;
import junit.framework.TestCase;

public class AgentTest extends TestCase {
    private Agent agent;

    public void setUp() throws Exception {
        super.setUp();
        Environment environment = new Environment();
        agent = new Agent(environment, new Position(0d,0d));
    }

    public void testMove() throws Exception {
        agent.setDirection(Direction.NORTH);
        assertEquals(0d, agent.getPosition().getX(), 0.001d);
        assertEquals(0d, agent.getPosition().getY(), 0.001d);
        agent.move(2d);
        assertEquals(0d, agent.getPosition().getX(), 0.001d);
        assertEquals(agent.getEnvironment().getSizeY() - 2d, agent.getPosition().getY(), 0.001d);
    }
}