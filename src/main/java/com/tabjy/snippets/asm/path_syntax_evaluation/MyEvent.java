package com.tabjy.snippets.asm.path_syntax_evaluation;

import jdk.jfr.Event;

public class MyEvent extends Event {
	public MyEvent() {
		super();
		throw new RuntimeException();
	}
}
