package org.openhab.binding.orvibo.internal.orvibo;

/**
 * The {@link OrviboDeviceStatusCallback} defines the device status callback interface.
 *
 * @author Eranga Jayasundera - Initial contribution
 */
public interface OrviboDeviceStatusCallback {

    void onStatusChanged(OrviboDevice device);

    void onStatusMayBeChanged(OrviboDevice device);

}
