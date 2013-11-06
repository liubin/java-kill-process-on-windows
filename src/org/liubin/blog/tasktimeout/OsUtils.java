package org.liubin.blog.tasktimeout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class OsUtils {
    static class ReturnValue {
        public ReturnValue(int exitCode, String stdout) {
            this.exitCode = exitCode;
            this.stdout = stdout;
        }

        public int exitCode;
        public String stdout;

        @Override
        public String toString() {
            return "exitCode : " + exitCode + "; stdout " + stdout;
        }
    }

    /**
     * call an OS command.
     * 
     * @param cmd
     * @throws IOException
     * @throws InterruptedException
     */
    public static ReturnValue exec(String cmd) throws IOException,
            InterruptedException {

        // call OS command
        Process process = Runtime.getRuntime().exec(cmd);

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

    /**
     * kill an OS command
     * 
     * @param pid
     */
    public static void kill(long pid) {
        System.out.println("kill process id " + pid);
        String cmd = "TASKKILL /F /PID " + pid;
        ReturnValue rv;
        try {
            rv = OsUtils.exec(cmd);
            System.out.println("Kill result " + rv.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
