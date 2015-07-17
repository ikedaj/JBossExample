package org.jboss.examples.ticketmonster.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Booking.class)
public abstract class Booking_ {

	public static volatile SetAttribute<Booking, Ticket> tickets;
	public static volatile SingularAttribute<Booking, Performance> performance;
	public static volatile SingularAttribute<Booking, String> contactEmail;
	public static volatile SingularAttribute<Booking, Long> id;
	public static volatile SingularAttribute<Booking, Date> createdOn;
	public static volatile SingularAttribute<Booking, String> cancellationCode;

}

