package basic.core.singleton;

// 순수 java코드를 통해서 싱글톤을 구현하는 방법
// Spring에서는 알아서 기본적으로 싱글톤으로 객체를 생성함!
public class SingletonService {
    // 1. static 영역에 객체를 딱 1개만 생성
    private static final SingletonService instance = new SingletonService();

    // 2. 외부에서 조회할 수 있도록 public으로 생성
    public static SingletonService getInstance(){
        return instance;
    }
    // 3. 생성자를 private으로 막아서 외부에서 new 키워드를 사용한 객체 생성을 막아야 함
    private SingletonService(){
    }
}
