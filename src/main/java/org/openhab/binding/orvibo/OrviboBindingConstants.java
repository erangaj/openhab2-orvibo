package org.openhab.binding.orvibo;

import com.google.common.collect.ImmutableSet;
import org.eclipse.smarthome.core.thing.ThingTypeUID;

import java.util.Set;

/**
 * The {@link OrviboBindingConstants} class defines common constants, which are
 * used across the whole binding.
 * 
 * @author Eranga Jayasundera - Initial contribution
 */
public class OrviboBindingConstants {

    public static final String BINDING_ID = "orvibo";
    
    // List of all Thing Type UIDs
    public final static ThingTypeUID THING_TYPE_SOCKET = new ThingTypeUID(BINDING_ID, "socket");

    // List of all Channel ids
    public final static String CHANNEL_STATE = "state";

    public static final String MAC = "mac";

    //Orvibo protocol constants
    public static final int PORT = 10000;
    public static final int REFRESH_DELAY = 5000;
    public static final int BACKGROUND_REFRESH_INTERVAL = 5; // In mins

    public static final String BROADCAST_IP = "255.255.255.255";
    public static final String MAGIC_WORD = "6864";
    public static final String COMMAND_ID_DISCOVER = "7161";
    public static final String COMMAND_ID_SUBSCRIBE = "636c";
    public static final String COMMAND_ID_SET_STATE = "6463";
    public static final String MESSAGE_ID_DISCOVER = "7161";
    public static final String MESSAGE_ID_SUBSCRIBE = "0018";
    public static final String MESSAGE_ID_STATE_CHANGE = "0017";

    public static final String DEVICE_LABEL = "Orvibo device with MAC ";

    public final static Set<ThingTypeUID> SUPPORTED_THING_TYPES = ImmutableSet.of(THING_TYPE_SOCKET);
}
