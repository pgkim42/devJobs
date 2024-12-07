# 1202 프로젝트 시작

# 1202 ~ 1204 User(회원)
- **Back-End**
- 일반 회원가입, 소셜 회원가입(카카오, 네이버) **(완료)**
- 일반 로그인, 소셜 로그인 **(완료)**
- 메일 인증 서비스 **(완료)**

- **Front-End**
- 회원가입, 로그인 화면 구현 **(완료)**
- 회원가입 검증 **(완료)**
  - ID 중복체크
  - 비밀번호 검증(숫자 영어 혼용 8~13자까지)
  - 비밀번호 확인 검증
  - 이메일 형식 검증
  - 인증번호 검증

# 1205 JobPosting(구인공고)
- common.file로 파일첨부 스토리지 생성
- 파일첨부 단위테스트
- PostingStatus를 Enum으로 사용하려 했으나 "모집중", "마감"만 있어서 String으로 전환
- 관련 dto, entity, repository, service 일부 구현
- 현재 service 중 register만 완료

# 1205 User(회원)
- 일반로그인 시 LocalStorage 저장
- nickname 속성 검증 추가

# 1206 JobPosting(구인공고), CompanyProfile(기업프로필)
- JobPosting 이미지 업로드 기능 구현
- JobPosting에 CompanyProfile객체 외래키 연결
- CompanyProfile dto, entity, repository, repository test(CRUD)
- repositorytest로 데이터 생성 후 CompanyProfile PK를 받고 JobPosting테이블에 profileCd가 컬럼에 추가 되는 것 까지 확인

# 1207 JobPosting(구인), CompanyProfile(기업프로필)
- BaseEntity를 common.file로 이동
- JobPosting, CompanyProfile - CRUD 기능 구현
- Job, Company 모두 User 테이블의 USER_CODE의 PK값 외래키로 설정 필요로 함(추후 수정)

# 1207 User(회원)
- nickname 속성 -> name 속성으로 변경 및 기존 nickname 검증 코드 삭제
- 소셜로그인 시 LocalStorage 내 userId, email, name, type 데이터 저장

# 1207 JobPosting(구인), CompanyProfile(기업프로필), Resume(이력서)
- 파일 업로드 경로 구조 변경 - 업로드된 파일을 각 패키지별로 관리하기위해, jobposting과 resume의 하위 폴더를 C:\\upload 아래에 생성하고 파일을 저장 하도록 FileUtil 클래스에서 경로를 동적으로 설정
  (FileUtil 클래스에서 업로드 경로를 패키지명에 맞게 동적으로 생성하도록 수정... C:\uploadfile\jobposting\이미지.jpg 방식)
- Resume 이력서 패키지 생성 - 패키지 기본 구조 생성 후 register까지 진행
  (JSON <-> DTO 로 저장하기 위해 LanguagesSkillsDTO, CertificationsDTO 클래스 생성이 필요했음, Mapper를 사용하기 위해 implementation 'com.fasterxml.jackson.core:jackson-databind' 의존성 추가 필요)
- PostMan에 dto타입으로 {"education": "dd", "languageSkills": [{"language": "영어","level": "1"}]} / resumeFolder로 매개 변수 2개 받아서 dto,file 전송해야함
