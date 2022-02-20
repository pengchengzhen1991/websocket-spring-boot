package com.siwei.darwin.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Data
@Accessors(chain = true)
@MappedSuperclass
public class BaseEntity {

    /**
     * 主键id
     **/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 更新人
     */
    private String updatedBy;

    /**
     * 数据插入前的操作
     */
    @PrePersist
    public void setInsertBefore() {
        this.createTime = new Date();
        this.updateTime = new Date();
    }

    /**
     * 数据修改前的操作
     */
    @PreUpdate
    public void setUpdateBefore() {
        this.updateTime = new Date();
    }

}
