package org.openhab.binding.orvibo.internal.orvibo;

import java.util.HashMap;
import java.util.Map;

/**
 * The {@link OrviboState} enum shows the possible states of Orvibo devices.
 *
 * @author Eranga Jayasundera - Initial contribution
 */
public enum OrviboState {

    ON("01"),
    OFF("00");

    private String stateId;
    private static Map<String, OrviboState> allStates = new HashMap<String, OrviboState>();

    static {
        for (OrviboState state : values()) {
            allStates.put(state.getId(), state);
        }
    }

    OrviboState(String stateId) {
        this.stateId = stateId;
    }

    public String getId() {
        return stateId;
    }

    public static OrviboState fromState(String state) {
        return allStates.get(state);
    }

}
