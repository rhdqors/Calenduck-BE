# Calenduck
### **바쁜 일상의 효율적인 문화생활 Calenduck**


<aside>
💡 최신 유행과 내가 좋아하는 아티스트만 모아서 볼 수 없을까?

</aside>

코로나19 팬데믹이 풀리고 영화, 뮤지컬 그리고 오프라인 공연 등이 다시 활성화되고 있습니다. 하지만 우리의 바쁜 현대인들은 어떤 공연이 있는지, 아티스트는 누구인지 여러가지를 찾아보고 일정을 맞춰야 하는 번거로움 끝에 공연을 보곤 합니다.

이럴 때 내가 원하는 것만 저장하고, 잊지 않게 며칠 남았는지 D-DAY 표시와 알림을 해준다면 어떨까? 라는 궁금함에서 시작된 프로젝트입니다. 바쁜 일상에서 효율적인 문화생활을 합시다.

### ✅ 서비스 핵심 기능
1. **내가 원하는 공연, 아티스트를 검색해요** <br>
-> 언제 어떤 공연이 있는지 달력 형식으로 볼 수 있습니다. <br>


2. **검색한 공연 및 아티스트를 보기 쉬운 내 달력에 저장해요** <br>
-> 저장하고 언제든지 쉽게 확인할 수 있습니다. <br>


3. **공연 시작 1일, 3일, 7일전 알람을 받아요** <br>
-> 혹시나 잊어버려도 친절히 알려줍니다. <br>


4. 지역별, 공연별, 검색별 최근 유행 및 인기도를 확인할 수 있어요 <br>
-> 즐거운 문화 생활을 합시다.


### 🛠 기술 스택
- **Backend**: Java 17, Spring Boot 2.7.5, Spring Security, JPA
- **Database**: MySQL, Redis
- **Migration**: Flyway
- **Auth**: OAuth 2.0 (Kakao)
- **External API**: KOPIS 공연예술통합전산망

### 🚀 기술적 도전 및 성과

| 개선 항목 | Before | After | 개선율 |
|-----------|--------|-------|--------|
| 외부 API 배치 처리 | 49초 | 59ms | **99.8% ↓** |
| DB 조회 최적화 | 매 요청 DB 접근 | Redis 캐싱 | DB 부하 감소 |
| 인기검색어 | 미구현 | Redis Sorted Set | 실시간 랭킹 |

#### 📝 상세 기술 블로그
- [멀티스레드 + 배치 처리로 API 성능 99% 개선하기](https://rhdqors.tistory.com/123)
- [Redis 캐싱으로 DB 부하 줄이기](#) <!-- TODO: 블로그 링크 -->
- [Flyway로 DB 마이그레이션 관리하기](#) <!-- TODO: 블로그 링크 -->

---

### 📑 ERD
![image](https://github.com/rhdqors/turkey-project/assets/108318494/eb1ce28e-f5e1-433e-b82f-5f39bdcd081f)

### ⚙️ Service Architecture
![image](https://github.com/rhdqors/turkey-project/assets/108318494/c7276915-91c0-4060-a279-83f62b37423f)
```agsl
📁 calenduck _ 
           |_ 📁 domain _ 
           |            |_ 📁 bookmark _
           |                           |_ 📁 controller
           |                           |_ 📁 dto
           |                           |_ 📁 entity
           |                           |_ 📁 repository
           |                           |_ 📁 service
           |            |_ 📁 detailInfo
           |            |_ 📁 performance
           |            |_ 📁 user
           |_ 📁 global _ 
           |            |_ 📁 config
           |            |_ 📁 entity
           |            |_ 📁 exception
           |            |_ 📁 jwt
           |            |_ 📁 message
           |            |_ 📁 scheduler
           |_ 📋 CalenduckApplication

```