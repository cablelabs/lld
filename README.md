# Low Latency DOCSIS/Wi-Fi Packet Marking Examples 
## Low Latency DOCSIS
Low Latency DOCSIS (LLD) technology is comprised of a suite of features that provide an opportunity for latency sensitive traffic to be delivered across the cable access network with significantly lower latency and jitter than ever before on any consumer broadband network.  LLD technology generally relies upon applications identifying their low latency network traffic, so they can be appropriately classified and make use of the low latency queue. 

There are two classes of applications that can benefit from Low Latency DOCSIS technology:
* Applications that have sparse traffic that will not cause congestion and latency. These applications are not responsive to congestion - they simply send their traffic and expect that it will not cause congestion or latency. Good examples include traditional online multi-player games that send a packet every tick with state data.
* Applications that send large volumes of traffic that need low latency, but that are responsive to congestion in the network. These applications can benefit from using a technology known as "Low Latency, Low Loss, Scalable Throughput (L4S)". Support for this technology is including in the LLD feature set, but is beyond the scope of what we have in this repository. Information on L4S can be found in this [IETF draft architecture](https://tools.ietf.org/html/draft-ietf-tsvwg-l4s-arch-06).

Applications that fall into the first class can be very simply modified to signal which traffic would benefit from low latency treatment to both DOCSIS and Wi-Fi. The application does this by setting a Differentiated Services Code Point (DSCP) in the IP packet header, specifically in the TOS byte. Devices that support Low Latency DOCSIS can key on this value and direct the traffic into the low latency queue, bypassing any bulk classic traffic that may have built a queue of data in the classic DOCSIS queue. The low latency queue is not a high priority queue - it is simply a separate queue that is designed for low latency traffic. It is short queue that is kept emptier, resulting in lower latency.

## Low Latency Wi-Fi
For Wi-Fi, the DSCP packet marking activates Wi-Fi Multi Media (WMM). Wi-Fi (since 802.11n) has four classes ("Access Categories") of traffic - Background, Best Effort, Video and Voice (in increasing priority). By default, Wi-Fi gear maps traffic into these four Access Categories based on the upper three bits of the DSCP.  The majority of traffic uses the Best Effort Access Category, but the Video and Voice Access Categories can provide lower latency and lower jitter when used by sparse traffic applications.

## Ok - Which DSCP value should I use?
The IETF is working on [standardizing](https://datatracker.ietf.org/doc/draft-ietf-tsvwg-nqb/) a new DSCP value that applications can use for this purpose in the future, called "NQB" (for Non-Queue-Building).  When it gets assigned, it will be recommended to start using that value.  Until that time, we recommend using the existing value of "Class Selector 5" (CS5), which is the decimal value 40. Both values will result in traffic being classified to the low latency queue in LLD, and both values will map to the Video Access Category in Wi-Fi.

## TOS values vs. DSCP values
The Type of Service (TOS) byte in an IP packet header (it's been renamed "Traffic Class" in IPv6) contains both a 6-bit Differentiated Services field, and a 2-bit Explicit Congestion Notification field. The BSD socket APIs were designed for the original designation of this byte as for TOS, and take an 8-bit value. While Differentiated Services values are 6-bit values, they appear in the top 6 bits of the TOS byte. Hence, the examples use the DSCP value multiplied by 4 (or shifted left by two bits). 

Hence in the examples you will see things like:

```java
int dscp = 40;  
// 40 corresponds to the recommended code point of CS5
DatagramSocket socket = new DatagramSocket();

socket.setTrafficClass(dscp<<2);
```

## Examples Provided
We have provided some examples (some full apps, some code snippets) that show how to perform this traffic marking for the following platforms:
* Windows  (code snippet) 
* Linux/BSD (code snippet)
* Android (complete basic application)
* Desktop Java (complete basic application)

The Linux example is applicable to a variety of Linux or BSD-based platforms. It uses traditional BSD sockets that apply to Linux, OSX and some game console platforms.



## Why can't I mark all my traffic as low latency?
Low Latency DOCSIS has been designed to support segregation of low latency traffic in a different queue from latency-causing traffic. Classic traffic (typically TCP) is designed to fill network buffers in order to get the highest possible throughput, and to probe for the capacity (throughput) of the network. This behavior ordinarily causes latency for all traffic that sits behind these full buffers. By creating a separate short queue, DOCSIS allows latency-sensitive traffic to avoid sitting behind that classic traffic.

If classic traffic were to be marked as low latency through the use of DSCP, it would actually get worse throughput, because the low latency queue is shorter and thus incompatible with the expectations of classic senders. It will also potentially cause additional latency for latency-sensitive traffic. The new L4S technology referenced above allows for a future where high throughput applications can also achieve low latency, but implementing it requires work in the application and server. 
