package org.openhab.binding.orvibo.internal.discovery;

import org.eclipse.smarthome.config.discovery.AbstractDiscoveryService;
import org.eclipse.smarthome.config.discovery.DiscoveryResult;
import org.eclipse.smarthome.config.discovery.DiscoveryResultBuilder;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.openhab.binding.orvibo.OrviboBindingConstants;
import org.openhab.binding.orvibo.internal.orvibo.OrviboClient;
import org.openhab.binding.orvibo.internal.orvibo.OrviboDevice;
import org.openhab.binding.orvibo.internal.orvibo.OrviboDiscoveryCallback;
import org.openhab.binding.orvibo.internal.orvibo.OrviboListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.openhab.binding.orvibo.OrviboBindingConstants.*;

/**
 * The {@link OrviboDiscoveryService} is used to discover Orvibo devices.
 *
 * @author Eranga Jayasundera - Initial contribution
 */
public class OrviboDiscoveryService extends AbstractDiscoveryService implements OrviboDiscoveryCallback {

    private Logger logger = LoggerFactory.getLogger(AbstractDiscoveryService.class);

    private OrviboClient orviboClient;

    public OrviboDiscoveryService() {
        super(OrviboBindingConstants.SUPPORTED_THING_TYPES, 30, true);
    }

    public void activate() {
        logger.debug("Starting Orvibo discovery...");
        orviboClient.getListener().setDiscoveryCallback(this);
        startScan();
    }

    @Override
    public void deactivate() {
        logger.debug("Stopping Orvibo discovery...");
        orviboClient.getListener().setDiscoveryCallback(null);
        stopScan();
    }

    @Override
    protected void startScan() {
        logger.debug("Starting Orvibo search...");
        if (orviboClient != null) {
            orviboClient.discover();
        } else {
            logger.debug("Orvibo client is not initialized yet.");
        }
    }

    @Override
    protected synchronized void stopScan() {
        removeOlderResults(getTimestampOfLastScan());
        super.stopScan();
    }

    @Override
    protected void startBackgroundDiscovery() {
        startScan();
    }

    @Override
    protected void stopBackgroundDiscovery() {
        stopScan();
    }

    public void setOrviboClient(OrviboClient orviboClient) {
        this.orviboClient = orviboClient;
        this.orviboClient.startListener();
    }

    public void unsetOrviboClient(OrviboClient orviboClient) {
        OrviboListener listener = this.orviboClient.getListener();
        if (listener!=null) {
            listener.setDiscoveryCallback(null);
        }
        this.orviboClient = null;
    }

    @Override
    public void onDeviceFound(OrviboDevice device) {
        logger.debug("orvibo device found: " + device);
        String mac = device.getMacAddress();
        ThingUID thingUID = new ThingUID(THING_TYPE_SOCKET, mac);
        DiscoveryResult discoveryResult = DiscoveryResultBuilder.create(thingUID)
                .withProperty(MAC, mac)
                .withLabel(DEVICE_LABEL + mac).build();
        thingDiscovered(discoveryResult);
    }
}
