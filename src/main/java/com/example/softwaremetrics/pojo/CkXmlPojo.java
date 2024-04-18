package com.example.softwaremetrics.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class CkXmlPojo {
    private String name;
    private List<CkXmlItemPojo> classes;
}
