package com.remake.poki.persistence.id;

import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.BeforeExecutionGenerator;
import org.hibernate.generator.EventType;
import org.hibernate.generator.OnExecutionGenerator;

import java.security.SecureRandom;
import java.util.EnumSet;

public class SecureRandomIdGenerator implements BeforeExecutionGenerator, OnExecutionGenerator {
    private static final SecureRandom RNG = new SecureRandom();

    @Override
    public EnumSet<EventType> getEventTypes() {
        return EnumSet.of(EventType.INSERT);
    }

    @Override
    public Object generate(SharedSessionContractImplementor session, Object owner, Object currentValue, EventType eventType) {
        if (currentValue != null) return currentValue;
        long v;
        do {
            v = RNG.nextLong() & Long.MAX_VALUE;
        } while (v == 0L);
        return v;
    }

    @Override
    public boolean referenceColumnsInSql(Dialect dialect) {
        return false;
    }

    @Override
    public boolean writePropertyValue() {
        return false;
    }

    @Override
    public String[] getReferencedColumnValues(Dialect dialect) {
        return new String[0];
    }

    @Override
    public boolean generatedOnExecution() {
        return false;
    }
}