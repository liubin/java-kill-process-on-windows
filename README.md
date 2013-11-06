## java-kill-process-on-windows

this is a test sample for use java call OS command and then kill it if it running for a long duration.
Under Linux that will be so easy and not be an issue :-)

background : execute a OS command and terminate it if it running over the given time.

### 1. how to make a long-running OS command time-out?
use `Callable` to run a thread and `future.get(time,timeunit)` to make a time-out event,there kill the OS command executed by Java.

### 2. how to find the Process ID that executed by `Runtime.getRuntime().exec()` ?
if this case ,we used Windows command TASKLIST for get a list of PIDs of the given IMAGENAME (Here is PING.EXE).


### 3. my approach
running a daemon thread to periodical get process list,then caculate the running duration then kill the over-time running processes.

See TimeoutTaskKiller.java

### 4. is there any easy and efficient way ?
If you know, please let me know too :-)


2013-11-06