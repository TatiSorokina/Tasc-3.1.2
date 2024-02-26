package ru.itmentor.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ru.itmentor.spring.boot_security.demo.model.Role;
import ru.itmentor.spring.boot_security.demo.model.User;
import ru.itmentor.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Controller
public class AdminController {
    private final UserService service;

    @Autowired
    public AdminController(UserService service) {
        this.service = service;
    }

    @GetMapping("/admin")
    public String index(Model model) {
        model.addAttribute("list", service.getUsers());
        return "admin/table";
    }

    @RequestMapping("admin/delete/{id}")
    public String delete(@PathVariable("id") int id) {
        service.deleteUser(id);
        return "redirect:/admin";
    }

    @RequestMapping("admin/new")
    public String newUser(Model model) {
        model.addAttribute("user", new User());
        List<Role> roles = service.getRoles();
        model.addAttribute("allRoles", roles);
        return "admin/newUser";
    }

    @RequestMapping(value = "admin/savenew", method = RequestMethod.POST)
    public String saveNewUser(@ModelAttribute("user") @Valid User user,
                              BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<Role> roles = service.getRoles();
            model.addAttribute("allRoles", roles);
            return "admin/newUser";
        }
        service.addUser(user);
        return "redirect:/admin";
    }

    @RequestMapping(value = "admin/save", method = RequestMethod.POST)
    public String saveUser(@ModelAttribute("user") @Valid User user,
                           BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<Role> roles = service.getRoles();
            model.addAttribute("allRoles", roles);
            return "admin/editUser";
        }
        service.updateUser(user.getId(), user);
        return "redirect:/admin";
    }

    @RequestMapping("admin/edit/{id}")
    public ModelAndView showEditUserPage(@PathVariable(name = "id") int id) {
        ModelAndView mav = new ModelAndView("admin/editUser");
        User user = service.getUserById(id);
        mav.addObject("user", user);
        List<Role> roles = service.getRoles();
        mav.addObject("allRoles", roles);
        return mav;
    }
}