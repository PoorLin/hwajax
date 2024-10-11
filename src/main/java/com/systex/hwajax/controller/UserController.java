package com.systex.hwajax.controller;

import com.systex.hwajax.dto.user.UserDTO;
import com.systex.hwajax.model.user.GreateUsersForm;
import com.systex.hwajax.model.user.LoginForm;
import com.systex.hwajax.model.user.Users;
import com.systex.hwajax.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/index")
    public String index(){
        return "index";
    }

    @GetMapping("/user/login")
    public String loginPage(Model model,HttpSession httpSession){
        UserDTO user= (UserDTO)httpSession.getAttribute("user");
        //若已登入
        if(user !=null){
            return "redirect:/index";
        }

        model.addAttribute("loginForm", new LoginForm());
        return "/user/login";
    }

    @GetMapping("/user/loginAjax")
    public String ajaxLoginPage(HttpSession httpSession){
        UserDTO user= (UserDTO)httpSession.getAttribute("user");
        //若已登入
        if(user !=null){
            return "redirect:/index";
        }
        return "/user/loginAjax";
    }

    @GetMapping("/user/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("userForm", new GreateUsersForm());
        return "user/register";
    }


    @PostMapping("/user/register")
    public String register( @Valid @ModelAttribute("userForm") GreateUsersForm userForm, BindingResult br){
if(br.hasErrors()){
    return "user/register";
}
        Users users = Users.builder()
                .gender(userForm.getGender())
                .password(userForm.getPassword1())
                .name(userForm.getName())
                .email(userForm.getEmail())
                .build();
       return userService.register(users,br);
    }

    @GetMapping("/user/logout")
    public String logout(HttpSession httpSession){
        httpSession.invalidate();
        return "redirect:/user/login";
    }


}