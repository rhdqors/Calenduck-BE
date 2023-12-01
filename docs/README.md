# Calenduck Refactoring List

1. 클래스 분리
   - 예를 들어 UserService에서 회원가입과 로그인, 알람 기능을 담당하지만 로그인 유저에대한 알람 기능은 클래스 분리 가능할 듯. 
   - srp(Single Responsibility Principle) 단일 책임 원칙 준수
2. 서비스로직 수정 및 메서드 분리
   - 중복 있으면 제거
   - 함수가 한 가지 기능만 하도록 최대한 작게
   - depth 2~3 유지 (목표: 2) -> for,if 같이쓰면 depth = 2
   - enum 사용
3. Restful Api 확인
4. 그 외...

performance
1. crud관련 한 클래스
2. 인기도, 검색 관련 클래스
3. 데이터 불러오는 클래스 or 매핑

xmltomap
1. 쿼리 관련 -> 레포지토리
2. api데이터 가져오는 클래스 -> HttpRequest
3. 가져온 xml 데이터 변환 클래스 -> DataConversion
4. 배치에서 http요청이 들어감.
4. 배치 클래스 -> BatchManager
5. 찜목록은 북마크로 넘기는게 나을듯?




