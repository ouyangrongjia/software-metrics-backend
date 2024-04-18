package com.example.softwaremetrics.service;

import com.example.softwaremetrics.Common.Constant;
import com.example.softwaremetrics.core.CK;
import com.example.softwaremetrics.core.CKNumber;
import com.example.softwaremetrics.core.CKReport;
import com.example.softwaremetrics.pojo.LkPojo;
import org.apache.log4j.BasicConfigurator;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class LKService {
    public List<CKNumber> start(List<File> targetFile) {
        List<CKNumber> finalResult = new ArrayList<>();
        BasicConfigurator.configure();
        for (File file : targetFile) {
            CKReport report = new CK().calculate(file.getAbsolutePath());
            for (CKNumber result : report.all()) {
                if (result.isError()) continue;
                finalResult.add(result);
            }
        }
        return finalResult;
    }

    public List<LkPojo> handleRequest(MultipartFile[] files) {
        List<File> targetFiles = new ArrayList<>();
        for (MultipartFile file : files) {
            File targetFile = new File(Constant.UPLOAD_PATH,
                    UUID.randomUUID() + "###" + file.getOriginalFilename());
            try {
                file.transferTo(targetFile);
                targetFiles.add(targetFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        List<CKNumber> results = start(targetFiles);
        return convertToVO(results);
    }

    private List<LkPojo> convertToVO(List<CKNumber> results) {
        List<LkPojo> vos = new ArrayList<>();
        for (CKNumber result : results) {
            LkPojo lkVO = new LkPojo();
            lkVO.setName(result.getFile().split("###")[1]);
            lkVO.setClazz(result.getClassName());
            lkVO.setType(result.getType());
            lkVO.setCS(String.valueOf(result.getWmc()));
            lkVO.setNOO(String.valueOf(result.getRfc()));
            lkVO.setNOA(String.valueOf(result.getLcom()));
            lkVO.setSI(String.valueOf(result.getCbo()));
            vos.add(lkVO);
        }
        return vos;
    }
}
