package org.liubin.blog.tasktimeout;

import java.util.Date;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Set;

import org.liubin.blog.tasktimeout.OsUtils.ReturnValue;

public class TimeoutTaskKiller implements Runnable {

    // hash table contains all tasks.
    // <key, value> is <PID,created_TS>
    private static Hashtable<Long, Long> processes;

    public TimeoutTaskKiller() {
    }

    @Override
    public void run() {
        while (true) {

            System.out.println("+++++++++ killer is comming");

            // get the latest task list for the command(PING.EXE)
            getTaskList();

            // do kill old job
            kill();

            System.out.println("+++++++++ killer is going");

            // simply sleep for the next job
            try {
                Thread.sleep(10 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * kill timeout job for every 10 seconds.
     */
    private void kill() {
        long now = System.currentTimeMillis();

        Set<Entry<Long, Long>> entries = (Set<Entry<Long, Long>>) processes
                .entrySet();

        for (Entry<Long, Long> e : entries) {
            long pid = e.getKey();
            long startedAt = e.getValue();

            System.out.println("pid " + pid + " started at "
                    + new Date(startedAt));
            if (now - startedAt > TaskExecutionTimeOutTest.TIMOUT_TASK_RUNNING_DURATION) {
                OsUtils.kill(pid);
            }
        }
    }

    /**
     * 
     * update the task list
     * 
     * make this method static is just for TaskWithPid to use the TASKLIST call.
     * 
     * @return
     */
    public static synchronized Hashtable<Long, Long> getTaskList() {

        String cmd = "TASKLIST /FI \"imagename eq "
                + TaskExecutionTimeOutTest.CMD_IMAGE_NAME_UNDER_TEST
                + "\" /svc /nh /fo csv";

        if (processes == null) {
            processes = new Hashtable<Long, Long>();
        }

        // step 1 get all new task's pid list
        Hashtable<Long, Long> oldProcesses = processes;

        processes = new Hashtable<Long, Long>();

        try {
            ReturnValue rv = OsUtils.exec(cmd);
            String[] ss = rv.stdout.split("\n");
            for (int i = 0; i < ss.length; i++) {
                String line = ss[i];
                if (line.toUpperCase().contains(
                        TaskExecutionTimeOutTest.CMD_IMAGE_NAME_UNDER_TEST)) {
                    // we use csv format ,so split it with ,
                    String[] cols = line.split(",");
                    if (cols.length == 3) {
                        String ping = cols[0];
                        if (ping.replace("\"", "")
                                .toUpperCase()
                                .equals(TaskExecutionTimeOutTest.CMD_IMAGE_NAME_UNDER_TEST)) {
                            String pid = cols[1];
                            try {
                                processes.put(
                                        Long.parseLong(pid.replace("\"", "")),
                                        System.currentTimeMillis());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return processes;
        }
        // step 2 . update old processes's lastticks

        HashtableUtils.merge(oldProcesses, processes);

        // for test
        dump();

        return processes;
    }

    private static void dump() {

        Set<Entry<Long, Long>> entries = (Set<Entry<Long, Long>>) processes
                .entrySet();

        System.out.println("begin dump processes. size is " + processes.size());
        for (Entry<Long, Long> e : entries) {
            System.out.println("PID:" + e.getKey() + ",started at : "
                    + new Date(e.getValue()));
        }
        System.out.println("end dump processes");

    }
}
