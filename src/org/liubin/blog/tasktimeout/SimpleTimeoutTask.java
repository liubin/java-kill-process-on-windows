package org.liubin.blog.tasktimeout;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class SimpleTimeoutTask implements Callable<Boolean> {

    private String cmd;

    public SimpleTimeoutTask(String s) {
        this.cmd = s;
    }

    /**
     * run an OS command with timeout
     * 
     */
    public void test() {

        // make a thread pool.
        ExecutorService threadsPool = Executors.newSingleThreadExecutor();

        boolean b;

        // start a new thread.
        Future<Boolean> future = threadsPool.submit(this);
        try {
            // wait for time out
            b = future.get(10, TimeUnit.SECONDS);
            System.out.println(b);
        } catch (java.util.concurrent.TimeoutException te) {
            // cancel
            boolean bc = future.cancel(true);
            System.out.println("thread cancel resutl : " + bc);
        } catch (InterruptedException e) {
        } catch (ExecutionException e) {
        }
        // shutdown thread pool
        threadsPool.shutdownNow();
    }

    @Override
    public Boolean call() throws Exception {

        // this is a synchronized calling
        OsUtils.ReturnValue rv = OsUtils.exec(this.cmd);

        // return
        return rv.exitCode == 0;
    }
}