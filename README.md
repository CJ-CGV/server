# CGV 영화 예매 서비스

대용량 트래픽에 대응할 수 있는 MSA 구조 영화 예매 서비스입니다.


<br>

# ERD

![image](https://github.com/user-attachments/assets/b2a72ba6-d309-43db-b00a-d66c073973e3)


<br>

# 🗺️아키텍처


![image](https://github.com/user-attachments/assets/c5ad366c-97ae-48da-b665-618efc14a5e9)


<br>

# 💡사용 기술


- Java 17
- SpringBoot 3.4.4
- JPA
- Mysql
- Redis
- Kafka
- Ngrinder
- Gradle
  
<br>

# 📌개발 과정


- 동시 요청 속 정합성 문제 인식 및 데드락 원인 분석 - https://kangwook.tistory.com/46
- 데드락 방지와 정합성 해결을 위한 낙관적 락, 비관적 락 비교 및 비관적 락의 성능 개선 - https://kangwook.tistory.com/47
- 부하테스트와 모니터링을 통한 대기열의 필요성 검증 및 Redis sortedSet을 통한 대기열 구현 - https://kangwook.tistory.com/48
- Redis 대기열의 문제점 확인 및 해결을 위한 Kafka 추가 구성(부하테스트와 모니터링을 통해 검증) - https://kangwook.tistory.com/49






