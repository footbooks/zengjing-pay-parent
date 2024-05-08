package com.mayikt.pay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author zengjing
 * @since 2023-12-30
 */
@TableName("pay_merchant_info")
public class PayMerchantInfoDO implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 商户id
     */
    private String merchantId;

    /**
     * 企业名称
     */
    private String enterpriseName;

    /**
     * 社会信用代码
     */
    private String socialCreditCode;

    /**
     * 营业执照图片
     */
    private String businessLicenseUrl;

    /**
     * 法人
     */
    private String juridicalPerson;

    /**
     * 法人身份证信息
     */
    private String juridicalPersonUrl;

    /**
     * 联系人s
     */
    private String contacts;

    /**
     * 联系人手机号码
     */
    private String contactNumber;

    /**
     * 联系人微信openid
     */
    private String contactWxOpenid;

    /**
     * 审核状态 0未审核1审核通过2审核拒绝
     */
    private Integer auditStatus;

    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 修改时间
     */
    private Date updateDate;


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

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getSocialCreditCode() {
        return socialCreditCode;
    }

    public void setSocialCreditCode(String socialCreditCode) {
        this.socialCreditCode = socialCreditCode;
    }

    public String getBusinessLicenseUrl() {
        return businessLicenseUrl;
    }

    public void setBusinessLicenseUrl(String businessLicenseUrl) {
        this.businessLicenseUrl = businessLicenseUrl;
    }

    public String getJuridicalPerson() {
        return juridicalPerson;
    }

    public void setJuridicalPerson(String juridicalPerson) {
        this.juridicalPerson = juridicalPerson;
    }

    public String getJuridicalPersonUrl() {
        return juridicalPersonUrl;
    }

    public void setJuridicalPersonUrl(String juridicalPersonUrl) {
        this.juridicalPersonUrl = juridicalPersonUrl;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getContactWxOpenid() {
        return contactWxOpenid;
    }

    public void setContactWxOpenid(String contactWxOpenid) {
        this.contactWxOpenid = contactWxOpenid;
    }

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }


    @Override
    public String toString() {
        return "PayMerchantInfo{" +
        "id=" + id +
        ", merchantId=" + merchantId +
        ", enterpriseName=" + enterpriseName +
        ", socialCreditCode=" + socialCreditCode +
        ", businessLicenseUrl=" + businessLicenseUrl +
        ", juridicalPerson=" + juridicalPerson +
        ", juridicalPersonUrl=" + juridicalPersonUrl +
        ", contacts=" + contacts +
        ", contactNumber=" + contactNumber +
        ", contactWxOpenid=" + contactWxOpenid +
        ", auditStatus=" + auditStatus +
        ", createDate=" + createDate +
        ", updateDate=" + updateDate +
        "}";
    }
}
