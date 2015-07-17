package org.jboss.examples.ticketmonster.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SectionAllocation.class)
public abstract class SectionAllocation_ {

	public static volatile SingularAttribute<SectionAllocation, Performance> performance;
	public static volatile SingularAttribute<SectionAllocation, Section> section;
	public static volatile SingularAttribute<SectionAllocation, Long> id;
	public static volatile SingularAttribute<SectionAllocation, Long> version;
	public static volatile SingularAttribute<SectionAllocation, long[][]> allocated;
	public static volatile SingularAttribute<SectionAllocation, Integer> occupiedCount;

}

