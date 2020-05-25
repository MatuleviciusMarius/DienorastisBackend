package com.marius.dienorastis.controllers;

import com.marius.dienorastis.EntryService;
import com.marius.dienorastis.MyUserDetailsService;
import com.marius.dienorastis.models.Entry;
import com.marius.dienorastis.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
public class AppController {

    @Autowired
    private EntryService entryService;
    @Autowired
    MyUserDetailsService userDetailsService;

    @RequestMapping("/")
    public String viewHomePage(Model model) {
        List<Entry> listEntries = entryService.listAll();
        model.addAttribute("listEntries", listEntries);
        return "index";
    }

    @GetMapping("/get")
    public List<Entry> getEntries() {
        if (!Objects.isNull(userDetailsService.getCurrentUser()))
            return entryService.getUserEntries(userDetailsService.getCurrentUser());
        else
            return null;
    }

    @PostMapping(value = "/save", consumes = "application/json", produces = "application/json")
    public Entry saveEntry(@RequestBody Entry entry) {
        entry.setUser(userDetailsService.getCurrentUser());
        entryService.save(entry);
        return entry;
    }

    @DeleteMapping("/delete/{id}")
    public String deleteEntryById(@PathVariable Integer id) {
        if (!Objects.isNull(entryService.get(id))) {
            entryService.delete(id);
            return "Entry deleted";
        } else
            return "Entry with id : " + id + " does not exist";
    }

    @PutMapping("/update/{id}")
    public Entry updateEntry(@RequestBody Entry entry, @PathVariable(name = "id") int id) {
        if (!Objects.isNull(entryService.get(id)))
        entryService.delete(id);
        else return null;

        entry.setUser(userDetailsService.getCurrentUser());
        entryService.save(entry);
        return entry;
    }

    @PostMapping(value = "/register", consumes = "application/json")
    public void registerUser(@RequestBody User user) {
        if (isValidEmail(user.getEmail()) && !StringUtils.isEmpty(user.getPassword())) {
            userDetailsService.registerUser(user);
        }
    }

    static boolean isValidEmail(String email) {
        if (StringUtils.isEmpty(email))
            return false;
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

}
