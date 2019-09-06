package kr.co.apexsoft.jpaboot._support;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Access(AccessType.FIELD)
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class AbstractBaseEntity implements Serializable {

    @JsonIgnore
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    protected LocalDateTime createdDateTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    @LastModifiedDate
    @Column(name = "last_modified_at", updatable = true)
    protected LocalDateTime lastModifiedDateTime;

    @JsonIgnore
    @Column(name = "created_by", updatable = false)
    @CreatedBy
    protected String createdBy;

    @JsonIgnore
    @Column(name = "modified_by", updatable = true)
    @LastModifiedBy
    protected String modifiedBy;
}
