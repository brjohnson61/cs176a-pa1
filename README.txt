Documentation:

To compile:
Type javac *.java at the terminal of the directory containing the files.

Example:
csil-machineX > javac *.java

To run:
Type java followed by filename without the .java extension

Example:
csil-machineX > java server_java_tcp

Then, type a command followed by areguments, followed by ">" and a file name for the output.

Example:
csil-machineX > ls -a > temp;

TCP Writeup:
    This part of the assignment was pretty straightforward, because I have programmed using TCP in the past and in a different class this quarter.
    However, some difficulty arose when I tried to wrap too many buffered streams around the input/output streams, so I switch to Object Input/Output
    Streams for sending information to and from the socket.  Difficulty also arose when trying to make a system call to terminal, but I was able to
    understand how to do it after a little bit of searching.

UDP Writeup:
    By far the more difficult part of the assignment, using UDP to send messages back and forth from server top client has taught me a lot.  My final code
    is not perfect, becasue there are still some errors when sending some of the bigger files across the server.  However, transmission time is very fast, 
    and allows for "send and forget" (with ACK of course) style of sending messages. ALthough this is faster, more relies upon the programmer to make sure
    information reaches its destination.  I was able to implement the acknowledgement signal to verify each send as well as create up to 2 additional send tries.
    Overall, this project's hands on experience with UDP has taught me many things not only about protocol, but also about system calls and using abstract data types.

Conclusion/Thoughts:
    This project has taught me a lot about UDP.  I used website: http://www.baeldung.com/udp-in-java to gain knowledge about udp in general,
    and based my thought process around their sample server/client.  However, the code I have in my final project has evolved tremendously as both my
    code and understanding has completely evolved through debugging and changing my code around, so there are not any snippets in my project that I used directly from
    the code written on this site.  The other site I used for reference was the official oracle website to get the return values and information
    about methods built into the java libraries I am using.  For the tcp portion of this project I did not use any code snippets other than how to
    run a system command in java for linux.  I reference this location in my code.  The reason for this is that I am also enrolled in cs171 and I
    used tcp to communicate with multiple servers and clients simultaneously, so the knowledge of tcp that I used in this project was from prior 
    experience.  Thank you for taking the time to read this, and have a great day.

