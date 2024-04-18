package com.example.softwaremetrics.service;

import com.example.softwaremetrics.Common.Constant;
import com.example.softwaremetrics.core.CK;
import com.example.softwaremetrics.core.CKNumber;
import com.example.softwaremetrics.core.CKReport;
import com.example.softwaremetrics.pojo.CkPojo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CKJavaService {
    public List<CKNumber> start(List<File> targetFiles) {
        List<CKNumber> finalResult = new ArrayList<>();
        for (File file : targetFiles) {
            CKReport report = new CK().calculate(file.getAbsolutePath());
            for (CKNumber result : report.all()) {
                if (result.isError()) continue;
                finalResult.add(result);
            }
        }
        return finalResult;
    }
    public List<CkPojo> handleRequest(MultipartFile[] files) {
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

    private List<CkPojo> convertToVO(List<CKNumber> results) {
        List<CkPojo> pojos = new ArrayList<>();
        for (CKNumber result : results) {
            CkPojo pojo = new CkPojo();
            pojo.setName(result.getFile().split("###")[1]);
            pojo.setCLASS(result.getClassName());
            pojo.setType(result.getType());
            pojo.setWMC(String.valueOf(result.getWmc()));
            pojo.setRFC(String.valueOf(result.getRfc()));
            pojo.setLCOM(String.valueOf(result.getLcom()));
            pojo.setCBO(String.valueOf(result.getCbo()));
            pojo.setDIT(String.valueOf(result.getDit()));
            pojo.setNOC(String.valueOf(result.getNoc()));
            pojos.add(pojo);
        }
        return pojos;
    }
}