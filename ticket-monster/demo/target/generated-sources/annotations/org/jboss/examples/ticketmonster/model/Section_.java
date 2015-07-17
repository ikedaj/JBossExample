package org.jboss.examples.ticketmonster.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Section.class)
public abstract class Section_ {

	public static volatile SingularAttribute<Section, Venue> venue;
	public static volatile SingularAttribute<Section, String> name;
	public static volatile SingularAttribute<Section, String> description;
	public static volatile SingularAttribute<Section, Integer> rowCapacity;
	public static volatile SingularAttribute<Section, Long> id;
	public static volatile SingularAttribute<Section, Integer> numberOfRows;

}

