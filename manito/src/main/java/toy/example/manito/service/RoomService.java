package toy.example.manito.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import toy.example.manito.domain.*;
import toy.example.manito.repository.RoomRepository;
import toy.example.manito.repository.UserRepository;
import toy.example.manito.repository.UserRoomRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final UserRoomRepository userRoomRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long makeRoom(Long userId){
        LocalDateTime current = LocalDateTime.now();
        LocalDateTime expired = current.plusDays(7);
        Room room = new Room(expired, current, RoomStatus.READY);
        roomRepository.save(room);
        // userId로 user_room 테이블 추가
        User user = userRepository.findOne(userId);
        Room findRoom = roomRepository.findOne(room.getId());
        userRoomRepository.save(user, findRoom);
        return findRoom.getId();
    }

    @Transactional
    public void addRoom(Long userId, Long roomId){
        User user = userRepository.findOne(userId);
        Room room = roomRepository.findOne(roomId);
        userRoomRepository.save(user, room);
    }

    public List<Room> findRooms(){
        return roomRepository.fidAll();
    }

    @Transactional
    public void matchRoom(Long roomId){
        // roomStatus바꾸기 위한 변경감지
        Room findRoom = roomRepository.findOne(roomId);
        findRoom.changeRoomStatus(RoomStatus.DOING);
        // roomId에 해당하는 모든 startId를 가져온다
        List<UserRoom> userRooms = userRoomRepository.findByRoomId(roomId);
        List<Long> userIds = userRooms.stream().map(ur -> ur.getStartUser().getId()).collect(Collectors.toList());
        Long ids[] = new Long[userIds.size()];
        ids = userIds.toArray(new Long[userIds.size()]);
//        System.out.println("=======================================");
//        for (Long id : userIds) {
//            System.out.println("id = " + id);
//        }
//        System.out.println("=======================================");
        // 해당 번호들을 자기가 아닌 수에게 랜덤으로 할당 --> 변경감지 사용
        Random r = new Random();
        Long a[] = new Long[userIds.size()];
        for (int i=0;i<userIds.size();i++) {
            int idx = r.nextInt(userIds.size());
            if(idx == i)
            {
                i--;
                continue;
            }
            a[i] = ids[idx];
            for(int j=0;j<i;j++)
            {
                if(a[i] == a[j])
                    i--;
            }
        }
//        System.out.println("=======================================");
//        for (Long aLong : a) {
//            System.out.println("aLong = " + aLong);
//        }
//        System.out.println("=======================================");
        int idx=0;
        for (UserRoom ur : userRooms) {
            User findUser = userRepository.findOne(a[idx++]);
            ur.changeEndUser(findUser);
            Mission mission = new Mission("5000원 미만의 선물 사주기", ur);
            ur.addMission(mission);
        }
    }
}
