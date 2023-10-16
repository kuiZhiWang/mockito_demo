package pri.wkz.mockitodemo.demos.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * @author kuiZhi Wang
 */
@Entity
@Getter
@Setter
@Table(name = "sys_cache", indexes = {
        @Index(name = "IDX_sys_cache_key_expired", columnList = "name,expired")
})
public class SysCache {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "value", nullable = false, columnDefinition = "text")
    private String value;
    @Column(name = "created", nullable = false, updatable = false)
    private long created;
    @Column(name = "expired", nullable = false)
    private long expired;
}
