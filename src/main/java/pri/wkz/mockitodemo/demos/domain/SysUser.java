package pri.wkz.mockitodemo.demos.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * @author kuiZhi Wang
 */
@Entity
@Getter
@Setter
@Table(name = "sys_user", uniqueConstraints = {
        @UniqueConstraint(name = "UK_sys_user_email", columnNames = {"email"})
})
public class SysUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone")
    private String phone;
}
