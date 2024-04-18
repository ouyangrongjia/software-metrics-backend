package com.example.softwaremetrics.metric;

import org.eclipse.jdt.core.dom.*;
import com.example.softwaremetrics.core.CKNumber;
import com.example.softwaremetrics.core.CKReport;
import java.util.HashSet;
import java.util.Set;

/**
 * CBO（Coupling Between Objects）指标用于衡量一个类与其他类之间的耦合程度，
 * 即一个类对其他类的依赖程度。高度的耦合会增加代码的复杂性和维护成本，
 * 因此 CBO 指标通常用于评估类的设计质量。
 * <p>
 * CBO 的计算方法基于类中与其他类的依赖关系数量，包括字段类型、方法返回类型、
 * 方法参数类型、方法抛出异常类型等等。每个与其他类相关的类型都会增加类的 CBO 值。
 * <p>
 * CBO 的计算公式如下：
 *   CBO = 类中与其他类相关的依赖关系数量
 * <p>
 * CBO 指标的值越高，表示类与其他类之间的依赖关系越多，耦合程度越高，
 * 类的设计越复杂。因此，为了降低类的耦合度，应该尽量减少类与其他类之间的依赖关系。
 */

public class CBO extends ASTVisitor implements Metric{
    private Set<String> coupling = new HashSet<String>();

    @Override
    public boolean visit(VariableDeclarationStatement node) {
        coupleTo(node.getType().resolveBinding());
        return super.visit(node);
    }

    @Override
    public boolean visit(ClassInstanceCreation node) {
        coupleTo(node.getType().resolveBinding());
        return super.visit(node);
    }

    @Override
    public boolean visit(ArrayCreation node) {
        coupleTo(node.getType().resolveBinding());
        return super.visit(node);
    }

    @Override
    public boolean visit(FieldDeclaration node) {
        coupleTo(node.getType().resolveBinding());
        return super.visit(node);
    }

    public boolean visit(ReturnStatement node) {
        if (node.getExpression() != null) {
            coupleTo(node.getExpression().resolveTypeBinding());
        }
        return super.visit(node);
    }

    @Override
    public boolean visit(TypeLiteral node) {
        coupleTo(node.resolveTypeBinding());
        coupleTo(node.getType().resolveBinding());
        return super.visit(node);
    }

    public boolean visit(ThrowStatement node) {
        coupleTo(node.getExpression().resolveTypeBinding());
        return super.visit(node);
    }

    public boolean visit(TypeDeclaration node) {
        ITypeBinding type = node.resolveBinding();

        ITypeBinding binding = type.getSuperclass();
        if (binding != null)
            coupleTo(binding);

        for (ITypeBinding interfaces : type.getInterfaces()) {
            coupleTo(interfaces);
        }

        return super.visit(node);
    }

    public boolean visit(MethodDeclaration node) {

        IMethodBinding method = node.resolveBinding();
        if (method == null)
            return super.visit(node);

        coupleTo(method.getReturnType());

        for (ITypeBinding param : method.getParameterTypes()) {
            coupleTo(param);
        }

        return super.visit(node);
    }

    @Override
    public boolean visit(CastExpression node) {
        coupleTo(node.getType().resolveBinding());

        return super.visit(node);
    }

    @Override
    public boolean visit(InstanceofExpression node) {

        coupleTo(node.getRightOperand().resolveBinding());
        coupleTo(node.getLeftOperand().resolveTypeBinding());

        return super.visit(node);
    }

    public boolean visit(NormalAnnotation node) {
        coupleTo(node.resolveTypeBinding());
        return super.visit(node);
    }

    public boolean visit(MarkerAnnotation node) {
        coupleTo(node.resolveTypeBinding());
        return super.visit(node);
    }

    public boolean visit(SingleMemberAnnotation node) {
        coupleTo(node.resolveTypeBinding());
        return super.visit(node);
    }

    public boolean visit(ParameterizedType node) {
        ITypeBinding binding = node.resolveBinding();
        if (binding == null)
            return super.visit(node);

        coupleTo(binding);

        for (ITypeBinding types : binding.getTypeArguments()) {
            coupleTo(types);
        }

        return super.visit(node);
    }

    private void coupleTo(ITypeBinding binding) {
        if (binding == null)
            return;
        if (binding.isWildcardType()) //通配符
            return;

        String type = binding.getQualifiedName();
        if (type.equals("null"))
            return;

        if (!isFromJava(type) && !binding.isPrimitive()) //Java标准库类和原始类型不统计
            coupling.add(type.replace("[]", ""));
    }

    private boolean isFromJava(String type) {
        return type.startsWith("java.") || type.startsWith("javax.");
    }

    @Override
    public void execute(CompilationUnit cu, CKNumber number, CKReport report) {
        cu.accept(this);
    }

    @Override
    public void setResult(CKNumber result) {
        result.setCbo(coupling.size());
    }
}
