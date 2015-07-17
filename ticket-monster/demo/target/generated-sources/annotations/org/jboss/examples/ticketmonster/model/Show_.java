package org.jboss.examples.ticketmonster.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Show.class)
public abstract class Show_ {

	public static volatile SingularAttribute<Show, Venue> venue;
	public static volatile SetAttribute<Show, TicketPrice> ticketPrices;
	public static volatile SingularAttribute<Show, Long> id;
	public static volatile SingularAttribute<Show, Event> event;
	public static volatile SetAttribute<Show, Performance> performances;

}

