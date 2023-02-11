package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.persistence.Task;
import ru.job4j.todo.service.TaskService;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/")
    public String getIndex(Model model) {
        model.addAttribute("tasks", taskService.findAll());
        return "index";
    }

    @GetMapping("/task/create")
    public String getCreatePage(Model model) {
        Task task = new Task();
        model.addAttribute("task", task);
        return "/task/create";
    }

    @PostMapping("/task/create")
    public String create(@ModelAttribute Task task, Model model) {
        try {
            taskService.create(task);
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "errors/404";
        }
    }

    @PostMapping("/task/update")
    public String update(@ModelAttribute Task task, Model model) {
        taskService.update(task);
        return "redirect:/";
    }
    @PostMapping("/task/complete")
    public String complete(@ModelAttribute Task task, Model model) {
        task.setDone(true);
        taskService.update(task);
        return "redirect:/";
    }
    @GetMapping("/task/edit/{id}")
    public String getByIdEdit(Model model, @PathVariable int id) {
        var optional = taskService.findById(id);
        if (optional.isEmpty()) {
            model.addAttribute("message", "Заметка не найдена");
            return "errors/404";
        }
        model.addAttribute("task", optional.get());
        return "task/one";
    }

    @GetMapping("/task/delete/{id}")
    public String delete(Model model, @PathVariable int id) {
        taskService.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/task/{id}")
    public String getById(Model model, @PathVariable int id) {
        var optional = taskService.findById(id);
        if (optional.isEmpty()) {
            model.addAttribute("message", "Заметка не найдена");
            return "errors/404";
        }
        model.addAttribute("task", optional.get());
        return "task/look";
    }

    @GetMapping("/task/onlyDone")
    public String getIndexOnlyDone(Model model) {
        model.addAttribute("tasks", taskService.findAll().stream()
                .filter(Task::isDone).collect(Collectors.toList()));
        return "index";
    }

    @GetMapping("/task/onlyNotDone")
    public String getIndexOnlyNotDone(Model model) {
        model.addAttribute("tasks", taskService.findAll().stream()
                .filter(p -> !p.isDone()).collect(Collectors.toList()));
        return "index";
    }
}
