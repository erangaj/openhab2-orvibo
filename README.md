#Orvibo S20 / S10 Binding for OpenHAB2

This is an OpenHAB2 binding for integrating [Orvibo S20 Smart Sockets](http://www.orvibo.com/product_en.html).
The binding is written in pure Java and has been tested for S20 smart sockets, but may work with S10 sockets as well.

Currently, this binding is NOT compatible with Orvibo Coco Smart WiFi Power Strip.

## Installation
Steps to add an Orvibo device to your OpenHab2:
1. Setup your Orvibo device as described in the manual and test it using the Orbivo app. Make sure your Orvibo device and the OpenHAB2 are conected to the same local network.

2. Download the [binding JAR file](https://github.com/erangaj/openhab2-orvibo/blob/erangaj-bin-v1/org.openhab.binding.orvibo-1.0.0-SNAPSHOT.jar?raw=true) to the 'addons' directory of OpenHAB2.

3. Define a new 'Switch' [item](https://github.com/openhab/openhab/wiki/Explanation-of-items) for the Orvibo socket.

4. Start OpenHAB2 and navigate to the Admin UI (Paper UI).

5. Goto Inbox, select 'Search for Things', select 'Orvibo Binding' and select the discovered 'Orvibo Smart Socket'.

6. Update the name if necessory and click 'Add as Thing'.

7. Goto Configuration -> Things and select the newly added Orvibo device. Link the item created in above step #3 with the device.

Notes:
* If the device status of the newly added device is shown as 'OFFLINE' on the OpenHAB2 UI, try restarting OpenHAB2.
* Alternatively, you my add devices manually with using the MAC address (in lowercase, without any special characters such
as ':' or '-') of the device.

## Thanks
* [node-orvibo](https://github.com/Grayda/node-orvibo) project.
* OpenHAB2 community
* and, of course [Orvibo](http://www.orvibo.com/) for creating great low cost smart sockets.
