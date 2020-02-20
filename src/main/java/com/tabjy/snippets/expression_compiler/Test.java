package com.tabjy.snippets.expression_compiler;

import java.util.List;

public class Test {
    public static void main(String[] args) throws IllegalSyntaxException {
//        new Tokenizer("org.package.name.ClassName.this.field.that[+0.1]").tokens.forEach(System.out::println);
        List<Tokenizer.Token> tokens = new Tokenizer("com.tabjy.snippets.expression_compiler.Test.this").getTokens();
        tokens.forEach(System.out::println);
        
        Parser parser = new Parser(tokens);
        parser.parse();
    }
}
