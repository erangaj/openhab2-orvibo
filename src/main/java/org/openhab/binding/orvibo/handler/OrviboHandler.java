/**
 * Copyright (c) 2014-2015 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.orvibo.handler;

import static org.openhab.binding.orvibo.OrviboBindingConstants.*;

import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.openhab.binding.orvibo.internal.orvibo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * The {@link OrviboHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 * 
 * @author Eranga Jayasundera - Initial contribution
 */
public class OrviboHandler extends BaseThingHandler {

    private Logger logger = LoggerFactory.getLogger(OrviboHandler.class);
    private OrviboClient client;

    public OrviboHandler(Thing thing, OrviboClient orviboClient) {
		super(thing);
        this.client = orviboClient;
	}

	@Override
	public void handleCommand(ChannelUID channelUID, Command command) {
        if(channelUID.getId().equals(CHANNEL_STATE)) {
            OnOffType onoff = (OnOffType) command;
            final String mac = (String) getThing().getConfiguration().get("mac");
            if (logger.isDebugEnabled()) {
                logger.debug("Set the state of the Orvibo device with MAC " + mac + " to " + onoff);
            }
            client.subscribeAndSetDeviceState(mac, onoff==OnOffType.ON ? OrviboState.ON : OrviboState.OFF);
        }
	}

    @Override
    public void initialize() {
        scheduler.scheduleAtFixedRate(new RefreshRunnable(), BACKGROUND_REFRESH_INTERVAL, BACKGROUND_REFRESH_INTERVAL, TimeUnit.MINUTES);
        client.startListener();
        updateStatus(ThingStatus.OFFLINE);
    }

    @Override
    public void dispose() {
    }

    public void onStatusChanged(OrviboDevice device) {
        synchronized (this) {
            if (logger.isDebugEnabled()) {
                logger.debug("The Orvibo device with MAC " + getThing().getConfiguration().get("mac") + " is online.");
            }
            updateStatus(ThingStatus.ONLINE);
            updateState(CHANNEL_STATE, device.getState() == OrviboState.ON ? OnOffType.ON : OnOffType.OFF);
        }
    }

    public void checkStatus() {
        synchronized (this) {
            scheduler.schedule(new RefreshRunnable(), REFRESH_DELAY, TimeUnit.MILLISECONDS);
        }
    }

    private class RefreshRunnable implements Runnable {

        @Override
        public void run() {
            String mac = (String) getThing().getConfiguration().get("mac");
            client.subscribe(mac);
        }
    }
}
