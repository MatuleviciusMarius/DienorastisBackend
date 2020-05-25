package com.marius.dienorastis;

import com.marius.dienorastis.dao.EntryRepo;
import com.marius.dienorastis.models.Entry;
import com.marius.dienorastis.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EntryService {

    @Autowired
    private EntryRepo repo;

    public List<Entry> listAll() {
        return repo.findAll();
    }

    public List<Entry> getUserEntries(User user) {
        return repo.getUserEntries(user.getId());
    }

    public void save(Entry entry) {
        repo.save(entry);
    }

    public Optional<Entry> get(Integer id) {
        return repo.findById(id);
    }

    public void delete(Integer id) {
        repo.deleteById(id);
    }
}
