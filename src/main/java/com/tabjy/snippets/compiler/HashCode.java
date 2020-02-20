package com.tabjy.snippets.compiler;

import java.util.HashSet;

public class HashCode {
	public static void main(String[] args) {
		HashSet<Object> set = new HashSet();
		
		set.add(new MyObject());
		set.add(new MyObject());
		set.add(new MyObject());
		
		System.out.print(set.size());
		System.out.print(set.contains(new MyObject()));
	}
	
	public static class MyObject {
//		@Override
//		public int hashCode() {
////			return 13;
//			
//		}
		
		@Override
		public boolean equals(Object obj) {
//			if (obj.getClass() == this.getClass()) {
//                return hashCode() == obj.hashCode();
//            }
//			
//			return obj.hashCode() == hashCode();
			return true;
		}
	}
}
