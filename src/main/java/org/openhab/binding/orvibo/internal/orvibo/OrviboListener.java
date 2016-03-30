package org.openhab.binding.orvibo.internal.orvibo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

import static org.openhab.binding.orvibo.OrviboBindingConstants.*;

/**
 * The {@link OrviboListener} listens for UDP messages from Orbivo(s)
 *
 * @author Eranga Jayasundera - Initial contribution
 */
public class OrviboListener extends Thread {

    private Logger logger = LoggerFactory.getLogger(OrviboListener.class);
    private OrviboDeviceStatusCallback callback;
    private OrviboDiscoveryCallback discoveryCallback;
    private boolean stop = false;
    private DatagramSocket socket;
    private Map<String, OrviboDevice> devices = new HashMap<String, OrviboDevice>();

    public OrviboListener(OrviboDeviceStatusCallback callback) {
        this.callback = callback;
        try {
            socket = new DatagramSocket(PORT);
            socket.setBroadcast(true);
        } catch (SocketException e) {
            logger.error("Error creating socket to talk to Orvibo.", e);
            throw new RuntimeException("Error creating socket to talk to Orvibo.", e);
        }

    }

    @Override
    public void run() {
        byte[] b = new byte[60];
        while (!stop) {
            try {
                DatagramPacket packet = new DatagramPacket(b, b.length);
                socket.receive(packet);
                String data = toHexString(packet.getData()).toLowerCase();

                if (logger.isDebugEnabled()) {
                    logger.debug("Orvibo message received " + data + " from " + packet.getAddress());
                }

                if (!data.startsWith(MAGIC_WORD)) {
                    // Invalid packet
                    continue;
                }

                int length = (b[2] * 10) + b[3];

                if (MESSAGE_ID_DISCOVER.equals(data.substring(8, 12)) && length > 41) {
                    OrviboDevice device = new OrviboDevice();
                    String mac = data.substring(14, 26);
                    if (devices.containsKey(mac)) {
                        continue;
                    }
                    device.setMacAddress(mac);
                    device.setMacPadding(data.substring(26, 38));
                    device.setMacAddressLE(data.substring(38, 50));
                    device.setMacPaddingLE(data.substring(50, 62));
                    device.setHardwareId(hexToString(data.substring(62, 74)));
                    device.setAgeInSeconds(asInt(b[37]) + (255 * asInt(b[38])) + (65025 * asInt(b[39])));
                    device.setIP(packet.getAddress());
                    device.setState(OrviboState.fromState(data.substring(82, 84)));
                    devices.put(mac, device);
                    if (discoveryCallback!=null) {
                        discoveryCallback.onDeviceFound(device);
                    }
                    callback.onStatusChanged(device);
                } else if (MESSAGE_ID_SUBSCRIBE.equals(data.substring(4, 8))) {
                    String mac = data.substring(12, 24);
                    if (devices.containsKey(mac)) {
                        OrviboDevice device = devices.get(mac);
                        device.setState(OrviboState.fromState(data.substring(46, 48)));
                        callback.onStatusChanged(device);
                    }
                } else if (MESSAGE_ID_STATE_CHANGE.equals(data.substring(4, 8))) {
                    String mac = data.substring(12, 24);
                    if (devices.containsKey(mac)) {
                        OrviboDevice device = devices.get(mac);
                        callback.onStatusMayBeChanged(device);
                    }
                }

            } catch (IOException e) {
                logger.error("Error while receving data", e);
            }
        }
    }

    public synchronized void startListener() {
        this.start();
    }

    public synchronized void stopListener() {
        this.stop = true;
        this.socket.close();
    }

    private int asInt(byte b) {
        return b > 0 ? b : 256 + b;
    }

    private String toHexString(byte[] bytes) {
        return DatatypeConverter.printHexBinary(bytes);
    }

    private String hexToString(String hex) {
        return new String(DatatypeConverter.parseHexBinary(hex));
    }

    public synchronized OrviboDevice getDiscoveredDevice(String mac) {
        return devices.get(mac);
    }

    public void setDiscoveryCallback(OrviboDiscoveryCallback discoveryCallback) {
        this.discoveryCallback = discoveryCallback;
    }
}
