package com.github.ashimjk.hazelcast.serializations;

import com.github.ashimjk.hazelcast.domain.Person;
import com.hazelcast.nio.serialization.PortableFactory;

public class PersonPortableFactory implements PortableFactory {

    public static final int FACTORY_ID = 1;

    @Override
    public Person create(int classId) {
        if (classId == Person.CLASS_ID) {
            return new Person();
        }

        return null;
    }

}
