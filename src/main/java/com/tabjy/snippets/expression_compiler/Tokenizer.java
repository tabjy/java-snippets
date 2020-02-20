package com.tabjy.snippets.expression_compiler;


import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

/*
Expression syntax: basically a reduced definition of the primary expression from Java Language Specification

(Begin non terminal symbols)

S   
    -> Expression
     | FieldAccess (with Expression)
     | ArrayAccess (with Expression)

Expression
    -> ThisLiteral // ThisExpression
     | TypeName Dot ThisLiteral  // OutwardsCastingExpressing
     | FieldAccess
     | ArrayAccess
     | IntegerLiteral

TypeName // TypeNameIdentifiers
    -> TypeIdentifier
     | PackageOrTypeName Dot TypeIdentifier

PackageOrTypeName // PackageOrTypeName 
    -> Identifier
     | PackageOrTypeName Dot Identifier // TODO: left recursion needs to be resolved

TypeIdentifier // TypeIdentifier
    -> Identifier

FieldAccess // FieldAccessExpression
    -> Expression Dot Identifier
     | SuperLiteral Dot Identifier
     | TypeName Dot SuperLiteral Dot Identifier
     | FieldName

FieldName // FieldName
    -> Identifier

ArrayAccess // ArrayAccessExpression
    -> Expression LeftBracket Expression RightBracket // ExpressionConstantArrayAccessExpression
     | Expression LeftBracket IntegerLiteral RightBracket // ConstantArrayAccessExpression

IntegerLiteral
    -> DecimalLiteral

(Begin terminal symbols)

Identifier
    -> [A-z_]+[A-z0-9_]*
    
DecimalLiteral
    -> (-|+)?[0-9]+

ThisLiteral
    -> "this"

SuperLiteral
    -> "super"

Dot
    -> "."

LeftBracket
    : "["
    
RightBracket
    : "]"
 */

public class Tokenizer {

    public enum Type {
        IDENTIFIER,
        DECIMAL_LITERAL,
        THIS_LITERAL,
        SUPER_LITERAL,
        DOT, // "."
        L_BRACKET, // "["
        R_BRACKET // "]"
    }

    public static class Token {
        public final Type type;
        public final String lexeme;
        public final int pos;

        private static Pattern DECIMAL_NUM_PATTERN = Pattern.compile("[-+]?[0-9]+");
        private static Pattern IDENTIFIER_PATTERN = Pattern.compile("[A-z_]+[A-z0-9_]*");

        public Token(Type type, String lexeme, int pos) {
            this.type = type;
            this.lexeme = lexeme;
            this.pos = pos;
        }

        @Override
        public String toString() {
            if (type == Type.IDENTIFIER) {
                return "IDENTIFIER<" + lexeme + ">:" + pos;
            }

            if (type == Type.DECIMAL_LITERAL) {
                return "DECIMAL_LITERAL<" + lexeme + ">:" + pos;
            }

            return type.name() + ":" + pos;
        }

        public static Token from(String lexeme, int pos) throws IllegalSyntaxException {
            switch (lexeme) {
                case "[":
                    return new Token(Type.L_BRACKET, "[", pos);
                case "]":
                    return new Token(Type.R_BRACKET, "]", pos);
                case ".":
                    return new Token(Type.DOT, ".", pos);
                case "this":
                    return new Token(Type.THIS_LITERAL, "this", pos);
                case "super":
                    return new Token(Type.SUPER_LITERAL, "super", pos);
                    
            }

            if (DECIMAL_NUM_PATTERN.matcher(lexeme).matches()) {
                return new Token(Type.DECIMAL_LITERAL, lexeme, pos);
            }

            if (IDENTIFIER_PATTERN.matcher(lexeme).matches()) {
                return new Token(Type.IDENTIFIER, lexeme, pos);
            }

            throw new IllegalSyntaxException(String.format("unexpected symbol %s at position %d", lexeme, pos));
        }
    }

    private final String expression;
    private final List<Token> tokens = new LinkedList<>();

    public Tokenizer(String expression) throws IllegalSyntaxException {
        this.expression = expression;

        StringBuilder lexemeBuilder = new StringBuilder();
        int pos = 0;
        while (pos < expression.length()) {
            char c = expression.charAt(pos);
            String lexeme = lexemeBuilder.toString();
            if (lexeme.isEmpty() && Character.isWhitespace(c)) {
                continue; // skip white spaces
            }
            switch (c) {
//                case '(':
//                case ')':
                case '[':
                case ']':
                case '.':
                    if (!lexeme.isEmpty()) { // commit last token
                        tokens.add(Token.from(lexemeBuilder.toString(), pos - lexeme.length()));
                    }
                    lexemeBuilder = new StringBuilder();
                    tokens.add(Token.from(String.valueOf(c), pos));
                    break;
                default:
                    lexemeBuilder.append(c);
            }
            pos++;
        }

        // commit final token
        String lexeme = lexemeBuilder.toString();
        if (!lexeme.isEmpty()) {
            tokens.add(Token.from(lexemeBuilder.toString(), pos - lexeme.length()));
        }
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public String getExpression() {
        return expression;
    }
}
