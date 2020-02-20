package com.tabjy.snippets.expression_compiler;

import java.util.Iterator;
import java.util.List;

// a recursive descent parser

public class Parser {
    private final TokenStream tokens;

    public Parser(List<Tokenizer.Token> tokens) {
        this.tokens = new TokenStream(tokens);
    }

    private static class TokenStream implements Iterator<Tokenizer.Token> {
        private final List<Tokenizer.Token> tokens; 
        private int pos = 0;

        public TokenStream(List<Tokenizer.Token> tokens) {
            this.tokens = tokens;
        }

        public int getPos() {
            return pos;
        }
        
        public void setPos(int pos) {
            this.pos = pos;
        }
        
        public Tokenizer.Token at(int pos) {
            if (pos < 0 || pos >= tokens.size()) {
                return null;
            }

            return tokens.get(pos);
        }

        public Tokenizer.Token current() {
            return at(pos);
        }
        
        public Tokenizer.Token next() {
            pos++;
            return current();
        }

        public Tokenizer.Token peek(int advance) {
            return at(pos + advance);
        }

        public Tokenizer.Token peek() {
            return peek(1);
        }

        public boolean hasMoreThan(int advance) {
            return pos + advance < tokens.size();
        }

        public boolean hasNext() {
            return hasMoreThan(0);
        }
    }

    /*
    S -> Expression
     */
    public  void parse() throws IllegalSyntaxException {
//        tokens.next();
        tokens.setPos(0);
        Ast.IExpression expression = parseExpression();
    }
    
    /*
    Expression -> "this"
                | TypeName "." "this"
                | FieldAccess
                | ArrayAccess
     */
    private Ast.IExpression parseExpression() throws IllegalSyntaxException {
        if (tokens.current() == null) {
            throw new IllegalSyntaxException("unexpected end of input: expects an expression");
        }

        if (tokens.current().type == Tokenizer.Type.THIS_LITERAL && !tokens.hasNext()) {
             return new Ast.ThisExpression();
        }

        int pos = tokens.getPos();

        try {
            Ast.TypeName type = parseTypeName();
            if (!tokens.hasMoreThan(1) || tokens.next().type != Tokenizer.Type.DOT || tokens.next().type != Tokenizer.Type.THIS_LITERAL) {
                throw new IllegalSyntaxException();
            }
            return new Ast.OutwardsCastingExpressing(type);
        } catch (IllegalSyntaxException e) {
            tokens.setPos(pos);
        }

        try {
            return parseFieldAccess();
        }  catch (IllegalSyntaxException e) {
            tokens.setPos(pos);
        }

        return parseArrayAccess();
    }

    private Ast.TypeName parseTypeName() throws IllegalSyntaxException {
        if (tokens.current() == null) {
            throw new IllegalSyntaxException("unexpected end of input: expects class or package identifier");
        }

        int pos = tokens.getPos();
        
        // TODO: check with caller class and its class loader
        try {
          String pkg = Parser.class.getPackage().getName();
          if (!pkg.isEmpty()) {
              pkg += ".";
          }
          Class<?> target = Parser.class.getClassLoader().loadClass(pkg + tokens.current().lexeme);
          return new Ast.TypeName(target.getPackageName(), target.getSimpleName());
        } catch (ClassNotFoundException e) {
            tokens.setPos(pos);
        }

        try {
            String pkg = Parser.class.getPackage().getName();
            if (!pkg.isEmpty()) {
                pkg += ".";
            }
            Class<?> target = Parser.class.getClassLoader().loadClass(pkg + tokens.current().lexeme);
            return new Ast.TypeName(target.getPackageName(), target.getSimpleName());
        } catch (ClassNotFoundException e) {
            tokens.setPos(pos);
        }

        // TODO: check nested class
        return parsePartialTypeName("");
    }
    
    private Ast.TypeName parsePartialTypeName(String partialTypeName) throws IllegalSyntaxException {
        if (tokens.current() == null || tokens.current().type != Tokenizer.Type.IDENTIFIER) {
            throw new IllegalSyntaxException("unexpected end of input: expects a class or a package identifier fragment");
        }

        String fragment = partialTypeName;
        if (!fragment.isEmpty()) {
            fragment += ".";
        }
        fragment += tokens.current().lexeme;

        try {
            // TODO: check with caller class and its class loader
            Class<?> target = Parser.class.getClassLoader().loadClass(fragment);
            return new Ast.TypeName(target.getPackage().getName(), target.getSimpleName());
        } catch (ClassNotFoundException e) {
            // no op
        }

        if (!tokens.hasNext()) {
            throw new IllegalSyntaxException("unexpected end of input: expects a class or a package identifier fragment");
        }

        if (tokens.next().type != Tokenizer.Type.DOT) {
            throw new IllegalSyntaxException("unexpected token type: expects Type.DOT but got " + tokens.current().type.name());
        }

        tokens.next();
        return parsePartialTypeName(fragment);
    }
    
    private Ast.FieldAccessExpression parseFieldAccess() throws IllegalSyntaxException {
        return null;
    }
    
    private Ast.ArrayAccessExpression parseArrayAccess() throws IllegalSyntaxException {
        return null;
    }
}
