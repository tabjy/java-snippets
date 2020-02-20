package com.tabjy.snippets.jfr;

import jdk.jfr.Configuration;
import jdk.jfr.Event;
import jdk.jfr.Recording;
import org.openjdk.jmc.flightrecorder.CouldNotLoadRecordingException;
import org.openjdk.jmc.flightrecorder.JfrLoaderToolkit;

import java.io.IOException;
import java.text.ParseException;

public class CharFields {

	static class MessageEvent extends Event {
		char message;
		long test;
	}

	public static void main(String... args) throws IOException, ParseException, CouldNotLoadRecordingException {
		Recording recording = new Recording(Configuration.getConfiguration("default"));
		recording.start();
		MessageEvent m1 = new MessageEvent();
		m1.message = '?';
		m1.commit();
		MessageEvent m2 = new MessageEvent();
		m2.message = '!';
		m2.commit();
		MessageEvent m3 = new MessageEvent();
		m3.message = '.';
		m3.commit();
		recording.stop();

		JfrLoaderToolkit.loadEvents(recording.getStream(recording.getStartTime(), recording.getStopTime()));
	}
}