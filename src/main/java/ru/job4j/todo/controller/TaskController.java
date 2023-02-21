package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.CategoryService;
import ru.job4j.todo.service.PriorityService;
import ru.job4j.todo.service.TaskService;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TimeZone;


@Controller
@AllArgsConstructor
@RequestMapping("/task")
public class TaskController {

    private final CategoryService categoryService;

    private final TaskService taskService;

    @GetMapping("/list")
    public String getIndex(@SessionAttribute User user,  Model model) {
        var zoneId = user.getZone().toZoneId();
        var userTasks = taskService.findAllByUser(user.getId());
        userTasks.forEach(p -> p.setCreated(p.getCreated()
                        .atZone(TimeZone.getDefault().toZoneId()).withZoneSameInstant(zoneId).toLocalDateTime()));
        model.addAttribute("tasks", new HashSet<>(userTasks));
        return "task/list";
    }

    @GetMapping("/create")
    public String getCreatePage(Model model) {
        var task = new Task();
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("task", task);
        model.addAttribute("categoryId", new ArrayList<Integer>());
        return "task/create";
    }

    @PostMapping("/create")
    public String create(@SessionAttribute User user, @ModelAttribute Task task,
                         @RequestParam("categoryId") List<Integer> categoryId, Model model) {
        task.setUser(user);
        var allCat = categoryService.findAll();
        var categories = categoryId.stream().map(p -> {
            return allCat.get(p - 1);
        }).toList();
        task.getCategories().addAll(categories);
        if (taskService.create(task)) {
            model.addAttribute("message", "Ошибка создания задачи");
            return "errors/404";
        }
        return "redirect:/task/list";
    }

    @PostMapping("/update")
    public String update(@SessionAttribute User user, @ModelAttribute Task task,
                         @RequestParam("categoryId") List<Integer> categoryId, Model model) {
        task.setUser(user);
        var allCat = categoryService.findAll();
        var categories = categoryId.stream().map(p -> {
            return allCat.get(p - 1);
        }).toList();
        task.getCategories().addAll(categories);
        if (!taskService.update(task)) {
            model.addAttribute("message", "Ошибка при редактировании задачи");
            return "errors/404";
        }
        return "redirect:/task/list";
    }
    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id) {
        var optional = taskService.findById(id);
        if (optional.isEmpty()) {
            model.addAttribute("message", "Заметка не найдена");
            return "errors/404";
        }
        var task = optional.get();
        var categoriesId = task.getCategories().stream().map(Category::getId).toList();
        var categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("task", task);
        model.addAttribute("categoriesId", categoriesId);
        return "task/look";
    }

    @GetMapping("/complete/{id}")
    public String complete(@PathVariable int id, Model model) {
        var task = taskService.findById(id).get();
        task.setDone(true);
        taskService.update(task);
        return "redirect:/task/list";
    }
    @GetMapping("/edit/{id}")
    public String getByIdEdit(Model model, @PathVariable int id) {
        model.addAttribute("categories", categoryService.findAll());
        var optional = taskService.findById(id);
        if (optional.isEmpty()) {
            model.addAttribute("message", "Заметка не найдена");
            return "errors/404";
        }
        model.addAttribute("task", optional.get());
        return "task/one";
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable int id) {
        if (!taskService.deleteById(id)) {
            model.addAttribute("message", "Ошибка при удалении задачи");
            return "errors/404";
        }
        return "redirect:/task/list";
    }

    @GetMapping("/onlyDone")
    public String getIndexOnlyDone(@SessionAttribute User user, Model model) {
            model.addAttribute("tasks", taskService.filterBy(true, user.getId()));
            return "task/list";
    }

    @GetMapping("/onlyNotDone")
    public String getIndexOnlyNotDone(@SessionAttribute User user, Model model) {
            model.addAttribute("tasks", taskService.filterBy(false, user.getId()));
            return "task/list";
    }
}
