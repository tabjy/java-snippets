package com.tabjy.snippets.expression_compiler;

public class Ast {
    interface IExpression {
        
    }  
    
    public static class ThisExpression implements IExpression {
        
    }
    
    public static class OutwardsCastingExpressing implements IExpression {
        private final TypeName typeName;

        OutwardsCastingExpressing(TypeName typeName) {
            this.typeName = typeName;
        }
    }
    
    public static class FieldAccessExpression implements IExpression {
        public FieldAccessExpression(TypeName typeName, Tokenizer.Token identifier) {
            assert identifier.type == Tokenizer.Type.IDENTIFIER; // Production runtime check
        }
    }
    
    public static class ArrayAccessExpression implements IExpression {
        public ArrayAccessExpression(IExpression arrayExpression, IExpression indexExpression) {
            
        }
    }
    
    public static class TypeName {
        private final String packageName;
        private final String className;
        
        public TypeName(String packageName, String className) {
            this.packageName = packageName;
            this.className = className;
        }
    }
}
