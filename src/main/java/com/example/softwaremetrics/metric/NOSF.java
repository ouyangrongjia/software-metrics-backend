package com.example.softwaremetrics.metric;

import com.example.softwaremetrics.core.CKNumber;
import com.example.softwaremetrics.core.CKReport;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Modifier;

// Number of Static Fields
public class NOSF extends ASTVisitor implements Metric { // 静态变量数量

    private int fields;

    @Override
    public boolean visit(FieldDeclaration node) {
        if (Modifier.isStatic(node.getModifiers())) {
            fields++;
        }

        return false;
    }

    @Override
    public void execute(CompilationUnit cu, CKNumber number, CKReport report) {
        cu.accept(this);
    }

    @Override
    public void setResult(CKNumber result) {
        result.setNosf(fields);
    }
}
