## Multi Module Design

### movie-api
- 클라이언트와의 인터페이스를 담당.
- 클라이언트 요청을 moive-application 모듈로 전달.

### movie-application
- 유즈케이스 역할 수행.
- 여러 도메인 서비스를 조합하여 비즈니스 처리.

### movie-business
- 비즈니스 규칙을 정의하고, 도메인 로직 수행.
- movie-application에 필요한 서비스를 제공.

### movie-infrastructure
- 데이터 저장소 및 외부 API 연동.
- Redis, Kafka 등 인프라 기능 제공.