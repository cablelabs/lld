/*               Copyright (C) 1993 Hewlett-Packard Company
                         ALL RIGHTS RESERVED.
 
  The enclosed software and documentation includes copyrighted works
  of Hewlett-Packard Co. For as long as you comply with the following
  limitations, you are hereby authorized to (i) use, reproduce, and 
  modify the software and documentation, and to (ii) distribute the 
  software and documentation, including modifications, for 
  non-commercial purposes only.
    
  1.  The enclosed software and documentation is made available at no
      charge in order to advance the general development of
      high-performance networking products.
 
  2.  You may not delete any copyright notices contained in the 
      software or documentation. All hard copies, and copies in
      source code or object code form, of the software or
      documentation (including modifications) must contain at least
      one of the copyright notices.
 
  3.  The enclosed software and documentation has not been subjected
      to testing and quality control and is not a Hewlett-Packard Co. 
      product. At a future time, Hewlett-Packard Co. may or may not 
      offer a version of the software and documentation as a product.
  
  4.  THE SOFTWARE AND DOCUMENTATION IS PROVIDED "AS IS".
      HEWLETT-PACKARD COMPANY DOES NOT WARRANT THAT THE USE,
      REPRODUCTION, MODIFICATION OR DISTRIBUTION OF THE SOFTWARE OR
      DOCUMENTATION WILL NOT INFRINGE A THIRD PARTY'S INTELLECTUAL
      PROPERTY RIGHTS. HP DOES NOT WARRANT THAT THE SOFTWARE OR
      DOCUMENTATION IS ERROR FREE. HP DISCLAIMS ALL WARRANTIES,
      EXPRESS AND IMPLIED, WITH REGARD TO THE SOFTWARE AND THE 
      DOCUMENTATION. HP SPECIFICALLY DISCLAIMS ALL WARRANTIES OF
      MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
  
  5.  HEWLETT-PACKARD COMPANY WILL NOT IN ANY EVENT BE LIABLE FOR ANY 
      DIRECT, INDIRECT, SPECIAL, INCIDENTAL OR CONSEQUENTIAL DAMAGES
      (INCLUDING LOST PROFITS) RELATED TO ANY USE, REPRODUCTION,
      MODIFICATION, OR DISTRIBUTION OF THE SOFTWARE OR DOCUMENTATION.
*/

/* Here is the inux/BSD-style code (taken from open source, netperf). 
   To set the same CS5 value that Windows uses for AudioVideo, pass in 0xA0 as socket_tos.
*/
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>

 
int set_socket_tos(SOCKET sock, int family, int socket_tos)
{
 
    int my_tos = -3; 
    socklen_t sock_opt_len;
 
    switch (family)
    {
#if defined(IP_TOS)
    case AF_INET:
        /* should I mask-away anything above the byte? */
        my_tos = socket_tos;
        if (setsockopt(sock,
                       IPPROTO_IP,
                       IP_TOS,
                       (const char *)&my_tos, sizeof(my_tos)) == SOCKET_ERROR)
        {     
            fprintf(where,
                    "%s ip_tos failed with %s (errno %d)\n",
                    __FUNCTION__,
                    strerror(errno),
                    errno);
            fflush(where);
            my_tos = -2; 
        }     
        else  
        {     
            sock_opt_len = sizeof(my_tos);
            getsockopt(sock,
                       IPPROTO_IP,
                       IP_TOS,
                       (char *)&my_tos,
                       &sock_opt_len);
        }     
        break;
#endif
#if defined(IPV6_TCLASS)
    case AF_INET6:
        /* should I mask-away anything above the byte? */
        my_tos = socket_tos;
        if (setsockopt(sock,
                       IPPROTO_IPV6,
                       IPV6_TCLASS,
                       (const char *)&my_tos, sizeof(my_tos)) == SOCKET_ERROR)
        {     
            fprintf(where,
                    "%s ip_tos failed with %s (errno %d)\n",
                    __FUNCTION__,
                    strerror(errno),
                    errno);
            fflush(where);
            my_tos = -2; 
        }     
        else  
        {     
            sock_opt_len = sizeof(my_tos);
            getsockopt(sock,
                       IPPROTO_IPV6,
                       IPV6_TCLASS,
                       (char *)&my_tos,
                       &sock_opt_len);
        }     
        break;
#endif
    }
    return my_tos;
}

