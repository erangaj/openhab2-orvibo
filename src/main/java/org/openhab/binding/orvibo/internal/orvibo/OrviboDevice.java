package org.openhab.binding.orvibo.internal.orvibo;

import java.net.InetAddress;

/**
 * The {@link OrviboDevice} represnts an Orvibo device
 *
 * @author Eranga Jayasundera - Initial contribution
 */
public class OrviboDevice {

    private String macAddress;
    private String macPadding;
    private String macAddressLE;
    private String macPaddingLE;
    private String hardwareId;
    private long ageInSeconds;
    private OrviboState state;
    private InetAddress ip;
    private OrviboState stateAfterSubscription;

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getMacPadding() {
        return macPadding;
    }

    public void setMacPadding(String macPadding) {
        this.macPadding = macPadding;
    }

    public String getMacAddressLE() {
        return macAddressLE;
    }

    public void setMacAddressLE(String macAddressLE) {
        this.macAddressLE = macAddressLE;
    }

    public String getMacPaddingLE() {
        return macPaddingLE;
    }

    public void setMacPaddingLE(String macPaddingLE) {
        this.macPaddingLE = macPaddingLE;
    }

    public String getHardwareId() {
        return hardwareId;
    }

    public void setHardwareId(String hardwareId) {
        this.hardwareId = hardwareId;
    }

    public long getAgeInSeconds() {
        return ageInSeconds;
    }

    public void setAgeInSeconds(long ageInSeconds) {
        this.ageInSeconds = ageInSeconds;
    }

    public OrviboState getState() {
        return state;
    }

    public void setState(OrviboState state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrviboDevice device = (OrviboDevice) o;
        return macAddress != null ? macAddress.equals(device.macAddress) : device.macAddress == null;

    }

    @Override
    public int hashCode() {
        return macAddress != null ? macAddress.hashCode() : 0;
    }

    public void setIP(InetAddress ip) {
        this.ip = ip;
    }

    public InetAddress getIP() {
        return ip;
    }

    public void setStateAfterSubscription(OrviboState stateAfterSubscription) {
        this.stateAfterSubscription = stateAfterSubscription;
    }

    public OrviboState getStateAfterSubscription() {
        return stateAfterSubscription;
    }

    @Override
    public String toString() {
        return "OrviboDevice{" +
                "ip=" + ip +
                ", macAddress='" + macAddress + '\'' +
                ", state=" + state +
                '}';
    }

}
