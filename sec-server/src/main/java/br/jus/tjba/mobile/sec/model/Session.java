package br.jus.tjba.mobile.sec.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by rudolfoborges on 19/04/17.
 */
@Entity
@Table(name = "sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Session {

    @Id
    @Size(max = 150)
    private String id;

    @NotNull
    @Size(max = 250)
    private String key;

    @NotNull
    @Size(max = 50)
    private String ip;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

}
