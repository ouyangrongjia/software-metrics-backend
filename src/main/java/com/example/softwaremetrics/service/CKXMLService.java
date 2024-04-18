package com.example.softwaremetrics.service;

import com.example.softwaremetrics.Common.Constant;
import com.example.softwaremetrics.domain.CKBean;
import com.example.softwaremetrics.domain.Clazz;
import com.example.softwaremetrics.pojo.CkXmlItemPojo;
import com.example.softwaremetrics.pojo.CkXmlPojo;
import com.example.softwaremetrics.util.MetricUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CKXMLService {
    public List<CKBean> start(File targetFile) throws Exception {
        List<CKBean> finalResult = new ArrayList<>();

        if (targetFile != null) {
            URI uri = targetFile.toURI();
            URL url;
            url = uri.toURL();
            MetricUtil metricUtil = new MetricUtil();
            List<Clazz> classes = metricUtil.metric(url);

            for (Clazz e : classes) {
                CKBean bean = new CKBean(targetFile.getName(), e.getName(),
                        "class", String.valueOf(e.getWmc()),
                        String.valueOf(e.getWmc()), "0",
                        String.valueOf(e.getCbo()), String.valueOf(e.getDit()),
                        String.valueOf(e.getNoc()));
                finalResult.add(bean);
            }
        }
        return finalResult;
    }

    public List<CkXmlPojo> handleMultiRequest(MultipartFile[] files) {
        List<CkXmlPojo> result = new ArrayList<>();
        for (MultipartFile file : files) {
            CkXmlPojo vo = handleRequest(file);
            result.add(vo);
        }
        return result;
    }
    public CkXmlPojo handleRequest(MultipartFile file) {
        List<CKBean> result;
        try {
            File targetFile = new File(Constant.UPLOAD_PATH,
                    UUID.randomUUID() + "###" + file.getOriginalFilename());
            file.transferTo(targetFile);
            result = start(targetFile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        List<CkXmlItemPojo> ckVOS = convertToVO(result);
        return new CkXmlPojo(file.getOriginalFilename(), ckVOS);
    }
    private List<CkXmlItemPojo> convertToVO(List<CKBean> results) {
        List<CkXmlItemPojo> vos = new ArrayList<>();
        for (CKBean result : results) {
            CkXmlItemPojo ckVO = new CkXmlItemPojo();
            ckVO.setCLASS(result.getClazz());
            ckVO.setWMC(String.valueOf(result.getWmc()));
            ckVO.setRFC(String.valueOf(result.getRfc()));
            ckVO.setLCOM(String.valueOf(result.getLcom()));
            ckVO.setCBO(String.valueOf(result.getCbo()));
            ckVO.setDIT(String.valueOf(result.getDit()));
            ckVO.setNOC(String.valueOf(result.getNoc()));
            vos.add(ckVO);
        }
        return vos;
    }
}