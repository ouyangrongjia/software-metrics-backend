package com.example.softwaremetrics.metric;

import com.example.softwaremetrics.core.CKNumber;
import com.example.softwaremetrics.core.CKReport;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.core.dom.*;

/**
 * WMC (Weighted Methods per Class) 表示类的权重方法数，用于衡量类的复杂度。
 * 它是指类中方法的数量加权总和。
 * 计算 WMC 指标的公式为：
 * WMC = 方法数量加权总和，权重可认为是圈复杂度，即方法圈复杂度和
 */
public class WMC extends ASTVisitor implements Metric {

    protected int cc = 0;

    public boolean visit(MethodDeclaration node) {

        increaseCc();
        return super.visit(node);
    }

    @Override
    public boolean visit(ForStatement node) {
        increaseCc();

        return super.visit(node);
    }

    @Override
    public boolean visit(EnhancedForStatement node) {
        increaseCc();
        return super.visit(node);
    }

    @Override
    public boolean visit(ConditionalExpression node) {
        increaseCc();
        return super.visit(node);
    }

    @Override
    public boolean visit(DoStatement node) {
        increaseCc();
        return super.visit(node);
    }

    @Override
    public boolean visit(WhileStatement node) {
        increaseCc();
        return super.visit(node);
    }

    @Override
    public boolean visit(SwitchCase node) {
        if (!node.isDefault())
            increaseCc();
        return super.visit(node);
    }

    @Override
    public boolean visit(Initializer node) {
        increaseCc();
        return super.visit(node);
    }


    @Override
    public boolean visit(CatchClause node) {
        increaseCc();
        return super.visit(node);
    }

    public boolean visit(IfStatement node) {

        String expr = node.getExpression().toString().replace("&&", "&").replace("||", "|");
        int ands = StringUtils.countMatches(expr, "&");
        int ors = StringUtils.countMatches(expr, "|");

        increaseCc(ands + ors);
        increaseCc();

        return super.visit(node);
    }

    private void increaseCc() {
        increaseCc(1);
    }

    protected void increaseCc(int qtd) {
        cc++;
    }

    @Override
    public void execute(CompilationUnit cu, CKNumber number, CKReport report) {
        cu.accept(this);

    }

    @Override
    public void setResult(CKNumber result) {
        result.setWmc(cc);
    }


}
