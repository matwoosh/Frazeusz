package pl.edu.agh.ki.frazeusz.gui.crawler;

/**
 * Created by Wojtek on 2017-01-25.
 */

public class BinarySemaphore {
    private int S = 1;       // 1 = Unlocked, 0 = Locked

    public synchronized void acquire() throws InterruptedException {
        while (S == 0) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        S = 0;
    }

    public synchronized void release() {
        S = 1;
        this.notifyAll();
    }
}