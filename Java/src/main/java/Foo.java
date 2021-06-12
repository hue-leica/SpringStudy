import java.lang.reflect.Array;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Foo {

    public static void main(String[] args) {
        List<OnlineClass> springClasses = new ArrayList<>();
        springClasses.add(new OnlineClass(1, "spring boot", true));
        springClasses.add(new OnlineClass(5, "rest api development", false));

        /* Instant.now() 는 기계시간을 의미 --> 메소드 실행시간 비교 등에 사용 */
        Instant instant = Instant.now(); // 지금 기준 기계시간 --> UTC 기준(GMT)으로 출력
        // 현재 컴퓨터 시스템 Zone 정보를 가져온다
        ZoneId zone = ZoneId.systemDefault();
        System.out.println(zone);
        ZonedDateTime zonedDateTime = instant.atZone(zone);
        System.out.println(zonedDateTime);

        /* 인류용 일시 */
        // 현재 시간 --> 현재 컴퓨터 Zone 정보 참조(배포하면 배포 Zone 반)
        LocalDateTime now = LocalDateTime.now();
        // 원하는 시간대 만들기
        LocalDateTime birthTime = LocalDateTime.of(1996, Month.AUGUST, 8, 0, 0, 0);
        // 원하는 Zone의 시간 참조
        ZonedDateTime noewInKorea = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        // 기계시간 <--> 인류시간 서로 변환 가능!

        /* 인류용 기간 - Period */
        LocalDate today = LocalDate.now();
        LocalDate oneDay = LocalDate.of(2021, Month.AUGUST, 8);

        Period period = Period.between(today, oneDay); // 2개의 날짜 일(day) 차이 반환 (30일 넘어가면 월로 넘어감)
        Period until = today.until(oneDay); // 위와 동일

        /* 기계용 기간 - Duration */
        Instant now2 = Instant.now();
        Instant plus = now2.plus(10, ChronoUnit.SECONDS);
        Duration duration = Duration.between(now2, plus); // 기계 시간 비교

        /* Formatting --> 이미 정의 된것이 많으니 찾아서 써도 좋다 */
        LocalDateTime now3 = LocalDateTime.now();
        DateTimeFormatter MMddyyyy = DateTimeFormatter.ofPattern("MM/dd/yyyy"); // 직접 formatter 정의
        DateTimeFormatter basicIsoDate = DateTimeFormatter.BASIC_ISO_DATE; // 미리 정의된 formatter
        System.out.println(now3.format(MMddyyyy));
        System.out.println(now3.format(basicIsoDate));

        /* Parsing --> 원하는 형식으로 파싱 */
        LocalDate parse = LocalDate.parse("08/08/1996", MMddyyyy);
        System.out.println(parse);



    }
}
