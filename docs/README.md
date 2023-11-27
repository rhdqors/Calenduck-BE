# Calenduck Refactoring List

1. 클래스 분리
   - 예를 들어 UserService에서 회원가입과 로그인, 알람 기능을 담당하지만 로그인 유저에대한 알람 기능은 클래스 분리 가능할 듯. 
   - srp(Single Responsibility Principle) 단일 책임 원칙 준수
2. 서비스로직 수정 및 메서드 분리
   - 함수가 한 가지 기능만 하도록 최대한 작게
   - depth 2~3 유지 (목표: 2) -> for,if 같이쓰면 depth = 2
   - enum 사용
3. Restful Api 확인
4. 그 외...

