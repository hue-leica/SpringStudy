package toy.example.manito.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import toy.example.manito.domain.Mission;
import toy.example.manito.domain.Room;
import toy.example.manito.domain.User;
import toy.example.manito.domain.UserRoom;
import toy.example.manito.service.UserService;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    /* 회원 가입 */
    @PostMapping("/api/v1/user")
    public createUserResponse saveUser(@RequestBody createUserRequest request){
        User user = new User(request.name, request.password);
        userService.join(user);
        return new createUserResponse(userService.findUser(user.getId()).getId());
    }
    /* 모든 유저 조회 */
    @GetMapping("/api/v1/users")
    public List<UserDto> getUsers(){
        return userService.findAllUser().stream()
                .map(u -> new UserDto(u))
                .collect(Collectors.toList());
    }

    @Data
    static class UserDto{
        private Long id;
        private String name;
        private List<UserRoomDto> userRooms;

        public UserDto(User user) {
            id = user.getId();
            name = user.getName();
            userRooms = user.getUserRooms().stream()
                    .map(ur -> new UserRoomDto(ur))
                    .collect(Collectors.toList());
        }
    }

    @Data
    static class UserRoomDto{
        private Long id;
        private Long roomId;

        public UserRoomDto(UserRoom userRoom) {
            id = userRoom.getId();
            roomId = userRoom.getRoom().getId();
        }
    }

    @Data
    static class createUserRequest{
        private String name;
        private String password;
    }

    @Data
    static class createUserResponse{
        private Long id;

        public createUserResponse(Long id) {
            this.id = id;
        }
    }
}
