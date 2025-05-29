package com.atguigu.cloud.iotcloudspring.DTO.Device.Response;

import com.atguigu.cloud.iotcloudspring.DTO.Device.DataPointDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InferRequest {
    private List<DataPointDTO> data;
}
