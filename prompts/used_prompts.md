# Used Prompt
## Cursor
- UserService.java 74-80 에서 IllegalArgumentException예외를 발생시키지 말고 이 프로젝트 구조에 맞게 common/exception 에 관련 커스텀 예외 클래스 생성하고 메서드 나누지않고 호출부에서 PasswordException(errorcode.PASSWORD_REQUIRED)같이 errorcode 파라미트로 넘기는 방식으로 수정해줘 
- UserService.java 75-77 이 케이스를 테스트하도록 UserControllerTest 파일에서 선언된 객체를 사용해서 테스트코드를 작성해줘
- UserService.java 75-77 여기에서 UserService의 해당 라인 케이스를 테스트하기위한 테스트코드를 작성해줘.
    - 테스트코드는 기존 UserControllerTest에 선언되어있는 객체들을 사용해서 작성해줘. 새로운 객체는 추가하지말고
    - 기존에 있는 domain/user/api/UserController파일의 @PutMapping("/users/me") api 를 호출해서 테스틑 해야해
- 이 프로젝트의 OAuth2SuccessHandler,CustomOAuth2UserService,WebSecurityConfig, application.yml 파일들을 참고해서 oauth가 정상적으로 구현되어있는지, 보안상 권장되지않는 코드가 있는지 알려줘
- oauth 인증한 email 이 기존에 일반적으로 회원가입한 email과 동일하다면 운영상 이슈가 발생하니까 provider 정보를 추가해서 구분되도록 수정해줘
- OAuth 인증 관련해서 테스트코드를 작성해줘
- CustomUserDetails과 책임을 분리하기위해 CustomOAuth2User을 별도로 생성해줘

## ChatGPT
- SpringBoot 3.4.5 버전과 SQLite 연동. 최신버전으로 연동 설정을 알려줘
- SQLite 문법에 맞게 초기화 쿼리를 작성해줘 (엔티티 클래스 소스 중략)
- HS512 생성하도록 자바 샘플 코드
- todo를 삭제 api로 삭제하려고할때, 로그인한 사용자가 아닌 경우 삭제 불가하도록 예외를 터트리도록 구현해줘
- oauth인증 후 받은 jwt를 generate할때 provider 정보로 인증 user를 특정해야하는데 적절하지않아. (소스 중략)
- 이렇게 userRepository 의존성가지면 순환참조 발생하지않아?
- SpringBoot 3.4.5 버전과 호환되는 Swagger 버전 의존성 추가 