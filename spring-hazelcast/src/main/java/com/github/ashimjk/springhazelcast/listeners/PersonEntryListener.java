package com.github.ashimjk.springhazelcast.listeners;

import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.EntryListener;
import com.hazelcast.map.MapEvent;

public class PersonEntryListener implements EntryListener {

    @Override
    public void entryAdded(EntryEvent event) {
        System.out.println("PersonEntryListener.entryAdded");
    }

    @Override
    public void entryEvicted(EntryEvent event) {
        System.out.println("PersonEntryListener.entryEvicted");
    }

    @Override
    public void entryExpired(EntryEvent event) {
        System.out.println("PersonEntryListener.entryExpired");
    }

    @Override
    public void entryRemoved(EntryEvent event) {
        System.out.println("PersonEntryListener.entryRemoved");
    }

    @Override
    public void entryUpdated(EntryEvent event) {
        System.out.println("PersonEntryListener.entryUpdated");
    }

    @Override
    public void mapCleared(MapEvent event) {
        System.out.println("PersonEntryListener.mapCleared");
    }

    @Override
    public void mapEvicted(MapEvent event) {
        System.out.println("PersonEntryListener.mapEvicted");
    }

}
