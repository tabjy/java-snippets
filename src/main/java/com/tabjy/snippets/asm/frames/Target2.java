package com.tabjy.snippets.asm.frames;

public class Target2 {
	private Object object = new Object();

	public boolean test(Object object, long timeout) throws InterruptedException {
		if (!this.object.equals(object)) {
			return false;
		}

		this.object = object;
		return true;
	}
} 