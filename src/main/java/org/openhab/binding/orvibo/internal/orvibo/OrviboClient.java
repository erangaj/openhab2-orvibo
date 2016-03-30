package org.openhab.binding.orvibo.internal.orvibo;

/**
 * The {@link OrviboClient} defines the interface of the Orvibo client
 *
 * @author Eranga Jayasundera - Initial contribution
 */
public interface OrviboClient {

    void discover();

    void subscribe(String mac);

    void subscribeAndSetDeviceState(String mac, OrviboState newState);

    void startListener();

    void stopListener();

    OrviboListener getListener();

    void setDeviceStatusCallback(OrviboDeviceStatusCallback callback);

}
