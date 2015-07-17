package org.jboss.examples.ticketmonster.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Seat.class)
public abstract class Seat_ {

	public static volatile SingularAttribute<Seat, Integer> number;
	public static volatile SingularAttribute<Seat, Section> section;
	public static volatile SingularAttribute<Seat, Integer> rowNumber;

}

