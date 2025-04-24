package com.atguigu.cloud.iotcloudspring.service;

//音色

import com.atguigu.cloud.iotcloudspring.Common.page.PageData;
import com.atguigu.cloud.iotcloudspring.Common.service.BaseService;
import com.atguigu.cloud.iotcloudspring.DTO.Ai.TimbrePageDTO;
import com.atguigu.cloud.iotcloudspring.DTO.Ai.VoiceDTO;
import com.atguigu.cloud.iotcloudspring.VO.TimbreDetailsVO;
import com.atguigu.cloud.iotcloudspring.pojo.Timbre.AiTtsVoice;

import java.util.List;

public interface TimbreService extends BaseService<AiTtsVoice> {
    /**
     * 根据ID获取音色名称
     *
     * @param id 音色ID
     * @return 音色名称
     */
    String getTimbreNameById(String id);

    /**
     * 分页获取音色指定tts的下的音色
     *
     * @param dto 分页查找参数
     * @return 音色列表分页数据
     */
    PageData<TimbreDetailsVO> page(TimbrePageDTO dto);

    List<VoiceDTO> getVoiceNames(String ttsModelId, String voiceName);

    /**
     * 获取音色指定id的详情信息
     *
     * @param timbreId 音色表id
     * @return 音色信息
     */
    TimbreDetailsVO get(String timbreId);
}
