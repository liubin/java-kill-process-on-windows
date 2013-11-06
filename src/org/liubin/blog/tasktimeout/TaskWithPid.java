package org.liubin.blog.tasktimeout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.liubin.blog.tasktimeout.OsUtils.ReturnValue;

public class TaskWithPid implements Callable<ReturnValue> {

    private String cmd;

    private Process process;

    public TaskWithPid(String s) {
        this.cmd = s;
    }

    @Override
    public ReturnValue call() throws Exception {

        // get input stream for STDOUT
        InputStream is = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        String line;

        String stdout = "";

        while ((line = reader.readLine()) != null) {
            stdout += line;
        }

        // wait to complete
        int retValue = process.waitFor();

        // close I/Os
        process.destroy();
        is.close();
        reader.close();

        ReturnValue rv = new ReturnValue(retValue, stdout);
        return rv;
    }

    public void test() throws IOException, InterruptedException {

        long newPid = -1L;

        // get now ping.exe's task list
        Hashtable<Long, Long> oldPids = TimeoutTaskKiller.getTaskList();

        // call OS command
        process = Runtime.getRuntime().exec(cmd);

        // get now ping.exe's task list
        Hashtable<Long, Long> newPids = TimeoutTaskKiller.getTaskList();

        // find out the new process id for ping.exe
        Hashtable<Long, Long> newElements = HashtableUtils.getNewElements(
                oldPids, newPids);

        if (newElements.size() > 0) { // It should be 1 !!
            // get the new process ID
            newPid = newElements.keys().nextElement();
        }

        System.out.println("new Pid is " + newPid);

        // since now we get a process ID,
        // so we can kill it after timeout

        // wait the task to finish and get all stdout.
        ReturnValue rv = waitForTask();

        if (rv == null) {
            // task execution is timeout
            // kill task
            OsUtils.kill(newPid);
        } else {
            System.out.println(rv.toString());
        }
    }

    private ReturnValue waitForTask() {
        // make a thread pool.
        ExecutorService threadsPool = Executors.newSingleThreadExecutor();

        // start a new thread.
        Future<ReturnValue> future = threadsPool.submit(this);
        try {
            // wait for time out
            return future.get(20, TimeUnit.SECONDS);

        } catch (java.util.concurrent.TimeoutException te) {
            // cancel
            boolean bc = future.cancel(true);
            System.out.println("thread cancel resutl : " + bc);
        } catch (InterruptedException e) {
        } catch (ExecutionException e) {
        } finally {
            // shutdown thread pool
            threadsPool.shutdownNow();
        }
        return null;
    }
}