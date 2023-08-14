# PracticeEx
PracticeEx backend
코드 작성 시 주의사항 (코드 컨벤션)
Mapping 경로 작성 시 단수형이 아니라 복수형
entity table 명은 단수로!
DB 컬럼명은 스네이크 스타일로 (ex. user_id)

커밋 메시지 형식
[init] 기능 구현 시 필요한 파일 생성 → [init] 회원가입 파일 생성
[update] 기능 구현 중 수정 → [update] 로그인 기능 수정
[complete] 기능 구현 완료 → [complete] 회원가입 완료
[refactor] 리팩터링 → [refactor] ~~ 개선

브랜치 명 규칙
ex. 기능 추가: feature/이슈번호/{기능 설명}
ex. 버그 수정: hotfix/이슈번호/{짧은 설명}
ex. 문서 관리: docs/이슈번호/readme

푸시 전에 정렬
코드 정렬 : Ctrl + Alt + L
안 쓰는 import문 삭제 : Ctrl + Alt(Shift?) + O

머지된 브랜치 처리
기능 완성 시 삭제
기능 개발 중이면 계속 사용

머지 후 새 브랜치 생성 과정
git checkout dev
git branch -d <브랜치명>
git pull origin dev
git branch <새로 할거>
git checkout <새로판브랜치>
또 작업하기

화이팅
