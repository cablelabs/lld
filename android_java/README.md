# Android/Java DSCP Example
This is an example of setting the DSCP value of traffic, sent out from an android device written in java. The example uses a pair of classes called EchoClient and EchoServer driven by other java classes to send and receive UDP traffic using the java.net.DatagramSocket and java.net.DatagramPacket classes. A TCP based implementation would use the same method for setting the DSCP value.


## Provided Projects
Provided here is both an android studio example project in AndroidJavaTos and an intellij idea project in DesktopJavaTos. The android studio project is more thoroughly commented. The intellij project is modified to run on a desktop. 


## Example Code
For a declared DatagramSocket or Socket you can set the tos (Type of service) byte to effectively set the DSCP value. The following method is recommended.

```java
int tos = 160;
//160 corrosponds to the recommended code point of CS5
DatagramSocket socket = new DatagramSocket();

socket.setTrafficClass(tos);
```

On some legacy systems the following deprecated method may be supported.

```java
import static java.net.StandardSocketOptions.IP_TOS;
socket.setOption(IP_TOS, tos);
```

The recommended DSCP value is CS5 which corresponds to a tos value of 160 decimal or a0 hexadecimal.
