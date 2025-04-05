package com.examportal.entity;

import lombok.*;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ROLE")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class Role implements java.io.Serializable{

    private static final long serialVersionUID = 6899010265715612120L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id", unique = true, nullable = false)
    private Long roleId;

    @Column(name = "role_name", nullable = false)
    private String roleName;

    @Column(name = "created_ts", nullable = false)
    private Timestamp createdTs;
    
    
}
