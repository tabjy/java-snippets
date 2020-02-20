package com.tabjy.snippets.expression_compiler;

import com.sun.tools.corba.se.idl.constExpr.Expression;
import org.eclipse.jdt.internal.compiler.ast.FieldReference;

import java.util.List;

/*
    Expression
        -> this
         | TypeName . this
         | FieldAccess

    TypeName
        -> TypeIdentifier
         | PackageOrTypeName Dot TypeIdentifier

    PackageOrTypeName 
        -> identifier
         | PackageOrTypeName . identifier

    TypeIdentifier
        -> identifier

    FieldAccess
        -> Expression . identifier
         | super . identifier
         | TypeName . super . identifier
         | FieldName

    FieldName
        -> identifier

    identifier // terminal symbols
        -> [A-z_]+[A-z0-9_]*
     */
public class ExpressionStateMachine {
    public static void main(String[] args) {
        ExpressionStateMachine fsm = new ExpressionStateMachine(ExpressionStateMachine.class, "this");
        
    } 

    private final Class<?> caller;
    private final String expression;

    private ExpressionStateMachine(Class<?> caller, String expression) {
        this.caller = caller;
        this.expression = expression;
    }
    
    private List<FieldReference> solve() {
        return null;
    }
}
