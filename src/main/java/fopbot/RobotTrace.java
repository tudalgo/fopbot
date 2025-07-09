package fopbot;

import fopbot.Transition.RobotAction;
import org.jetbrains.annotations.NotNull;

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
    private @NotNull List<Transition> transitions = new ArrayList<>();

    /**
     * Constructs and initializes robot trace with the specified trace as previous traces of the
     * constructed robot trace.
     *
     * @param robotTrace the previous traces of the constructed robot trace
     */
    public RobotTrace(final @NotNull RobotTrace robotTrace) {
        this.transitions = new ArrayList<>(robotTrace.getTransitions());
    }

    /**
     * Constructs and initializes robot trace with an empty trace.
     */
    public RobotTrace() {
    }

    /**
     * Returns the previous transitions of this trace.
     *
     * @return the previous transitions of this trace
     */
    public @NotNull List<Transition> getTransitions() {
        return transitions;
    }

    /**
     * Sets previous transitions of the actions of robots to the specified transitions.
     *
     * @param transitions the new transitions for this trace
     */
    public void setTransitions(final @NotNull List<Transition> transitions) {
        this.transitions = transitions;
    }

    @Override
    public @NotNull Iterator<Transition> iterator() {
        return transitions.iterator();
    }

    /**
     * Traces the specified robot with its associated action.
     *
     * @param r           the robot to trace
     * @param robotAction the action of the robot
     */
    public void trace(final @NotNull Robot r, final @NotNull RobotAction robotAction) {
        transitions.add(new Transition(robotAction, r));
        World.getGlobalWorld().checkActionLimit();
    }

    @Override
    public @NotNull String toString() {
        return "RobotTrace{"
            + "transitions=" + transitions
            + '}';
    }

}
