package fopbot;

import fopbot.Transition.RobotAction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RobotTrace implements Iterable<Transition> {
    private List<Transition> transitions = new ArrayList<>();

    public RobotTrace(RobotTrace robotTrace) {
        this.transitions = new ArrayList<>(robotTrace.getTransitions());
    }

    public RobotTrace() {
    }

    @Override
    public Iterator<Transition> iterator() {
        return transitions.iterator();
    }

    public void setTransitions(List<Transition> transitions) {
        this.transitions = transitions;
    }

    public List<Transition> getTransitions() {
        return transitions;
    }

    @Override
    public String toString() {
        return "RobotTrace{" +
            "transitions=" + transitions +
            '}';
    }

    public void trace(Robot r, RobotAction robotAction) {
        transitions.add(new Transition(robotAction, r));
    }
}
