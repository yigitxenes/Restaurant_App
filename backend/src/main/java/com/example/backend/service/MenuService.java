package com.example.backend.service;

import com.example.backend.entity.MenuItem;
import com.example.backend.repository.MenuItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {

    private final MenuItemRepository menuItemRepository;

    public MenuService(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    public List<MenuItem> getActiveMenu() {
        return menuItemRepository.findByIsActiveTrue();
    }
}
