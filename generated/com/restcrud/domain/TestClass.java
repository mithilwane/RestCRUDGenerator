package com.restcrud.domain;

import java.lang.Boolean;
import java.lang.Long;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;

@Data
@MappedSuperclass
@Inheritance(
    strategy = InheritanceType.TABLE_PER_CLASS
)
public class TestClass {
  @Column(
      name = "id"
  )
  private Long id;

  @Temporal(TemporalType.TIMESTAMP)
  private Date createdDate;

  @Temporal(TemporalType.TIMESTAMP)
  private Date deletedDate;

  @Column(
      name = "active"
  )
  private Boolean active;
}
