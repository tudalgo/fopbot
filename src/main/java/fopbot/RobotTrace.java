package fopbot;

import fopbot.Transition.RobotAction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Enables the analysis or tracing of the actions of robots.
 *
 * @see Robot
 */
public class RobotTrace implements Iterable<Transition> {

    /**
     * The transitions contain the analysis * or tracing of the actions of robots.
     */
    private List<Transition> transitions = new ArrayList<>();

    /**
     * Constructs and initializes robot trace with the specified trace as previous traces of the
     * constructed robot trace.
     *
     * @param robotTrace the previous traces of the constructed robot trace
     */
    public RobotTrace(RobotTrace robotTrace) {
        this.transitions = new ArrayList<>(robotTrace.getTransitions());
    }

    /**
     * Constructs and initializes robot trace with an empty trace.
     */
    public RobotTrace() {
    }

    @Override
    public Iterator<Transition> iterator() {
        return transitions.iterator();
    }

    /**
     * Sets previous transitions of the actions of robots to the specified transitions.
     *
     * @param transitions the new transitions for this trace
     */
    public void setTransitions(List<Transition> transitions) {
        this.transitions = transitions;
    }

    /**
     * Returns the previous transitions of this trace.
     *
     * @return the previous transitions of this trace
     */
    public List<Transition> getTransitions() {
        return transitions;
    }

    /**
     * Traces the specified robot with its associated action.
     *
     * @param r           the robot to trace
     * @param robotAction the action of the robot
     */
    public void trace(Robot r, RobotAction robotAction) {
        transitions.add(new Transition(robotAction, r));
        World.getGlobalWorld().checkActionLimit();
    }

    @Override
    public String toString() {
        return "RobotTrace{"
            + "transitions=" + transitions
            + '}';
    }

}
