package com.github.ashimjk.hazelcast.domain;

import com.github.ashimjk.hazelcast.serializations.PersonPortableFactory;
import com.hazelcast.nio.serialization.PortableReader;
import com.hazelcast.nio.serialization.PortableWriter;
import com.hazelcast.nio.serialization.VersionedPortable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.IOException;
import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Person implements VersionedPortable {

    public static final int CLASS_ID = 1;
    private static final int VERSION_ID = 1;
    private static final String ID_FIELD = "id";
    private static final String NAME_FIELD = "name";
    private static final String DOB_FIELD = "dob";
    private static final String EMAIL_FIELD = "email";

    @Id
    private Long id;
    private String name;
    private LocalDate dob;
    private String email;

    @Override
    public int getFactoryId() {
        return PersonPortableFactory.FACTORY_ID;
    }

    @Override
    public int getClassId() {
        return CLASS_ID;
    }

    @Override
    public int getClassVersion() {
        return VERSION_ID;
    }

    @Override
    public void writePortable(PortableWriter writer) throws IOException {
        writer.writeLong(ID_FIELD, id);
        writer.writeString(NAME_FIELD, name);
        writer.writeString(EMAIL_FIELD, email);
        writer.writeDate(DOB_FIELD, dob);
    }

    @Override
    public void readPortable(PortableReader reader) throws IOException {
        if (reader.hasField(ID_FIELD)) {
            id = reader.readLong(ID_FIELD);
        }
        if (reader.hasField(NAME_FIELD)) {
            name = reader.readString(NAME_FIELD);
        }
        if (reader.hasField(EMAIL_FIELD)) {
            email = reader.readString(EMAIL_FIELD);
        }
        if (reader.hasField(DOB_FIELD)) {
            dob = reader.readDate(DOB_FIELD);
        }
    }

}
