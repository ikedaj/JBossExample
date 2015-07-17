package org.jboss.examples.ticketmonster.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Performance.class)
public abstract class Performance_ {

	public static volatile SingularAttribute<Performance, Date> date;
	public static volatile SingularAttribute<Performance, Show> show;
	public static volatile SingularAttribute<Performance, Long> id;

}

