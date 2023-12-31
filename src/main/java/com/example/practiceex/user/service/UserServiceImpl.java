package com.example.practiceex.user.service;

import com.example.practiceex.common.exception.CustomException;
import com.example.practiceex.common.message.CustomErrorCode;
import com.example.practiceex.user.dto.EditUserRequestDto;
import com.example.practiceex.user.dto.SignupRequestDto;
import com.example.practiceex.user.entity.User;
import com.example.practiceex.user.entity.UserRoleEnum;
import com.example.practiceex.user.repository.UserRepository;
import com.example.practiceex.userpassword.dto.EditPasswordRequestDto;
import com.example.practiceex.userpassword.entity.UserPassword;
import com.example.practiceex.userpassword.repository.UserPasswordRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserPasswordRepository userPasswordRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.token}")
    private String adminToken;

    @Transactional
    @Override
    public void signup(SignupRequestDto requestDto) {
        String email = requestDto.getEmail();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 사용자 존재 여부 확인
        if (findUserByEmail(email) != null) {
            throw new CustomException(CustomErrorCode.USER_ALREADY_EXISTS, null);
        }

        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin() && requestDto.getAdminToken().equals(adminToken)) {
            role = UserRoleEnum.ADMIN;
        }

        User user = userRepository.save(new User(email, password, role));
        userPasswordRepository.save(new UserPassword(password, user));
    }

    @Transactional
    @Override
    public User editUserInfo(EditUserRequestDto requestDto, User user) {
        User found = findUser(user.getId());
        if (found == null)
            throw new CustomException(CustomErrorCode.USER_NOT_FOUND, null);

        found.editUserInfo(requestDto.getNickname(), requestDto.getIntroduction());
        return found;
    }

    @Transactional
    @Override
    public void deleteUserInfo(Long userId, User user) {
        User found = findUser(userId);

        confirmUser(found, user);

        userPasswordRepository.deleteAllByUser_Id(found.getId());
        userRepository.deleteById(found.getId());
    }

    @Transactional
    @Override
    public void editUserPassword(EditPasswordRequestDto requestDto, User user) {
        User found = findUser(user.getId());

        checkPassword(requestDto.getPassword(), found.getPassword());
        checkNewPassword(requestDto.getNewPassword1(), requestDto.getNewPassword2());
        checkRecentPasswords(found.getId(), requestDto.getNewPassword1());

        String newPassword = passwordEncoder.encode(requestDto.getNewPassword1());
        userPasswordRepository.save(new UserPassword(newPassword, found));
        found.editPassword(newPassword);
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new CustomException(CustomErrorCode.USER_NOT_FOUND, null));
    }

    private void confirmUser(User user1, User user2) {
        if(!user1.getId().equals(user2.getId()) && user2.getRole() != UserRoleEnum.ADMIN)
            throw new CustomException(CustomErrorCode.UNAUTHORIZED_REQUEST, null);
    }

    private void checkPassword(String inputPassword, String userPassword) {
        if (!passwordEncoder.matches(inputPassword, userPassword)) {
            throw new CustomException(CustomErrorCode.OLD_PASSWORD_MISMATCHED, null);
        }
    }

    private void checkNewPassword(String newPassword1, String newPassword2) {
        if(!newPassword1.equals(newPassword2)) {
            throw new CustomException(CustomErrorCode.NEW_PASSWORD_MISMATCHED, null);
        }
    }

    private void checkRecentPasswords(Long userId, String newPassword) {
        List<UserPassword> userPasswords = userPasswordRepository.get3RecentPasswords(userId);
        userPasswords.forEach(password -> {
            if (passwordEncoder.matches(newPassword, password.getPassword()))
                throw new CustomException(CustomErrorCode.PASSWORD_RECENTLY_USED, null);
        });
    }
}
