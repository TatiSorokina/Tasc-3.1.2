package ru.itmentor.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ru.itmentor.spring.boot_security.demo.dao.RoleDao;
import ru.itmentor.spring.boot_security.demo.dao.UserDao;
import ru.itmentor.spring.boot_security.demo.model.Role;
import ru.itmentor.spring.boot_security.demo.model.User;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final RoleDao roleDao;

    @Autowired
    public UserServiceImpl(UserDao userDao, RoleDao roleDao) {
        this.userDao = userDao;
        this.roleDao = roleDao;
    }

    @Override
    @Transactional
    public void addUser(User user) {
        userDao.save(user);
    }

    @Override
    @Transactional
    public void updateUser(int id, User user) {
        User updatedUser = userDao.getById(id);
        if (updatedUser != null) {
            updatedUser.setName(user.getUsername());
            updatedUser.setEmail(user.getEmail());
            updatedUser.setAge(user.getAge());
            updatedUser.setPassword(user.getPassword());
            updatedUser.setRoleList(user.getRoleList());
            userDao.save(updatedUser);
        }
    }

    @Override
    @Transactional
    public void deleteUser(int id) {
        userDao.deleteById(id);
    }

    @Override
    public List<User> getUsers() {
        return userDao.findAll();
    }

    @Override
    public List<Role> getRoles() {
        return roleDao.findAll();
    }

    @Override
    public User getUserById(int id) {
        return userDao.getById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        Optional<User> user = userDao.findByName(name);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }
        return user.get();
    }
}