package study.datajpa.dto;

public interface NestedClosedProjection {

    String getUsername();
    TeamInfo getTeam();

    /* 연관된 엔티티에 대한 인터페이스도 만들어줘야 한다
       --> 실제 수행시 DB SELECT는 최적화가 안되지만, 원하는 정보 출력은 가능함 */
    interface TeamInfo{
        String getName();
    }
}
