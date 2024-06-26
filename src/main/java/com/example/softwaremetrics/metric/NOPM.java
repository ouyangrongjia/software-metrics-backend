package com.example.softwaremetrics.metric;

import com.example.softwaremetrics.core.CKNumber;
import com.example.softwaremetrics.core.CKReport;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;

// Number of Public Methods
public class NOPM extends ASTVisitor implements Metric{
    private int methods;

    @Override
    public boolean visit(MethodDeclaration node) {
        if (Modifier.isPublic(node.getModifiers())) {
            methods++;
        }

        return false;
    }

    @Override
    public void execute(CompilationUnit cu, CKNumber number, CKReport report) {
        cu.accept(this);
    }

    @Override
    public void setResult(CKNumber result) {
        result.setNopm(methods);
    }
}
