SEOMOIM
=============
소모임과 당근마켓을 모티브로 해서
지속적인 모임 활동은 부담스럽지만 1회 성 모임을 참여하고 싶은 사람들을 위해 <br>
카레 고리 와 위치 기반으로 모임을 만들고 참여할 수 있는 백엔드 API 서버를 구축하는것이 목표입니다.<br>
Rest API형 서버로써 서버 공부에 집중할 수 있도록 하고 추후 필요시 웹 화면은 프로토타입으로 개발 예정입니다.

## 프로젝트 use_case

-
##  프로젝트의 주요 관심사
<b>공통사항</b><br>
- 솔로프로젝트이지만 협업환경과 유사한과정으로 진행
- 객체지향 설계에 입각하여 기능구현
- 대규모 트래픽 환경에서의 성능 개선 목표
 <br><br>
- 
  <b>성능 최적화</b><br>
- 서버 부하를 줄이기 위해 캐싱 서버 적극 활용
- DB서버와의 통신을 최소화 N+1 문제 해결
- 인덱스와 쿼리 튜닝을 활용
- 비동기를 활용하여 빠른 시간 내에 외부 API 호출
  <br><br>
  <b>그 외</b><br>
- Project Wiki를 참고해주세요!
  <br><br>

### 브랜치 관리 전략
Git Flow를 사용하여 브랜치를 관리합니다.<br>
모든 브랜치는 리뷰를 진행한 후 merge를 진행합니다.<br>
<br>
- Main : 배포시 사용합니다.
- Develop : 완전히 개발이 끝난 부분에 대해서만 Merge를 진행합니다.
- Feature : 기능 개발을 진행할 때 사용합니다.
- Release : 배포를 준비할 때 사용합니다.
- Hot-Fix : 배포를 진행한 후 발생한 버그를 수정해야 할 때 사용합니다.
  <br><br>

###  커밋 규칙

| Message             | 설명                                                                                                           |
| ---------------- | -------------------------------------------------------------------------------------------------------------- |
| [feat]             | 새로운 기능을 추가할 경우 ex) [feat] 로그인 기능 추가                                                                                               |
| [fix]             | 버그를 고친 경우                                                                                                      |
| [design]             | CSS 등 사용자 UI 디자인 변경                                                                                                      |
| [!breaking change]            | 커다란 API 변경의 경우                                                       |
| [!hotfix]         | 치명적인 버그를 고쳐야하는 경우                                                                                                  |
| [style]             | 코드 포맷변경, 세미콜론 누락, 코드수정이 없는 경우.                                                  |
| [refactor]            | 프로덕션 코드 리펙토링할 경우 |
| [comment]           | 필요한 주석 추가 및 변경                                                                                  |
| [docs]          | 문서를 수정한 경우                                                                                      |
| [test]           | 테스트 코드 작업을할 경우                                                             |
| [chore]           | 빌드 테스트 업데이트, 패키지 매니저를 설정하는 경우                                                                             |
| [rename] | 파일 혹은 폴더명을 수정하거나 옮기는 작업만 하는 경우                                                                                         |
| [remove]          | 삭제하는 작업만 수행한 경우                                                                        |
| [init]             | 브랜치 초기화 및 초기셋팅 관련된 설정일 경우|

### 테스트
- ...
- ...
- ...

### 성능 테스트
공부하면서 진행<br>


## 사용 기술 및 환경
Java11, Spring boot, Spring data jpa, QueryDSL, mysql<br>
Spring security, OAuth2, Spring batch, AWS, Docker
<br>

## 이슈
<https://2juillet.tistory.com/><br>
개인블로그 기술<br>

[//]: # (<br>)

[//]: # (## CI)

[//]: # (Jenkins : 서버 운영을 종료하였습니다.<br>)

[//]: # (Naver Cloud Platform&#40;Cloud server&#41;를 사용하고 있습니다.<br>)

[//]: # (PR시마다 자동 Build 및 Test 적용<br>)

[//]: # (비로그인 상태로도 확인이 가능합니다.<br>)

[//]: # ()
[//]: # (## CD)

[//]: # (Docker 이미지를 제작하여 배포합니다.<br>)

[//]: # (CI 서버에서 빌드 완료시 Shell script가 작동하여 빌드된 이미지가 docker hub에 저장됩니다.<br>)

[//]: # (Push 완료시 Delfood 메인 서버에서 docker hub에 올라간 이미지를 받아 실행시킵니다.<br>)

[//]: # ()
[//]: # ()
[//]: # (<br>)
## Database
- Mysql<br>
 aws 서비스를 사용하고 있습니다.
- Redis<br>
docker 컨테이너를 사용하고 있습니다.
<br>

## 화면 설계


### 고객 화면 프로토타입

### 관리자 화면 프로토타입

## 프로젝트 DB ERD