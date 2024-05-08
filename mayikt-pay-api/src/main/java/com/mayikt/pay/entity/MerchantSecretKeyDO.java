package com.mayikt.pay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author zengjing
 * @since 2024-01-07
 */
@TableName("merchant_secret_key")
public class MerchantSecretKeyDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

    private String merchantId;

    private String appId;

    private String appKey;

    /**
     * 0正常 ，1停止
     */
    private Integer disabled;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;
    private String saltKey;
    private String permissionList;

    public String getPermissionList() {
        return permissionList;
    }


    public void setPermissionList(String permissionList) {
        this.permissionList = permissionList;
    }

    public String getSaltKey() {
        return saltKey;
    }

    public void setSaltKey(String saltKey) {
        this.saltKey = saltKey;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public Integer getDisabled() {
        return disabled;
    }

    public void setDisabled(Integer disabled) {
        this.disabled = disabled;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public String toString() {
        return "MerchantSecretKeyDO{" +
                "id=" + id +
                ", merchantId='" + merchantId + '\'' +
                ", appId='" + appId + '\'' +
                ", appKey='" + appKey + '\'' +
                ", disabled=" + disabled +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                ", saltKey='" + saltKey + '\'' +
                ", permissionList='" + permissionList + '\'' +
                '}';
    }
}
