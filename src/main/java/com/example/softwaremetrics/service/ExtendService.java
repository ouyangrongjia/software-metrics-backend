package com.example.softwaremetrics.service;

import com.example.softwaremetrics.Common.Constant;
import com.example.softwaremetrics.core.CK;
import com.example.softwaremetrics.core.CKNumber;
import com.example.softwaremetrics.core.CKReport;
import com.example.softwaremetrics.pojo.ExtendPojo;
import org.apache.log4j.BasicConfigurator;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ExtendService {
    public List<CKNumber> start(List<File> targetFile) {
        List<CKNumber> finalResult = new ArrayList<>();
        BasicConfigurator.configure();
        for (File file : targetFile) {
            CKReport report = new CK().calculate(file.getAbsolutePath());
            for (CKNumber result : report.all()) {
                if (result.isError()) {
                    continue;
                }
                finalResult.add(result);
            }
        }
        return finalResult;
    }

    public List<ExtendPojo> handleRequest(MultipartFile[] files) {
        List<File> targetFiles = new ArrayList<>();
        for (MultipartFile file : files) {
            // 每个文件生成唯一的ID
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
        for (int i = 0; i < results.size(); i++) {
            results.get(i).setFile(targetFiles.get(i).getName().split("###")[1]);
        }
        return convertToVO(results);
    }

    private List<ExtendPojo> convertToVO(List<CKNumber> results) {
        List<ExtendPojo> pojos = new ArrayList<>();
        for (CKNumber result : results) {
            ExtendPojo pojo = new ExtendPojo();
            pojo.setName(result.getFile());
            pojo.setNOM(String.valueOf(result.getNom()));
            pojo.setNOPM(String.valueOf(result.getNopm()));
            pojo.setNOSM(String.valueOf(result.getNosm()));
            pojo.setNOF(String.valueOf(result.getNof()));
            pojo.setNOPF(String.valueOf(result.getNopf()));
            pojo.setNOSF(String.valueOf(result.getNosf()));
            pojo.setNOSI(String.valueOf(result.getNosi()));
            pojos.add(pojo);
        }

        return pojos;
    }
}

