package com.example.softwaremetrics.controller;

import com.example.softwaremetrics.Common.CommonResponse;
import com.example.softwaremetrics.service.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@RestController
@RequestMapping("/metrics")
@CrossOrigin(origins = "*")
public class MetricsController {
    @Resource
    private CKXMLService ckXMLService;

    @Resource
    private CKJavaService ckJavaService;

    @Resource
    private TraditionService traditionService;

    @Resource
    private LKService lkService;

    @Resource
    private ExtendService extendService;


    @PostMapping("/ck/xml")
    public CommonResponse<?> ckXML(@RequestParam("file") MultipartFile[] files) {
        System.out.println("================  CK XML ===============");
        System.out.println(Arrays.toString(files));
        System.out.println("=====================================");
        return CommonResponse.createForSuccess(ckXMLService.handleMultiRequest(files));
    }

    @PostMapping("/ck/java")
    public CommonResponse<?> ckJava(@RequestParam("file") MultipartFile[] files, ServletRequest request, ServletResponse response) {
        System.out.println("================  CK  ===============");
        RequestCors((HttpServletRequest) request, (HttpServletResponse) response);
        System.out.println(Arrays.toString(files));
        System.out.println("=====================================");
        return CommonResponse.createForSuccess(ckJavaService.handleRequest(files));
    }

    @PostMapping("/tradition")
    public CommonResponse<?> tradition(@RequestParam("file") MultipartFile[] files, ServletRequest request, ServletResponse response) {
        System.out.println("============  TRADITION  ============");
        RequestCors((HttpServletRequest) request, (HttpServletResponse) response);
        System.out.println(Arrays.toString(files));
        System.out.println("=====================================");
        return CommonResponse.createForSuccess(traditionService.handleRequest(files));
    }

    @PostMapping("/lk")
    public CommonResponse<?> lk(@RequestParam("file") MultipartFile[] files, ServletRequest request, ServletResponse response) {
        System.out.println("================  LK  ==============");
        RequestCors((HttpServletRequest) request, (HttpServletResponse) response);
        System.out.println(Arrays.toString(files));
        System.out.println("=====================================");
        return CommonResponse.createForSuccess(lkService.handleRequest(files));
    }

    @PostMapping("/extend")
    public CommonResponse<?> extend(@RequestParam("file") MultipartFile[] files, ServletRequest request, ServletResponse response) {
        System.out.println("==============  EXTEND  =============");
        System.out.println(Arrays.toString(files));
        RequestCors((HttpServletRequest) request, (HttpServletResponse) response);
        System.out.println("=====================================");
        return CommonResponse.createForSuccess(extendService.handleRequest(files));
    }

    private static void RequestCors(HttpServletRequest request, HttpServletResponse response) {
        HttpServletRequest httpServletRequest = request;
        HttpServletResponse httpServletResponse = response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE,PATCH");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", "Origin, X-Request-With, Authorization, Content-Type, Accept");
        httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpServletResponse.setHeader("Access-Control-Max-Age", "3600");
    }
}