package org.openhab.binding.orvibo.internal.orvibo;

/**
 * The {@link OrviboDiscoveryCallback} defines the discovery callback interface.
 *
 * @author Eranga Jayasundera - Initial contribution
 */
public interface OrviboDiscoveryCallback {

    void onDeviceFound(OrviboDevice device);

}
