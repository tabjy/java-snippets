package com.tabjy.snippets.expression_compiler;

import org.openjdk.jmc.agent.util.expression.ReferenceChainElement;

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
    
    private List<ReferenceChainElement.FieldReference> solve() {
        return null;
    }
}
