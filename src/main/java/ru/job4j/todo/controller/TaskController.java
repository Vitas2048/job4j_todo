package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.service.TaskService;

import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/list")
    public String getIndex(Model model) {
        model.addAttribute("tasks", taskService.findAll());
        return "task/list";
    }

    @GetMapping("/create")
    public String getCreatePage(Model model) {
        Task task = new Task();
        model.addAttribute("task", task);
        return "task/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Task task, Model model) {
            if (taskService.create(task)) {
                model.addAttribute("message", "Ошибка создания задачи");
                return "errors/404";
            }
        return "redirect:/task/list";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Task task, Model model) {
        if (!taskService.update(task)) {
            model.addAttribute("message", "Ошибка при редактировании задачи");
            return "errors/404";
        }
        return "redirect:/task/list";
    }
    @PostMapping("/complete")
    public String complete(@ModelAttribute Task task, Model model) {
        task.setDone(true);
        taskService.update(task);
        return "redirect:/task/list";
    }
    @GetMapping("/edit/{id}")
    public String getByIdEdit(Model model, @PathVariable int id) {
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

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id) {
        var optional = taskService.findById(id);
        if (optional.isEmpty()) {
            model.addAttribute("message", "Заметка не найдена");
            return "errors/404";
        }
        model.addAttribute("task", optional.get());
        return "task/look";
    }

    @GetMapping("/onlyDone")
    public String getIndexOnlyDone(Model model) {
        model.addAttribute("tasks", taskService.filterBy(true));
        return "task/list";
    }

    @GetMapping("/onlyNotDone")
    public String getIndexOnlyNotDone(Model model) {
        model.addAttribute("tasks", taskService.filterBy(false));
        return "task/list";
    }
}
