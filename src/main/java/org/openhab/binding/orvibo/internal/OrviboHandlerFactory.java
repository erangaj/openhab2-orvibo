package org.openhab.binding.orvibo.internal;

import static org.openhab.binding.orvibo.OrviboBindingConstants.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.openhab.binding.orvibo.handler.OrviboHandler;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.openhab.binding.orvibo.internal.orvibo.OrviboClient;
import org.openhab.binding.orvibo.internal.orvibo.OrviboDevice;
import org.openhab.binding.orvibo.internal.orvibo.OrviboDeviceStatusCallback;

/**
 * The {@link OrviboHandlerFactory} is responsible for creating things and thing 
 * handlers.
 *
 * @author Eranga Jayasundera - Initial contribution
 */
public class OrviboHandlerFactory extends BaseThingHandlerFactory implements OrviboDeviceStatusCallback {
    
    private final static Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Collections.singleton(THING_TYPE_SOCKET);
    private OrviboClient orviboClient;
    private Map<String, OrviboHandler> handlers = new HashMap<String, OrviboHandler>();

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID);
    }

    @Override
    protected ThingHandler createHandler(Thing thing) {

        ThingTypeUID thingTypeUID = thing.getThingTypeUID();

        if (thingTypeUID.equals(THING_TYPE_SOCKET)) {
            OrviboHandler orviboHandler = new OrviboHandler(thing, orviboClient);
            handlers.put(thing.getUID().getId(), orviboHandler);
            return orviboHandler;
        }

        return null;
    }

    @Override
    public void onStatusChanged(OrviboDevice device) {
        OrviboHandler handler = this.handlers.get(device.getMacAddress());
        if (handler!=null) {
            handler.onStatusChanged(device);
        }
    }

    @Override
    public void onStatusMayBeChanged(OrviboDevice device) {
        OrviboHandler handler = this.handlers.get(device.getMacAddress());
        if (handler!=null) {
            handler.checkStatus();
        }
    }

    public void setOrviboClient(OrviboClient orviboClient) {
        orviboClient.setDeviceStatusCallback(this);
        this.orviboClient = orviboClient;
    }

    public void unsetOrviboClient(OrviboClient orviboClient) {
        this.orviboClient.setDeviceStatusCallback(this);
        this.orviboClient = null;
    }

}
