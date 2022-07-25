# 내꺼도 V1 
우리 서비스는 배달비를 더치페이하는 서비스 입니다.

### 🛵 계속해서 오르는 배달비

- 비싼 배달비에 주문이 망설여지시나요?

### 🧑‍🤝‍🧑 옆집, 윗집 내 이웃들과

- 같은 건물의 사람들끼리 먹고싶은 음식을 함께 시켜 배달비를 절약 할 수 있습니다.

### 🍽️ 집 앞에서 원하는 음식을!

- 채팅을 통해 원하는 음식을 결정하고 아파트로비, 로비 1층 등 원하는 곳에서 음식을 받아가세요


## 스토어 링크
현재 스토어에서 다운로드 할수 있습니다. [스토어로 이동](https://play.google.com/store/apps/details?id=com.naeggeodo.presentation)<br>



## ☎️ 팀원
|                | Jayden  | Minhyeok  | Dahye | Seoyun | Sanghoon |
|----------------|:--------------:|:--------------:|:--------------:|:--------------:|:--------------:|
| **Github**     | [<img src="https://github.com/cjy0019.png?size=150" width="150px;" alt="cjy0019"/>](https://github.com/cjy0019) | [<img src="https://github.com/kmh916.png?size=150" width="150px;" alt=""/>](https://github.com/kmh916) | [<img src="https://github.com/jodahye.png?size=150" width="150px;" alt="jodahye"/>](https://github.com/JODAHYE) | [<img src="https://github.com/seoyun75.png?size=150" width="150px;" alt="seoyoon"/>](https://github.com/seoyun75) | [<img src="https://github.com/uasang01.png?size=150" width="150px;" alt="uasang01"/>](https://uasang01.tistory.com/) |
| **E-mail**     | cjy0029@naver.com | kmh102808@gmail.com | dahye8043@gmail.com | goeun944@gmail.com  | ddhtyuu@gmail.com  |
| **Github**     | https://github.com/cjy0019  | https://github.com/kmh916 | https://github.com/JODAHYE  |  https://github.com/seoyun75  | https://github.com/uasang01 | 
| **Blog**       | https://velog.io/@cjy0029 | https://velog.io/@kmh916 | https://dal-dagury.tistory.com/  | 🛵 | https://uasang01.tistory.com/ | 
| **Position**   | FrontEnd / IOS | BackEnd | FrontEnd | BackEnd | Android |



## Tech Stack
Kotlin, Jetpack, Clean Architecture ... 


## 🔨 기능 소개

#### 공통
<details>
<summary>펼쳐보기</summary>
<div markdown="1">

1. API 요청 시 AccessToken이 만료되었다면 RefreshToken을 이용하여 AccessToken을 갱신하고 기존 API를 다시 요청합니다
2. RefreshToken이 만료되었다면 로그인 화면으로 이동합니다.
3. 로그인 후 하단의 바텀텝을 이용하여 화면을 전환할 수 있습니다.
4. Screen Back Stack이 없을 때 폰의 뒤로 버튼을 누르면 '한번 더 누르면 앱이 종료됩니다' 라는 토스트가 뜨며 두번 누르면 앱이 종료됩니다.
</div>
</details>

#### 로그인 화면
<details>
<summary>펼쳐보기</summary>
<div markdown="1">

1. 카카오, 네이버 로그인을 통해 회원가입 / 로그인 할 수 있습니다.
2. 로그아웃 한 경우가 아니라면 자동 로그인처리 됩니다.
3. 로그인 후 홈 화면으로 이동합니다.
</div>
</details>

#### 메인 화면
<details>
<summary>펼쳐보기</summary>
<div markdown="1">

1. 하단의 5가지 탭이 있고 Default는 홈 탭입니다
</div>
</details>

#### 홈 탭
<details>
<summary>펼쳐보기</summary>
<div markdown="1">
  
1. 음식 카테고리(전체, 치킨, 피자 ...)별로 나와 같은 주소의 유저들이 생성한 채팅방리스트를 보여줍니다
2. 상단의 검색창을 클릭하면 다음 주소 API를 이용하여 주소를 검색할 수 있습니다(아파트나 공동주택 주소여야 서비스를 이용할 수 있습니다)
3. 유저의 주소 정보가 없다면 주소를 검색하라는 버튼을 보여줍니다
4. 해당 카테고리의 채팅방이 없다면 채팅방을 생성하라는 버튼을 보여줍니다
5. 채팅방 리스트에서 '함께 주문하기' 버튼을 터치하면 해당 채팅방 화면으로 이동합니다
</div>
</details>

#### 내꺼톡 탭
<details>
<summary>펼쳐보기</summary>
<div markdown="1">
  
1. 현재 내가 참여 중인 내꺼톡 리스트를 보여줍니다
2. 리스트에서 채팅방 제목과 위치정보 해당 채팅방에서 마지막으로 주고받은 메시지 내용, 시간을 볼 수 있습니다
3. 참여중인 채팅방의 방장일 경우 수정 버튼이 있으며 터치 시 채팅방 이름을 변경하는 Dialog를 띄웁니다
4. 채팅방을 터치하면 해당 채팅방 화면으로 이동합니다
</div>
</details>

#### 내꺼톡 생성 탭
<details>
<summary>펼쳐보기</summary>
<div markdown="1">
  
1. 언제 주문할지 버튼 3개중 하나를 터치하면 생성 화면으로 이동합니다
2. 두개의 탭이 있고, 하나는 새로 채팅방을 생성하기 위한 화면, 하나는 기존 생성 내역을 보여주는 화면입니다
3. 새로 채팅방을 생성하기 위해서 제목, 카테고리, 최대인원을 선택해야합니다
4. 외에 배달앱 링크, 태그, 채팅방 이미지를 정할 수 있습니다
5. 채팅방 이미지를 선택하지 않았다면 카테고리에 따라 기본 이미지로 생성됩니다.
6. 조건이 만족된다면 생성하기 버튼이 활성화 됩니다
7. 생성 내역 화면은 이전에 생성했던 채팅방 리스트를 보여줍니다
8. 이전 생성 내역 중 자주 만드는 정보를 별 버튼으로 북마크, 북마그 취소 할 수 있습니다
9. (다음부터)북마크된 생성 내역은 맨 위로 정렬됩니다
10. 필요 없는 생성 내역은 X버튼으로 삭제할 수 있습니다
11. 채팅방을 선택하면 생성하기 버튼이 활성화 됩니다
12. 채팅방을 생성하면 해당 채팅방 화면으로 이동합니다
</div>
</details>

#### 채팅 화면
<details>
<summary>펼쳐보기</summary>
<div markdown="1">
  
1. 채팅방 상단에는 채팅방 정보(채팅방 제목, 입장 가능 인원, 현재 참여 중인 인원, 배달앱 링크를 입력했다면 해당 링크로 이동 가능한 버튼)을 보여줍니다
2. 방장일 경우 채팅방 정보 밑에 '돈을 받으셨나요?'버튼을 보여줍니다
3. 채팅방에 유저가 입장하거나 퇴장 시 안내 메세지를 보여줍니다
4. 채팅방의 유저들과 채팅과 사진을 주고받을 수 있습니다
5. 채팅방 하단에는 채팅을 입력하는 EditText와 자주사용하는 문구(Bottom Dialog)를 보여주는 버튼, 사진 전송을 위한 버튼, 채팅 전송 버튼이 있습니다
6. 문구 Dialog에서 자주쓰는 문구를 터치하면 해당 문구가 전송됩니다
7. 문구들은 5개까지 추가할 수 있으며, 삭제할 수 있습니다
8. 사진 전송을 위한 버튼을 누르면 핸드폰 저장소에 접근할 수 있는 권한이 있는지 먼저 확인합니다
9. 권한이 없다면 권한을 요청하며, 이미 거절했던 경우에는 권한이 필요하다는 스낵바가 나타나며 권한 허용을 위해 앱 설정으로 이동하는 버튼이 보입니다
10. 권한이 있다면 하단에 핸드폰의 사진들을 볼 수 있는 RecyclerView가 나타며, 사진을 선택후 전송버튼을 누르면 사진이 전송됩니다
11. 채팅방 상단 오른쪽에 햄버거 버튼을 터치하면 내꺼톡 서랍(Drawer)이 열리며, 최근에 주고받은 사진(최대5개)과 참여중인 유저들의 닉네임을 보여줍니다
12. 내꺼톡 서랍에서 방장의 프로필은 왕관아이콘으로, 일반 유저들은 유저 아이콘으로 표시되며, 본인 여부를 확인 할 수 있습니다
13. 방장인 경우 강퇴 버튼이 활성화되며 유저들을 강퇴할 수 있습니다
14. 강퇴당한 유저는 채팅방에서 나가지며 이전 화면으로 이동하고 다시는 해당 채팅방에 참여할 수 없습니다
15. 서랍 하단 나가기 버튼을 이용해 채팅방을 나갈 수 있습니다
16. 중복된 접속(같은 아이디로 접속)은 불가능합니다
17. 방장은 '돈을 받으셨나요?' 버튼터치시 해당 화면으로 이동하며 방장을 제외한 유저와 유저마다 수령 완료 버튼이 표시됩니다
18. 수령 완료 버튼을 누르면 해당 유저는 수령 완료 처리됩니다
19. 하단의 채팅방 종료하기 버튼을 누눌러 채팅방을 종료할 수 있으며 종료된 채팅방은 검색되지 않습니다
</div>
</details>

#### 검색 탭 기능
<details>
<summary>펼쳐보기</summary>
<div markdown="1">
  
1. 채팅방 제목으로 검색을 할 수 있습니다
2. 많이 사용된 태그를 보여주며, 검색된 채팅방이 있다면 사라집니다 
3. 검색된 채팅방이 있더라도 검색창을 누르면 태그가 보여집니다
4. 각각의 태그를 클릭했을 때 해당 태그를 사용한 전국의 채팅방이 모두 보여집니다
5. 채팅방을 누르면 해당 채팅방에 입장합니다
</div>
</details>

#### 더보기 탭 기능
<details>
<summary>펼쳐보기</summary>
<div markdown="1">
  
1. 회원가입 시 임의로 생성된 닉네임을 보여줍니다
2. 닉네임 옆 수정하기 버튼으로 닉네임을 변경할 수 있습니다
4. 최근 3개월간 주문은 정상적으로 종료된 채팅방의 개수를 나타냅니다
5. 참여 중인 내꺼도는 참여중인 채팅방 개수를 보여주며 누르면 내꺼톡 탭으로 이동합니다
6. 건의하기, 신고하기를 누르면 Dialog가 나타나며 내용을 입력 후 건의, 신고할 수 있습니다
7. 공지사항, 이용약관, 개인정보 처리방침 등을 클릭하면 해당 노션으로 이동합니다
8. 로그아웃 버튼을 누르면 로그아웃되며, 로그인 화면으로 이동합니다
</div>
</details>

### 설계
- UI 설계 : [피그마](https://www.figma.com/file/flk99RkWlj4rw1djcltdhl/%EB%82%B4%EA%BA%BC%EB%8F%84-(1)?node-id=0%3A1) [피그마2](https://www.figma.com/file/Y8sO6Abym7rEPof5FIr0Vv/%EB%82%B4%EA%BA%BC%EB%8F%84-(Copy)?node-id=0%3A1)

- DB ERD : [ERD](https://www.erdcloud.com/d/Wbg7xBJihLSrvxBbG)

### 커밋 규칙

feat : 새로운 기능에 대한 커밋\
fix : 버그 수정에 대한 커밋\
build : 빌드 관련 파일 수정에 대한 커밋\
chore : 그 외 자잘한 수정에 대한 커밋\
ci : CI관련 설정 수정에 대한 커밋\
docs : 문서 수정에 대한 커밋\
style : 코드 스타일 혹은 포맷 등에 관한 커밋\
refactor :  코드 리팩토링에 대한 커밋\
test : 테스트 코드 수정에 대한 커밋
