package com.tabjy.snippets.graal;

import jdk.jfr.Category;
import jdk.jfr.Description;
import jdk.jfr.Event;
import jdk.jfr.Label;

public class HelloJfr {
    @Label("Clock Event")
    @Description("+1s as the time goes")
    @Category("Clock")
    public static class ClockEvent extends Event {
        @Label("Time")
        @Description("Time in millis.")
        private long timeMillis;

        public ClockEvent() {
            timeMillis = System.currentTimeMillis();
        }

        public void setTimeMillis(long timeMillis) {
            this.timeMillis = timeMillis;
        }

        public long getTimeMillis() {
            return timeMillis;
        }

        @Override
        public String toString() {
            return timeMillis + "ms, " + (timeMillis / 1000 % 2 == 0 ? "tik" : "tok");
        }
    }

    ClockEvent event;

    public HelloJfr() {
        event = new ClockEvent();

        System.out.println(event);
    }

    public void run() throws InterruptedException {
        event.begin();
        Thread.sleep(1000);
        event.end();
        event.commit();
    }

    public static void main(String[] args) throws InterruptedException {
        HelloJfr i = new HelloJfr();

        System.out.println(Math.random());
    }
}
