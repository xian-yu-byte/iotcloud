package com.atguigu.cloud.iotcloudspring.pojo.mqtt;

import com.atguigu.cloud.iotcloudspring.filter.enums.mqtt.MqttTopicConfigType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * mqtt主题表
 * @TableName mqtt_topic_config
 */
@TableName(value ="mqtt_topic_config")
@Data
public class MqttTopicConfig implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户id,  关联到用户表
     */
    private Long userId;

    /**
     * 项目id，关联到项目表
     */
    private Long projectId;

    /**
     * 设备id，关联到设备表
     */
    private Long deviceId;

    /**
     * 设备唯一标识
     */
    private String deviceKey;

    /**
     * 主题
     */
    private String topic;

    /**
     * 主题类型
     */
    private MqttTopicConfigType topicType;

    /**
     * 是否为后端自动订阅
     */
    private Integer autoSubscribed;

    /**
     * 表明主题是有效的
     */
    private Integer effective;

    /**
     * 描述
     */
    private String description;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        MqttTopicConfig other = (MqttTopicConfig) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getProjectId() == null ? other.getProjectId() == null : this.getProjectId().equals(other.getProjectId()))
            && (this.getDeviceId() == null ? other.getDeviceId() == null : this.getDeviceId().equals(other.getDeviceId()))
            && (this.getDeviceKey() == null ? other.getDeviceKey() == null : this.getDeviceKey().equals(other.getDeviceKey()))
            && (this.getTopic() == null ? other.getTopic() == null : this.getTopic().equals(other.getTopic()))
            && (this.getTopicType() == null ? other.getTopicType() == null : this.getTopicType().equals(other.getTopicType()))
            && (this.getAutoSubscribed() == null ? other.getAutoSubscribed() == null : this.getAutoSubscribed().equals(other.getAutoSubscribed()))
            && (this.getEffective() == null ? other.getEffective() == null : this.getEffective().equals(other.getEffective()))
            && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
            && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getProjectId() == null) ? 0 : getProjectId().hashCode());
        result = prime * result + ((getDeviceId() == null) ? 0 : getDeviceId().hashCode());
        result = prime * result + ((getDeviceKey() == null) ? 0 : getDeviceKey().hashCode());
        result = prime * result + ((getTopic() == null) ? 0 : getTopic().hashCode());
        result = prime * result + ((getTopicType() == null) ? 0 : getTopicType().hashCode());
        result = prime * result + ((getAutoSubscribed() == null) ? 0 : getAutoSubscribed().hashCode());
        result = prime * result + ((getEffective() == null) ? 0 : getEffective().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        result = prime * result + ((getUpdatedAt() == null) ? 0 : getUpdatedAt().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", projectId=").append(projectId);
        sb.append(", deviceId=").append(deviceId);
        sb.append(", deviceKey=").append(deviceKey);
        sb.append(", topic=").append(topic);
        sb.append(", topicType=").append(topicType);
        sb.append(", autoSubscribed=").append(autoSubscribed);
        sb.append(", effective=").append(effective);
        sb.append(", description=").append(description);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}