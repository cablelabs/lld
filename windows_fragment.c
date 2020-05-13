/* © 2020 Cable Television Laboratories, Inc.  All Rights Reserved.

   Redistribution and use in source and binary forms, with or without modification, 
   are permitted provided that the following conditions are met:

   1. Redistributions of source code must retain the above copyright notice, 
      this list of conditions and the following disclaimer.
   2. Redistributions in binary form must reproduce the above copyright notice, 
      this list of conditions and the following disclaimer in the documentation 
      and/or other materials provided with the distribution.
   3. Neither the name of the copyright holder nor the names of its contributors 
      may be used to endorse or promote products derived from this software without 
      specific prior written permission.

   THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
   ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
   WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
   IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
   INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, 
   BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, 
   DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF 
   LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE 
   OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED 
   OF THE POSSIBILITY OF SUCH DAMAGE.
*/



#include <winsock2.h>
#include <windows.h>
#include <qos2.h>
#pragma comment (lib, "Qwave.lib")

int init_Winsock() {
    WSADATA wsaData;
    int iResult;
    iResult = WSAStartup(MAKEWORD(2, 2), &wsaData);
    if (iResult != 0) {
	 printf("WSAStartup failed with error: %d\n", iResult);
	 return 1;
    }
    return 0;
}


//set_socket_codepoint uses the qwave API to create a register a QOS flow and set the TOS parameter from a preset QOS_TRAFFIC_TYPE
//Parameters:
// socket - the socket object being used
// trafficType - member of the QOS_TRAFFIC_TYPE enum, use QOSTrafficTypeAudioVideo for CS5
// flowId - unique integer value for this flow. Many implementations just use 0
// addr - if the socket is already connected, you may pass `NULL` for `addr`
//        otherwise, the port and address of `addr` must match that in the connect call for the socket
int set_socket_codepoint(SOCKET socket, QOS_TRAFFIC_TYPE trafficType, QOS_FLOWID flowId, PSOCKADDR addr) {
    QOS_VERSION QosVersion = { 1 , 0 };
    HANDLE qosHandle;
    int sizeofTOS = sizeof(int);

    if (QOSCreateHandle(&QosVersion, &qosHandle) == FALSE) {
			//printf("%s:%d - QOSCreateHandle failed (%d)\n", __FILE__, __LINE__, GetLastError());
			return GetLastError();
    }
    if (QOSAddSocketToFlow(qosHandle, socket, addr, trafficType, QOS_NON_ADAPTIVE_FLOW, &flowId) == FALSE) {
			//printf("%s:%d - QOSAddSocketToFlow failed (%d)\n", __FILE__, __LINE__, GetLastError());
			return GetLastError();
    }
    return 0;
}

