package com.tabjy.snippets.meta;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.IntUnaryOperator;
import java.util.stream.Stream;

public class StreamTest {

	public static void main(String[] args) {
		Arrays.stream(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}).map(new IntUnaryOperator() {
			@Override
			public int applyAsInt(int operand) {
				return operand + 1;
			}
		}).filter( i -> {
			return i > 5;
		});
	}
}
