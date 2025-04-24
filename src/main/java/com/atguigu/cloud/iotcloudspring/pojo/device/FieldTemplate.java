package com.atguigu.cloud.iotcloudspring.pojo.device;

import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "field_template")
public class FieldTemplate implements Serializable {
    private Long id;
    private String fieldKey;
    private String displayName;
    private String dataType;
    private String defaultUnit;
    private String defaultValue;
    private String description;
}
