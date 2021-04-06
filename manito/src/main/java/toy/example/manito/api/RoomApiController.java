package toy.example.manito.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import toy.example.manito.domain.Room;
import toy.example.manito.domain.RoomStatus;
import toy.example.manito.domain.UserRoom;
import toy.example.manito.service.RoomService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class RoomApiController {

    private final RoomService roomService;

    /* Room 만들기 */
    @PostMapping("/api/v1/room")
    public CreateRoomResponse saveRoom(@RequestBody CreateRoomRequest request){
        return new CreateRoomResponse(roomService.makeRoom(request.userId));
    }
    /* 모든 Room 조회 */
    @GetMapping("/api/v1/rooms")
    public List<RoomDto> getRooms(){
        return roomService.findRooms().stream()
                .map(r -> new RoomDto(r))
                .collect(Collectors.toList());
    }
    /* Room에 입장하기 */
    @PostMapping("/api/v1/room/enter")
    public void enterRoom(@RequestBody EnterRoomRequest request){
        roomService.addRoom(request.userId, request.roomId);
    }
    /* 마니또 매칭하기 */
    @PostMapping("/api/v1/room/match")
    public void matchRoom(@RequestBody MatchRoomRequest request){
        roomService.matchRoom(request.roomId);
    }

    @Data
    static class MatchRoomRequest{
        private Long roomId;
    }

    @Data
    static class EnterRoomRequest{
        private Long userId;
        private Long roomId;
    }

    @Data
    static class RoomDto{
        private Long id;
        private LocalDateTime effective_date;
        private LocalDateTime created_date;
        private RoomStatus roomStatus;
        private List<UserRoomDto> userRooms; //= new ArrayList<>();

        public RoomDto(Room room) {
            id = room.getId();
            effective_date = room.getEffective_date();
            created_date = room.getCreated_date();
            roomStatus = room.getRoomStatus();
            userRooms = room.getUserRooms().stream()
                    .map(ur -> new UserRoomDto(ur))
                    .collect(Collectors.toList());
        }
    }

    @Data
    static class UserRoomDto{
        private Long id;
        private Long roomId;
        private Long startUserId;
        private Long endUserId = -1L;

        public UserRoomDto(UserRoom userRoom) {
            id = userRoom.getId();
            roomId = userRoom.getRoom().getId();
            startUserId = userRoom.getStartUser().getId();
            if(userRoom.getEndUser() != null)
                endUserId = userRoom.getEndUser().getId();
        }
    }

    @Data
    static class CreateRoomRequest{
        private Long userId;
    }

    @Data
    static class CreateRoomResponse{
        private Long id;

        public CreateRoomResponse(Long id) {
            this.id = id;
        }
    }
}
