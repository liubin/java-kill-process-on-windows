package org.liubin.blog.tasktimeout;

public class TaskExecutionTimeOutTest {

    // which command is under test.
    public final static String CMD_IMAGE_NAME_UNDER_TEST = "PING.EXE";

    // command will cause timeout.
    public final static String CMD_TIME_OUT = "ping -n 6000 127.0.0.1";

    // command that will NOT cause timeout.
    public final static String CMD_NOT_TIME_OUT = "ping -n 3 127.0.0.1";

    // how long can a task running .
    // used by killer thread to kill timeout task.
    // 120 seconds, in millisecond
    public final static int TIMOUT_TASK_RUNNING_DURATION = 120 * 1000;

    public static void main(String[] args) {

        // timeout task killer thread
        (new Thread(new TimeoutTaskKiller())).start();

        // test case 1 simple timeout task test.
        // add a PING.EXE thread per 20 seconds for 10 times
        // and after 120 seconds ,
        // TimeoutTaskKiller will kill PING.exe every 10 seconds
        new Thread() {
            @Override
            public void run() {

                SimpleTimeoutTask stt;
                for (int i = 0; i < 10; i++) {
                    stt = new SimpleTimeoutTask(CMD_TIME_OUT);
                    stt.test();
                    try {
                        Thread.sleep(20 * 1000);
                    } catch (InterruptedException e1) {
                    }
                }
            }
        }.start();

        // test case 2 TaskWithPid will kill the started process when time out.
        TaskWithPid twp = new TaskWithPid(CMD_TIME_OUT);
        try {
            twp.test();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
