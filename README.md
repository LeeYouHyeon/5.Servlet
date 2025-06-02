기초적인 게시판을 예시로 삼아 Servlet과 MVC 패턴을 구현하는 방법을 배운 프로젝트

구현 기능
   1. 게시판 CRUD
   2. 글에 이미지 파일 하나 업로드 가능(수정 가능)
   3. 멤버 기능
      1. 회원가입, 로그인, 로그아웃, 개인정보 수정, 회원탈퇴
      2. 로그인 시 글, 댓글 작성할 때 작성자를 로그인 정보로 자동 대체

사용한 개발 언어 및 라이브러리
   1. 언어 : JDK 11
   2. DB : MySQL 8.0.41
   3. Servlet Container : Apache Tomcat 9.0
   4. 라이브러리 : https://mvnrepository.com/
      1) MySQL Connector/J 8.0.33 : 자바와 DB 연결. MySQL의 버전과 앞 두 숫자가 같은 것으로 사용
      2) SLF4J API Module 1.7.36, Apache Log4j API 1.7.36, Apache Log4j Core 2.18.0, Apache Log4j Web 2.18.0 : 로그 관련 라이브러리
      3) JSTL 1.2 : JSP에 반복, 조건, DB 접근 등을 구현하는 커스텀 태그 라이브러리. 표준 라이브러리다.
      4) MyBatis 3.5.10 : 쿼리문 작성을 도와주는 오픈소스 ORM(Object-Relation Mapping) 프레임워크
      5) JSON.simple 1.1.1 : JSON 객체와 String 사이를 변환해주는 라이브러리
      6) Apache Commons Fileupload 1.4, Apache Commons IO 2.11.0, Thumbnailator 0.4.17 : 파일 업로드 관련
