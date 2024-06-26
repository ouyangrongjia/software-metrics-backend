package com.example.softwaremetrics.metric;

import com.example.softwaremetrics.core.CKNumber;
import com.example.softwaremetrics.core.CKReport;
import org.eclipse.jdt.core.dom.*;

// Number of Static Invocations 静态方法的调用次数。
public class NOSI extends ASTVisitor implements Metric { //调用的静态方法数

    private int count = 0;

    public boolean visit(MethodInvocation node) {

        IMethodBinding binding = node.resolveMethodBinding();
        if (binding != null && Modifier.isStatic(binding.getModifiers())) {
            count++;
        }

        return super.visit(node);
    }

    @Override
    public void setResult(CKNumber result) {
        result.setNosi(count);
    }

    @Override
    public void execute(CompilationUnit cu, CKNumber number, CKReport report) {
        cu.accept(this);
    }

}
