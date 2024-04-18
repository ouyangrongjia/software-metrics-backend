package com.example.softwaremetrics.metric;

import com.example.softwaremetrics.core.CKNumber;
import com.example.softwaremetrics.core.CKReport;
import org.eclipse.jdt.core.dom.CompilationUnit;

public interface Metric {
    void execute(CompilationUnit cu, CKNumber result, CKReport report);

    void setResult(CKNumber result);
}
