package org.openhab.binding.orvibo.internal.orvibo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.LinkedHashMap;

import static org.openhab.binding.orvibo.OrviboBindingConstants.*;

/**
 * The {@link OrviboClientImpl} implements Orvibo communication protocol
 *
 * @author Eranga Jayasundera - Initial contribution
 */
public class OrviboClientImpl implements OrviboDeviceStatusCallback, OrviboClient {

    private Logger logger = LoggerFactory.getLogger(OrviboClientImpl.class);
    private DatagramSocket socket;
    private OrviboListener listener;
    private OrviboDeviceStatusCallback deviceStatusCallback;

    public OrviboClientImpl() {
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            logger.error("Error creating socket to talk to Orvibo.", e);
            throw new RuntimeException("Error creating socket to talk to Orvibo.", e);
        }
    }

    @Override
    public void discover() {
        synchronized (this) {
            byte[] message = prepareMessage(COMMAND_ID_DISCOVER, "", "", null);
            try {
                sendMessage(message, socket, InetAddress.getByName(BROADCAST_IP));
            } catch (IOException e) {
                logger.error("Error sending discover message.", e);
            }
        }
    }

    @Override
    public void subscribe(String mac) {
        synchronized (this) {
            OrviboDevice device = listener.getDiscoveredDevice(mac);
            if (device != null) {
                subscribe(device);
            }
        }
    }

    private void subscribe(OrviboDevice device) {
        LinkedHashMap<String, String> data = new LinkedHashMap<String, String>();
        data.put("macReversed", device.getMacAddressLE());
        data.put("macPadding", device.getMacPaddingLE());

        byte[] message = prepareMessage(COMMAND_ID_SUBSCRIBE, device.getMacAddress(), device.getMacPadding(), data);
        sendMessage(message, socket, device.getIP());
    }

    private void setDeviceState(OrviboDevice device, OrviboState newState) {
        LinkedHashMap<String, String> data = new LinkedHashMap<String, String>();
        data.put("padding", "00000000");
        data.put("newState", newState.getId());
        byte[] message = prepareMessage(COMMAND_ID_SET_STATE, device.getMacAddress(), device.getMacPadding(), data);
        sendMessage(message, socket, device.getIP());
    }

    @Override
    public void subscribeAndSetDeviceState(String mac, OrviboState newState) {
        synchronized (this) {
            OrviboDevice device = listener.getDiscoveredDevice(mac);
            if (device == null) {
                return;
            }
            device.setStateAfterSubscription(newState);
            subscribe(device);
            device.setState(newState);
        }
    }

    @Override
    public void startListener() {
        synchronized (this) {
            if (listener == null) {
                listener = new OrviboListener(this);
                listener.startListener();
            }
        }
    }

    @Override
    public void stopListener() {
        synchronized (this) {
            if (listener != null) {
                listener.stopListener();
            }
            listener = null;
        }
    }

    @Override
    public void onStatusChanged(OrviboDevice device) {
        OrviboState newState = device.getStateAfterSubscription();
        if (newState != null) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // ignore
            }
            device.setStateAfterSubscription(null);
            setDeviceState(device, newState);
        } else if (deviceStatusCallback!=null) {
            deviceStatusCallback.onStatusChanged(device);
        }
    }

    @Override
    public void onStatusMayBeChanged(OrviboDevice device) {
        if (deviceStatusCallback!=null) {
            deviceStatusCallback.onStatusMayBeChanged(device);
        }
    }

    @Override
    public OrviboListener getListener() {
        return listener;
    }

    public void setDeviceStatusCallback(OrviboDeviceStatusCallback callback) {
        this.deviceStatusCallback = callback;
    }

    private byte[] prepareMessage(String commandID, String macAddress, String macPadding, LinkedHashMap<String, String> data) {
        String dataStr = "";
        if (data != null) {
            for (String value : data.values()) {
                dataStr += value;
            }
        }

        String packet = MAGIC_WORD + "0000" + commandID + macAddress + macPadding + dataStr;
        StringBuilder len = new StringBuilder(Integer.toHexString((packet.length()/2)).toLowerCase());
        while (len.length() < 4) {
            len.insert(0, "0");
        }
        String message = MAGIC_WORD + len + commandID + macAddress + macPadding + dataStr;
        if (logger.isDebugEnabled()) {
            logger.debug("Sending Orvibo message " + message + ("".equals(macAddress) ? " to everyone." : " to MAC address " + macAddress));
        }
        return DatatypeConverter.parseHexBinary(message);
    }

    private void sendMessage(byte[] message, DatagramSocket sock, InetAddress ip) {
        try {
            sock.send(new DatagramPacket(message, message.length, ip, PORT));
        } catch (IOException e) {
            logger.error("Error sending Orvibo message to " + ip);
        }
    }
}
