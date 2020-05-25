package com.marius.dienorastis.dao;

import com.marius.dienorastis.models.Entry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EntryRepo extends JpaRepository<Entry, Integer> {
    @Query("select e from Entry e where e.user.id = ?1")
    List<Entry> getUserEntries(int userId);
}
