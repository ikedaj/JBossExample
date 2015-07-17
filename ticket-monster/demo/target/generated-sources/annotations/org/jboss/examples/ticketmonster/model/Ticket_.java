package org.jboss.examples.ticketmonster.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Ticket.class)
public abstract class Ticket_ {

	public static volatile SingularAttribute<Ticket, Seat> seat;
	public static volatile SingularAttribute<Ticket, TicketCategory> ticketCategory;
	public static volatile SingularAttribute<Ticket, Float> price;
	public static volatile SingularAttribute<Ticket, Long> id;

}

