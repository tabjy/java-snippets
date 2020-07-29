package com.tabjy.snippets.asm.frames;

public class Target {
	public int echo(int arg) {
		if (arg > 42) {
			return 42;
		}
		return arg;
	}
	
	public int test() {
		int var = 42;
		return var;
	}
}
