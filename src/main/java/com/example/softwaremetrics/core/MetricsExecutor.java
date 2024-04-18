package com.example.softwaremetrics.core;

import com.example.softwaremetrics.metric.ClassInfo;
import com.example.softwaremetrics.metric.Metric;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FileASTRequestor;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class MetricsExecutor extends FileASTRequestor {
    private CKReport report;
    private Callable<List<Metric>> metrics;

    private static Logger log = Logger.getLogger(MetricsExecutor.class);

    public MetricsExecutor(Callable<List<Metric>> metrics) {
        this.metrics = metrics;
        this.report = new CKReport();
    }


    @Override
    public void acceptAST(String sourceFilePath,
                          CompilationUnit cu) {

        CKNumber result = null;

        try {
            ClassInfo info = new ClassInfo();
            cu.accept(info);
            if (info.getClassName() == null) return;

            result = new CKNumber(sourceFilePath, info.getClassName(), info.getType());

            int loc = new LOCCalculator().calculate(new FileInputStream(sourceFilePath));
            result.setLoc(loc);

            for (Metric visitor : metrics.call()) {
                visitor.execute(cu, result, report);
                visitor.setResult(result);
            }
//			log.info(result);
            report.add(result);
        } catch (Exception e) {
            if (result != null) result.error();
            log.error("error in " + sourceFilePath, e);
        }
    }

    public CKReport getReport() {
        return report;
    }
}
