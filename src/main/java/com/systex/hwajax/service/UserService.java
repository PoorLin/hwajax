package com.systex.hwajax.service;

import com.systex.hwajax.dao.UsersDao;
import com.systex.hwajax.dto.HttpResponseData;
import com.systex.hwajax.dto.user.UserDTO;
import com.systex.hwajax.model.user.LoginForm;
import com.systex.hwajax.model.user.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.Map;
import java.util.Optional;

import static com.systex.hwajax.constant.ResponseCodeConstant.LOGIN_ERROR;
import static com.systex.hwajax.constant.ResponseCodeConstant.SUCCESS;
import static com.systex.hwajax.util.ShaEncoding.parseSHA256;

@Service
public class UserService {

    @Autowired
    private UsersDao usersDao;


    public String register(Users users, BindingResult br) {

        if (usersDao.existsByEmail(users.getEmail())) {
            br.rejectValue("email", "error.userForm", "此電子信箱已存在");
            return "/user/register";
        }
        users.setPassword(parseSHA256(users.getPassword()));
        usersDao.save(users);
        return "redirect:/user/loginAjax";
    }

    public UserDTO loginForm(LoginForm user, Map<String, String> errorMsg) {
        Optional<Users> optUser = usersDao.findByEmail(user.getEmail());
        if (optUser.isPresent()) {
            Users dbuser = optUser.get();
            String sha256Password = parseSHA256(user.getPassword());

            //如果密碼一致則登入成功，沒有則失敗
            if (sha256Password.equals(dbuser.getPassword())) {
                return UserDTO.builder()
                        .id(dbuser.getId())
                        .name(dbuser.getName())
                        .email(dbuser.getEmail())
                        .gender(dbuser.getGender())
                        .build();
            } else {
                errorMsg.put("password", "帳號密碼錯誤");
                return null;
            }
        } else {
            errorMsg.put("password", "帳號密碼錯誤");
            return null;
        }
    }

    public HttpResponseData login(LoginForm user) {
        Optional<Users> optUser = usersDao.findByEmail(user.getEmail());
        // 如果此email存在就繼續檢驗密碼，沒有則失敗
        if (optUser.isPresent()) {
            Users dbuser = optUser.get();
            String sha256Password = parseSHA256(user.getPassword());

            //如果密碼一致則登入成功，沒有則失敗
            if (sha256Password.equals(dbuser.getPassword())) {
                return new HttpResponseData<>(
                        SUCCESS,
                        UserDTO.builder()
                                .id(dbuser.getId())
                                .name(dbuser.getName())
                                .email(dbuser.getEmail())
                                .gender(dbuser.getGender())
                                .build()

                );
            } else {
                return new HttpResponseData<>(LOGIN_ERROR);
            }
        } else {
            return new HttpResponseData<>(LOGIN_ERROR);
        }
    }


}
