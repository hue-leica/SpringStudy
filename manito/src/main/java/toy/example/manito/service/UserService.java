package toy.example.manito.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toy.example.manito.domain.User;
import toy.example.manito.repository.UserRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /* 회원 가입 */
    @Transactional
    public Long join(User user){
        return userRepository.save(user);
    }

    /* 특정 유저 조회 */
    public User findUser(Long id){
        return userRepository.findOne(id);
    }

    /* 모든 유저 조회 */
    public List<User> findAllUser(){
        return userRepository.findAll();
    }
}
