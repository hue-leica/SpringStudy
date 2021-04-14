package study.datajpa.entity;

import lombok.Getter;
import org.apache.tomcat.jni.Local;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@MappedSuperclass // 반드시 필요
@Getter
public class JpaBaseEntity {

    /* 실수로라도 변경되지 않도록 false 처리 */
    @Column(updatable = false)
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    /* 최초 Persist될 때 수행 */
    @PrePersist
    public void prePersist(){
        LocalDateTime now = LocalDateTime.now();
        createdDate = now;
        lastModifiedDate = now;
    }

    /* 이후 값이 변경될 때 마다 수행 */
    @PreUpdate
    public void preUpdate(){
        lastModifiedDate = LocalDateTime.now();
    }
}
