package study.datajpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;
import java.util.UUID;

@SpringBootApplication
public class DataJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataJpaApplication.class, args);
	}

	/* 등록자 / 수정자를 자동으로 추가하는 과정 */
	@Bean
	public AuditorAware<String> auditorProvider(){
		/* 실제로는 사용자의 id를 파싱해서 넣어줘야 한다 */
		return () -> Optional.of(UUID.randomUUID().toString());
	}

}
