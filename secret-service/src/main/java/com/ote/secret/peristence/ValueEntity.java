package com.ote.secret.peristence;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "T_SECRET_VALUE")
@NoArgsConstructor
public class ValueEntity extends SecretEntity {

    @Column(name = "VALUE")
    private String value;
}
