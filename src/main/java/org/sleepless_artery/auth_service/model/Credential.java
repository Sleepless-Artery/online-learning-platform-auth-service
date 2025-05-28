package org.sleepless_artery.auth_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;


@Entity
@Table(name = "credentials")
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Credential implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email_address", nullable = false, unique = true)
    private String emailAddress;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "credentials_roles",
            joinColumns = @JoinColumn(name = "credential_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;
}
