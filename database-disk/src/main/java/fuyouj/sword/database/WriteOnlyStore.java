package fuyouj.sword.database;

import fuyouj.sword.database.disk.WriteOnlyObject;

public interface WriteOnlyStore {

    <T extends WriteOnlyObject> void append(T item);

    void clear(String name, Object id);
}
